package hr.fer.zemris.java.tecaj.hw6.observer1;

/**
 * Observer interface for {@link IntegerStorage}.
 * 
 * @author Dan
 *
 */
public interface IntegerStorageObserver {

	/**
	 * Action to be performed when value of given {@link IntegerStorage}, that this
	 * observer is registered to, is changed.
	 * 
	 * @param istorage
	 *            {@code IntegerStorage} that this observer is registered to and
	 *            whose value changed
	 */
	public void valueChanged(IntegerStorage istorage);
}
