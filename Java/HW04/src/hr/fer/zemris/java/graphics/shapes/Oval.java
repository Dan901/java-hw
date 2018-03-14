package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * This class represents an abstract oval and inherits class
 * {@link GeometricShape}. It's defined by horizontal and vertical radiuses and
 * coordinates of the center.
 * 
 * @author Dan
 *
 */
public abstract class Oval extends GeometricShape {

	/**
	 * Horizontal radius.
	 */
	protected int horizontalRadius;
	/**
	 * Vertical radius.
	 */
	protected int verticalRadius;
	/**
	 * x coordinate of the center
	 */
	private int x;
	/**
	 * y coordinate of the center
	 */
	private int y;

	/**
	 * Creates a new {@code Oval} with given parameters.
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
	public Oval(int x, int y, int horizontalRadius, int verticalRadius) {
		if (horizontalRadius < 1 || verticalRadius < 1) {
			throw new IllegalArgumentException("Radius has to be at least 1.");
		}

		this.horizontalRadius = horizontalRadius;
		this.verticalRadius = verticalRadius;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x coordinate of the center
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x coordinate of the center to a new value.
	 * 
	 * @param x
	 *            new coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y coordinate of the center
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y coordinate of the center to a new value.
	 * 
	 * @param y
	 *            new coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public boolean containsPoint(int x, int y) {
		if(x == this.x && y == this.y){
			return true;
		}
		
		double expr = (Math.pow(x - this.x, 2) / Math.pow(horizontalRadius - 1, 2))
				+ (Math.pow(y - this.y, 2) / Math.pow(verticalRadius - 1, 2));

		return expr <= 1.0;
	}
	
	@Override
	public void draw(BWRaster r) {
		int lowerY = Math.max(0, this.y - verticalRadius);
		int upperY = Math.min(r.getHeight(), this.y + verticalRadius + 1);
		int lowerX = Math.max(0, this.x - horizontalRadius);
		int upperX = Math.min(r.getWidth(), this.x + horizontalRadius + 1);
		
		for(int y = lowerY; y < upperY; y++){
			for(int x = lowerX; x < upperX; x++){
				if (containsPoint(x, y)) {
					r.turnOn(x, y);
				}
			}
		}
	}

}
