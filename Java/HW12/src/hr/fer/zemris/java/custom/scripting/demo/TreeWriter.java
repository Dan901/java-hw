package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * This program opens a smart script file, parses it using
 * {@link SmartScriptParser} and than recreates file's original content
 * (approximately) using {@link WriterVisitor}. <br>
 * Single command line argument is expected as a path of the smart script file.
 *
 * @author Dan
 */
public class TreeWriter {

	/**
	 * Implementation of {@code INodeVisitor} that writes every {@code Node} to
	 * the standard output in a way that another parsing would generate the same
	 * document.
	 *
	 * @author Dan
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			String text = node.getText();
			text = text.replace("\\", "\\\\");
			text = text.replace("{$", "\\{$");
			System.out.print(text);
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			StringJoiner sj = new StringJoiner(" ", "{$ FOR ", " $}");
			sj.add(node.getVariable().asText());
			sj.add(node.getStartExpression().asText());
			sj.add(node.getEndExpression().asText());
			if (node.getStepExpression() != null) {
				sj.add(node.getStepExpression().asText());
			}
			System.out.print(sj.toString());

			int n = node.numberOfChildren();
			for (int i = 0; i < n; i++) {
				node.getChild(i).accept(this);
			}

			System.out.print("{$END$}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			StringJoiner sj = new StringJoiner(" ", "{$= ", " $}");
			for (Element element : node.getElements()) {
				sj.add(element.asText());
			}
			System.out.print(sj.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int n = node.numberOfChildren();
			for (int i = 0; i < n; i++) {
				node.getChild(i).accept(this);
			}
		}
	}

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("File path expected!");
			return;
		}

		SmartScriptParser p;
		try {
			String docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
			p = new SmartScriptParser(docBody);
		} catch (IOException e) {
			System.out.println("An I/O exception occured: " + e.getMessage() + " Exiting.");
			return;
		} catch (SmartScriptParserException e) {
			System.out.println("Invalid file: " + e.getMessage() + " Exiting.");
			return;
		}

		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}

}
