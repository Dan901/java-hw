package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Interface for a visitor design pattern. <br>
 * Concrete implementations should define an action for each available
 * {@code Node} type.
 *
 * @author Dan
 */
public interface INodeVisitor {

	/**
	 * Called when the visitor is visiting a {@code TextNode}.
	 * 
	 * @param node
	 *            current node
	 */
	void visitTextNode(TextNode node);

	/**
	 * Called when the visitor is visiting a {@code ForLoopNode}.
	 * 
	 * @param node
	 *            current node
	 */
	void visitForLoopNode(ForLoopNode node);

	/**
	 * Called when the visitor is visiting a {@code EchoNode}.
	 * 
	 * @param node
	 *            current node
	 */
	void visitEchoNode(EchoNode node);

	/**
	 * Called when the visitor is visiting a {@code DocumentNode}.
	 * 
	 * @param node
	 *            current node
	 */
	void visitDocumentNode(DocumentNode node);
}
