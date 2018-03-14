package hr.fer.zemris.java.hw16.trazilica.commands;

import java.io.IOException;

import hr.fer.zemris.java.hw16.trazilica.Command;
import hr.fer.zemris.java.hw16.trazilica.Environment;
import hr.fer.zemris.java.hw16.trazilica.Status;

/**
 * Exit command terminates the console.
 * 
 * @author Dan
 */
public class ExitCommand implements Command {

	@Override
	public Status execute(Environment env, String argument) throws IOException{
		return Status.TERMINATE;
	}

}
