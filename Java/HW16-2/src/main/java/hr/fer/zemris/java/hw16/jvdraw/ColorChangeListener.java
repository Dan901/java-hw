package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;

/**
 * Interface for listeners in observer design pattern, that listen for color
 * selection changes.
 *
 * @author Dan
 */
public interface ColorChangeListener {

	/**
	 * Called when a new color is selected.
	 * 
	 * @param source
	 *            source of the notification
	 * @param oldColor
	 *            old color
	 * @param newColor
	 *            new color
	 */
	void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
