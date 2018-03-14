package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * This {@code Node} represents an entire document.
 * 
 * @author Dan
 *
 */
public class DocumentNode extends Node {

	/**
	 * Creates a new empty {@code DocumentNode}.
	 */
	public DocumentNode() {
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
