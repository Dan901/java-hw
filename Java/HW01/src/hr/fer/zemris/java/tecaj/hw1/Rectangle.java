package hr.fer.zemris.java.tecaj.hw1;

import java.util.Scanner;

/**
 * This program calculates area and perimeter of a rectangle. Width and height
 * can be given as command line arguments or once the program is running. They
 * can't be negative.
 * 
 * @author Dan
 *
 */
public class Rectangle {

	/**
	 * Demonstration of a program for calculating area and perimeter of a
	 * rectangle.
	 * 
	 * @param args
	 *            If not empty, there has to be 2 positive real numbers
	 *            representing width and height of a rectangle.
	 */
	public static void main(String[] args) {
		double height = 0, width = 0;

		if (args.length == 0) {
			Scanner sc = new Scanner(System.in);
			width = input("width", sc);
			height = input("height", sc);
			sc.close();
		} else {
			if (args.length == 2) {
				width = Double.parseDouble(args[0]);
				height = Double.parseDouble(args[1]);
			} else {
				System.err.println("Invalid number of arguments was provided.");
				System.exit(0);
			}
		}

		double area = width * height;
		double perimeter = 2 * (width + height);

		System.out.println("You have specified a rectangle of width " + width + " and height " + height
				+ ". Its area is " + area + " and its perimeter is " + perimeter + ".");
	}

	/**
	 * Reads a line from given {@code Scanner} until it reads a positive number,
	 * which it than returns.
	 * 
	 * @param arg
	 *            Name of the value that has to be read.
	 * @param sc
	 *            {@code Scanner} to read from.
	 * @return Positive number read from {@code Scanner}.
	 */
	public static double input(String arg, Scanner sc) {
		double x = -1;

		while (true) {
			System.out.print("Please provide " + arg + ": ");
			String line = sc.nextLine();
			line.trim();

			if (line.isEmpty()) {
				System.out.println("Nothing was given.");
				continue;
			} else {
				x = Double.parseDouble(line);
			}

			if (x >= 0) {
				return x;
			} else
				System.out.println("The " + arg + " must not be negative.");
		}
	}
}
