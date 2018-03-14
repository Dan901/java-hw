package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Swing component for choosing colors and displaying currently chosen color.
 * <br>
 * This is a subject in observer design pattern and observer registration is
 * available.
 *
 * @author Dan
 * @see ColorChangeListener
 */
public class JColorArea extends JComponent implements IColorProvider {

	/** */
	private static final long serialVersionUID = 1L;

	/** Size of this component. */
	private static final Dimension SIZE = new Dimension(15, 15);

	/** Current selected color. */
	private Color selectedColor;

	/** {@code List} containing all registered listeners. */
	private List<ColorChangeListener> listeners;

	/** Listener for showing {@link JColorChooser} when clicked on. */
	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			Color color = JColorChooser.showDialog(JColorArea.this, "Choose a color", selectedColor);
			if (color != null) {
				setColor(color);
			}
		}
	};

	/**
	 * Creates a new {@code JColorArea}.
	 * 
	 * @param selectedColor
	 *            current selected color
	 */
	public JColorArea(Color selectedColor) {
		this.selectedColor = selectedColor;
		listeners = new CopyOnWriteArrayList<>();
		addMouseListener(mouseListener);
	}

	@Override
	public Dimension getPreferredSize() {
		return SIZE;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Insets ins = getInsets();
		Dimension dim = getSize();
		g.setColor(selectedColor);
		g.fillRect(ins.left, ins.top, dim.width - ins.left - ins.right, dim.height - ins.top - ins.bottom);
	}

	/**
	 * Adds a listener to the list that's notified each time a change to the
	 * color selection occurs.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	/**
	 * Removes a listener from the list that's notified each time a change to
	 * color selection occurs.
	 * 
	 * @param l
	 *            to be removed
	 */
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	/**
	 * Sets new color and notifies all registered listeners.
	 * 
	 * @param color
	 *            new color
	 */
	private void setColor(Color color) {
		Color oldColor = selectedColor;
		selectedColor = color;
		listeners.forEach(l -> l.newColorSelected(this, oldColor, color));
		repaint();
	}

}
