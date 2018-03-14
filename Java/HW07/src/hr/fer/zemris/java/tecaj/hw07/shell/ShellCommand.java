package hr.fer.zemris.java.tecaj.hw07.shell;

import java.util.List;

/**
 * Interface for one shell command that can be executed.
 * 
 * @author Dan
 *
 */
public interface ShellCommand {

	/**
	 * Executes the command.
	 * @param env {@code Environment} where the command will be executed.
	 * @param argument command arguments, if necessary
	 * @return status of the shell after the command is executed
	 */
	ShellStatus executeCommand(Environment env, String argument);
	
	/**
	 * @return name of the command
	 */
	String getCommandName();
	
	/**
	 * Returns command description in form of a {@code List} where every {@code String} is one line.
	 * @return description of the command
	 */
	List<String> getCommandDescription();
}
