package hr.fer.zemris.java.tecaj.hw3.prob1;

/**
 * Defines possible states of a {@link Lexer}.
 * 
 * @author Dan
 *
 */
public enum LexerState {

	/**
	 * Basic state.
	 */
	BASIC,

	/**
	 * In extended state, {@code Lexer} will treat everything as a word.
	 */
	EXTENDED;
}
