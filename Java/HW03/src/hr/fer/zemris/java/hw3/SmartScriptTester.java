package hr.fer.zemris.java.hw3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * This program tests {@link SmartScriptParser}. It accepts a single command
 * line argument: path to the document. After getting the document structure it
 * recreates the document body and prints it to {@code System.out}.
 * 
 * @author Dan
 *
 */
public class SmartScriptTester {

	/**
	 * Demonstration.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws IOException
	 *             if provided path is invalid
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Provide filepath as a single argument.");
			return;
		}

		String docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document! " + e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);

		System.out.println(originalDocumentBody);

		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		@SuppressWarnings("unused")
		DocumentNode document2 = parser2.getDocumentNode();
	}

	/**
	 * Recreates document body in form of a {@code String} from given document
	 * structure.
	 * 
	 * @param document
	 *            structure of a document
	 * @return document body
	 */
	private static String createOriginalDocumentBody(DocumentNode document) {
		StringBuffer docBody = new StringBuffer();

		nodeToText(document, docBody);

		return docBody.toString();
	}

	/**
	 * Builds a {@code String} from this {@code Node} and its all children
	 * {@code Node}s and appends it to given {@code StringBuffer}.
	 * 
	 * @param node
	 *            {@code Node} to build a {@code String} from
	 * @param docBody
	 *            {@code StringBuffer} to append to
	 */
	private static void nodeToText(Node node, StringBuffer docBody) {
		boolean nonEmptyTag = false;

		if (node instanceof TextNode) {
			String text = ((TextNode) node).getText();
			text = text.replace("\\", "\\\\");
			text = text.replace("{$", "\\{$");

			docBody.append(text);

		} else if (node instanceof EchoNode) {
			docBody.append("{$= ");

			Element[] elements = ((EchoNode) node).getElements();
			for (Element element : elements) {
				docBody.append(elementToText(element));
				docBody.append(' ');
			}

			docBody.append("$}");

		} else if (node instanceof ForLoopNode) {
			nonEmptyTag = true;

			docBody.append("{$ FOR ");
			docBody.append(elementToText(((ForLoopNode) node).getVariable()));
			docBody.append(' ');
			docBody.append(elementToText(((ForLoopNode) node).getStartExpression()));
			docBody.append(' ');
			docBody.append(elementToText(((ForLoopNode) node).getEndExpression()));
			docBody.append(' ');

			Element step = ((ForLoopNode) node).getStepExpression();
			if (step != null) {
				docBody.append(elementToText(step));
				docBody.append(' ');
			}

			docBody.append("$}");
		}

		int size = node.numberOfChildren();
		for (int i = 0; i < size; i++) {
			nodeToText(node.getChild(i), docBody);
		}

		if (nonEmptyTag) {
			docBody.append("{$END$}");
		}
	}

	/**
	 * Builds a {@code String} from given {@code Element}.
	 * 
	 * @param element
	 *            to build a {@code String} from
	 * @return new {@code String} representing given {@code Element}
	 */
	private static String elementToText(Element element) {
		String text = element.asText();

		if (element instanceof ElementString) {
			text = text.replace("\\", "\\\\");
			text = text.replace("\"", "\\\"");
			text = text.replace("\n", "\\n");
			text = text.replace("\r", "\\r");
			text = text.replace("\t", "\\t");

			return "\"" + text + "\"";
		} else if (element instanceof ElementFunction) {
			return "@" + text;
		}

		return text;
	}

}
