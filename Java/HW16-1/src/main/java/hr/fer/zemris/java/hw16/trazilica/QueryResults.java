package hr.fer.zemris.java.hw16.trazilica;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw16.trazilica.commands.QueryCommand;

/**
 * Results of the {@link QueryCommand}. <br>
 * Results consist of {@link Document} objects mapped to a similarity value.
 * Documents should be added in descending order of the similarity value.
 * 
 * @author Dan
 */
public class QueryResults {

	/** {@code Document} mapped to the similarity value. */
	private Map<Document, Double> results;

	/** {@code List} of documents in order they were added. */
	private List<Document> documents;

	/**
	 * Creates a new {@link QueryResults}.
	 */
	public QueryResults() {
		results = new HashMap<>();
		documents = new ArrayList<>();
	}

	/**
	 * Adds a result for one document.
	 * 
	 * @param simValue
	 *            similarity value of the query with the given document
	 * @param doc
	 *            document
	 */
	public void addResult(Double simValue, Document doc) {
		results.put(doc, simValue);
		documents.add(doc);
	}

	/**
	 * Gets the document with the given index.
	 * 
	 * @param index
	 *            of the document representing the order documents were added in
	 * @return {@code Document} with given index
	 */
	public Document getDocument(int index) {
		return documents.get(index);
	}

	/**
	 * Prints the results to the given {@code Environment}.
	 * 
	 * @param env
	 *            environment to print the results on
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void printResults(Environment env) throws IOException {
		int i = 0;
		for (Document doc : documents) {
			env.writeln(String.format("[%d] (%.4f) %s", i, results.get(doc), doc.getPath()));
			i++;
		}
	}
}
