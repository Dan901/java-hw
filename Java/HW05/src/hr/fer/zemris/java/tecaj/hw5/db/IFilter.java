package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 * 
 * @author Dan
 *
 */
public interface IFilter {
	/**
	 * Evaluates this predicate on the given {@code StudentRecord}.
	 * 
	 * @param record
	 *            {@code StudentRecord} to check
	 * @return {@code true} if the input argument matches the predicate,
	 *         otherwise {@code false}
	 */
	boolean accepts(StudentRecord record);
}
