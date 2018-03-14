package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program calculates i-th root of a complex number given via command line.
 * First argument is real part of a complex number, second is imaginary part and
 * third is the root number, that has to be bigger than 1.
 * 
 * @author Dan
 *
 */
public class Roots {

	/**
	 * Demonstration of {@link Roots}.
	 * 
	 * @param args
	 *            First argument is real part of a complex number, second is
	 *            imaginary part and third is the root number, that has to be
	 *            bigger than 1.
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Invalid number of arguments.");
			System.exit(0);
		}

		double real = Double.parseDouble(args[0]);
		double img = Double.parseDouble(args[1]);
		int n = Integer.parseInt(args[2]);

		if (n <= 1) {
			System.out.println("Invalid root number.");
			System.exit(0);
		}

		double r = Math.sqrt(real * real + img * img);
		double fi = Math.atan2(img, real);
		double realRoot = Math.pow(r, (1.0 / n));

		System.out.println("You requested calculation of " + n + " roots. Solutions are: ");

		for (int i = 0; i < n; i++) {
			double x = realRoot * (Math.cos((fi + 2 * i * Math.PI) / n));
			double y = realRoot * (Math.sin((fi + 2 * i * Math.PI) / n));

			System.out.printf("%d) %f %+fi%n", i + 1, x, y);
		}
	}

}
