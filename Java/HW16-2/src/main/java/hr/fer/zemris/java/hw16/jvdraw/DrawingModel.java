package hr.fer.zemris.java.hw16.jvdraw;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Interface for model containing {@link GeometricalObject} objects. <br>
 * This is a subject in observer design pattern and observer registration is
 * available.
 *
 * @author Dan
 */
public interface DrawingModel {

	/**
	 * Returns the number of stored objects.
	 * 
	 * @return number of objects
	 */
	int getSize();

	/**
	 * Returns the object at the specified index.
	 * 
	 * @param index
	 *            the requested index
	 * @return object at the index
	 */
	GeometricalObject getObject(int index);

	/**
	 * Adds an object to this model.
	 * 
	 * @param object
	 *            object to add
	 */
	void add(GeometricalObject object);

	/**
	 * Removes an object from this model.
	 * 
	 * @param object
	 *            object to remove
	 */
	void remove(GeometricalObject object);

	/**
	 * Removes all objects in this model.
	 */
	void clear();

	/**
	 * Adds a listener to the list that's notified each time a change to the
	 * data model occurs.
	 * 
	 * @param l
	 *            listener to be added
	 */
	void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Removes a listener from the list that's notified each time a change to
	 * the data model occurs.
	 * 
	 * @param l
	 *            to be removed
	 */
	void removeDrawingModelListener(DrawingModelListener l);
}
