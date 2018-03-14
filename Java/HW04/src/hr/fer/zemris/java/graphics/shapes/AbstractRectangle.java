package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * This class represents an abstract rectangle and inherits class
 * {@link GeometricShape}. It's defined by coordinates of its top-left corner
 * and length of both sides.
 * 
 * @author Dan
 *
 */
public abstract class AbstractRectangle extends GeometricShape {

	/**
	 * Height of the rectangle.
	 */
	protected int height;
	/**
	 * Width of the rectangle.
	 */
	protected int width;
	/**
	 * x coordinate of the top-left corner
	 */
	private int x;
	/**
	 * y coordinate of the top-left corner
	 */
	private int y;

	/**
	 * Creates a new {@code AbstractRectangle} with given parameters.
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
	public AbstractRectangle(int x, int y, int width, int height) {
		if (height <= 0 || width <= 0) {
			throw new IllegalArgumentException("Height and width have to be at least 1.");
		}

		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean containsPoint(int x, int y) {
		// @formatter:off
		if (x < this.x || x >= this.x + this.width)	return false;
		if (y < this.y || y >= this.y + this.height) return false;
		// @formatter:on

		return true;
	}

	/**
	 * @return the x coordinate of the top-left corner
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x coordinate of the top-left corner to a new value.
	 * 
	 * @param x
	 *            x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y coordinate of the top-left corner
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y coordinate of the top-left corner to a new value.
	 * 
	 * @param y
	 *            y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public void draw(BWRaster r) {
		int lowerY = Math.max(0, this.y);
		int upperY = Math.min(r.getHeight(), this.y + height);
		int lowerX = Math.max(0, this.x);
		int upperX = Math.min(r.getWidth(), this.x + width);
		
		for(int y = lowerY; y < upperY; y++){
			for(int x = lowerX; x < upperX; x++){
				r.turnOn(x, y);
			}
		}
	}

}
