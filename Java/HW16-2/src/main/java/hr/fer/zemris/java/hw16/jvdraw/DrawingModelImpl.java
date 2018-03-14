package hr.fer.zemris.java.hw16.jvdraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Implementation of {@link DrawingModel} interface.
 *
 * @author Dan
 */
public class DrawingModelImpl implements DrawingModel {

	/** {@code List} of currently stored objects. */
	private List<GeometricalObject> objects;

	/** {@code List} containing all registered listeners. */
	private List<DrawingModelListener> listeners;

	/**
	 * Creates a new {@code DrawingModelImpl}.
	 */
	public DrawingModelImpl() {
		objects = new ArrayList<>();
		listeners = new CopyOnWriteArrayList<>();
	}

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(Objects.requireNonNull(object));
		int index = objects.size() - 1;
		listeners.forEach(l -> l.objectsAdded(this, index, index));
	}

	@Override
	public void remove(GeometricalObject object) {
		objects.remove(object);
		int index = objects.size();
		listeners.forEach(l -> l.objectsRemoved(this, index, index));
	}

	@Override
	public void clear() {
		int index = objects.size() - 1;
		objects.clear();
		listeners.forEach(l -> l.objectsChanged(this, 0, index));
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

}
