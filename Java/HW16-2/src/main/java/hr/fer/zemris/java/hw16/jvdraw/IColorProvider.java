package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;

/**
 * Interface for objects than provide color for which observers might be
 * interested in.
 *
 * @author Dan
 */
public interface IColorProvider {

	/**
	 * Gets current color.
	 * 
	 * @return current color
	 */
	Color getCurrentColor();
}
