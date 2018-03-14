package hr.fer.zemris.java.graphics.shapes;

/**
 * This class represents a rectangle and inherits {@link AbstractRectangle}.
 * It's defined by coordinates of its top-left corner and length of both sides.
 * 
 * @author Dan
 *
 */
public class Rectangle extends AbstractRectangle {

	/**
	 * Creates a new {@code Rectangle} with given parameters.
	 * 
	 * @param height
	 *            height of the rectangle, has to be positive
	 * @param width
	 *            width of the rectangle, has to be positive
	 * @param x
	 *            x coordinate of the top-left corner
	 * @param y
	 *            y coordinate of the top-left corner
	 */
	public Rectangle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	/**
	 * Sets the height of the rectangle to a new value.
	 * 
	 * @param height
	 *            height to set
	 */
	public void setHeight(int height) {
		if (height <= 0) {
			throw new IllegalArgumentException("Height has to be positive.");
		}

		this.height = height;
	}

	/**
	 * Sets the width of the rectangle to a new value.
	 * 
	 * @param width
	 *            width to set
	 */
	public void setWidth(int width) {
		if (width <= 0) {
			throw new IllegalArgumentException("Width has to be positive.");
		}

		this.width = width;
	}

	/**
	 * @return height of this object
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return width of this object
	 */
	public int getWidth() {
		return width;
	}

}
