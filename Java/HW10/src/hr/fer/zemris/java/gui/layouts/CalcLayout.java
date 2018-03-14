package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Custom layout manager designed for calculator GUI. <br>
 * Similar to the {@link GridLayout}, but with a fixed number of rows and
 * columns as well as one bigger space than the rest for screen component.
 * 
 * @author Dan
 *
 */
public class CalcLayout implements LayoutManager2 {

	/** Fixed number of rows. */
	private static final int ROWS = 5;

	/** Fixed number of columns. */
	private static final int COLS = 7;

	/** Number of columns occupied by the first component (screen). */
	private static final int COLUMS_FOR_FIRST = 5;

	/** Position of first component (1,1) */
	public static final RCPosition FIRST_POSITION = new RCPosition(1, 1);

	/** Horizontal and vertical gap between rows and columns in pixels. */
	private int gap;

	/** Map for all components with their position as key */
	private Map<RCPosition, Component> components;

	/**
	 * Creates a new {@link CalcLayout} without gaps between rows and columns.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Creates a new {@link CalcLayout} with given arguments.
	 * 
	 * @param gap
	 *            horizontal and vertical gap between rows and columns in pixels
	 */
	public CalcLayout(int gap) {
		if (gap < 0) {
			throw new IllegalArgumentException("Expected positive number for gap.");
		}
		this.gap = gap;
		components = new HashMap<>();
	}

	/**
	 * @return horizontal and vertical gap between rows and columns in pixels
	 */
	public int getGap() {
		return gap;
	}

	/**
	 * Sets the horizontal gap between rows in pixels to given value.
	 * 
	 * @param gap
	 *            new positive value
	 */
	public void setGap(int gap) {
		if (gap < 0) {
			throw new IllegalArgumentException("Expected positive number for gap.");
		}
		this.gap = gap;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		components.entrySet().forEach(e -> {
			if (e.getValue().equals(comp)) {
				components.remove(e.getKey());
			}
		});
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension max = getMaxDimension(Component::getPreferredSize);
		Insets insets = parent.getInsets();
		int w = max.width;
		int h = max.height;

		return new Dimension(insets.left + insets.right + COLS * w + (COLS - 1) * gap,
				insets.top + insets.bottom + ROWS * h + (ROWS - 1) * gap);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension max = getMaxDimension(Component::getMinimumSize);
		Insets insets = parent.getInsets();
		int w = max.width;
		int h = max.height;

		return new Dimension(insets.left + insets.right + COLS * w + (COLS - 1) * gap,
				insets.top + insets.bottom + ROWS * h + (ROWS - 1) * gap);
	}

	@Override
	public void layoutContainer(Container parent) {
		if (components.isEmpty()) {
			return;
		}

		Insets insets = parent.getInsets();
		int width = (parent.getWidth() - insets.left - insets.right - (COLS - 1) * gap) / COLS;
		int height = (parent.getHeight() - insets.top - insets.bottom - (ROWS - 1) * gap) / ROWS;

		components.entrySet().forEach(new Consumer<Map.Entry<RCPosition, Component>>() {
			@Override
			public void accept(Entry<RCPosition, Component> entry) {
				int x = insets.left;
				int y = insets.top;

				if (entry.getKey().equals(FIRST_POSITION)) {
					entry.getValue().setBounds(x, y, COLUMS_FOR_FIRST * width + (COLUMS_FOR_FIRST - 1) * gap, height);
					return;
				}

				int col = entry.getKey().getColumn();
				int row = entry.getKey().getRow();
				if (col > 1) {
					x += (col - 1) * (width + gap);
				}
				if (row > 1) {
					y += (row - 1) * (height + gap);
				}
				entry.getValue().setBounds(x, y, width, height);
			}
		});
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition pos;
		if (constraints instanceof String) {
			pos = RCPosition.fromString((String) constraints);
		} else if (constraints instanceof RCPosition) {
			pos = (RCPosition) constraints;
		} else {
			throw new IllegalArgumentException("Components position should be RCPosition of a String.");
		}
		checkPosition(pos);

		if (components.containsKey(pos)) {
			throw new IllegalArgumentException("Given component position: " + pos.getRow() + "," + pos.getColumn()
					+ " is already taken by another component.");
		}
		components.put(pos, comp);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		Dimension max = getMaxDimension(Component::getMaximumSize);
		Insets insets = target.getInsets();
		int w = max.width;
		int h = max.height;

		return new Dimension(insets.left + insets.right + COLS * w + (COLS - 1) * gap,
				insets.top + insets.bottom + ROWS * h + (ROWS - 1) * gap);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

	/**
	 * Checks if component has a legal {@link RCPosition}.
	 * 
	 * @param pos
	 *            position to check
	 * @throws IllegalArgumentException
	 *             if given {@code pos} is illegal for this {@link CalcLayout}.
	 */
	private void checkPosition(RCPosition pos) {
		int row = pos.getRow();
		int col = pos.getColumn();

		//@formatter:off
		if(row >= 2 && row <= ROWS && col <= COLS) return;
		if(pos.equals(FIRST_POSITION)) return;
		if(row == 1 && col > COLUMS_FOR_FIRST  && col <= COLS) return;
		//@formatter:on
		throw new IllegalArgumentException("Invalid component position: " + row + "," + col);
	}

	/**
	 * Uses the given {@code strategy} to get {@code Dimension} for every
	 * {@code Component} in this manager and returns the new {@code Dimension}
	 * with maximum obtained width and height.
	 * 
	 * @param strategy
	 *            function for getting {@code Dimension} for each
	 *            {@code Component}
	 * @return new {@link Dimension} with maximum positive width and height from
	 *         all {@code Components}
	 */
	private Dimension getMaxDimension(Function<Component, Dimension> strategy) {
		List<Dimension> dimensions = components.entrySet().parallelStream()
				.filter(e -> !e.getKey().equals(FIRST_POSITION)).map(e -> strategy.apply(e.getValue()))
				.filter(Objects::nonNull).collect(Collectors.toList());

		Dimension first = strategy.apply(components.get(FIRST_POSITION));
		int firstWidth = (int) Math.ceil((first.getWidth() - (COLUMS_FOR_FIRST - 1) * gap) / COLUMS_FOR_FIRST);
		if (first != null) {
			dimensions.add(new Dimension(firstWidth, first.height));
		}

		OptionalInt width = dimensions.parallelStream().mapToInt(d -> d.width).max();
		OptionalInt height = dimensions.parallelStream().mapToInt(d -> d.height).max();

		int w = width.isPresent() ? width.getAsInt() : 0;
		int h = height.isPresent() ? height.getAsInt() : 0;

		return new Dimension(Math.max(0, w), Math.max(0, h));
	}
}
