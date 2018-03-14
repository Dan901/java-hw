package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Command that terminates the shell and takes no arguments.
 * 
 * @author Dan
 *
 */
public class ExitCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Terminates the shell.";

	/**
	 * Creates a new {@code ExitCommand}.
	 */
	public ExitCommand() {
		super("exit", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		return ShellStatus.TERMINATE;
	}

}
