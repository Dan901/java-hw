package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Implementation of {@link RasterView} which produces a {@code String} from
 * given {@link BWRaster}.
 * 
 * @author Dan
 *
 */
public class StringRasterView implements RasterView {

	/**
	 * Default representation of turned on pixel.
	 */
	private static final char DEFAULT_ON = '*';
	/**
	 * Default representation of turned off pixel.
	 */
	private static final char DEFAULT_OFF = '.';
	/**
	 * Representation of turned on pixel.
	 */
	private char on;
	/**
	 * Representation of turned off pixel.
	 */
	private char off;

	/**
	 * Creates a new {@code StringRasterView} with specified characters for
	 * representation of turned on and turned off pixels.
	 * 
	 * @param on
	 *            representation of turned on pixel
	 * @param off
	 *            representation of turned on pixel
	 */
	public StringRasterView(char on, char off) {
		this.on = on;
		this.off = off;
	}

	/**
	 * Creates a new {@code StringRasterView} with default characters for
	 * representation of turned on and turned off pixels.
	 */
	public StringRasterView() {
		this(DEFAULT_ON, DEFAULT_OFF);
	}

	/**
	 * Produces a {@code String} from the given raster.
	 * 
	 * @return newly created {@code String}
	 */
	@Override
	public Object produce(BWRaster raster) {
		StringBuilder s = new StringBuilder();
		int width = raster.getWidth();
		int height = raster.getHeight();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (raster.isTurnedOn(x, y)) {
					s.append(on);
				} else {
					s.append(off);
				}
			}

			s.append('\n');
		}

		return s.toString().trim();
	}

}
