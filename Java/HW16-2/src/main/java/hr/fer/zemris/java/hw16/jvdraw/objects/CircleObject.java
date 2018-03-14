package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Circle object defined by center and a radius.
 *
 * @author Dan
 */
public class CircleObject extends GeometricalObject {

	/** Radius of this {@code CircleObject}. */
	protected int radius;

	/**
	 * Setter for radius.
	 * 
	 * @param radius
	 *            radius to set
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		secondPoint = new Point(firstPoint.x + radius, firstPoint.y + radius);
	}

	@Override
	public void setFirstPoint(Point firstPoint) {
		super.setFirstPoint(firstPoint);
		calculateRadius();
	}

	@Override
	public void setSecondPoint(Point secondPoint) {
		super.setSecondPoint(secondPoint);
		calculateRadius();
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(fgColor);
		Rectangle r = getSquare();
		g.drawOval(r.x, r.y, r.width, r.height);
	}

	@Override
	public String toJVDFormat() {
		return String.format("CIRCLE %d %d %d %d %d %d ", firstPoint.x, firstPoint.y, radius, fgColor.getRed(),
				fgColor.getGreen(), fgColor.getBlue());
	}

	/**
	 * Gets surrounding square of this {@code CircleObject}.
	 * 
	 * @return surrounding square
	 */
	protected Rectangle getSquare() {
		return new Rectangle(firstPoint.x - radius, firstPoint.y - radius, 2 * radius, 2 * radius);
	}

	/**
	 * Calculates radius of this {@code CircleObject}.
	 */
	private void calculateRadius() {
		if (firstPoint == null || secondPoint == null) {
			radius = 0;
		} else {
			int dx = firstPoint.x - secondPoint.x;
			int dy = firstPoint.y - secondPoint.y;
			radius = (int) Math.sqrt(dx * dx + dy * dy);
		}
	}
}
