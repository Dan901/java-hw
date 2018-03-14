package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Graphics2D;

/**
 * Line object defined by two points.
 *
 * @author Dan
 */
public class LineObject extends GeometricalObject {

	@Override
	public void draw(Graphics2D g) {
		g.setColor(fgColor);
		g.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
	}

	@Override
	public String toJVDFormat() {
		return String.format("LINE %d %d %d %d %d %d %d", firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y,
				fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
	}
	
}
