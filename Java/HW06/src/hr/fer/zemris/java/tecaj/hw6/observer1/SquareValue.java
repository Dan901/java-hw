package hr.fer.zemris.java.tecaj.hw6.observer1;

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
	public void valueChanged(IntegerStorage istorage) {
		int n = istorage.getValue();
		System.out.printf("Provided new value: %d, square is %d%n", n, n * n);
	}

}
