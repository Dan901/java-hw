package hr.fer.zemris.java.hw16.trazilica;

import java.io.IOException;

/**
 * Interface for all commands.
 * 
 * @author Dan
 */
public interface Command {

	/**
	 * Executes this {@code Command}.
	 * 
	 * @param env
	 *            {@code Environment} this command is executed in
	 * @param argument
	 *            argument which was received or {@code null}
	 * @return {@code Status} after the command is executed
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	Status execute(Environment env, String argument) throws IOException;
}
