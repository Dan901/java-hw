package hr.fer.zemris.java.hw16.trazilica.commands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import hr.fer.zemris.java.hw16.trazilica.Command;
import hr.fer.zemris.java.hw16.trazilica.Document;
import hr.fer.zemris.java.hw16.trazilica.Environment;
import hr.fer.zemris.java.hw16.trazilica.QueryResults;
import hr.fer.zemris.java.hw16.trazilica.Status;

/**
 * Type command expects an integer argument representing index of the document
 * in the results from the last {@link QueryCommand}. That document is than
 * written to the {@link Environment}.
 *
 * @author Dan
 */
public class TypeCommand implements Command {

	@Override
	public Status execute(Environment env, String argument) throws IOException {
		QueryResults results = env.getQueryResults();
		if (results == null) {
			env.writeln("First run 'query' command.");
			return Status.CONTINUE;
		}

		Document document;
		try {
			int index = Integer.parseInt(argument);
			document = results.getDocument(index);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			env.writeln("Argument should be an integer from an interval [0,n-1]"
					+ " where n is the number of results returned by the last query command.");
			return Status.CONTINUE;
		}

		String docInfo = "Document: " + document.getPath();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < docInfo.length(); i++) {
			sb.append('-');
		}
		String splitter = sb.toString();

		env.writeln(splitter);
		env.writeln(docInfo);
		env.writeln(splitter);
		env.writeln(new String(Files.readAllBytes(document.getPath()), StandardCharsets.UTF_8));
		env.writeln(splitter);

		return Status.CONTINUE;
	}

}
