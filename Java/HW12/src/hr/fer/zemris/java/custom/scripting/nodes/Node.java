package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all nodes in a graph that is constructed from a document.
 * Classes that extend this class represent specific types of nodes.
 * 
 * @author Dan
 *
 */
public abstract class Node {

	/**
	 * Array for storing children {@code Node}s.
	 */
	private List<Node> array;

	/**
	 * Adds a given {@code Node} as a children of this {@code Node}.
	 * 
	 * @param child
	 *            child {@code Node}
	 * @throws IllegalArgumentException
	 *             if {@code Node} is {@code null}
	 */
	public void addChildNode(Node child) {
		if (array == null) {
			array = new ArrayList<>();
		}

		array.add(child);
	}

	/**
	 * @return number of current direct children {@code Node}s
	 */
	public int numberOfChildren() {
		if (array == null) {
			return 0;
		} else {
			return array.size();
		}
	}

	/**
	 * Returns the child {@code Node} at given index.
	 * 
	 * @param index
	 *            index of a child {@code Node}
	 * @return the child {@code Node} at given index.
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public Node getChild(int index) {
		if (array == null) {
			throw new IndexOutOfBoundsException("This node has no children.");
		}
		return array.get(index);
	}

	/**
	 * Abstract method that allows the usage of visitor design pattern. Concrete
	 * implementations should call an appropriate method in given
	 * {@code INodeVisitor} depending on the node type.
	 * 
	 * @param visitor
	 *            visitor reference
	 */
	abstract public void accept(INodeVisitor visitor);
}
