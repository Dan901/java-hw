package hr.fer.zemris.java.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hr.fer.zemris.java.graphics.raster.BWRaster;
import hr.fer.zemris.java.graphics.raster.BWRasterMem;
import hr.fer.zemris.java.graphics.shapes.Circle;
import hr.fer.zemris.java.graphics.shapes.Ellipse;
import hr.fer.zemris.java.graphics.shapes.GeometricShape;
import hr.fer.zemris.java.graphics.shapes.Rectangle;
import hr.fer.zemris.java.graphics.shapes.Square;
import hr.fer.zemris.java.graphics.views.RasterView;
import hr.fer.zemris.java.graphics.views.SimpleRasterView;

/**
 * Demonstration of shapes and drawing. Program accepts width and height of the
 * raster as one or two command line arguments.
 * 
 * @author Dan
 *
 */
public class Demo {

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws IOException
	 *             if IO exception occurs
	 */
	public static void main(String[] args) throws IOException {
		int height = 0, width = 0;

		try {
			if (args.length == 1) {
				height = width = Integer.parseInt(args[0]);
			} else if (args.length == 2) {
				width = Integer.parseInt(args[0]);
				height = Integer.parseInt(args[1]);
			} else {
				System.out.println("Invalid number of arguments.");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid number format.");
			return;
		}

		BWRaster raster;
		try {
			raster = new BWRasterMem(height, width);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid number of arguments.");
			return;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		int n;
		try {
			n = Integer.parseInt(reader.readLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("Invalid number format.");
			return;
		}

		GeometricShape[] shapes = new GeometricShape[n];

		for (int i = 0; i < n; i++) {
			try {
				shapes[i] = getShape(reader.readLine().trim());
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Invalid number of arguments.");
				return;
			} catch (NumberFormatException e) {
				System.out.println("Invalid number format.");
				return;
			} catch (UnsupportedOperationException e) {
				System.out.println("Invalid shape " + e.getMessage());
				return;
			}
		}

		boolean flipEnabled = false;
		for (GeometricShape shape : shapes) {
			if (shape == null) {
				if (flipEnabled) {
					flipEnabled = false;
					raster.disableFlipMode();
				} else {
					flipEnabled = true;
					raster.enableFlipMode();
				}
			} else {
				shape.draw(raster);
			}
		}

		RasterView view = new SimpleRasterView();
		view.produce(raster);
	}

	/**
	 * Produces a {@code GeometricShape} from {@code String}.
	 * 
	 * @param line
	 *            {@code String} containing name and necessary parameters
	 * @return new {@code GeometricShape}
	 */
	private static GeometricShape getShape(String line) {
		String[] words = line.split("\\s+");

		switch (words[0]) {
		case "FLIP":
			return null;
		case "RECTANGLE":
			return new Rectangle(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Integer.parseInt(words[3]),
					Integer.parseInt(words[4]));
		case "SQUARE":
			return new Square(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Integer.parseInt(words[3]));
		case "ELLIPSE":
			return new Ellipse(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Integer.parseInt(words[3]),
					Integer.parseInt(words[4]));
		case "CIRCLE":
			return new Circle(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Integer.parseInt(words[3]));
		default:
			throw new UnsupportedOperationException(words[0]);
		}

	}

}
