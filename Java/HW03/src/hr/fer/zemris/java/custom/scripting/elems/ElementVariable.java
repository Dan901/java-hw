package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents a variable expression.
 * 
 * @author Dan
 *
 */
public class ElementVariable extends Element {

	/**
	 * Name of the variable.
	 */
	private String name;

	/**
	 * Creates a new {@code ElementVariable} with given name.
	 * 
	 * @param name
	 *            name of the variable
	 */
	public ElementVariable(String name) {
		if(name == null){
			throw new NullPointerException();
		}
		
		this.name = name;
	}

	/**
	 * @return the name of this {@code ElementVariable}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return name of this {@code ElementVariable}
	 */
	@Override
	public String asText() {
		return name;
	}
}
