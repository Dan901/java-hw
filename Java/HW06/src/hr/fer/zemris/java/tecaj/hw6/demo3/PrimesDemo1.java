package hr.fer.zemris.java.tecaj.hw6.demo3;

/**
 * Demonstration of {@link PrimesCollection}.
 * 
 * @author Dan
 *
 */
public class PrimesDemo1 {

	/**
	 * Demonstration.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5);
		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}
	}

}
