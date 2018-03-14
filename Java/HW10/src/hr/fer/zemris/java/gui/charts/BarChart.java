package hr.fer.zemris.java.gui.charts;

import java.util.List;
import java.util.Objects;

/**
 * Model class for {@link BarChartComponent} that holds all values and relevant
 * information for drawing those values.
 * 
 * @author Dan
 * @see XYValue
 */
public class BarChart {

	/** List of values to display. */
	private List<XYValue> values;

	/** Description for x-axis. */
	private String xAxisLabel;

	/** Description for y-axis. */
	private String yAxisLabel;

	/** Minimum shown y-axis value. */
	private int yMin;

	/** Maximum shown y-axis value. */
	private int yMax;

	/** Distance between two adjacent y values. */
	private int gap;

	/**
	 * Creates a new {@link BarChart} with given arguments.
	 * 
	 * @param values
	 *            list of values to display
	 * @param xAxisLabel
	 *            description for x-axis
	 * @param yAxisLabel
	 *            description for y-axis
	 * @param yMin
	 *            minimum shown y-axis value (inclusive)
	 * @param yMax
	 *            maximum shown y-axis value (inclusive)
	 * @param gap
	 *            distance between two adjacent y values
	 */
	public BarChart(List<XYValue> values, String xAxisLabel, String yAxisLabel, int yMin, int yMax, int gap) {
		this.values = Objects.requireNonNull(values);
		this.xAxisLabel = Objects.requireNonNull(xAxisLabel);
		this.yAxisLabel = Objects.requireNonNull(yAxisLabel);

		if (yMax <= yMin) {
			throw new IllegalArgumentException("yMax has to be bigger than yMin!");
		}
		this.yMin = yMin;
		this.yMax = yMax;

		if (gap <= 0) {
			throw new IllegalArgumentException("Gap has to be positive!");
		}
		this.gap = gap;
	}

	/**
	 * @return the list of values to display
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * @return the description for x-axis
	 */
	public String getxAxisLabel() {
		return xAxisLabel;
	}

	/**
	 * @return the description for y-axis
	 */
	public String getyAxisLabel() {
		return yAxisLabel;
	}

	/**
	 * @return the minimum shown y-axis value
	 */
	public int getyMin() {
		return yMin;
	}

	/**
	 * @return the maximum shown y-axis value
	 */
	public int getyMax() {
		return yMax;
	}

	/**
	 * @return the distance between two adjacent y values
	 */
	public int getGap() {
		return gap;
	}

}
