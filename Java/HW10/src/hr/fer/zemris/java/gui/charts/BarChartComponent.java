package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;

/**
 * This {@link JComponent} is responsible for drawing every {@link XYValue} from
 * {@link BarChart} model given in the constructor. <br>
 * Range for y-axis is taken from the given model, however there is no fixed
 * distance between adjacent x-axis values. In other words, only values from
 * given model are drawn without spaces between them. For example, if the model
 * contains the next x-axis values: 1, 2, 10; only those three will be drawn,
 * without empty spaces between.
 * <p>
 * <strong>Warning:</strong> If the model contains more values with the same
 * x-axis value, only the first one will be drawn.
 * 
 * @author Dan
 *
 */
public class BarChartComponent extends JComponent {

	/** */
	private static final long serialVersionUID = 1L;

	/** Font for displaying axis labels. */
	private static final Font TEXT_FONT = new Font(null, Font.PLAIN, 15);

	/** Font for displaying numbers. */
	private static final Font NUMBER_FONT = new Font(null, Font.BOLD, 18);

	/** Color for all text. */
	private static final Color TEXT_COLOR = Color.BLACK;

	/** Color for grid lines. */
	private static final Color LINE_COLOR = new Color(255, 204, 153);

	/** Color for axis lines. */
	private static final Color AXIS_COLOR = Color.GRAY;

	/** Color for bar filling. */
	private static final Color BAR_COLOR = new Color(0, 128, 255);

	/** Gap between labels and numbers. */
	private static final int TEXT_GAP = 15;

	/** Line segment after axes. */
	private static final int LINE_EXTRA = 10;

	/** Size of the axis arrow. */
	private static final int ARROW_SIZE = 10;

	/** Model with all the data that needs to be displayed. */
	private BarChart model;

	/** Distance in pixels between y and (y-1). */
	private double unitHeight;

	/** {@link Rectangle} representing current drawing space. */
	private Rectangle space;

	/** {@link Point} representing origin of the drawn coordinate system. */
	private Point origin;

	/**
	 * Creates a new {@link BarChartComponent} that draws values contained in
	 * given {@link BarChart}.
	 * 
	 * @param model
	 *            model with all the data that needs to be displayed
	 */
	public BarChartComponent(BarChart model) {
		this.model = Objects.requireNonNull(model);
		setBackground(Color.WHITE);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Insets ins = getInsets();
		Dimension dim = getSize();
		space = new Rectangle(ins.left, ins.top, dim.width - ins.left - ins.right, dim.height - ins.top - ins.bottom);
		g2d.setColor(getBackground());
		g2d.fillRect(space.x, space.y, space.width, space.height);

		g2d.setFont(TEXT_FONT);
		g2d.setColor(TEXT_COLOR);
		drawLabels(g2d);
		drawYContent(g2d);
		drawXContent(g2d);
	}

	/**
	 * Draws the x and y-axis labels, as specified by a {@link BarChart}.
	 * 
	 * @param g2d
	 *            {@code Graphics2D} object for drawing
	 */
	private void drawLabels(Graphics2D g2d) {
		// x label
		String xLabel = model.getxAxisLabel();
		FontMetrics fm = g2d.getFontMetrics();
		int x = (space.width - fm.stringWidth(xLabel)) / 2;
		int y = space.y + space.height - fm.getDescent();
		g2d.drawString(xLabel, x, y);
		int reduce = fm.getHeight() + TEXT_GAP;
		// reduce drawing space
		space.height -= reduce;

		// y label
		String yLabel = model.getyAxisLabel();
		y = space.x + fm.getAscent();
		x = space.y - space.height + (space.height - fm.stringWidth(yLabel)) / 2;
		AffineTransform defaultAt = g2d.getTransform();
		g2d.setTransform(AffineTransform.getQuadrantRotateInstance(3));
		g2d.drawString(yLabel, x, y);
		g2d.setTransform(defaultAt);
		// reduce drawing space
		space.x += reduce;
		space.width -= reduce;
	}

	/**
	 * Draws the numbers and lines associated with the y-axis.
	 * 
	 * @param g2d
	 *            {@code Graphics2D} object for drawing
	 */
	private void drawYContent(Graphics2D g2d) {
		g2d.setFont(NUMBER_FONT);
		FontMetrics fm = g2d.getFontMetrics();
		int widestNumber = Math.max(fm.stringWidth(Integer.toString(model.getyMax())),
				fm.stringWidth(Integer.toString(model.getyMin())));
		int numbersRightLine = space.x + widestNumber;
		origin = new Point(numbersRightLine + TEXT_GAP, space.y + space.height - fm.getHeight() - TEXT_GAP);

		int rows = (int) Math.ceil((model.getyMax() - model.getyMin()) / (double) model.getGap());
		double rowHeight = (origin.y - LINE_EXTRA - ARROW_SIZE) / rows;
		unitHeight = rowHeight / model.getGap();

		// draw numbers for y axis
		int baseline = origin.y + fm.getAscent() / 2;
		int currentY = model.getyMin();
		for (int i = 0; i <= rows; i++) {
			String number = Integer.toString(currentY);
			g2d.drawString(number, numbersRightLine - fm.stringWidth(number), baseline);
			currentY += model.getGap();
			baseline -= rowHeight;
		}

		// draw horizontal lines
		g2d.setColor(AXIS_COLOR);
		int lineX = origin.x - LINE_EXTRA;
		int lineY = origin.y;
		int endX = space.x + space.width;
		// draw arrow
		g2d.fillPolygon(new int[] { endX, endX - ARROW_SIZE, endX - ARROW_SIZE },
				new int[] { lineY, lineY + ARROW_SIZE / 2, lineY - ARROW_SIZE / 2 }, 3);

		for (int i = 0; i <= rows; i++) {
			g2d.drawLine(lineX, lineY, endX - ARROW_SIZE, lineY);
			lineY -= rowHeight;
			g2d.setColor(LINE_COLOR);
		}
	}

	/**
	 * Draws the numbers and lines associated with the x-axis and fills the
	 * bars.
	 * 
	 * @param g2d
	 *            {@code Graphics2D} object for drawing
	 */
	private void drawXContent(Graphics2D g2d) {
		// draw x axis and arrow
		g2d.setColor(AXIS_COLOR);
		g2d.drawLine(origin.x, origin.y + LINE_EXTRA, origin.x, space.y + ARROW_SIZE);
		g2d.fillPolygon(new int[] { origin.x, origin.x + ARROW_SIZE / 2, origin.x - ARROW_SIZE / 2 },
				new int[] { space.y, space.y + ARROW_SIZE, space.y + ARROW_SIZE }, 3);

		// get all values and sort them
		Set<XYValue> values = new TreeSet<>(XYValue.COMPARE_BY_X);
		values.addAll(model.getValues());
		int columns = values.size();
		if (columns == 0) {
			return;
		}

		int endX = space.x + space.width;
		double colWidth = (endX - origin.x - LINE_EXTRA - ARROW_SIZE) / columns;
		int x = origin.x + 1;
		int baseline = space.height;
		double centerX = colWidth / 2. + origin.x;
		FontMetrics fm = g2d.getFontMetrics();

		for (XYValue value : values) {
			// draw numbers for x axis
			g2d.setColor(TEXT_COLOR);
			String text = Integer.toString(value.getX());
			double textX = centerX - fm.stringWidth(text) / 2.;
			g2d.drawString(text, (int) textX, baseline);
			centerX += colWidth;

			// draw bars
			g2d.setColor(BAR_COLOR);
			int y = (int) (origin.y - unitHeight * Math.abs(value.getY() - model.getyMin()));
			g2d.fillRect(x, y, (int) colWidth, origin.y - y);

			// draw vertical lines
			g2d.setColor(LINE_COLOR);
			x += colWidth;
			g2d.drawLine(x, origin.y + LINE_EXTRA, x, space.y + ARROW_SIZE);
			x += 1;
		}
	}

}
