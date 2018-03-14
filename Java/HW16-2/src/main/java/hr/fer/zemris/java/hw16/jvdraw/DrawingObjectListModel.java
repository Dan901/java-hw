package hr.fer.zemris.java.hw16.jvdraw;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * {@link AbstractListModel} that adapts the {@link DrawingModel}.
 *
 * @author Dan
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {

	/** */
	private static final long serialVersionUID = 1L;

	/** Model that holds all relevant information. */
	private DrawingModel adaptee;

	/**
	 * Creates a new {@code DrawingObjectListModel} that adapts the given
	 * {@code DrawingModel}.
	 * 
	 * @param model
	 *            model to adapt
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.adaptee = model;
		model.addDrawingModelListener(this);
	}

	@Override
	public int getSize() {
		return adaptee.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return adaptee.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(this, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(this, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(this, index0, index1);
	}

}
