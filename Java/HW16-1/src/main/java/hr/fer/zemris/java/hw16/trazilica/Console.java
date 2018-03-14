package hr.fer.zemris.java.hw16.trazilica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import hr.fer.zemris.java.hw16.trazilica.commands.ExitCommand;
import hr.fer.zemris.java.hw16.trazilica.commands.QueryCommand;
import hr.fer.zemris.java.hw16.trazilica.commands.ResultsCommand;
import hr.fer.zemris.java.hw16.trazilica.commands.TypeCommand;

/**
 * Console program that executes the commands given by the user. <br>
 * At the start, program expects one command line argument: path to the folder
 * with all documents as text files. Files are converted into {@link Document}
 * objects and stored in the {@link Environment} so that the commands can access
 * them. <br>
 * Documents are represented by TF-IDF vectors and the main command is
 * {@link QueryCommand} that calculates similarity of the given query to all
 * documents. <br>
 * Stopwords that are extracted from a separate file are ignored while parsing
 * documents.
 * 
 * @author Dan
 * @see Document
 */
public class Console {

	/** File with all stopwords. */
	private static final String STOPWORDS_FILE = "hrvatski_stoprijeci.txt";

	/** All available commands. */
	private static Map<String, Command> commands;

	static {
		commands = new HashMap<>();
		commands.put("exit", new ExitCommand());
		commands.put("query", new QueryCommand());
		commands.put("type", new TypeCommand());
		commands.put("results", new ResultsCommand());
	}

	/**
	 * Implementation of the {@link Environment} which uses standard input and
	 * output for communicating with the user.
	 */
	private static Environment environment = new Environment() {

		/** Input stream. */
		private BufferedReader input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

		/** Output stream. */
		private BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));

		/** {@code Set} with stopwords. */
		private Set<String> stopwords = new HashSet<>();

		/** {@code List} with parsed documents. */
		private List<Document> documents = new ArrayList<>();

		/** {@code Map} with words from all documents. */
		private Map<String, Integer> vocabulary = new HashMap<>();

		/** Latest query result. */
		private QueryResults result;

		@Override
		public void addStopword(String word) {
			stopwords.add(Objects.requireNonNull(word));
		}

		@Override
		public Collection<String> getStopwords() {
			return Collections.unmodifiableSet(stopwords);
		}

		@Override
		public void addDocument(Document doc) {
			documents.add(Objects.requireNonNull(doc));
			doc.getWords().forEach(w -> vocabulary.putIfAbsent(w, vocabulary.size()));
		}

		@Override
		public List<Document> getDocuments() {
			return Collections.unmodifiableList(documents);
		}

		@Override
		public Map<String, Integer> getVocabulary() {
			return Collections.unmodifiableMap(vocabulary);
		}

		@Override
		public void buildVectors() {
			documents.forEach(d -> d.buildVector(vocabulary, documents));
		}

		@Override
		public QueryResults getQueryResults() {
			return result;
		}

		@Override
		public void setQueryResults(QueryResults result) {
			this.result = Objects.requireNonNull(result);
		}

		@Override
		public String readLine() throws IOException {
			String line = input.readLine();

			if (line == null) {
				throw new IOException("End of the stream is reached.");
			} else {
				return line;
			}
		}

		@Override
		public void write(String text) throws IOException {
			output.write(text);
			output.flush();
		}

		@Override
		public void writeln(String text) throws IOException {
			output.write(text);
			output.newLine();
			output.flush();
		}

		@Override
		public Iterable<Command> commands() {
			return Collections.unmodifiableCollection(commands.values());
		}

	};

	/**
	 * {@link SimpleFileVisitor} that creates a {@code Document} object from
	 * each file.
	 * 
	 * @author Dan
	 */
	private static class DocumentFileVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			String text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
			Document document = Document.createDocument(text, environment.getStopwords());
			document.setPath(file.toAbsolutePath());
			environment.addDocument(document);
			return FileVisitResult.CONTINUE;
		}
	}

	/**
	 * Program entry point. <br>
	 * {@code Environment} is initialized, documents parsed and program starts
	 * listening for user commands.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Expected 1 argument: folder with documents.");
			return;
		}

		try {
			loadStopwords();
		} catch (IOException e) {
			System.err.println("Couldn't load stopwords: " + e.getMessage());
			return;
		}

		Path folder = Paths.get(args[0]);
		if (!folder.toFile().isDirectory()) {
			System.err.println("Given argument is not a directory.");
			return;
		}

		try {
			Files.walkFileTree(folder, new DocumentFileVisitor());
		} catch (IOException e) {
			System.err.println("Couldn't parse documents: " + e.getMessage());
			return;
		}

		environment.buildVectors();

		try {
			environment.writeln("Vocabulary contains " + environment.getVocabulary().size() + " words.");

			Status status = null;
			do {
				environment.writeln("");
				environment.write("Enter command > ");

				String[] input = environment.readLine().trim().split("\\s+", 2);
				Command command = commands.get(input[0]);
				if (command == null) {
					environment.writeln("Unknown command.");
					continue;
				}

				String arg = null;
				if (input.length == 2) {
					arg = input[1];
				}
				status = command.execute(environment, arg);

			} while (status != Status.TERMINATE);
			environment.writeln("Goodbye!");

		} catch (IOException e) {
			System.err.println("An I/O exception occured: " + e.getMessage());
		}
	}

	/**
	 * Adds all words in {@link #STOPWORDS_FILE} as a stopword to the
	 * {@code Environment}.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private static void loadStopwords() throws IOException {
		BufferedReader reader = Files.newBufferedReader(Paths.get(STOPWORDS_FILE));
		String line;

		while ((line = reader.readLine()) != null) {
			environment.addStopword(line.trim());
		}
	}

}
