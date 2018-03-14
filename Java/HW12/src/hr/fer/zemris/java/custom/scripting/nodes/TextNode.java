package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * This {@code Node} represents a piece of text in a document.
 * 
 * @author Dan
 *
 */
public class TextNode extends Node {

	/**
	 * Text of this {@code Node}.
	 */
	private final String text;

	/**
	 * Creates a new {@code TextNode} with given text.
	 * 
	 * @param text
	 *            text of this {@code TextNode}
	 */
	public TextNode(String text) {
		if (text == null) {
			throw new NullPointerException();
		}

		this.text = text;
	}

	/**
	 * @return the text of this {@code TextNode}
	 */
	public String getText() {
		return text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}

}
