package hr.fer.zemris.java.tecaj.hw6.observer2;

/**
 * This observer of {@link IntegerStorage} counts the number of times the value
 * of {@code IntegerStorage} changed.
 * 
 * @author Dan
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * Number of times the value changed.
	 */
	private int count;

	/**
	 * Prints the number of times the value changed standard output.
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		count++;
		System.out.println("Number of value changes since tracking: " + count);
	}

}
