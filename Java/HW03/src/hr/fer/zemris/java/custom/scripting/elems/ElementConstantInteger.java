package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents a constant {@code Integer} expression.
 * 
 * @author Dan
 *
 */
public class ElementConstantInteger extends Element {

	/**
	 * Value of the constant.
	 */
	private final int value;

	/**
	 * Creates a new {@code ElementConstantInteger} with given value.
	 * 
	 * @param value
	 *            value of the constant
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	/**
	 * @return the value of this {@code ElementConstantInteger}
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return {@code String} representation of the value of this
	 *         {@code ElementConstantInteger}
	 */
	@Override
	public String asText() {
		return Integer.toString(value);
	}

}
