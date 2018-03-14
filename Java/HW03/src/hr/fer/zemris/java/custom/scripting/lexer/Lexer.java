package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * This class represents a lexical analysis processor used by
 * {@link SmartScriptParser}. It can operate in three states, as defined in
 * {@link LexerState}. It produces the next {@link Token} when
 * {@link #nextToken()} method is called. Types of {@code Token}s are defined in
 * {@link TokenType}.
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
	 * Index of a first unprocessed character.
	 */
	private int currentIndex;

	/**
	 * Last generated token.
	 */
	private Token token;

	/**
	 * Current state.
	 */
	private LexerState state;

	/**
	 * Creates a {@code Lexer} with given {@code String} as input text.
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

		data = text.toCharArray();
		state = LexerState.TEXT;
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
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Nothing to tokenize.");
		}

		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return token;
		}

		if (state == LexerState.TEXT) {
			token = nextTokenText();

		} else if (state == LexerState.TAG) {
			try {
				token = nextTokenTag();
			} catch (IndexOutOfBoundsException e) {
				throw new LexerException("End of input before closing tag.");
			}

		} else {
			token = nextTokenString();
		}

		return token;
	}

	/**
	 * Generates a new {@code Token} in string state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token nextTokenString() {
		Token newToken;
		char current = data[currentIndex];

		if (current == '\"') {
			newToken = new Token(TokenType.SYMBOL, Character.valueOf(current));
			currentIndex++;

		} else {
			try {
				newToken = generateString();
			} catch (IndexOutOfBoundsException e) {
				throw new LexerException("No character to escape.");
			}
		}

		return newToken;
	}

	/**
	 * Generates a new string {@code Token} in string state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token generateString() {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char current = data[currentIndex];

			if (current == '\"') {
				break;
			}

			if (current == '\\') {
				currentIndex++;
				current = data[currentIndex];

				if (current == 'n') {
					current = '\n';
				} else if (current == 'r') {
					current = '\r';
				} else if (current == 't') {
					current = '\t';
				} else if (current != '\\' && current != '\"') {
					throw new LexerException("Invalid escape character " + current + " in string.");
				}
			}

			sb.append(current);
			currentIndex++;

			if (currentIndex >= data.length) {
				break;
			}
		}

		return new Token(TokenType.STRING, sb.toString());
	}

	/**
	 * Generates a new {@code Token} in text state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token nextTokenText() {
		Token newToken;

		if (isStartTag()) {
			newToken = new Token(TokenType.START_TAG, null);
			currentIndex += 2;

		} else {
			try {
				newToken = generateText();
			} catch (IndexOutOfBoundsException e) {
				throw new LexerException("No character to escape.");
			}
		}

		return newToken;
	}

	/**
	 * Generates a new text {@code Token} in text state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token generateText() {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char current = data[currentIndex];

			if (isStartTag()) {
				break;
			}

			if (current == '\\') {
				currentIndex++;
				current = data[currentIndex];

				if (current != '\\' && current != '{') {
					throw new LexerException("Invalid escape character " + current + " in text.");
				}
			}

			sb.append(current);
			currentIndex++;

			if (currentIndex >= data.length) {
				break;
			}
		}

		return new Token(TokenType.TEXT, sb.toString());
	}

	/**
	 * @return {@code True} only if next unprocessed character sequence is "{$"
	 */
	private boolean isStartTag() {
		if (currentIndex + 1 >= data.length) {
			return false;
		}
		return (data[currentIndex] == '{' && data[currentIndex + 1] == '$');
	}

	/**
	 * Generates a new {@code Token} in tag state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token nextTokenTag() {
		while (Character.isWhitespace(data[currentIndex])) {
			currentIndex++;

			if (currentIndex == data.length) {
				throw new LexerException("End of input before closing tag.");
			}
		}

		Token newToken = null;
		char current = data[currentIndex];

		if (Character.isLetter(current)) {
			newToken = new Token(TokenType.WORD, generateName());

		} else if (current == '-' && Character.isDigit(data[currentIndex + 1])) {
			newToken = generateNumber();

		} else if (current == '@' && Character.isLetter(data[currentIndex + 1])) {
			currentIndex++;
			newToken = new Token(TokenType.FUNCTION, generateName());

		} else if (current == '$' && data[currentIndex + 1] == '}') {
			newToken = new Token(TokenType.END_TAG, null);
			currentIndex += 2;

		} else if (Character.isDigit(current)) {
			newToken = generateNumber();

		}

		if (newToken == null) {
			newToken = new Token(TokenType.SYMBOL, Character.valueOf(current));
			currentIndex++;
		}

		return newToken;
	}

	/**
	 * Generates a new number {@code Token} in tag state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private Token generateNumber() {
		StringBuffer sb = new StringBuffer();

		char current = data[currentIndex];
		
		if(current == '-'){
			sb.append(current);
			currentIndex++;
		}
		
		while (true) {
			current = data[currentIndex];

			if (Character.isDigit(current) || current == '.') {
				sb.append(current);
				currentIndex++;
			} else {
				break;
			}

			if (currentIndex >= data.length) {
				break;
			}
		}

		String number = sb.toString();
		Token newToken;

		try {
			if (number.contains(".")) {
				newToken = new Token(TokenType.DOUBLE, Double.parseDouble(number));
			} else {
				newToken = new Token(TokenType.INTEGER, Integer.parseInt(number));
			}
		} catch (NumberFormatException e) {
			throw new LexerException("Invalid number format.");
		}

		return newToken;
	}

	/**
	 * Generates a valid variable or function name in tag state.
	 * 
	 * @return newly generated {@code Token}
	 */
	private String generateName() {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char current = data[currentIndex];

			if (!Character.isLetterOrDigit(current) && current != '_') {
				break;
			}

			sb.append(current);
			currentIndex++;

			if (currentIndex >= data.length) {
				break;
			}
		}

		return sb.toString();
	}

}
