package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Represents a comparator between two {@code Strings}.
 * 
 * @author Dan
 *
 */
public interface IComparisonOperator {

	/**
	 * Compares given {@code Strings}.
	 * 
	 * @param value1
	 *            first {@code String}
	 * @param value2
	 *            first {@code String}
	 * @return result of comparison; {@code true} if it's satisfied, otherwise
	 *         {@code false}
	 */
	boolean satisfied(String value1, String value2);
}
