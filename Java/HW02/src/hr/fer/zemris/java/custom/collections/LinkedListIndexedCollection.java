package hr.fer.zemris.java.custom.collections;

/**
 * Implementation of a {@link Collection} that uses an linked list of
 * {@code Objects} for storing elements. Duplicate elements are allowed and
 * storage of null references is not allowed. When a list is first created, it
 * contains no items, unless it is given another collection to copy elements
 * from.
 * 
 * @author Dan
 *
 */
public class LinkedListIndexedCollection extends Collection {

	/**
	 * Static class for representing one node of the list.
	 * 
	 * @author Dan
	 *
	 */
	private static class ListNode {
		/**
		 * Value of the node.
		 */
		private Object value;

		/**
		 * Reference to next node in the list.
		 */
		private ListNode next;

		/**
		 * Reference to previous node in the list.
		 */
		private ListNode previous;
	}

	/**
	 * Current size of collection.
	 */
	private int size;

	/**
	 * Reference to the first node in the list.
	 */
	private ListNode first;

	/**
	 * Reference to the last node in the list.
	 */
	private ListNode last;

	/**
	 * Creates an empty linked list.
	 */
	public LinkedListIndexedCollection() {
	}

	/**
	 * Creates a new linked list with same elements as given collection.
	 * 
	 * @param collection
	 *            collection of elements to be added to this collection
	 * @throws NullPointerException
	 *             if given collection is {@code null}
	 */
	public LinkedListIndexedCollection(Collection collection) {
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

		ListNode newNode = new ListNode();
		newNode.value = value;

		if (first == null) {
			first = last = newNode;
		} else {
			last.next = newNode;
			newNode.previous = last;
			last = newNode;
		}

		size++;
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
		return getNode(index).value;
	}

	/**
	 * Private method for getting the whole node at the specified index.
	 * 
	 * @param index
	 *            index of the node to return
	 * @return the node at the specified position in this collection
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	private ListNode getNode(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		ListNode current;
		if (index < size / 2) {
			current = first;
			for (int i = 0; i < index; i++) {
				current = current.next;
			}
		} else {
			current = last;
			for (int i = size - 1; i > index; i--) {
				current = current.previous;
			}
		}

		return current;
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
		first = last = null;
		size = 0;
	}

	/**
	 * Inserts the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (adds one to their indices).
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

		if (position == size) {
			add(value);
		} else {
			ListNode afterNew = first;
			for (int i = 0; i < position; i++) {
				afterNew = afterNew.next;
			}

			ListNode newNode = new ListNode();
			newNode.value = value;

			newNode.next = afterNew;
			newNode.previous = afterNew.previous;
			afterNew.previous = newNode;

			if (position == 0) {
				first = newNode;
			} else {
				newNode.previous.next = newNode;
			}

			size++;
		}
	}

	/**
	 * Returns the index of the first occurrence of the specified element in
	 * this list, or -1 if this list does not contain the element.
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in
	 *         this list, or -1 if this list does not contain the element
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		ListNode current = first;
		for (int i = 0; i < size; i++) {
			if (current.value.equals(value)) {
				return i;
			}
			current = current.next;
		}

		return -1;
	}

	/**
	 * Removes the element at the specified position in this list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 * 
	 * @param index
	 *            index of the element to be removed
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public void remove(int index) {
		ListNode node = getNode(index);
		ListNode next = node.next;
		ListNode previous = node.previous;

		if (previous == null) {
			first = next;
		} else {
			previous.next = next;
			node.previous = null;
		}

		if (next == null) {
			last = previous;
		} else {
			next.previous = previous;
			node.next = null;
		}

		node.value = null;
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
		Object[] array = new Object[size];
		ListNode current = first;

		for (int i = 0; i < size; i++) {
			array[i] = current.value;
			current = current.next;
		}

		return array;
	}

	@Override
	public void forEach(Processor processor) {
		ListNode current = first;

		while (current != null) {
			processor.process(current.value);
			current = current.next;
		}
	}

}
