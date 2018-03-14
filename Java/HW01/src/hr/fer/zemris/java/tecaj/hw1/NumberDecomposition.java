package hr.fer.zemris.java.tecaj.hw1;

/**
 * This program calculates and prints the decomposition of a given natural
 * number, greater than 1, onto prime factors. Number is given as a command line
 * argument.
 * 
 * @author Dan
 *
 */
public class NumberDecomposition {

	/**
	 * Demonstration of {@link NumberDecomposition}.
	 * @param args One integer bigger than 1.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments.");
			System.exit(0);
		}

		int n = Integer.parseInt(args[0]);
		if (n <= 1) {
			System.out.println("Argument has to be bigger than 1.");
			System.exit(0);
		}

		System.out.println("You requested decomposition of number " + n + " onto prime factors. Here they are:");

		for (int i = 2, b = 0; i <= n; i++) {
			while (n % i == 0) {
				b++;
				System.out.println(b + ". " + i);
				n /= i;
			}
		}
	}
}
