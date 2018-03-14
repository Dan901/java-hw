package hr.fer.zemris.java.tecaj.hw3.prob1;

/**
 * This class represents a simple lexical analysis processor. It can operate in
 * two states, as defined in {@link LexerState}. It produces the next
 * {@link Token} when {@link #nextToken()} method is called. Types of
 * {@code Token}s are defined in {@link TokenType}. In basic state, escaping of
 * a digit (or backslash '\') is allowed so it can be a part of a word.
 * 
 * @author Dan
 *
 */
public class Lexer {

	/**
	 * Storage of input data.
	 */
	private final char[] data;

	/**
	 * Last generated token.
	 */
	private Token token;

	/**
	 * Index of a first unprocessed character.
	 */
	private int currentIndex;

	/**
	 * Current state.
	 */
	private LexerState state;

	/**
	 * Creates a {@code Lexer} with given {@code String} as input text. Default
	 * {@link LexerState} is basic.
	 * 
	 * @param text
	 *            input text
	 * @throws IllegalArgumentException
	 *             if text is {@code null}
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Input string is null.");
		}

		data = text.trim().toCharArray();
		state = LexerState.BASIC;
	}

	/**
	 * Sets the state of a {@code Lexer}.
	 * 
	 * @param state
	 *            the state to set
	 * @throws IllegalArgumentException
	 *             if state is {@code null}
	 */
	public void setState(LexerState state) {
		if (state == null) {
			throw new IllegalArgumentException("State cannot be null.");
		}

		this.state = state;
	}

	/**
	 * @return last generated {@code Token}
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Processes next character, or group of characters, from input text and
	 * produces a {@code Token} based on current state.
	 * 
	 * @return newly generated {@code Token}
	 * @throws LexerException
	 *             if the whole input has been tokenized already or input text
	 *             is invalid
	 * @throws NumberFormatException
	 *             if number is to big for {@code Long} type.
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Nothing to tokenize.");
		}

		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return token;
		}

		if (state == LexerState.BASIC) {
			token = nextTokenBasic();
		} else {
			token = nextTokenExtended();
		}

		while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
		}

		return token;
	}

	/**
	 * Generates a new {@code Token} in basic state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token nextTokenBasic() {
		Token newToken;
		char current = data[currentIndex];

		if (Character.isLetter(current) || current == '\\') {
			try {
				newToken = generateWord();
			} catch (IndexOutOfBoundsException e) {
				throw new LexerException("Invalid input.");
			}
		} else if (Character.isDigit(current)) {
			try {
				newToken = generateNumber();
			} catch (NumberFormatException e) {
				throw new LexerException("Invalid number.");
			}
		} else {
			newToken = new Token(TokenType.SYMBOL, Character.valueOf(current));
			currentIndex++;
		}

		return newToken;
	}

	/**
	 * Generates a new {@code Token} in extended state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token nextTokenExtended() {
		Token newToken;
		char current = data[currentIndex];

		if (current == '#') {
			newToken = new Token(TokenType.SYMBOL, Character.valueOf(current));
			currentIndex++;
		} else {
			newToken = generateWordExtended();
		}

		return newToken;
	}

	/**
	 * Generates a new number {@code Token} in basic state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token generateNumber() {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char current = data[currentIndex];

			if (Character.isDigit(current)) {
				sb.append(current);
				currentIndex++;
			} else {
				break;
			}

			if (currentIndex >= data.length) {
				break;
			}
		}

		long number = Long.parseLong(sb.toString());
		return new Token(TokenType.NUMBER, number);
	}

	/**
	 * Generates a new word {@code Token} in basic state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token generateWord() {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char current = data[currentIndex];

			if (Character.isLetter(current)) {
				sb.append(current);
				currentIndex++;
			} else if (current == '\\') {
				currentIndex++;
				current = data[currentIndex];

				if (current == '\\' || Character.isDigit(current)) {
					sb.append(current);
					currentIndex++;
				} else {
					throw new LexerException("Invalid escape sequence.");
				}
			} else {
				break;
			}

			if (currentIndex >= data.length) {
				break;
			}
		}

		return new Token(TokenType.WORD, sb.toString());
	}

	/**
	 * Generates a new word {@code Token} in extended state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token generateWordExtended() {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char current = data[currentIndex];

			if (currentIndex >= data.length || Character.isWhitespace(current) || current == '#') {
				break;
			}

			sb.append(current);
			currentIndex++;
		}

		return new Token(TokenType.WORD, sb.toString());
	}

}
