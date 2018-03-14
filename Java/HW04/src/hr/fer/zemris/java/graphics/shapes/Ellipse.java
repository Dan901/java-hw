package hr.fer.zemris.java.graphics.shapes;

/**
 * This class represents an ellipse and inherits {@link Oval}. It's defined by
 * horizontal and vertical radiuses and coordinates of the center.
 * 
 * @author Dan
 *
 */
public class Ellipse extends Oval {

	/**
	 * Creates a new {@code Ellipse} with given parameters.
	 * 
	 * @param horizontalRadius
	 *            horizontal radius
	 * @param verticalRadius
	 *            vertical radius
	 * @param x
	 *            x coordinate of the center
	 * @param y
	 *            y coordinate of the center
	 */
	public Ellipse(int x, int y, int horizontalRadius, int verticalRadius) {
		super(x, y, horizontalRadius, verticalRadius);
	}

	/**
	 * @return the horizontal radius
	 */
	public int getHorizontalRadius() {
		return horizontalRadius;
	}

	/**
	 * Sets the horizontal radius to a new value.
	 * 
	 * @param horizontalRadius
	 *            new radius
	 */
	public void setHorizontalRadius(int horizontalRadius) {
		if (horizontalRadius < 1) {
			throw new IllegalArgumentException("Radius has to be at least 1.");
		}

		this.horizontalRadius = horizontalRadius;
	}

	/**
	 * @return the vertical radius
	 */
	public int getVerticalRadius() {
		return verticalRadius;
	}

	/**
	 * Sets the vertical radius to a new value.
	 * 
	 * @param verticalRadius
	 *            new radius
	 */
	public void setVerticalRadius(int verticalRadius) {
		if (verticalRadius < 1) {
			throw new IllegalArgumentException("Radius has to be at least 1.");
		}

		this.verticalRadius = verticalRadius;
	}

}
