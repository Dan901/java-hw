package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;

/**
 * Database implementation that holds {@link StudentRecord}s. It offers 2
 * methods: first for fetching records in <b>O(1)</b> with known JMBAG, and
 * second for filtering with given {@link IFilter}.
 * 
 * @author Dan
 *
 */
public class StudentDatabase {

	/**
	 * List of all records.
	 */
	private List<StudentRecord> records;

	/**
	 * Map of all records with JMBAG as key.
	 */
	private SimpleHashtable<String, StudentRecord> recordsMap;

	/**
	 * Creates a new {@code StudentDatabase} with given students.
	 * 
	 * @param lines
	 *            {@code List} where every {@code String} must have 4 fields
	 *            separated by a whitespace that define a student
	 */
	public StudentDatabase(List<String> lines) {
		Objects.requireNonNull(lines);

		records = new LinkedList<>();
		recordsMap = new SimpleHashtable<>();

		for (String line : lines) {
			StudentRecord record = newRecord(line);
			records.add(record);
			recordsMap.put(record.getJmbag(), record);
		}

	}

	/**
	 * Creates a new {@code StudentRecord} from given {@code String}.
	 * 
	 * @param line
	 *            {@code String} with student's information
	 * @return newly created {@code StudentRecord}
	 */
	private StudentRecord newRecord(String line) {
		String[] fields = line.split("\\t");
		if (fields.length != 4) {
			throw new IllegalArgumentException("Invalid student format");
		}

		return new StudentRecord(fields[0], fields[1], fields[2], Integer.parseInt(fields[3]));
	}

	/**
	 * Returns a {@code StudentRecord} with given JMBAG in <b>O(1)</b>
	 * complexity.
	 * 
	 * @param jmbag
	 *            JMBAG of a student
	 * @return {@code StudentRecord} with given JMBAG
	 */
	public StudentRecord forJMBAG(String jmbag) {
		Objects.requireNonNull(jmbag, "JMBAG cannot be null.");
		return recordsMap.get(jmbag);
	}

	/**
	 * Returns every {@code StudentRecord} that matches the given
	 * {@code IFilter}.
	 * 
	 * @param filter
	 *            filter to apply to every {@code StudentRecord}
	 * @return {@code List} of filtered {@code StudentRecord}s
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> filteredRecords = new LinkedList<>();

		for (StudentRecord record : records) {
			if (filter.accepts(record)) {
				filteredRecords.add(record);
			}
		}

		return filteredRecords;
	}

}
