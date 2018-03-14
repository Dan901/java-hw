package hr.fer.zemris.java.hw16.trazilica.commands;

import java.io.IOException;

import hr.fer.zemris.java.hw16.trazilica.Command;
import hr.fer.zemris.java.hw16.trazilica.Environment;
import hr.fer.zemris.java.hw16.trazilica.QueryResults;
import hr.fer.zemris.java.hw16.trazilica.Status;

/**
 * Prints the results of the last {@link QueryCommand}.
 * 
 * @author Dan
 */
public class ResultsCommand implements Command {

	@Override
	public Status execute(Environment env, String argument) throws IOException {
		QueryResults results = env.getQueryResults();
		if(results == null){
			env.writeln("No results to show. First run 'query' command.");
		} else {
			results.printResults(env);
		}
		
		return Status.CONTINUE;
	}

}
