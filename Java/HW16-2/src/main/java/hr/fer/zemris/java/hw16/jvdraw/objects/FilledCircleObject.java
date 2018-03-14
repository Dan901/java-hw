package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Same as {@link CircleObject} just filled with background color.
 *
 * @author Dan
 */
public class FilledCircleObject extends CircleObject {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		g.setColor(bgColor);
		Rectangle r = getSquare();
		g.fillOval(r.x + 1, r.y + 1, r.width - 1, r.height - 1);
	}

	@Override
	public String toJVDFormat() {
		return String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", firstPoint.x, firstPoint.y, radius, fgColor.getRed(),
				fgColor.getGreen(), fgColor.getBlue(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
	}
}
