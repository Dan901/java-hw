package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;

import hr.fer.zemris.java.hw16.jvdraw.JColorArea;

/**
 * Factory for {@link GeometricalObject} objects.
 *
 * @author Dan
 */
public class GeometricalObjectFactory {

	/** Provider that creates new instances of {@code GeometricalObjects}. */
	private GeometricalObjectProvider provider;

	/** Generic name for new {@code GeometricalObjects}. */
	private String name;

	/** Foreground color of new {@code GeometricalObjects}. */
	private Color fgColor;

	/** Background color of new {@code GeometricalObjects}. */
	private Color bgColor;

	/** Number of created {@code GeometricalObjects}. */
	private int numberOfCreatedObjects;

	/**
	 * Creates a new {@code GeometricalObjectFactory} with given arguments.
	 * 
	 * @param provider
	 *            provider that creates new instances of
	 *            {@code GeometricalObjects}
	 * @param name
	 *            generic name for new {@code GeometricalObjects}
	 * @param fgColorArea
	 *            foreground color
	 * @param bgColorArea
	 *            background color
	 */
	public GeometricalObjectFactory(GeometricalObjectProvider provider, String name, JColorArea fgColorArea,
			JColorArea bgColorArea) {
		this.provider = provider;
		this.name = name;
		fgColor = fgColorArea.getCurrentColor();
		bgColor = bgColorArea.getCurrentColor();
		fgColorArea.addColorChangeListener((src, oldC, newC) -> fgColor = newC);
		bgColorArea.addColorChangeListener((src, oldC, newC) -> bgColor = newC);
	}

	/**
	 * Factory method for new {@code GeometricalObjects}.
	 * 
	 * @return {@code GeometricalObject} specific for this factory
	 */
	public GeometricalObject createObject() {
		GeometricalObject object = provider.getObject();
		object.setFgColor(fgColor);
		object.setBgColor(bgColor);
		object.setName(name + " " + ++numberOfCreatedObjects);
		return object;
	}
}
