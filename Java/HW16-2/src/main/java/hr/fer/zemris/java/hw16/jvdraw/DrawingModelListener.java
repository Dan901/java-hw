package hr.fer.zemris.java.hw16.jvdraw;

/**
 * Interface for listeners in observer design pattern, that listen for changes
 * in {@link DrawingModel}.
 *
 * @author Dan
 */
public interface DrawingModelListener {

	/**
	 * Called when new objects are added to the model.
	 * 
	 * @param source
	 *            {@code DrawingModel} that changed
	 * @param index0
	 *            one end of the new interval
	 * @param index1
	 *            the other end of the new interval
	 */
	void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Called when objects are removed from the model.
	 * 
	 * @param source
	 *            {@code DrawingModel} that changed
	 * @param index0
	 *            one end of the removed interval
	 * @param index1
	 *            the other end of the removed interval
	 */
	void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Called when change in the model is not simple enough to call one of the
	 * other two methods.
	 * 
	 * @param source
	 *            {@code DrawingModel} that changed
	 * @param index0
	 *            one end of the new interval
	 * @param index1
	 *            the other end of the new interval
	 */
	void objectsChanged(DrawingModel source, int index0, int index1);
}
