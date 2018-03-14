package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;

/**
 * Implementation of a {@link Collection} that uses an {@code Array} of
 * {@code Objects} for storing elements. Duplicate elements are allowed and
 * storage of null references is not allowed. When an array is first created, it
 * contains no items, unless it is given another collection to copy elements
 * from.
 * 
 * @author Dan
 *
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * Current size of collection.
	 */
	private int size;

	/**
	 * Current capacity of allocated array of object references.
	 */
	private int capacity;

	/**
	 * An array of object references which length is determined by capacity
	 * variable.
	 */
	private Object[] elements;
	
	/**
	 * Initial capacity.
	 */
	private static final int INITITAL_CAPACITY = 16;

	/**
	 * Creates an empty collection with initial capacity set to 16.
	 */
	public ArrayIndexedCollection() {
		this(INITITAL_CAPACITY);
	}

	/**
	 * Creates an empty collection with given initial capacity.
	 * 
	 * @param initialCapacity
	 *            initial capacity of a collection
	 * @throws IllegalArgumentException
	 *             if the initial capacity is less than 1
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity of a collection cannot be less than 1.");
		}

		capacity = initialCapacity;
		elements = new Object[capacity];
	}

	/**
	 * Creates a new collection with same elements as given collection.
	 * 
	 * @param collection
	 *            collection of elements to be added to this collection
	 */
	public ArrayIndexedCollection(Collection collection) {
		this(collection, collection.size());
	}

	/**
	 * Creates a new collection with same elements as given collection and with
	 * given initial capacity. Collection will have the initial capacity set to
	 * given argument only if it is bigger than that of a given collection.
	 * 
	 * @param collection
	 *            collection of elements to be added to this collection
	 * @param initialCapacity
	 *            initial capacity of a collection
	 * @throws IllegalArgumentException
	 *             if the initial capacity is less than 1
	 * @throws NullPointerException
	 *             if given collection is {@code null}
	 */
	public ArrayIndexedCollection(Collection collection, int initialCapacity) {
		this(initialCapacity);

		if (collection != null) {
			addAll(collection);
		} else {
			throw new NullPointerException();
		}
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Adds the given element to the end of this collection.
	 * 
	 * @throws IllegalArgumentException
	 *             if the element is null
	 */
	@Override
	public void add(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("Element of this collection cannot be null.");
		}

		checkCapacity();

		elements[size] = value;
		size++;
	}

	/**
	 * Ensures there is enough capacity for storing one more element.
	 */
	private void checkCapacity() {
		if (size == capacity) {
			capacity = 2 * capacity;
			elements = Arrays.copyOf(elements, capacity);
		}
	}

	/**
	 * Returns the element that is stored at the specified position in this
	 * collection.
	 * 
	 * @param index
	 *            index of the element to return
	 * @return the element at the specified position in this collection
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public Object get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		return elements[index];
	}

	@Override
	public boolean contains(Object value) {
		if (indexOf(value) != -1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void clear() {
		elements = new Object[INITITAL_CAPACITY];

		size = 0;
	}

	/**
	 * Inserts the specified element at the specified position in this
	 * collection. Shifts the element currently at that position (if any) and
	 * any subsequent elements to the right (adds one to their indices).
	 * 
	 * @param value
	 *            element to be inserted
	 * @param position
	 *            index at which the specified element is to be inserted
	 * @throws IllegalArgumentException
	 *             if the specified element is null
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt; size()</tt>)
	 */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new IllegalArgumentException("Element of this collection cannot be null.");
		}

		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}

		checkCapacity();

		for (int i = size - 1; i >= position; i--) {
			elements[i + 1] = elements[i];
		}

		elements[position] = value;
		size++;
	}

	/**
	 * Returns the index of the first occurrence of the specified element in
	 * this collection, or -1 if this collection does not contain the element.
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in
	 *         this collection, or -1 if this collection does not contain the
	 *         element
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		for (int i = 0; i < size; i++) {
			if (elements[i].equals(value)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Removes the element at the specified position in this collection. Shifts
	 * any subsequent elements to the left (subtracts one from their indices).
	 * 
	 * @param index
	 *            index of the element to be removed
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		for (int i = index + 1; i < size; i++) {
			elements[i - 1] = elements[i];
		}

		size--;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);

		if (index != -1) {
			remove(index);
			return true;
		}

		return false;
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}

	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}

}
