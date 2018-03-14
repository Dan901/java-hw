package hr.fer.zemris.java.tecaj.hw3.prob1;

/**
 * Defines possible types of a {@link Token}.
 * 
 * @author Dan
 *
 */
public enum TokenType {

	/**
	 * Last token given by a {@link Lexer} represents that the whole input has
	 * been processed.
	 */
	EOF,

	/**
	 * Represents a word with {@code String} as a {@code Token} value.
	 */
	WORD,

	/**
	 * Represents a number with {@code Long} as a {@code Token} value.
	 */
	NUMBER,

	/**
	 * Represents a symbol with {@code Character} as a {@code Token} value.
	 */
	SYMBOL;
}
