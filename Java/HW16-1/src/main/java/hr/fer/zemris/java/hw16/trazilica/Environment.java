package hr.fer.zemris.java.hw16.trazilica;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Environment that contains all relevant information for parsing text into
 * {@link Document} objects and for executing commands that work with said
 * documents. <br>
 * It also offers methods for reading user input and writing to the user.
 * 
 * @author Dan
 *
 */
public interface Environment {

	/**
	 * Adds a stopword to internal {@code Collection}.
	 * 
	 * @param word
	 *            stopword
	 */
	void addStopword(String word);

	/**
	 * Gets all stopwords.
	 * 
	 * @return {@code Collection} of all stopwords
	 */
	Collection<String> getStopwords();

	/**
	 * Adds a {@code Document} to this {@code Environment}. Vocabulary is
	 * updated with words in this document.
	 * 
	 * @param doc
	 *            a new document
	 */
	void addDocument(Document doc);

	/**
	 * Gets all documents.
	 * 
	 * @return {@code List} of all documents
	 */
	List<Document> getDocuments();

	/**
	 * Gets the vocabulary containing words from all documents.
	 * 
	 * @return {@code Map} with words from vocabulary as keys mapped to a number
	 *         representing the order they were added in, which is useful for
	 *         creating vectors for documents
	 */
	Map<String, Integer> getVocabulary();

	/**
	 * Builds vectors for all documents in this {@code Environment}.
	 */
	void buildVectors();

	/**
	 * Gets the results of the last query.
	 * 
	 * @return results of the last query or {@code null}
	 */
	QueryResults getQueryResults();

	/**
	 * Sets the results to be returned by calling {@link #getQueryResults()}.
	 * 
	 * @param results
	 *            latest {@code QueryResults}
	 */
	void setQueryResults(QueryResults results);

	/**
	 * Reads a line from the input stream.
	 * 
	 * @return line from input stream
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	String readLine() throws IOException;

	/**
	 * Writes a {@code String} to the output stream.
	 * 
	 * @param text
	 *            {@code String} that needs to be written
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	void write(String text) throws IOException;

	/**
	 * Writes a line to the output stream.
	 * 
	 * @param text
	 *            {@code String} that needs to be written
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	void writeln(String text) throws IOException;

	/**
	 * Returns an {@code Iterable} collection of all available commands.
	 * 
	 * @return available commands
	 */
	Iterable<Command> commands();

}
