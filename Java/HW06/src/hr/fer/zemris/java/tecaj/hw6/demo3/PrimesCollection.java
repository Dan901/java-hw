package hr.fer.zemris.java.tecaj.hw6.demo3;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class stores a certain number of consecutive prime numbers. They are
 * calculated lazily (when needed).
 * 
 * @author Dan
 *
 */
public class PrimesCollection implements Iterable<Integer> {

	/**
	 * Number of primes.
	 */
	private int size;

	/**
	 * Creates a new {@code PrimesCollection} that stores given number of
	 * consecutive primes.
	 * 
	 * @param size
	 *            number of consecutive primes
	 * @throws IllegalArgumentException
	 *             if size is less than 1
	 */
	public PrimesCollection(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Size cannot be negative.");
		}
		this.size = size;
	}

	/**
	 * Returns an iterator over prime numbers.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Iterator for this {@code PrimesCollection}.
	 * 
	 * @author Dan
	 *
	 */
	private class IteratorImpl implements Iterator<Integer> {

		/**
		 * Current prime.
		 */
		private int current;
		
		/**
		 * Number of returned primes.
		 */
		private int returned;

		@Override
		public boolean hasNext() {
			return returned < size;
		}

		@Override
		public Integer next() {
			if (returned == size) {
				throw new NoSuchElementException();
			}

			if(returned == 0){
				current = 2;
			} else {
				do{
					current++;
				} while(!isPrime(current));
			}

			returned++;
			return current;
		}

		/**
		 * Calculates if given number is prime.
		 * 
		 * @param n
		 *            number to check
		 * @return {@code true} if given number is prime; {@code false}
		 *         otherwise
		 */
		private boolean isPrime(int n) {
			for (int i = 2; i <= Math.sqrt(n); i++) {
				if (n % i == 0)
					return false;
			}
			return true;
		}

	}
}
