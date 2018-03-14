package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Interface for implementation of drawing {@link BWRaster}.
 * 
 * @author Dan
 *
 */
public interface RasterView {

	/**
	 * Produces a new object that visually represents the given raster.
	 * 
	 * @param raster
	 *            raster to visualize
	 * @return visual representation of the given raster
	 */
	public Object produce(BWRaster raster);
}
