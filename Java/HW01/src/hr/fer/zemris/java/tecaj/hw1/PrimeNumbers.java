package hr.fer.zemris.java.tecaj.hw1;

/**
 * This program prints the first n prime numbers where n is given as a command
 * line argument and bigger than 0.
 * 
 * @author Dan
 *
 */
public class PrimeNumbers {

	/**
	 * Demonstration of {@link PrimeNumbers}.
	 * @param args One positive integer.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments.");
			System.exit(0);
		}

		int n = Integer.parseInt(args[0]);
		if (n < 1) {
			System.out.println("Argument has to be positive.");
			System.exit(0);
		}

		System.out.println("You requested calculation of " + n + " prime numbers. Here they are:");

		int prime = 2;
		int i = 0;
		while (i < n) {
			if (isPrime(prime)) {
				i++;
				System.out.println(i + ". " + prime);
			}
			prime++;
		}
	}

	/**
	 * Checks if given number is a prime.
	 * @param n Number to check.
	 * @return {@code True} if n is prime, {@code False} otherwise.
	 */
	public static boolean isPrime(int n) {
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}
}
