package hr.fer.zemris.java.tecaj.hw5.db;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;

/**
 * This class offers implementations of {@link IFieldValueGetter} in form of
 * static constants. They are also accessible via static method
 * {@link #getFieldValueGetter(String)}.
 * 
 * @author Dan
 *
 */
public class FieldValueGetters {

	/**
	 * Getter for first name.
	 */
	public static final IFieldValueGetter FIRST_NAME = (StudentRecord::getFirstName);
	/**
	 * Getter for last name.
	 */
	public static final IFieldValueGetter LAST_NAME = (StudentRecord::getLastName);
	/**
	 * Getter for JMBAG name.
	 */
	public static final IFieldValueGetter JMBAG = (StudentRecord::getJmbag);

	/**
	 * Contains all getters.
	 */
	private static SimpleHashtable<String, IFieldValueGetter> map = new SimpleHashtable<>();

	static {
		map.put("firstName", FIRST_NAME);
		map.put("lastName", LAST_NAME);
		map.put("jmbag", JMBAG);
	}

	/**
	 * Returns an {@link IFieldValueGetter} with given field name as key.
	 * 
	 * @param key
	 *            name of the field
	 * @return an {@code IFieldValueGetter}
	 */
	public static IFieldValueGetter getFieldValueGetter(String key) {
		return map.get(key);
	}
}
