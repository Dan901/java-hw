package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Thrown by methods in the {@link Lexer} class to indicate that an error
 * occurred in generating a new {@code Token}.
 * 
 * @author Dan
 *
 */
public class LexerException extends RuntimeException {

	@SuppressWarnings("javadoc")
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code LexerException} with <tt>null</tt> as its error
	 * message string.
	 */
	public LexerException() {
		super();
	}

	/**
	 * Constructs a new {@code LexerException} with given string as its error
	 * message string.
	 * 
	 * @param message
	 *            error message
	 */
	public LexerException(String message) {
		super(message);
	}

}
