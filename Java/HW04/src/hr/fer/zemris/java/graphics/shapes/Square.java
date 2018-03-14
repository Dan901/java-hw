package hr.fer.zemris.java.graphics.shapes;

/**
 * This class represents a square. Since square is a special case of rectangle
 * it inherits class {@link AbstractRectangle}. It's defined by coordinates of
 * its top-left corner and size.
 * 
 * @author Dan
 *
 */
public class Square extends AbstractRectangle {

	/**
	 * Creates a new {@code Square} with given parameters.
	 * 
	 * @param size
	 *            size of the side, has to be positive
	 * @param x
	 *            x coordinate of the top-left corner
	 * @param y
	 *            y coordinate of the top-left corner
	 */
	public Square(int x, int y, int size) {
		super(x, y, size, size);
	}

	/**
	 * @return length of the side
	 */
	public int getSize() {
		return height;
	}

	/**
	 * Sets the side of this square to a new positive value.
	 * 
	 * @param size
	 *            side of the square
	 */
	public void setSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size of the square has to be positive.");
		}

		height = width = size;
	}

}
