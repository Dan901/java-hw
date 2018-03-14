package hr.fer.zemris.java.tecaj.hw1;

/**
 * This program calculates i-th number of Hofstadter's Q sequence where i is
 * given as a command line argument.
 * 
 * @author Dan
 *
 */
public class HofstadterQ {

	/**
	 * Demonstration of {@link HofstadterQ}.
	 * 
	 * @param args
	 *            One positive integer.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments.");
			System.exit(0);
		}

		long i = Long.parseLong(args[0]);
		if (i < 1) {
			System.out.println("Argument has to be positive.");
			System.exit(0);
		}

		long n = q(i);
		System.out.println("You requested calculation of " + i
				+ ". number of Hofstadter's Q-sequence. The requested number is " + n + ".");
	}

	/**
	 * Recursive calculation of i-th number of Hofstadter's Q sequence.
	 * 
	 * @param i
	 *            Index of a wanted number.
	 * @return i-th number of Hofstadter's Q sequence
	 */
	public static long q(long i) {
		if (i <= 2)
			return 1;

		return q(i - q(i - 1)) + q(i - q(i - 2));
	}

}
