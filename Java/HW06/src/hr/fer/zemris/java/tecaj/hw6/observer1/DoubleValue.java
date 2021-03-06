package hr.fer.zemris.java.tecaj.hw6.observer1;

/**
 * This observer of {@link IntegerStorage} prints double value to standard
 * output every time the value of the {@code IntegerStorage} is changed but not
 * more than n times, where n is given in constructor of this
 * {@code DoubleValue}. Once given number of changes is reacher, observer will
 * unregister from the {@code IntegerStorage}.
 * 
 * @author Dan
 *
 */
public class DoubleValue implements IntegerStorageObserver {

	/**
	 * How many times to print double value.
	 */
	private int n;

	/**
	 * Creates a new {@code DoubleValue} with given argument.
	 * 
	 * @param n
	 *            number of times this {@code DoubleValue} prints double value
	 */
	public DoubleValue(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("n cannot be negative");
		}

		this.n = n;
	}

	/**
	 * Prints double value to standard output.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		if (n == 0) {
			istorage.removeObserver(this);
		} else {
			System.out.println("Double value: " + istorage.getValue() * 2);
			n--;
		}
	}

}
