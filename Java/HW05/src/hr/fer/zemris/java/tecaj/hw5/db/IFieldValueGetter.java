package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Strategy for obtaining a requested field value from given
 * {@code StudentRecord}.
 * 
 * @author Dan
 *
 */
public interface IFieldValueGetter {

	/**
	 * Obtains a field value from given {@code StudentRecord}.
	 * 
	 * @param record
	 *            {@code StudentRecord}
	 * @return a field value from given {@code StudentRecord}
	 */
	String get(StudentRecord record);
}
