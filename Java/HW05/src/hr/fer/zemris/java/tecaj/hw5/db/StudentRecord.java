package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Represents one student. Students are equal if they have the same JMBAG.
 * 
 * @author Dan
 *
 */
public class StudentRecord {
	/**
	 * JMBAG of a student.
	 */
	private final String jmbag;
	/**
	 * Last name of a student.
	 */
	private final String lastName;
	/**
	 * First name of a student.
	 */
	private final String firstName;
	/**
	 * Final grade of a student.
	 */
	private final int finalGrade;

	/**
	 * Creates a new {@code StudentRecord} with given parameters.
	 * 
	 * @param jmbag
	 *            JMBAG of a student
	 * @param lastName
	 *            last name of a student
	 * @param firstName
	 *            first name of a student
	 * @param finalGrade
	 *            final grade of a student
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		
		if(finalGrade < 1 || finalGrade > 5){
			throw new IllegalArgumentException("Invalid grade.");
		}
		this.finalGrade = finalGrade;
	}

	/**
	 * @return JMBAG of a student
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * @return last name of a student
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return first name of a student
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return final grade of a student
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	/**
	 * @return {@code true} is both {@code StudentRecord}s have the same JMBAG;
	 *         {@code false} otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}
	
	/**
	 * Returns student's JMBAG.
	 */
	@Override
	public String toString() {
		return jmbag;
	}

}
