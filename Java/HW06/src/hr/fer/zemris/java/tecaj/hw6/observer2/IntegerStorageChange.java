package hr.fer.zemris.java.tecaj.hw6.observer2;

import java.util.Objects;

/**
 * This class represents one change of value in {@link IntegerStorage}. It holds
 * all relevant information for performing an action caused by the change.
 * 
 * @author Dan
 *
 */
public class IntegerStorageChange {

	/**
	 * {@code IntegerStorage} whose value changed.
	 */
	private IntegerStorage storage;

	/**
	 * Old value.
	 */
	private int oldValue;

	/**
	 * New value.
	 */
	private int newValue;

	/**
	 * Creates new {@code IntegerStorageChange} with given arguments.
	 * 
	 * @param storage
	 *            {@code IntegerStorage} whose value changed
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new value
	 */
	public IntegerStorageChange(IntegerStorage storage, int oldValue, int newValue) {
		this.storage = Objects.requireNonNull(storage);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return {@code IntegerStorage} whose value changed
	 */
	public IntegerStorage getStorage() {
		return storage;
	}

	/**
	 * @return old value of {@code IntegerStorage}
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * @return new value of {@code IntegerStorage}
	 */
	public int getNewValue() {
		return newValue;
	}

}
