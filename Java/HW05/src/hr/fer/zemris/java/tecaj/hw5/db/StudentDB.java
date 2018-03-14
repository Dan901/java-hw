package hr.fer.zemris.java.tecaj.hw5.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Database emulator. Data is read from "database.txt" in this directory.
 * Supports basic query commands; use command "exit" to terminate.
 * 
 * @author Dan
 *
 */
public class StudentDB {

	/**
	 * Creates a new {@link StudentDatabase} with data from file and performs
	 * query operations.
	 * 
	 * @param args
	 *            not used
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static void main(String[] args) throws IOException {
		StudentDatabase db;
		try {
			db = loadDatabase("database.txt");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;

		while (true) {
			System.out.print("> ");

			line = reader.readLine();
			if (line == null) {
				break;
			}

			List<StudentRecord> filtered;
			try {
				filtered = runCommand(line.trim(), db);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				continue;
			}

			if (!filtered.isEmpty()) {
				printRecords(filtered);
			}
			
			System.out.println("Records selected:" + filtered.size());
		}
	}

	/**
	 * Creates a new {@link StudentDatabase} with data from given file path.
	 * 
	 * @param path
	 *            file path as {@code String}
	 * @return new {@code StudentDatabase}
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	static StudentDatabase loadDatabase(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);
		return new StudentDatabase(lines);
	}

	/**
	 * Runs given command (query) on given database.
	 * 
	 * @param command
	 *            query
	 * @param db
	 *            {@code StudentDatabase}
	 * @return {@code List} of filtered {@code StudentRecord}s
	 */
	static List<StudentRecord> runCommand(String command, StudentDatabase db) {
		if (command.equals("exit")) {
			System.exit(0);
		}

		Matcher indexMatcher = Pattern.compile("indexquery\\s+jmbag\\s*=\\s*\"(.+)\"").matcher(command);
		Matcher queryMatcher = Pattern.compile("query\\s+(.*)").matcher(command);

		if (indexMatcher.matches()) {
			System.out.println("Using index for record retrieval.");

			String jmbag = indexMatcher.group(1);

			List<StudentRecord> recordList = new LinkedList<>();
			StudentRecord record = db.forJMBAG(jmbag);

			if (record != null) {
				recordList.add(record);
			}

			return recordList;

		} else if (queryMatcher.matches()) {
			String query = queryMatcher.group(1).trim();
			IFilter filter = new QueryFilter(query);

			return db.filter(filter);

		} else {
			throw new IllegalArgumentException("Invalid command.");
		}

	}

	/**
	 * Prints given {@code StudentRecord}s on standard output.
	 * 
	 * @param records
	 *            records to print
	 */
	private static void printRecords(List<StudentRecord> records) {
		int maxJmbag = 0, maxFirstName = 0, maxLastName = 0;

		for (StudentRecord record : records) {
			maxJmbag = Math.max(maxJmbag, record.getJmbag().length());
			maxFirstName = Math.max(maxFirstName, record.getFirstName().length());
			maxLastName = Math.max(maxLastName, record.getLastName().length());
		}

		StringJoiner bounds = new StringJoiner("=+=", "+=", "=+");
		bounds.add(new String(new char[maxJmbag]).replace("\0", "="));
		bounds.add(new String(new char[maxLastName]).replace("\0", "="));
		bounds.add(new String(new char[maxFirstName]).replace("\0", "="));
		bounds.add("=");

		System.out.println(bounds.toString());

		for (StudentRecord record : records) {
			StringJoiner sj = new StringJoiner(" | ", "| ", " |");
			sj.add(fieldToString(record.getJmbag(), maxJmbag));
			sj.add(fieldToString(record.getLastName(), maxLastName));
			sj.add(fieldToString(record.getFirstName(), maxFirstName));
			sj.add(Integer.toString(record.getFinalGrade()));
			System.out.println(sj.toString());
		}

		System.out.println(bounds.toString());
	}

	/**
	 * Extends given {@code String}, representing student's field value, with
	 * spaces, until length is equal to size.
	 * 
	 * @param field
	 *            {@code String} to extend
	 * @param size
	 *            length of new {@code String}
	 * @return given {@code String} with added spaces at the end
	 */
	private static String fieldToString(String field, int size) {
		StringBuilder sb = new StringBuilder(field);
		for (int i = field.length(); i < size; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
}
