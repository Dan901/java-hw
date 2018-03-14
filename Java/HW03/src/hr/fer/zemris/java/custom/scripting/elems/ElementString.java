package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents a {@code String} expression.
 * 
 * @author Dan
 *
 */
public class ElementString extends Element {

	/**
	 * Value of the string.
	 */
	private String value;

	/**
	 * Creates a new {@code ElementString} with given value.
	 * 
	 * @param value
	 *            value of the string
	 */
	public ElementString(String value) {
		if(value == null){
			throw new NullPointerException();
		}
		
		this.value = value;
	}

	/**
	 * @return the value of this {@code ElementString}
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return value of this {@code ElementVariable}
	 */
	@Override
	public String asText() {
		return value;
	}
}
