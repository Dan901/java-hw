package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.CatCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CharsetsCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CopyCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ExitCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.HelpCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.HexdumpCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.LsCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.MkDirCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.SymbolCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.TreeCommand;

/**
 * This is a simple shell implementation.
 * <p>
 * Use command 'help' to see available commands, and command 'exit' to terminate
 * the shell.
 * <p>
 * {@code Environment} for executing commands uses system's standard input and
 * output for communication with the user.
 * 
 * @author Dan
 *
 */
public class MyShell {

	/**
	 * All available commands in this shell.
	 */
	private static Map<String, ShellCommand> commands;
	
	static {
		commands = new HashMap<>();
		commands.put("exit", new ExitCommand());
		commands.put("symbol", new SymbolCommand());
		commands.put("charsets", new CharsetsCommand());
		commands.put("mkdir", new MkDirCommand());
		commands.put("tree", new TreeCommand());
		commands.put("copy", new CopyCommand());
		commands.put("cat", new CatCommand());
		commands.put("ls", new LsCommand());
		commands.put("hexdump", new HexdumpCommand());
		commands.put("help", new HelpCommand());
	}

	/**
	 * {@code Environment} implementation that uses system's standard input and
	 * output for communication with the user.
	 * 
	 * @author Dan
	 *
	 */
	private static class EnvironmentImpl implements Environment {

		/**
		 * Symbol that the shell prints when current command input spans across
		 * multiple lines.
		 */
		private Character multilineSymbol;

		/**
		 * Symbol that the shell prints when expecting user input.
		 */
		private Character promptSymbol;

		/**
		 * Symbol that the user writes at the end of the line to indicate the
		 * command will span one more line.
		 */
		private Character morelinesSymbol;

		/**
		 * Input stream.
		 */
		private BufferedReader input;

		/**
		 * Output stream.
		 */
		private BufferedWriter output;

		/**
		 * Creates a new {@link EnvironmentImpl}.
		 */
		public EnvironmentImpl() {
			promptSymbol = '>';
			multilineSymbol = '|';
			morelinesSymbol = '\\';

			input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
			output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
		}

		/**
		 * @throws IOException
		 *             if an I/O error occurs or end of the stream is reached
		 */
		@Override
		public String readLine() throws IOException {
			String line = input.readLine();
			
			if(line == null){
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
		public Iterable<ShellCommand> commands() {
			return MyShell.commands.values();
		}

		@Override
		public Character getMultilineSymbol() {
			return multilineSymbol;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			multilineSymbol = Objects.requireNonNull(symbol);
		}

		@Override
		public Character getPromptSymbol() {
			return promptSymbol;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			promptSymbol = Objects.requireNonNull(symbol);
		}

		@Override
		public Character getMorelinesSymbol() {
			return morelinesSymbol;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			morelinesSymbol = Objects.requireNonNull(symbol);
		}

	}

	/**
	 * Environment in which commands are executed.
	 */
	private static Environment environment;

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		environment = new EnvironmentImpl();

		try {
			environment.writeln("Welcome to MyShell v 1.0");
			ShellStatus status = ShellStatus.CONTINUE;

			while (status != ShellStatus.TERMINATE) {
				environment.write(environment.getPromptSymbol() + " ");

				String[] input = readInput();
				ShellCommand command = commands.get(input[0]);
				if(command == null){
					environment.writeln("Unknown command.");
					continue;
				}
				
				String arguments = null;
				if(input.length == 2){
					arguments = input[1];
				}
				
				status = command.executeCommand(environment, arguments);

			}

		} catch (IOException e) {
			System.err.println("An I/O exception occured: " + e.getMessage() + " Exiting...");
		} catch (IllegalStateException e){
			System.err.println("An exception occured: \"" + e.getMessage() + "\" Exiting...");
		}
	}

	/**
	 * Reads user input and returns command name as one {@code String} and
	 * arguments (if given) as another.
	 * 
	 * @return {@code Array} of 1 or 2 {@code Strings}
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private static String[] readInput() throws IOException {
		StringBuilder input = new StringBuilder();

		boolean readMore = true;
		while (readMore) {
			String line = environment.readLine();
			
			if(line.endsWith(environment.getMorelinesSymbol().toString())){
				input.append(line.substring(0, line.length() - 1));
				environment.write(environment.getMultilineSymbol() + " ");
				
			} else {
				input.append(line);
				readMore = false;
			}
		}
		
		return input.toString().split("\\s+", 2);
	}

}
