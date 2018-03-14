package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Wrapper for a single value that can be either {@code Integer} or
 * {@code Double}.
 * <p>
 * Basic arithmetic operations are offered over that value. Numbers can also be
 * given as {@code Strings} and {@code null} reference is treated as
 * {@code Integer} with value 0. Other types will cause an exception.
 * 
 * @author Dan
 *
 */
public class ValueWrapper {

	/**
	 * Current value.
	 */
	private Object value;

	/**
	 * Creates a new {@code ValueWrapper} with given value.
	 * 
	 * @param value
	 *            value
	 * @throws IllegalArgumentException
	 *             if given value is not {@code Integer}, {@code Double} or a
	 *             {@code String}
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * @return current value of this {@code ValueWrapper}
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value of this {@code ValueWrapper} to given value.
	 * 
	 * @param value
	 *            new value
	 * @throws IllegalArgumentException
	 *             if given value is not {@code Integer}, {@code Double} or a
	 *             {@code String}
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Increments value of this {@code ValueWrapper} with given value.
	 * 
	 * @param incValue
	 *            increment value
	 * @throws IllegalArgumentException
	 *             if given value is not {@code Integer}, {@code Double} or
	 *             {@code String} containing a number
	 */
	public void increment(Object incValue) {
		checkType(value);
		value = NumberOperations.sum(convertToNumber(value), convertToNumber(incValue));
	}

	/**
	 * Decrements value of this {@code ValueWrapper} with given value.
	 * 
	 * @param decValue
	 *            decrement value
	 * @throws IllegalArgumentException
	 *             if given value is not {@code Integer}, {@code Double} or
	 *             {@code String} containing a number
	 */
	public void decrement(Object decValue) {
		checkType(value);
		value = NumberOperations.difference(convertToNumber(value), convertToNumber(decValue));
	}

	/**
	 * Multiplies value of this {@code ValueWrapper} with given value.
	 * 
	 * @param mulValue
	 *            multiplication value
	 * @throws IllegalArgumentException
	 *             if given value is not {@code Integer}, {@code Double} or
	 *             {@code String} containing a number
	 */
	public void multiply(Object mulValue) {
		checkType(value);
		value = NumberOperations.product(convertToNumber(value), convertToNumber(mulValue));
	}

	/**
	 * Divides value of this {@code ValueWrapper} with given value.
	 * 
	 * @param divValue
	 *            division value
	 * @throws IllegalArgumentException
	 *             if given value is not {@code Integer}, {@code Double} or
	 *             {@code String} containing a number
	 */
	public void divide(Object divValue) {
		checkType(value);
		value = NumberOperations.quotient(convertToNumber(value), convertToNumber(divValue));
	}

	/**
	 * Compares value of this {@code ValueWrapper} with given value.
	 * 
	 * @param withValue
	 *            comparing value
	 * @return {@code Integer} with value 0 if they are equal, with negative
	 *         value if this value is smaller than the argument and with
	 *         positive value if this value is bigger than the argument
	 */
	public int numCompare(Object withValue) {
		Number first = convertToNumber(value);
		Number second = convertToNumber(withValue);

		if (first instanceof Double || second instanceof Double) {
			return Double.compare(first.doubleValue(), second.doubleValue());
		} else {
			return Integer.compare(first.intValue(), second.intValue());
		}
	}

	/**
	 * Converts given {@code Object} to a {@code Number}.
	 * 
	 * @param value
	 *            object to convert
	 * @return {@code Number} with given value
	 */
	private Number convertToNumber(Object value) {
		if (value == null) {
			return Integer.valueOf(0);

		} else if (value instanceof String) {
			String stringValue = (String) value;

			try {
				Integer intValue = Integer.parseInt(stringValue);
				return intValue;
			} catch (NumberFormatException e) {
			}

			try {
				Double doubleValue = Double.parseDouble(stringValue);
				return doubleValue;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("String has to contain a number.");
			}

		} else if (value instanceof Integer || value instanceof Double) {
			return (Number) value;

		} else {
			throw new IllegalArgumentException("Only Integer, Double and String types are supported.");
		}
	}

	/**
	 * Checks if given value is {@code Integer}, {@code Double} or a
	 * {@code String} and throws an exception if not.
	 * 
	 * @param value
	 *            value to check
	 * @throws IllegalArgumentException
	 *             if type is not supported
	 */
	private void checkType(Object value) {
		//@formatter:off
		if(value == null) return;
		if(value instanceof Integer) return;
		if(value instanceof Double) return;
		if(value instanceof String) return;
		//@formatter:on

		throw new IllegalArgumentException("Invalid value type.");
	}

}
