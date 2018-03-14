package hr.fer.zemris.java.hw16.jvdraw.objects;

/**
 * Provider for {@link GeometricalObject} objects.
 *
 * @author Dan
 */
public interface GeometricalObjectProvider {

	/**
	 * Provides a new {@code GeometricalObject}.
	 * 
	 * @return new {@code GeometricalObject}
	 */
	GeometricalObject getObject();
}
