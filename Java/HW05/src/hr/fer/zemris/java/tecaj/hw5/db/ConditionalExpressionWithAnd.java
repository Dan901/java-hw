package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents more than one conditional expression (
 * {@link IConditionalExpression}) associated with logical AND. Expressions are
 * added by calling {@link #addExpression(IConditionalExpression)}.
 * 
 * @author Dan
 *
 */
public class ConditionalExpressionWithAnd implements IConditionalExpression {

	/**
	 * Contains all expressions.
	 */
	private List<IConditionalExpression> expressions;

	/**
	 * Creates a new empty {@code ConditionalExpressionWithAnd}.
	 */
	public ConditionalExpressionWithAnd() {
		expressions = new LinkedList<>();
	}

	/**
	 * Adds another {@code IConditionalExpression} to this expression.
	 */
	@Override
	public void addExpression(IConditionalExpression expression) {
		expressions.add(expression);
	}

	/**
	 * @return {@code true} if all composing expressions are satisfied;
	 *         otherwise {@code false}
	 */
	@Override
	public boolean evaluate(StudentRecord record) {
		boolean accept = true;
		for (IConditionalExpression expression : expressions) {
			accept = accept && expression.evaluate(record);
		}

		return accept;
	}

}
