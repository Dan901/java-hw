package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Implementation of {@link ListModel} interface that contains prime numbers.
 * <br>
 * When created only number 1 is in this model, and by calling method
 * {@link #next()} a new prime is generated.
 * 
 * @author Dan
 *
 */
public class PrimListModel implements ListModel<Integer> {

	/** List containing generated primes. */
	private List<Integer> primes;

	/** List containing all registered listeners. */
	private List<ListDataListener> listeners;

	/**
	 * Creates a new {@link PrimListModel} with only number 1 in this model.
	 */
	public PrimListModel() {
		primes = new ArrayList<>();
		primes.add(1);
		listeners = new ArrayList<>();
	}

	/**
	 * Generates and adds a new prime number to this {@link PrimListModel} and
	 * notifies every {@link ListDataListener} of the change.
	 */
	public void next() {
		int index = primes.size();
		int prime = primes.get(index - 1);
		do {
			prime++;
		} while (!isPrime(prime));
		primes.add(prime);
		
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
		listeners.stream().forEach(l -> l.intervalAdded(event));
	}

	@Override
	public int getSize() {
		return primes.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return primes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	/**
	 * Checks if given number is a prime.
	 * 
	 * @param n
	 *            number to check
	 * @return {@code true} if n is prime, {@code false} otherwise
	 */
	private static boolean isPrime(int n) {
		int end = (int) Math.sqrt(n);
		for (int i = 2; i <= end; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

}
