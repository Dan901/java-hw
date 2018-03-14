package hr.fer.zemris.java.gui.charts;

import java.util.Comparator;

/**
 * This class represents a 2D point with x and y coordinates.
 * 
 * @author Dan
 *
 */
public class XYValue {

	/**
	 * {@link Comparator} that compares {@code XYValue} objects based on their x
	 * coordinate.
	 */
	public static final Comparator<XYValue> COMPARE_BY_X = (v1, v2) -> Integer.compare(v1.getX(), v2.getX());

	/**
	 * Parses the given coordinates into a {@link XYValue} object.
	 * 
	 * @param s
	 *            {@code String} in format: {@code "x,y"}
	 * @return new {@code XYValue} object
	 */
	public static XYValue fromString(String s) {
		String[] elements = s.split(",");
		if (elements.length != 2) {
			throw new IllegalArgumentException("Illegal string format: " + s);
		}

		return new XYValue(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]));
	}

	/** x coordinate of this object. */
	private int x;

	/** y coordinate of this object. */
	private int y;

	/**
	 * Creates a new {@link XYValue} with given coordinates.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return x coordinate of this object
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return y coordinate of this object
	 */
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XYValue other = (XYValue) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
