package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Defines possible states of a {@link Lexer}.
 * 
 * @author Dan
 *
 */
public enum LexerState {

	/**
	 * Default state, for parsing plain text.
	 */
	TEXT,

	/**
	 * State for parsing text between tags ( '{$' and '$}' ).
	 */
	TAG,

	/**
	 * State for parsing strings.
	 */
	STRING;
}
