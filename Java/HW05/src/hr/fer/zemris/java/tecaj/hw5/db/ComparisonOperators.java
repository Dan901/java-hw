package hr.fer.zemris.java.tecaj.hw5.db;

import java.text.Collator;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;

/**
 * This class offers implementations of {@link IComparisonOperator} in form of
 * static constants. They are also accessible via static method
 * {@link #getComparisonOperator(String)}.
 * 
 * @author Dan
 *
 */
public class ComparisonOperators {

	/**
	 * For comparing strings depending on locale.
	 */
	private static Collator collator = Collator.getInstance();

	/**
	 * Checks if the first {@code String} is equal to the second {@code String}.
	 */
	public static final IComparisonOperator EQUAL = (v1, v2) -> v1.equals(v2);

	/**
	 * Checks if the first {@code String} is not equal to the second
	 * {@code String}.
	 */
	public static final IComparisonOperator NOT_EQUAL = (v1, v2) -> !v1.equals(v2);

	/**
	 * Checks if the first {@code String} is lexicographically greater than or
	 * equal to the second {@code String}.
	 */
	public static final IComparisonOperator GREATER_OR_EQUAL = (v1, v2) -> collator.compare(v1, v2) >= 0;

	/**
	 * Checks if the first {@code String} is lexicographically greater than the
	 * second {@code String}.
	 */
	public static final IComparisonOperator GREATER_THAN = (v1, v2) -> collator.compare(v1, v2) > 0;

	/**
	 * Checks if the first {@code String} is lexicographically less than or
	 * equal to the second {@code String}.
	 */
	public static final IComparisonOperator LESS_OR_EQUAL = (v1, v2) -> collator.compare(v1, v2) <= 0;

	/**
	 * Checks if the first {@code String} is lexicographically less than the
	 * second {@code String}.
	 */
	public static final IComparisonOperator LESS_THAN = (v1, v2) -> collator.compare(v1, v2) < 0;
	
	/**
	 * Checks if the first {@code String} is equal to the second {@code String}.
	 * Second can contain one wildcard '*'.
	 */
	public static final IComparisonOperator LIKE = new IComparisonOperator() {
		
		@Override
		public boolean satisfied(String value1, String value2) {
			if(value2.contains("*")){
				int i = value2.indexOf('*');
				return value1.startsWith(value2.substring(0, i)) && value1.endsWith(value2.substring(i + 1));
			} else {
				return value1.equals(value2);
			}
		}
	};

	/**
	 * Contains all operators.
	 */
	private static SimpleHashtable<String, IComparisonOperator> map = new SimpleHashtable<>();

	static {
		map.put("=", EQUAL);
		map.put("!=", NOT_EQUAL);
		map.put("LIKE", LIKE);
		map.put(">=", GREATER_OR_EQUAL);
		map.put(">", GREATER_THAN);
		map.put("<=", LESS_OR_EQUAL);
		map.put("<", LESS_THAN);
	}

	/**
	 * Returns an {@link IComparisonOperator} with given {@code String}
	 * representation as key.
	 * 
	 * @param key
	 *            {@code String} representation
	 * @return an {@code IComparisonOperator}
	 */
	public static IComparisonOperator getComparisonOperator(String key) {
		return map.get(key);
	}
}
