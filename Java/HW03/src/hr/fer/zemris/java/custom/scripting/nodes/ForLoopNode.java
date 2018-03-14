package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * This {@code Node} represents a single for-loop construct. It consists of 3 or
 * 4 {@link Element}s.
 * 
 * @author Dan
 *
 */
public class ForLoopNode extends Node {

	/**
	 * Variable of this {@code ForLoopNode}.
	 */
	private final ElementVariable variable;

	/**
	 * Start expression of this {@code ForLoopNode}.
	 */
	private final Element startExpression;

	/**
	 * End expression of this {@code ForLoopNode}.
	 */
	private final Element endExpression;

	/**
	 * Step expression of this {@code ForLoopNode}.
	 */
	private final Element stepExpression;

	/**
	 * Creates a new {@code ForLoopNode} with given {@code Element}s.
	 * 
	 * @param variable
	 *            variable of this {@code ForLoopNode}
	 * @param startExpression
	 *            start expression of this {@code ForLoopNode}
	 * @param endExpression
	 *            end expression of this {@code ForLoopNode}
	 * @param stepExpression
	 *            step expression of this {@code ForLoopNode}, can be
	 *            {@code null}
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		if (variable == null || startExpression == null || endExpression == null) {
			throw new NullPointerException();
		}

		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * @return the variable of this {@code ForLoopNode}
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * @return the start expression of this {@code ForLoopNode}
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * @return the end expression of this {@code ForLoopNode}
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * @return the step expression of this {@code ForLoopNode}
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

}
