package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Provides basic binary arithmetic operations over {@code Integers} and
 * {@code Doubles}. If both operands are {@code Integers} result will also be an
 * {@code Integer}; otherwise it will be a {@code Double}. Operands and result
 * are of type {@code Number}.
 * 
 * @author Dan
 *
 */
public class NumberOperations {

	/**
	 * Sums given numbers.
	 * 
	 * @param a
	 *            first summand
	 * @param b
	 *            second summand
	 * @return the sum of given numbers
	 */
	public static Number sum(Number a, Number b) {
		if (bothIntegers(a, b)) {
			return a.intValue() + b.intValue();
		} else {
			return a.doubleValue() + b.doubleValue();
		}
	}

	/**
	 * Subtracts second number from the first.
	 * 
	 * @param a
	 *            first number
	 * @param b
	 *            second number
	 * @return difference between given numbers
	 */
	public static Number difference(Number a, Number b) {
		if (bothIntegers(a, b)) {
			return a.intValue() - b.intValue();
		} else {
			return a.doubleValue() - b.doubleValue();
		}
	}

	/**
	 * Multiplies given numbers.
	 * 
	 * @param a
	 *            multiplier
	 * @param b
	 *            multiplicand
	 * @return product of given numbers
	 */
	public static Number product(Number a, Number b) {
		if (bothIntegers(a, b)) {
			return a.intValue() * b.intValue();
		} else {
			return a.doubleValue() * b.doubleValue();
		}
	}

	/**
	 * Divides second number from the first.
	 * 
	 * @param a
	 *            dividend
	 * @param b
	 *            divisor
	 * @return quotient of given numbers
	 */
	public static Number quotient(Number a, Number b) {
		if (bothIntegers(a, b)) {
			return a.intValue() / b.intValue();
		} else {
			return a.doubleValue() / b.doubleValue();
		}
	}

	/**
	 * Checks if both {@code Numbers} are {@code Integers}.
	 * 
	 * @param a
	 *            first number
	 * @param b
	 *            second number
	 * @return {@code true} only if both {@code Numbers} are {@code Integers};
	 *         {@code false} otherwise
	 */
	private static boolean bothIntegers(Number a, Number b) {
		return (a instanceof Integer && b instanceof Integer);
	}
}
