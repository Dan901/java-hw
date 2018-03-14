package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents a constant {@code Double} expression.
 * 
 * @author Dan
 *
 */
public class ElementConstantDouble extends Element {

	/**
	 * Value of the constant.
	 */
	private final double value;

	/**
	 * Creates a new {@code ElementConstantDouble} with given value.
	 * 
	 * @param value
	 *            value of the constant
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * @return the value of this {@code ElementConstantDouble}
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * @return {@code String} representation of the value of this
	 *         {@code ElementConstantDouble}
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}
}
