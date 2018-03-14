package hr.fer.zemris.java.tecaj.hw6.observer2;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Storage for {@code Integer} values.
 * <p>
 * When value is changed registered {@link IntegerStorageObserver}s are
 * notified. To register observers call
 * {@link #addObserver(IntegerStorageObserver)} method.
 * 
 * @author Dan
 *
 */
public class IntegerStorage {

	/**
	 * Integer value.
	 */
	private int value;

	/**
	 * Iterator if iteration over observers is currently in progress.
	 */
	private Iterator<IntegerStorageObserver> iterator;

	/**
	 * Registered observers.
	 */
	private Set<IntegerStorageObserver> observers;

	/**
	 * Creates a new {@code IntegerStorage} with given value and without
	 * observers.
	 * 
	 * @param initialValue
	 *            value of this {@code IntegerStorage}
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Adds given observer to this {@code IntegerStorage}. If value of this
	 * {@code IntegerStorage} is changed, the observer will be notified.
	 * 
	 * @param observer
	 *            {@code IntegerStorageObserver}, {@code null} values are not
	 *            allowed
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observers == null) {
			observers = new LinkedHashSet<>();
		}

		observers.add(Objects.requireNonNull(observer));
	}

	/**
	 * Removes the given observer from this {@code IntegerStorage} observers,
	 * meaning it will no longer receive notifications when value of this
	 * {@code IntegerStorage} is changed.
	 * 
	 * @param observer
	 *            {@code IntegerStorageObserver} to remove
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if (iterator == null) {
			observers.remove(observer);
		} else {
			//since this is not multi-threaded environment, only current observer can unregister
			iterator.remove();
		}
	}

	/**
	 * Removes all observers from this {@code IntegerStorage}.
	 */
	public void clearObservers() {
		observers.clear();
	}

	/**
	 * @return the value of this {@code IntegerStorage}
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the value of this {@code IntegerStorage} to given value and it the
	 * value changed, notifies all registered observers.
	 * 
	 * @param value
	 *            new value
	 */
	public void setValue(int value) {
		if (this.value != value) {
			IntegerStorageChange change = new IntegerStorageChange(this, this.value, value);
			this.value = value;

			if (observers != null) {
				iterator = observers.iterator();

				while (iterator.hasNext()) {
					iterator.next().valueChanged(change);
				}

				iterator = null;
			}
		}
	}
}
