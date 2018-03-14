package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Thrown by methods in the {@link SmartScriptParser} class to indicate that an
 * error occurred while parsing a document.
 * 
 * @author Dan
 *
 */
public class SmartScriptParserException extends RuntimeException {

	@SuppressWarnings("javadoc")
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code SmartScriptParserException} with <tt>null</tt> as
	 * its error message string.
	 */
	public SmartScriptParserException() {
		super();
	}

	/**
	 * Constructs a new {@code SmartScriptParserException} with given string as
	 * its error message string.
	 * 
	 * @param message
	 *            error message
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

}
