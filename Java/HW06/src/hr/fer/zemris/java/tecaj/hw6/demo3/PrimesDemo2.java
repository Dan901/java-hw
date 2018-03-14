package hr.fer.zemris.java.tecaj.hw6.demo3;

/**
 * Demonstration of {@link PrimesCollection}.
 * 
 * @author Dan
 *
 */
public class PrimesDemo2 {

	/**
	 * Demonstration.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}
	}

}
