package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Base class for all nodes in a graph that is constructed from a document.
 * Classes that extend this class represent specific types of nodes.
 * 
 * @author Dan
 *
 */
public class Node {

	/**
	 * Array for storing children {@code Node}s.
	 */
	private ArrayIndexedCollection array;

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
			array = new ArrayIndexedCollection();
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
		return (Node) array.get(index);
	}
}
