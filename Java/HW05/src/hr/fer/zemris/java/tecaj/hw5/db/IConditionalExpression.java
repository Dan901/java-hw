package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Interface for conditional expressions which evaluate on a
 * {@link StudentRecord}. Depending on implementation, this expression can be
 * composed of more expressions associated with some logical operator.
 * 
 * @author Dan
 *
 */
public interface IConditionalExpression {

	/**
	 * Evaluates an expression on given {@code StudentRecord}.
	 * 
	 * @param record
	 *            record to evaluate on
	 * @return {@code true} if this expression is satisfied; otherwise
	 *         {@code false}
	 */
	boolean evaluate(StudentRecord record);

	/**
	 * Used by a composite which contains more individual expressions.
	 * 
	 * @param expression
	 *            expression to add to this expression
	 * @throws UnsupportedOperationException
	 *             if it's not supported
	 */
	default void addExpression(IConditionalExpression expression) {
		throw new UnsupportedOperationException();
	}
}
