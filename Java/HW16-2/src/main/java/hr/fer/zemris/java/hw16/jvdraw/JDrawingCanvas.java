package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObjectFactory;

/**
 * Swing component for drawing {@link GeometricalObject} objects on. <br>
 * This is only a view of the {@link DrawingModel}.
 *
 * @author Dan
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	/** */
	private static final long serialVersionUID = 1L;

	/** Background color of the canvas. */
	private static final Color BG_COLOR = Color.WHITE;

	/** Model with all {@code GeometricalObjects}. */
	private DrawingModel model;

	/**
	 * {@code GeometricalObject} that is currently being drawn by the user; or
	 * {@code null}.
	 */
	private GeometricalObject currentObject;

	/** Factory for creating instances {@code GeometricalObject}. */
	private GeometricalObjectFactory objectFactory;

	/**
	 * {@code MouseListener} for clicks so that new {@code GeometricalObjects}
	 * can be drawn.
	 */
	private MouseListener clickListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (objectFactory == null) {
				return;
			}

			if (currentObject == null) {
				currentObject = objectFactory.createObject();
				currentObject.setFirstPoint(e.getPoint());
				currentObject.setSecondPoint(e.getPoint());
				addMouseMotionListener(motionListener);
				repaint();
			} else {
				removeMouseMotionListener(motionListener);
				GeometricalObject object = currentObject;
				currentObject = null;
				object.setSecondPoint(e.getPoint());
				model.add(object);
			}
		}
	};

	/**
	 * {@code MouseMotionListener} when new {@code GeometricalObject} is being
	 * drawn.
	 */
	private MouseMotionListener motionListener = new MouseMotionAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			currentObject.setSecondPoint(e.getPoint());
			repaint();
		}
	};

	/**
	 * Creates a new {@code JDrawingCanvas}.
	 * 
	 * @param model
	 *            {@code DrawingModel} with all objects
	 */
	public JDrawingCanvas(DrawingModel model) {
		this.model = model;
		model.addDrawingModelListener(this);
		addMouseListener(clickListener);
	}

	/**
	 * Setter for {@code GeometricalObjectFactory} that is used for new objects.
	 * 
	 * @param factory
	 *            factory to set
	 */
	public void setObjectFactory(GeometricalObjectFactory factory) {
		this.objectFactory = factory;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(BG_COLOR);
		Insets ins = getInsets();
		Dimension dim = getSize();
		g2d.fillRect(ins.left, ins.top, dim.width - ins.left - ins.right, dim.height - ins.top - ins.bottom);

		int n = model.getSize();
		for (int i = 0; i < n; i++) {
			model.getObject(i).draw(g2d);
		}

		if (currentObject != null) {
			currentObject.draw(g2d);
		}
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}

}
