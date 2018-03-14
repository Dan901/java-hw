package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * This class represents one output token given as a result of lexical analysis
 * with {@link Lexer}. Available types are defined in {@link TokenType}.
 * 
 * @author Dan
 *
 */
public class Token {

	/**
	 * Type of a token.
	 */
	private TokenType type;

	/**
	 * Value of a token.
	 */
	private Object value;

	/**
	 * Creates a new {@code Token} with given parameters.
	 * 
	 * @param type
	 *            type of a token, as defined in {@link TokenType}
	 * @param value
	 *            value of a token
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * @return the type of a token
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * @return the value of a token
	 */
	public Object getValue() {
		return value;
	}

}
