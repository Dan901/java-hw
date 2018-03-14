package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.objects.CircleObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircleObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.LineObject;

/**
 * Utility class for working with JVD files.
 *
 * @author Dan
 */
public class JVDFileUtil {

	/**
	 * Saves all objects from the given {@code DrawingModel} to the file.
	 * 
	 * @param model
	 *            model with all objects
	 * @param file
	 *            file to write in
	 */
	public static void saveModelToFile(DrawingModel model, Path file) {
		try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8);) {
			int n = model.getSize();
			for (int i = 0; i < n; i++) {
				writer.write(model.getObject(i).toJVDFormat());
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("An error occurred while writing to file.");
		}
	}

	/**
	 * Adds {@code ".jvd"} extension to the given file if it already doesn't
	 * have it.
	 * 
	 * @param file
	 *            file to modify
	 * @return the given file with appropriate extension
	 */
	public static Path addExtension(File file) {
		if (!file.toString().endsWith(".jvd")) {
			return Paths.get(file.toString() + ".jvd");
		} else {
			return file.toPath();
		}
	}

	/**
	 * Clears the given {@code DrawingModel} and fills it with objects from the
	 * JVD file.
	 * 
	 * @param model
	 *            model to store the objects in
	 * @param file
	 *            file in JVD format with object information
	 */
	public static void loadModelFromFile(DrawingModel model, Path file) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("An error occurred while reading from file.");
			return;
		}

		model.clear();

		int lineN = 0, circleN = 0, fcircleN = 0;
		for (String line : lines) {
			String[] elems = line.split("\\s+");
			String name = elems[0];
			try {
				if (name.equals("LINE")) {
					LineObject object = new LineObject();
					object.setFirstPoint(new Point(Integer.parseInt(elems[1]), Integer.parseInt(elems[2])));
					object.setSecondPoint(new Point(Integer.parseInt(elems[3]), Integer.parseInt(elems[4])));
					object.setFgColor(new Color(Integer.parseInt(elems[5]), Integer.parseInt(elems[6]),
							Integer.parseInt(elems[7])));
					object.setName("Line " + ++lineN);
					model.add(object);
				} else if (name.equals("CIRCLE")) {
					CircleObject object = new CircleObject();
					object.setFirstPoint(new Point(Integer.parseInt(elems[1]), Integer.parseInt(elems[2])));
					object.setRadius(Integer.parseInt(elems[3]));
					object.setFgColor(new Color(Integer.parseInt(elems[4]), Integer.parseInt(elems[5]),
							Integer.parseInt(elems[6])));
					object.setName("Circle " + ++circleN);
					model.add(object);
				} else if (name.equals("FCIRCLE")) {
					FilledCircleObject object = new FilledCircleObject();
					object.setFirstPoint(new Point(Integer.parseInt(elems[1]), Integer.parseInt(elems[2])));
					object.setRadius(Integer.parseInt(elems[3]));
					object.setFgColor(new Color(Integer.parseInt(elems[4]), Integer.parseInt(elems[5]),
							Integer.parseInt(elems[6])));
					object.setBgColor(new Color(Integer.parseInt(elems[7]), Integer.parseInt(elems[8]),
							Integer.parseInt(elems[9])));
					object.setName("Filled circle " + ++fcircleN);
					model.add(object);
				}
			} catch (NumberFormatException | IndexOutOfBoundsException e) {
				System.err.println("Invalid file format.");
				return;
			}
		}
	}

}
