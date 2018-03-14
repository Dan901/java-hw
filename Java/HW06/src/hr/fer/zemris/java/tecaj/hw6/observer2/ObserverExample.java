package hr.fer.zemris.java.tecaj.hw6.observer2;

/**
 * Demonstration of {@link IntegerStorage} and various
 * {@link IntegerStorageObserver}s.
 * 
 * @author Dan
 *
 */
public class ObserverExample {

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {

		IntegerStorage istorage = new IntegerStorage(20);

		istorage.addObserver(new SquareValue());
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}

}
