package hr.fer.zemris.java.tecaj.hw07.shell;

/**
 * Contains possible shell statuses after a command is executed.
 * 
 * @author Dan
 *
 */
public enum ShellStatus {

	/**
	 * Indicates that the command executed normally and the shell can continue taking commands.
	 */
	CONTINUE,
	
	/**
	 * Indicated that the shell should be terminated.
	 */
	TERMINATE;
}
