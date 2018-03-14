package hr.fer.zemris.java.tecaj.hw6.observer2;

/**
 * This observer of {@link IntegerStorage} prints squared value to standard
 * output every time the value of the {@code IntegerStorage} is changed.
 * 
 * @author Dan
 *
 */
public class SquareValue implements IntegerStorageObserver {

	/**
	 * Prints squared value to standard output.
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		int n = istorage.getNewValue();
		System.out.printf("Old value: %d, provided new value: %d, square is %d%n", istorage.getOldValue(), n, n * n);
	}

}
