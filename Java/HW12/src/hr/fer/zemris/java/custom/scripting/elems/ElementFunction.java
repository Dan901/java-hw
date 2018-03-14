package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents a function expression.
 * 
 * @author Dan
 *
 */
public class ElementFunction extends Element {

	/**
	 * Name of the function.
	 */
	private final String name;

	/**
	 * Creates a new {@code ElementFunction} with given name.
	 * 
	 * @param name
	 *            name of the function
	 */
	public ElementFunction(String name) {
		if(name == null){
			throw new NullPointerException();
		}
		
		this.name = name;
	}

	/**
	 * @return the name of this {@code ElementFunction}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return name of this {@code ElementFunction}
	 */
	@Override
	public String asText() {
		return "@" + name;
	}
}
