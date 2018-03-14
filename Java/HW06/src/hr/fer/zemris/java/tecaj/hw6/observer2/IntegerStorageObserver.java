package hr.fer.zemris.java.tecaj.hw6.observer2;

/**
 * Observer interface for {@link IntegerStorage}.
 * 
 * @author Dan
 *
 */
public interface IntegerStorageObserver {

	/**
	 * Action to be performed when value of {@link IntegerStorage}, that this
	 * observer is registered to, is changed.
	 * 
	 * @param iStorageChange
	 *            {@code IntegerStorageChange} with information about
	 *            {@code IntegerStorage}, old and new values
	 */
	public void valueChanged(IntegerStorageChange iStorageChange);
}
