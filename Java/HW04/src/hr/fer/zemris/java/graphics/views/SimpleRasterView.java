package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Implementation of {@link RasterView} which prints the given {@link BWRaster}
 * to standard output.
 * 
 * @author Dan
 *
 */
public class SimpleRasterView implements RasterView {

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
	 * Creates a new {@code SimpleRasterView} with specified characters for
	 * representation of turned on and turned off pixels.
	 * 
	 * @param on
	 *            representation of turned on pixel
	 * @param off
	 *            representation of turned on pixel
	 */
	public SimpleRasterView(char on, char off) {
		this.on = on;
		this.off = off;
	}

	/**
	 * Creates a new {@code SimpleRasterView} with default characters for
	 * representation of turned on and turned off pixels.
	 */
	public SimpleRasterView() {
		this(DEFAULT_ON, DEFAULT_OFF);
	}

	/**
	 * Prints the given raster to standard output.
	 * 
	 * @return {@code null}
	 */
	@Override
	public Object produce(BWRaster raster) {
		StringRasterView view = new StringRasterView(on, off);

		System.out.println((String) view.produce(raster));

		return null;
	}

}
