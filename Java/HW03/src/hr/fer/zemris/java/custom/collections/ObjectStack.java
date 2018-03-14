package hr.fer.zemris.java.custom.collections;

/**
 * This class represents a last-in-first-out (LIFO) stack of {@code Objects}.
 * The usual <tt>push</tt> and <tt>pop</tt> operations are provided, as well as
 * a method to <tt>peek</tt> at the top item on the stack and a method to test
 * for whether the stack is <tt>empty</tt>. When a stack is first created, it
 * contains no items.
 * 
 * @author Dan
 *
 */
public class ObjectStack {

	/**
	 * Collection for storing elements and delegating operations.
	 */
	private ArrayIndexedCollection array;

	/**
	 * Creates an empty stack.
	 */
	public ObjectStack() {
		array = new ArrayIndexedCollection();
	}

	/**
	 * Returns <code>true</code> if stack contains no elements.
	 * 
	 * @return <code>true</code> if stack contains no elements and
	 *         <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return array.isEmpty();
	}

	/**
	 * Returns the number of currently stored elements in this stack.
	 * 
	 * @return the number of elements in this stack
	 */
	public int size() {
		return array.size();
	}

	/**
	 * Pushes the given element onto this stack.
	 * 
	 * @param value
	 *            element to be added
	 * @throws IllegalArgumentException
	 *             if the element is null
	 */
	public void push(Object value) {
		array.add(value);
	}

	/**
	 * Removes the object at the top of this stack and returns that object as
	 * the value of this function.
	 *
	 * @return the object at the top of this stack
	 * @throws hr.fer.zemris.java.custom.collections.EmptyStackException
	 *             if this stack is empty
	 */
	public Object pop() {
		Object element = peek();

		array.remove(size() - 1);

		return element;
	}

	/**
	 * Looks at the object at the top of this stack without removing it from the
	 * stack.
	 *
	 * @return the object at the top of this stack
	 * @throws hr.fer.zemris.java.custom.collections.EmptyStackException
	 *             if this stack is empty
	 */
	public Object peek() {
		if (isEmpty()) {
			throw new hr.fer.zemris.java.custom.collections.EmptyStackException();
		}

		return array.get(size() - 1);
	}

	/**
	 * Removes all elements from this stack.
	 */
	public void clear() {
		array.clear();
	}

}
