package hr.fer.zemris.java.graphics.shapes;

/**
 * This class represents a circle and inherits {@link Oval}. It's defined by a
 * radius and coordinates of the center.
 * 
 * @author Dan
 *
 */
public class Circle extends Oval {

	/**
	 * Creates a new {@code Circle} with given parameters.
	 * 
	 * @param radius
	 *            radius of the circle
	 * @param x
	 *            x coordinate of the center
	 * @param y
	 *            y coordinate of the center
	 */
	public Circle(int x, int y, int radius) {
		super(x, y, radius, radius);
	}

	/**
	 * @return the radius of the circle
	 */
	public int getRadius() {
		return horizontalRadius;
	}

	/**
	 * Sets the radius to a new value.
	 * 
	 * @param radius
	 *            new radius
	 */
	public void setRadius(int radius) {
		if (radius < 1) {
			throw new IllegalArgumentException("Radius has to be at least 1.");
		}

		horizontalRadius = verticalRadius = radius;
	}
}
