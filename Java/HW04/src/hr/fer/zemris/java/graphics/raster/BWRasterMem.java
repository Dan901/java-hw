package hr.fer.zemris.java.graphics.raster;

/**
 * This class is an implementation of {@link BWRaster} which keeps all of its
 * data in memory. When creating new instances of this class, all pixels are
 * turned off.
 * 
 * @author Dan
 *
 */
public class BWRasterMem implements BWRaster {

	/**
	 * Width of this raster.
	 */
	private int width;
	/**
	 * Height of this raster.
	 */
	private int height;

	/**
	 * True if flip mode is currently enabled.
	 */
	private boolean flipEnabled;

	/**
	 * Array of size height * width, representing pixels (true when turned on).
	 */
	private boolean[][] pixels;

	/**
	 * Creates new {@code BWRasterMem} with given dimensions.
	 * 
	 * @param width
	 *            width of the raster
	 * @param height
	 *            height of the raster
	 */
	public BWRasterMem(int height, int width) {
		if (width < 1 || height < 1) {
			throw new IllegalArgumentException("Invalid raster dimensions.");
		}

		this.width = width;
		this.height = height;
		flipEnabled = false;
		pixels = new boolean[height][width];
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void clear() {
		for (boolean[] row : pixels) {
			for (int i = 0; i < width; i++) {
				row[i] = false;
			}
		}
	}

	@Override
	public void turnOn(int x, int y) {
		checkCoordiantes(x, y);

		if (flipEnabled) {
			pixels[y][x] = pixels[y][x] ? false : true;
		} else {
			pixels[y][x] = true;
		}
	}

	@Override
	public void turnOff(int x, int y) {
		checkCoordiantes(x, y);
		pixels[y][x] = false;
	}

	@Override
	public void enableFlipMode() {
		flipEnabled = true;
	}

	@Override
	public void disableFlipMode() {
		flipEnabled = false;
	}

	@Override
	public boolean isTurnedOn(int x, int y) {
		checkCoordiantes(x, y);
		return pixels[y][x];
	}

	/**
	 * Checks if given coordinates are valid for this raster and throws
	 * exception if not.
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 */
	private void checkCoordiantes(int x, int y) {
		if (x < 0 || x >= width) {
			throw new IllegalArgumentException("Invalid x coordiante.");
		}

		if (y < 0 || y >= height) {
			throw new IllegalArgumentException("Invalid y coordiante.");
		}
	}

}
