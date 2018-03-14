package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Abstract class for all geometric shapes.
 * 
 * @author Dan
 *
 */
public abstract class GeometricShape {

	/**
	 * Checks if the specified point belongs to this {@code GeometricShape}.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return {@code false} if the specified point is outside the shape;
	 *         {@code true} otherwise
	 */
	public abstract boolean containsPoint(int x, int y);

	/**
	 * Draws this filled {@code GeometricShape} on given {@link BWRaster}.
	 * 
	 * @param r
	 *            raster to draw on.
	 */
	public void draw(BWRaster r) {
		int width = r.getWidth();
		int height = r.getHeight();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (containsPoint(x, y)) {
					r.turnOn(x, y);
				}
			}
		}
	}
}
