package hr.fer.zemris.java.tecaj.hw2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents an unmodifiable complex number and offers basic
 * operations over complex numbers. It offers a constructor as well as 4 static
 * factory methods for creating instances of it.
 * 
 * @author Dan
 *
 */
public class ComplexNumber {

	/**
	 * Real part of a complex number.
	 */
	private double real;

	/**
	 * Imaginary part of a complex number.
	 */
	private double imaginary;

	/**
	 * Magnitude of a complex number.
	 */
	private double magnitude;

	/**
	 * Angle (in polar coordinates) of a complex number from 0 to 2<tt>&pi;</tt>
	 * .
	 */
	private double angle;

	/**
	 * Creates a complex number with given arguments.
	 * 
	 * @param real
	 *            real part
	 * @param imaginary
	 *            imaginary part
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;

		magnitude = Math.sqrt(real * real + imaginary * imaginary);

		angle = Math.atan2(imaginary, real);
		if (angle < 0) {
			angle += 2 * Math.PI;
		}
	}

	/**
	 * Creates a complex number without imaginary part (<tt>img(z) = 0</tt>).
	 * 
	 * @param real
	 *            real part
	 * @return a complex number without imaginary part
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}

	/**
	 * Creates a complex number without real part (<tt>real(z) = 0</tt>).
	 * 
	 * @param imaginary
	 *            imaginary part
	 * @return a complex number without real part
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}

	/**
	 * Creates a complex number with given polar coordinates.
	 * 
	 * @param magnitude
	 *            magnitude of a complex number
	 * @param angle
	 *            angle of a complex number
	 * @return a complex number
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	/**
	 * Converts a string to a complex number. Valid input is for example
	 * "2.3 - 3i", any part (real or imaginary) can be omitted.
	 * 
	 * @param s
	 *            string to parse
	 * @return a complex number
	 * @throws IllegalArgumentException
	 *             if given string does not contain a valid complex number
	 *             format
	 */
	public static ComplexNumber parse(String s) {
		if (s.isEmpty()) {
			throw new IllegalArgumentException("Empty string.");
		}

		s = s.replaceAll("\\s", "");
		Pattern pattern = Pattern.compile("([+-]?\\d*(\\.\\d+)?i?)([+-]\\d*(\\.\\d+)?i?)?", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);

		if (matcher.matches()) {
			String imgString, realString;

			if (isImaginary(matcher.group(1))) {
				realString = "0";
				imgString = matcher.group(1);
			} else if (matcher.group(3) == null) {
				realString = matcher.group(1);
				imgString = "0i";
			} else if (isImaginary(matcher.group(3))) {
				realString = matcher.group(1);
				imgString = matcher.group(3);
			} else {
				throw new IllegalArgumentException("Invalid complex number format.");
			}

			double real = Double.parseDouble(realString);
			double imaginary;
			String shortImg = imgString.substring(0, imgString.length() - 1); //without 'i' at the end

			if (shortImg.equals("+") || shortImg.length() == 0) {
				imaginary = 1;
			} else if (shortImg.equals("-")) {
				imaginary = -1;
			} else {
				imaginary = Double.parseDouble(shortImg);
			}

			return new ComplexNumber(real, imaginary);
		} else {
			throw new IllegalArgumentException("Not a complex number.");
		}
	}

	/**
	 * Helper method for parsing strings.
	 * 
	 * @param value
	 *            string to check
	 * @return {@code true} only if given string ends with "i"
	 */
	private static boolean isImaginary(String value) {
		value = value.toLowerCase();
		if (value.endsWith("i")) {
			return true;
		}
		return false;
	}

	/**
	 * @return the real part of a complex number
	 */
	public double getReal() {
		return real;
	}

	/**
	 * @return the imaginary part of a complex number
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * @return the magnitude of a complex number
	 */
	public double getMagnitude() {
		return magnitude;
	}

	/**
	 * @return the angle of a complex number
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Adds the given complex number to this number and returns the result as a
	 * new complex number.
	 * 
	 * @param c
	 *            number to add
	 * @return the result of addition
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(real + c.getReal(), imaginary + c.getImaginary());
	}

	/**
	 * Subtracts the given complex number from this number and returns the
	 * result as a new complex number.
	 * 
	 * @param c
	 *            number to subtract
	 * @return the result of subtraction
	 */
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(real - c.getReal(), imaginary - c.getImaginary());
	}

	/**
	 * Multiplies the given complex number with this number and returns the
	 * result as a new complex number.
	 * 
	 * @param c
	 *            number to multiply with
	 * @return the result of multiplication
	 */
	public ComplexNumber mul(ComplexNumber c) {
		return fromMagnitudeAndAngle(magnitude * c.getMagnitude(), angle + c.getAngle());
	}

	/**
	 * Divides this number with the given complex number and returns the result
	 * as a new complex number.
	 * 
	 * @param c
	 *            number to divide with
	 * @return the result of division
	 */
	public ComplexNumber div(ComplexNumber c) {
		return fromMagnitudeAndAngle(magnitude / c.getMagnitude(), angle - c.getAngle());
	}

	/**
	 * Raises this complex number to the n-th power and returns the result as a
	 * new complex number.
	 * 
	 * @param n
	 *            power
	 * @return the result of exponentiation
	 * @throws IllegalArgumentException
	 *             if n is negative
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Power cannot be negative");
		}

		return fromMagnitudeAndAngle(Math.pow(magnitude, n), angle * n);
	}

	/**
	 * Calculates n roots from this complex number.
	 * 
	 * @param n
	 *            root number
	 * @return all roots in an array
	 * @throws IllegalArgumentException
	 *             if n is not positive
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Root must be positive.");
		}

		ComplexNumber[] roots = new ComplexNumber[n];

		double realRoot = Math.pow(magnitude, (1.0 / n));

		for (int i = 0; i < n; i++) {
			double x = realRoot * (Math.cos((angle + 2 * i * Math.PI) / n));
			double y = realRoot * (Math.sin((angle + 2 * i * Math.PI) / n));

			roots[i] = new ComplexNumber(x, y);
		}

		return roots;
	}

	@Override
	public String toString() {
		return String.format("%f %+fi", real, imaginary);
	}

}
