package hr.fer.zemris.java.custom.collections;

/**
 * This class represents an interface for a general collection of objects with
 * some unimplemented methods that vary depending on implementation of a
 * collection.
 * 
 * @author Dan
 *
 */
public class Collection {

	/**
	 * Constructs an empty collection.
	 */
	protected Collection() {
		super();
	}

	/**
	 * Returns <code>true</code> if collection contains no elements.
	 * 
	 * @return <code>true</code> if collection contains no elements and
	 *         <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		if (size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the number of currently stored elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds the given element into this collection.
	 * 
	 * @param value
	 *            element to be added
	 */
	public void add(Object value) {

	}

	/**
	 * Returns <code>true</code> only if the collection contains given value.
	 * 
	 * @param value
	 *            element whose presence in this collection is to be tested
	 * @return <code>true</code> if this collection contains the specified
	 *         element
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Removes the first occurrence of the specified element from this
	 * collection, if it is present.
	 * 
	 * @param value
	 *            element to be removed from this collection, if present
	 * @return <code>true</code> if this collection contained the specified
	 *         element
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Allocates new array with size equals to the size of this collections,
	 * fills it with collection content and returns the array. This method never
	 * returns {@code null}.
	 * 
	 * @return an array containing all of the elements in this collection
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Method calls {@code process} method from given {@link Processor} for each
	 * element of this collection.
	 * 
	 * @param processor
	 *            the action to be performed for each element
	 */
	public void forEach(Processor processor) {

	}

	/**
	 * Adds all of the elements in the specified collection to this collection.
	 * 
	 * @param other
	 *            collection containing elements to be added to this collection
	 * @throws NullPointerException
	 *             if given collection is {@code null}
	 */
	public void addAll(Collection other) {
		if (other == null) {
			throw new NullPointerException();
		}

		Processor processor = new Processor() {
			@Override
			public void process(Object value) {
				add(value);
			}
		};

		other.forEach(processor);
	}

	/**
	 * Removes all elements from this collection.
	 */
	public void clear() {

	}
}
