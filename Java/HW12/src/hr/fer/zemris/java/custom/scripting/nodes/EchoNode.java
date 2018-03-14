package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * This {@code Node} represents a command which generates some textual output
 * dynamically. It consists of one or more {@link Element}s.
 * 
 * @author Dan
 *
 */
public class EchoNode extends Node {

	/**
	 * Elements of this {@code EchoNode}.
	 */
	private final Element[] elements;

	/**
	 * Creates a new {@code EchoNode} with given elements.
	 * 
	 * @param elements
	 *            elements of this {@code EchoNode}
	 */
	public EchoNode(Element[] elements) {
		this.elements = Objects.requireNonNull(elements);
	}

	/**
	 * @return the elements of this {@code EchoNode}
	 */
	public Element[] getElements() {
		return elements;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}

}
