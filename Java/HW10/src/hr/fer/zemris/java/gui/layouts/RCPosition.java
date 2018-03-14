package hr.fer.zemris.java.gui.layouts;

/**
 * Constraint class for {@link CalcLayout} layout manager. <br>
 * It defines two read-only properties: row and column.
 * 
 * @author Dan
 *
 */
public class RCPosition {

	/** Row number. */
	private int row;

	/** Column number. */
	private int column;

	/**
	 * Creates a new {@link RCPosition} with given arguments.
	 * 
	 * @param row
	 *            row number, at least 1
	 * @param column
	 *            column number, at least 1
	 */
	public RCPosition(int row, int column) {
		if (row < 1 || column < 1) {
			throw new IllegalArgumentException("Minimum number for row and column is 1.");
		}
		this.row = row;
		this.column = column;
	}

	/**
	 * @return the row number
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column number
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	/**
	 * Creates a new {@link RCPosition} from given {@code String}.
	 * 
	 * @param s
	 *            {@code String} with expected format: {@code row,column}
	 * @return new {@code RCPosition}
	 */
	public static RCPosition fromString(String s) {
		String[] elements = s.split(",");
		if (elements.length != 2) {
			throw new IllegalArgumentException("Invalid string format for position.");
		}
		return new RCPosition(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]));
	}

}
