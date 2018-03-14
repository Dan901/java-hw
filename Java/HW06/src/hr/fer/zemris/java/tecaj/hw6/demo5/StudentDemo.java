package hr.fer.zemris.java.tecaj.hw6.demo5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This program reads input from file "studenti.txt" in this directory for every
 * line creates a new {@link StudentRecord}. It also prints some statistics on
 * standard output.
 * 
 * @author Dan
 *
 */
public class StudentDemo {

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            not used
	 * @throws IOException
	 *             if I/O error occurs
	 */
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("studenti.txt"), StandardCharsets.UTF_8);

		List<StudentRecord> records = lines.stream().map(StudentDemo::createStudent).collect(Collectors.toList());

		// 1.
		long moreThan25 = records.parallelStream().filter(r -> r.getScore() > 25).count();
		System.out.println("Studenata koji imaju više od 25 bodova (MI + ZI + LAB) ima: " + moreThan25);

		// 2.
		long grade5 = records.parallelStream().filter(r -> r.getFinalGrade() == 5).count();
		System.out.println("Studenata s ocjenom 5 ima: " + grade5);

		// 3.
		List<StudentRecord> grade5Students = records.parallelStream().filter(r -> r.getFinalGrade() == 5)
				.collect(Collectors.toList());
		System.out.println("Studenti s ocjenom 5: " + grade5Students.toString());

		// 4.
		List<StudentRecord> grade5StudentsSorted = records.parallelStream().filter(r -> r.getFinalGrade() == 5)
				.sorted((r1, r2) -> -Double.compare(r1.getScore(), r2.getScore())).collect(Collectors.toList());
		System.out.println("Sortirani studenti po bodovima s ocjenom 5: " + grade5StudentsSorted.toString());

		// 5.
		List<String> failedJMBAGsSorted = records.parallelStream().filter(r -> r.getFinalGrade() == 1)
				.map(StudentRecord::getJmbag).sorted().collect(Collectors.toList());
		System.out.println("Studenata koji nisu položili ima: " + failedJMBAGsSorted.size());

		// 6.
		Map<Integer, List<StudentRecord>> mapByGrades = records.parallelStream()
				.collect(Collectors.groupingBy(StudentRecord::getFinalGrade));
		System.out.println("Studenata s ocjenom 3 ima: " + mapByGrades.get(3).size());

		// 7.
		Map<Integer, Integer> mapByGrades2 = records.parallelStream()
				.collect(Collectors.toMap(StudentRecord::getFinalGrade, (r -> 1), Integer::sum));
		System.out.println("Studenata s ocjenom 4 ima: " + mapByGrades2.get(4));

		// 8.
		Map<Boolean, List<StudentRecord>> failPass = records.parallelStream()
				.collect(Collectors.partitioningBy(r -> r.getFinalGrade() != 1));
		System.out.println("Studenata koji su prošli ima: " + failPass.get(true).size());
	}

	/**
	 * Creates a new {@code StudentRecord} with student's information in given
	 * {@code String}.
	 * 
	 * @param line
	 *            student's information
	 * @return {@code StudentRecord}
	 */
	private static StudentRecord createStudent(String line) {
		String[] fields = line.split("\\t");
		StudentRecord record = null;
		
		if(fields.length != 7){
			System.err.println("Invalid input text format.");
			System.exit(-1);
		}

		try {
			record = new StudentRecord(fields[0], fields[1], fields[2], Double.parseDouble(fields[3]),
					Double.parseDouble(fields[4]), Double.parseDouble(fields[5]), Integer.parseInt(fields[6]));
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid input text format.");
			System.exit(-1);
		}

		return record;
	}
}
