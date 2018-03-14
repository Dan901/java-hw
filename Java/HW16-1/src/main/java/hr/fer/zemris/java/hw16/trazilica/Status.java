package hr.fer.zemris.java.hw16.trazilica;

/**
 * Contains possible statuses after a command is executed.
 * 
 * @author Dan
 *
 */
public enum Status {

	/**
	 * Indicates that the command executed and the console can continue accepting commands.
	 */
	CONTINUE,
	
	/**
	 * Indicates that the console should be terminated.
	 */
	TERMINATE;
}
