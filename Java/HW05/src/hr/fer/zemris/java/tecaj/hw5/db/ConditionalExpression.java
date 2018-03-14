package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.Objects;

/**
 * Represents one conditional expression ({@link IConditionalExpression})
 * composed of {@link IFieldValueGetter}, {@link IComparisonOperator} and a
 * constant {@code String} for comparison.
 * 
 * @author Dan
 *
 */
public class ConditionalExpression implements IConditionalExpression {

	/**
	 * Getter for student's field value.
	 */
	private final IFieldValueGetter fieldValueGetter;

	/**
	 * Constant value for comparison.
	 */
	private final String value;

	/**
	 * Comparison operator.
	 */
	private final IComparisonOperator operator;

	/**
	 * Creates a new {@code ConditionalExpression} with given arguments.
	 * 
	 * @param fieldValueGetter
	 *            getter for student's field value
	 * @param value
	 *            constant value for comparison
	 * @param operator
	 *            comparison operator
	 */
	public ConditionalExpression(IFieldValueGetter fieldValueGetter, String value, IComparisonOperator operator) {
		this.fieldValueGetter = Objects.requireNonNull(fieldValueGetter);
		this.value = Objects.requireNonNull(value);
		this.operator = Objects.requireNonNull(operator);
	}

	@Override
	public boolean evaluate(StudentRecord record) {
		return operator.satisfied(fieldValueGetter.get(record), value);
	}

}
