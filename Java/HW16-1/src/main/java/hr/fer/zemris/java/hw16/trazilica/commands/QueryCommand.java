package hr.fer.zemris.java.hw16.trazilica.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import hr.fer.zemris.java.hw16.trazilica.Command;
import hr.fer.zemris.java.hw16.trazilica.Document;
import hr.fer.zemris.java.hw16.trazilica.Environment;
import hr.fer.zemris.java.hw16.trazilica.QueryResults;
import hr.fer.zemris.java.hw16.trazilica.Status;

/**
 * Query command expects words as arguments. Only words that are in the
 * vocabulary from all documents are taken into consideration. <br>
 * Same as with documents created from files, TF-IDF vector is created for the
 * query and similarity with every document is calculated. If the similarity is
 * bigger than 0, it's stored into a {@link QueryResults} object and printed to
 * the {@link Environment}.
 * 
 * @author Dan
 */
public class QueryCommand implements Command {

	/** Maximum number of documents in the results that are printed. */
	private static final int MAX_RESULTS = 10;

	@Override
	public Status execute(Environment env, String argument) throws IOException {
		if (argument == null || argument.isEmpty()) {
			env.writeln("Query command expects some words as an argument.");
			return Status.CONTINUE;
		}

		Document query = Document.createDocument(argument, env.getStopwords());
		query.removeIfNotInVocabulary(env.getVocabulary().keySet());
		if (query.getWords().isEmpty()) {
			env.writeln("None of the given words are in the vocabulary.");
			return Status.CONTINUE;
		}

		StringJoiner sj = new StringJoiner(", ", "[", "]");
		query.getWords().forEach(w -> sj.add(w));
		env.writeln("Query is: " + sj.toString());

		List<Document> documents = env.getDocuments();
		Map<Document, Double> simValues = new HashMap<>();
		query.buildVector(env.getVocabulary(), documents);
		documents.forEach(doc -> simValues.put(doc, query.calculateSimilarity(doc)));

		QueryResults results = new QueryResults();
		simValues.entrySet().stream().filter(e -> e.getValue() != 0)
				.sorted((e1, e2) -> -Double.compare(e1.getValue(), e2.getValue())).limit(MAX_RESULTS)
				.forEachOrdered(e -> results.addResult(e.getValue(), e.getKey()));

		env.setQueryResults(results);
		env.writeln("Best " + MAX_RESULTS + " results:");
		results.printResults(env);

		return Status.CONTINUE;
	}

}
