package hr.fer.zemris.java.tecaj.hw6.demo5;

import java.util.Objects;

/**
 * Represents one student.
 * 
 * @author Dan
 *
 */
public class StudentRecord {

	/**
	 * Student's JMBAG.
	 */
	private final String jmbag;

	/**
	 * Student's last name.
	 */
	private final String lastName;

	/**
	 * Student's first name.
	 */
	private final String firstName;

	/**
	 * Student's midterm exam score.
	 */
	private final double midtermExamScore;

	/**
	 * Student's final exam score.
	 */
	private final double finalExamScore;

	/**
	 * Student's exercises score.
	 */
	private final double exerciseScore;

	/**
	 * Student's final grade.
	 */
	private final int finalGrade;

	/**
	 * Creates a new student with given arguments.
	 * 
	 * @param jmbag
	 *            student's JMBAG
	 * @param lastName
	 *            student's last name
	 * @param firstName
	 *            ftudent's first name
	 * @param midtermExamScore
	 *            student's midterm exam score
	 * @param finalExamScore
	 *            student's final exam score
	 * @param exerciseScore
	 *            student's exercises score
	 * @param finalGrade
	 *            student's final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, double midtermExamScore,
			double finalExamScore, double exerciseScore, int finalGrade) {
		this.jmbag = Objects.requireNonNull(jmbag);
		this.lastName = Objects.requireNonNull(lastName);
		this.firstName = Objects.requireNonNull(firstName);
		this.midtermExamScore = midtermExamScore;
		this.finalExamScore = finalExamScore;
		this.exerciseScore = exerciseScore;

		if (finalGrade < 1 || finalGrade > 5) {
			throw new IllegalArgumentException("Invalid final grade.");
		}
		this.finalGrade = finalGrade;
	}

	/**
	 * @return student's JMBAG
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * @return student's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return student's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return student's midterm exam score
	 */
	public double getMidtermExamScore() {
		return midtermExamScore;
	}

	/**
	 * @return student's final exam score
	 */
	public double getFinalExamScore() {
		return finalExamScore;
	}

	/**
	 * @return student's exercises score
	 */
	public double getExerciseScore() {
		return exerciseScore;
	}

	/**
	 * @return student's final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}
	
	/**
	 * @return student's score sum
	 */
	public double getScore(){
		return midtermExamScore + finalExamScore + exerciseScore;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
