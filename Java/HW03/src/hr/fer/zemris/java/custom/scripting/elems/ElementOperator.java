package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents an operator expression.
 * 
 * @author Dan
 *
 */
public class ElementOperator extends Element {

	/**
	 * Operator symbol.
	 */
	private final String symbol;

	/**
	 * Creates a new {@code ElementOperator} with given symbol.
	 * 
	 * @param symbol
	 *            symbol of the operator
	 */
	public ElementOperator(String symbol) {
		if(symbol == null){
			throw new NullPointerException();
		}
		
		this.symbol = symbol;
	}

	/**
	 * @return the symbol of this {@code ElementOperator}
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return symbol of this {@code ElementOperator}
	 */
	@Override
	public String asText() {
		return symbol;
	}
}
