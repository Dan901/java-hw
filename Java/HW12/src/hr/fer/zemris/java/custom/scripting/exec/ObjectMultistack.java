package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a map, with {@code Strings} as keys that can be mapped
 * to multiple values, of type {@link ValueWrapper}, organized in a stack-like
 * (LIFO) structure.
 * 
 * @author Dan
 *
 */
public class ObjectMultistack {

	/**
	 * Node of a stack that contains a {@link ValueWrapper}.
	 * 
	 * @author Dan
	 *
	 */
	private static class MultistackEntry {

		/**
		 * Value of this entry.
		 */
		private ValueWrapper value;

		/**
		 * Next entry if more values are mapped to the same key.
		 */
		private MultistackEntry next;

		/**
		 * Creates a new {@code MultistackEntry} with given arguments.
		 * 
		 * @param value
		 *            value of this entry
		 * @param next
		 *            next entry in this stack
		 */
		private MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
	}

	/**
	 * Map for storage of all entries.
	 */
	private Map<String, MultistackEntry> map;

	/**
	 * Creates a new empty {@code ObjectMultistack}.
	 */
	public ObjectMultistack() {
		map = new HashMap<>();
	}

	/**
	 * Checks if given key (name) is not mapped to anything.
	 * 
	 * @param name
	 *            key to check
	 * @return {@code true} if this key not bounded to any stack; {@code false}
	 *         otherwise
	 */
	public boolean isEmpty(String name) {
		return map.get(name) == null;
	}

	/**
	 * Adds a given value to the stack bounded with given key (name).
	 * 
	 * @param name
	 *            key of the stack
	 * @param valueWrapper
	 *            value to be added
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		MultistackEntry entry = new MultistackEntry(valueWrapper, null);
		entry.next = map.put(name, entry);
	}

	/**
	 * Returns the last value added to the stack bounded with given key (name).
	 * 
	 * @param name
	 *            key of the stack
	 * @return last value added to the stack bounded with given key
	 * @throws EmptyStackException
	 *             if given key is not bounded to any stack
	 */
	public ValueWrapper peek(String name) {
		MultistackEntry entry = map.get(name);

		if (entry == null) {
			throw new EmptyStackException();
		} else {
			return entry.value;
		}
	}

	/**
	 * Returns and removes the last value added to the stack bounded with given
	 * key (name).
	 * 
	 * @param name
	 *            key of the stack
	 * @return last value added to the stack bounded with given key
	 * @throws EmptyStackException
	 *             if given key is not bounded to any stack
	 */
	public ValueWrapper pop(String name) {
		MultistackEntry entry = map.get(name);

		if (entry == null) {
			throw new EmptyStackException();
		} else {
			if (entry.next == null) {
				map.remove(name);
			} else {
				map.put(name, entry.next);
			}

			return entry.value;
		}
	}
}
