package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Abstract class for all geometrical objects that can be defined by two
 * {@link Point} objects.
 *
 * @author Dan
 */
public abstract class GeometricalObject {

	/** First point defining this {@code GeometricalObject}. */
	protected Point firstPoint;

	/** Second point defining this {@code GeometricalObject}. */
	protected Point secondPoint;

	/** Foreground color of this {@code GeometricalObject}. */
	protected Color fgColor;

	/** Background color of this {@code GeometricalObject}. */
	protected Color bgColor;

	/** Name of this {@code GeometricalObject}. */
	private String name;

	/**
	 * Setter for the first point.
	 * 
	 * @param firstPoint
	 *            first point defining this {@code GeometricalObject}
	 */
	public void setFirstPoint(Point firstPoint) {
		this.firstPoint = firstPoint;
	}

	/**
	 * Setter for the second point.
	 * 
	 * @param secondPoint
	 *            second point defining this {@code GeometricalObject}
	 */
	public void setSecondPoint(Point secondPoint) {
		this.secondPoint = secondPoint;
	}

	/**
	 * Setter for foreground color.
	 * 
	 * @param fgColor
	 *            foreground color to set
	 */
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	/**
	 * Setter for background color.
	 * 
	 * @param bgColor
	 *            background color to set
	 */
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * Setter for name.
	 * 
	 * @param name
	 *            name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Draws this {@code GeometricalObject}.
	 * 
	 * @param g
	 *            {@code Graphics2D} to draw on
	 */
	abstract public void draw(Graphics2D g);

	/**
	 * Builds a {@code String} in JVD format with information about this
	 * {@code GeometricalObject}.
	 * 
	 * @return a JVD formatted {@code String}
	 */
	abstract public String toJVDFormat();

}
