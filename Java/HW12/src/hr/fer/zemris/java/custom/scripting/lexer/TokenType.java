package hr.fer.zemris.java.custom.scripting.lexer;

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
	 * Represents plain text outside tags.
	 */
	TEXT,

	/**
	 * Represents a symbol with {@code Character} as a {@code Token} value.
	 */
	SYMBOL,

	/**
	 * Represents a word, between tags, with {@code String} as a {@code Token}
	 * value.
	 */
	WORD,

	/**
	 * Represents a number, between tags, with {@code Double} as a {@code Token}
	 * value.
	 */
	DOUBLE,

	/**
	 * Represents a number, between tags, with {@code Integer} as a
	 * {@code Token} value.
	 */
	INTEGER,

	/**
	 * Represents a function, between tags, with {@code String} as a
	 * {@code Token} value.
	 */
	FUNCTION,

	/**
	 * Represents a string, between tags, with {@code String} as a {@code Token}
	 * value.
	 */
	STRING,

	/**
	 * Represents "{$" sequence.
	 */
	START_TAG,

	/**
	 * Represents "$}" sequence.
	 */
	END_TAG;
}
