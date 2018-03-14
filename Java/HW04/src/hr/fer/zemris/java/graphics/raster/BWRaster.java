package hr.fer.zemris.java.graphics.raster;

/**
 * Black-and-White raster. This is an abstraction for all raster devices of
 * fixed width and height for which each pixel can be painted with only two
 * colors: black (when pixel is turned off) and white (when pixel is turned on).
 * 
 * @author Dan
 *
 */
public interface BWRaster {

	/**
	 * @return width of this raster
	 */
	public int getWidth();

	/**
	 * @return height of this raster
	 */
	public int getHeight();

	/**
	 * Turns off all pixels in this raster.
	 */
	public void clear();

	/**
	 * Turns on specified pixel unless flip mode is enabled, in which case pixel
	 * state is changed. In other words, if it was turned on it will be turned
	 * off and if it was turned off it will be turned on.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @throws IllegalArgumentException
	 *             if given coordinates are not valid for this raster
	 */
	public void turnOn(int x, int y);

	/**
	 * Turns off specified pixel.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @throws IllegalArgumentException
	 *             if given coordinates are not valid for this raster
	 */
	public void turnOff(int x, int y);

	/**
	 * Enables flip mode. When turned on, method {@link #turnOn(int, int)} flips
	 * the state of the pixel.
	 */
	public void enableFlipMode();

	/**
	 * Disables flip mode.
	 */
	public void disableFlipMode();

	/**
	 * Checks if specified pixel is turned on.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return {@code true} is specified pixel is turned on; {@code false}
	 *         otherwise
	 * @throws IllegalArgumentException
	 *             if given coordinates are not valid for this raster
	 */
	public boolean isTurnedOn(int x, int y);
}
