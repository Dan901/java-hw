package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.Lexer;
import hr.fer.zemris.java.custom.scripting.lexer.LexerException;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * This class represents a parser.
 * <p>
 * It builds a structure of {@link Node}s using {@link Lexer} for production of
 * {@link Token}s. Constructor expects the whole document text which it than
 * parses. If text is in any way invalid, {@link SmartScriptParserException}
 * will be thrown. If parsing is successful calling method
 * {@link #getDocumentNode()} will return the produced document structure.
 * <p>
 * Anything outside tags <tt>{$</tt> and <tt>$}</tt> is treated as plain text.
 * Valid tags are <tt>{$FOR ... $}</tt> and <tt>{$= ...$}</tt>. <tt>FOR</tt> tag
 * expects an <tt>{$END$}</tt> tag.
 * 
 * 
 * @author Dan
 *
 */
public class SmartScriptParser {

	/**
	 * {@code Lexer} for producing {@code Tokens}.
	 */
	private Lexer lexer;

	/**
	 * Contains generated document structure.
	 */
	private DocumentNode documentNode;

	/**
	 * Stack for generating document structure.
	 */
	private ObjectStack stack;

	/**
	 * Maximum number of elements in for loop.
	 */
	private static final int FOR_LOOP_ELEMENTS = 4;

	/**
	 * Creates a new parser and processes given document.
	 * 
	 * @param document
	 *            document to parse
	 * @throws SmartScriptParserException
	 *             if document is invalid in some way
	 */
	public SmartScriptParser(String document) {
		if (document == null) {
			throw new SmartScriptParserException("Document is null.");
		}

		lexer = new Lexer(document);
		stack = new ObjectStack();

		try {
			parseInTextState();
		} catch (LexerException e) {
			throw new SmartScriptParserException(e.getMessage());
		}
	}

	/**
	 * @return the produced {@code DocumentNode} by this
	 *         {@code SmartScriptParser}
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}

	/**
	 * Initially called by constructor, starts parsing the document in TEXT
	 * state.
	 */
	private void parseInTextState() {
		documentNode = new DocumentNode();
		stack.push(documentNode);

		Token token = lexer.nextToken();

		// read until EOF
		while (token.getType() != TokenType.EOF) {
			Node current = (Node) stack.peek();

			if (token.getType() == TokenType.TEXT) {
				current.addChildNode(new TextNode((String) token.getValue()));

			} else if (token.getType() == TokenType.START_TAG) {
				// switch to TAG state
				lexer.setState(LexerState.TAG);

				Node newNode = parseInTagState();

				// if tag was END, don't add it
				if (newNode != null) {
					current.addChildNode(newNode);
				}

				// return to TEXT state
				lexer.setState(LexerState.TEXT);

			} else {
				throw new SmartScriptParserException("Invalid document.");
			}

			token = lexer.nextToken();
		}

		Node last = (Node) stack.pop();
		if (!(last instanceof DocumentNode)) {
			throw new SmartScriptParserException("Invalid document. Not enough END tags.");
		}

	}

	/**
	 * Parses the tags in the document.
	 * 
	 * @return newly produced {@code Node}
	 */
	private Node parseInTagState() {
		Token token = lexer.nextToken();

		Node node;

		if (isSymbol(token, '=')) {
			node = generateEchoNode();

		} else if (token.getType() == TokenType.WORD) {
			String tagName = (String) token.getValue();

			if (tagName.equalsIgnoreCase("FOR")) {
				node = generateForLoopNode();
				stack.push(node);

			} else if (tagName.equalsIgnoreCase("END")) {
				token = lexer.nextToken();

				if (token.getType() != TokenType.END_TAG) {
					throw new SmartScriptParserException("Tag has to be closed.");
				}

				stack.pop();
				if (stack.isEmpty()) {
					throw new SmartScriptParserException("Invalid document. To many END tags.");
				}

				// END tag doesn't produce a node
				node = null;
			} else {
				throw new SmartScriptParserException("Invalid tag name.");
			}

		} else {
			throw new SmartScriptParserException("Invalid tag name.");
		}

		return node;
	}

	/**
	 * Produces a new {@code EchoNode}.
	 * 
	 * @return newly produced {@code EchoNode}
	 */
	private EchoNode generateEchoNode() {
		ArrayIndexedCollection elements = new ArrayIndexedCollection();
		Token token = lexer.nextToken();

		while (token.getType() != TokenType.END_TAG) {
			Element element;

			if (isSymbol(token, '\"')) {
				// switch to STRING state
				lexer.setState(LexerState.STRING);
				element = generateStringElement();
				lexer.setState(LexerState.TAG);

			} else {
				element = generateEchoElement(token);
			}

			elements.add(element);
			token = lexer.nextToken();
		}

		int size = elements.size();
		Element[] elementsArray = new Element[size];

		for (int i = 0; i < size; i++) {
			elementsArray[i] = (Element) elements.get(i);
		}

		return new EchoNode(elementsArray);
	}

	/**
	 * Produces a new {@code ForLoopNode}.
	 * 
	 * @return newly produced {@code ForLoopNode}
	 */
	private Node generateForLoopNode() {
		Token token = lexer.nextToken();

		// "{$ FOR" is already read, so next must be a variable
		if (token.getType() != TokenType.WORD) {
			throw new SmartScriptParserException("Invalid FOR tag");
		}

		Element[] elements = new Element[FOR_LOOP_ELEMENTS];

		int i = 0;
		elements[i] = new ElementVariable((String) token.getValue());

		i++;
		token = lexer.nextToken();

		while (token.getType() != TokenType.END_TAG) {
			if (i == FOR_LOOP_ELEMENTS) {
				throw new SmartScriptParserException("Too many elements in FOR tag.");
			}

			Element element;

			if (isSymbol(token, '\"')) {
				// switch to STRING state
				lexer.setState(LexerState.STRING);
				element = generateStringElement();
				lexer.setState(LexerState.TAG);

			} else {
				// variable or number
				element = generateForElement(token);
			}

			elements[i] = element;
			i++;
			token = lexer.nextToken();
		}

		Node newNode;
		try {
			newNode = new ForLoopNode((ElementVariable) elements[0], elements[1], elements[2], elements[3]);
		} catch (NullPointerException e) {
			throw new SmartScriptParserException("Not enough elements in for loop.");
		}

		return newNode;
	}

	/**
	 * Produces a new {@code ElementString}.
	 * 
	 * @return newly produced {@code ElementString}
	 */
	private ElementString generateStringElement() {
		// could be empty
		ElementString element = new ElementString("");
		Token token = lexer.nextToken();

		// quote char is already read
		if (token.getType() == TokenType.STRING) {
			element = new ElementString((String) token.getValue());
			token = lexer.nextToken();
		}

		if (!isSymbol(token, '\"')) {
			throw new SmartScriptParserException("Invalid string format.");
		}

		return element;
	}

	/**
	 * Produces a new {@code Element} from given {@code Token}, that is of valid
	 * type in FOR-tag (variable or number).
	 * 
	 * @param token
	 *            {@code Token} to produce an {@code Element} from
	 * @return newly produced {@code Element}
	 */
	private Element generateForElement(Token token) {
		Element element;

		if (token.getType() == TokenType.WORD) {
			element = new ElementVariable((String) token.getValue());

		} else if (token.getType() == TokenType.DOUBLE) {
			element = new ElementConstantDouble((double) token.getValue());

		} else if (token.getType() == TokenType.INTEGER) {
			element = new ElementConstantInteger((int) token.getValue());

		} else {
			throw new SmartScriptParserException("Invalid text between tags.");
		}

		return element;
	}

	/**
	 * Produces a new {@code Element} from given {@code Token}, that is of valid
	 * type in =-tag (variable, number, function or operator).
	 * 
	 * @param token
	 *            {@code Token} to produce an {@code Element} from
	 * @return newly produced {@code Element}
	 */
	private Element generateEchoElement(Token token) {
		// maybe its number or variable
		try {
			return generateForElement(token);
		} catch (SmartScriptParserException e) {
		}

		Element element;

		// or function, or operator
		if (token.getType() == TokenType.FUNCTION) {
			element = new ElementFunction((String) token.getValue());

		} else if (token.getType() == TokenType.SYMBOL && isValidOperator((char) token.getValue())) {
			element = new ElementOperator(String.valueOf((char) token.getValue()));

		} else {
			throw new SmartScriptParserException("Invalid text between tags.");
		}

		return element;
	}

	/**
	 * Checks if given operator is supported.
	 * 
	 * @param operator
	 *            to check
	 * @return {@code True} if given operator is supported
	 */
	private boolean isValidOperator(char operator) {
		return (operator == '+' || operator == '-' || operator == '*' || operator == '/' || operator == '^');
	}

	/**
	 * Checks if given {@code Token} is equal to given {@code char}.
	 * 
	 * @param token
	 *            {@code Token} to check
	 * @param symbol
	 *            to compare to
	 * @return {@code True} if given {@code Token} is a symbol which is equal to
	 *         given symbol.
	 */
	private boolean isSymbol(Token token, char symbol) {
		return (token.getType() == TokenType.SYMBOL && (char) token.getValue() == symbol);
	}

}
