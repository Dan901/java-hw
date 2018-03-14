package hr.fer.zemris.java.custom.collections;

/**
 * Thrown by methods in the {@link ObjectStack} class to indicate that the stack
 * is empty.
 * 
 * @author Dan
 *
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code EmptyStackException} with <tt>null</tt> as its
	 * error message string.
	 */
	public EmptyStackException() {
		super();
	}
}
