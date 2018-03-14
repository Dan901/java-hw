package hr.fer.zemris.java.tecaj.hw5.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * This class represents the implementation of a hash table that stores pairs
 * (key, value).
 * <p>
 * Two public constructors are available, one with default initial capacity and
 * second with arbitrary capacity. Load factor is {@value #LOAD_FACTOR}.
 * <p>
 * Key of the entry cannot be {@code null} reference, while value can.
 * 
 * @author Dan
 *
 * @param <K>
 *            key type
 * @param <V>
 *            value type
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/**
	 * This static nested class models one entry of the hash table that is
	 * implemented by {@link SimpleHashtable}.
	 * <p>
	 * Entry is a pair (key, value).
	 * 
	 * @author Dan
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 */
	public static class TableEntry<K, V> {

		/**
		 * Key of this entry.
		 */
		private final K key;

		/**
		 * Value of this entry.
		 */
		private V value;

		/**
		 * Next entry in the same slot.
		 */
		private TableEntry<K, V> next;

		/**
		 * Constructs table entry for one element.
		 * 
		 * @param key
		 *            key of the element
		 * @param value
		 *            value of the element
		 * @param next
		 *            next entry in the same slot
		 * @throws IllegalArgumentException
		 *             if key is {@code null}
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			Objects.requireNonNull(key, "Key cannot be null.");

			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * @return key of this entry
		 */
		public K getKey() {
			return key;
		}

		/**
		 * @return value of this entry
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets the value of this entry to given value.
		 * 
		 * @param value
		 *            new value
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Returns the {@code String} representation of this entry in format:
		 * key=value
		 */
		@Override
		public String toString() {
			return (key + "=" + value);
		}
	}

	/**
	 * Internal storage of entries.
	 */
	private TableEntry<K, V>[] table;

	/**
	 * Current number of stored entries.
	 */
	private int size;

	/**
	 * Used for fail-fast iteration; increased when structural change happens.
	 */
	private int modificationCount;

	/**
	 * Default table capacity.
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Load factor of the table; once reached, capacity will be doubled.
	 */
	private static final double LOAD_FACTOR = 0.75;

	/**
	 * Creates a new empty {@code SimpleHashtable} with capacity set to
	 * {@value #DEFAULT_CAPACITY}.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates a new empty {@code SimpleHashtable} with given capacity. If given
	 * capacity is not the power of 2, then capacity is set to first power of 2
	 * that is bigger than given capacity.
	 * 
	 * @param capacity
	 *            capacity of the table
	 * @throws IllegalArgumentException
	 *             if given size is less than 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("Size cannot be less than 1.");
		}

		int newCapacity = (int) Math.pow(2, Math.ceil((Math.log(capacity) / Math.log(2))));
		table = new TableEntry[newCapacity];
	}

	/**
	 * Calculates the table index of an entry.
	 * 
	 * @param key
	 *            key of the entry
	 * @return index of the entry with given key
	 */
	private int getIndex(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}

	/**
	 * Returns the {@code TableEntry} with given key in this
	 * {@code SimpleHashtable}.
	 * 
	 * @param key
	 *            key of the entry
	 * @return entry with given key or {@code null} if it doesn't exist in this
	 *         {@code SimpleHashtable}
	 */
	private TableEntry<K, V> getEntry(Object key) {
		if (key == null) {
			return null;
		}

		int index = getIndex(key);
		TableEntry<K, V> current = table[index];

		while (current != null) {
			if (current.key.equals(key)) {
				return current;
			}
			current = current.next;
		}

		return null;
	}

	/**
	 * Inserts the new entry into this {@code SimpleHashtable}. If entry with
	 * the same key already exists, value is updated.
	 * 
	 * @param key
	 *            key of the entry
	 * @param value
	 *            value of the entry
	 * @throws IllegalArgumentException
	 *             if key is {@code null}
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Key cannot be null.");

		TableEntry<K, V> entry = getEntry(key);
		if (entry != null) {
			entry.value = value;
			return;
		}

		entry = new TableEntry<K, V>(key, value, null);
		int index = getIndex(key);

		if (table[index] == null) {
			table[index] = entry;
		} else {
			TableEntry<K, V> last = table[index];
			while (last.next != null) {
				last = last.next;
			}

			last.next = entry;
		}

		size++;
		modificationCount++;

		if (size >= LOAD_FACTOR * table.length) {
			resize();
		}
	}

	/**
	 * Once number of entries in this {@code SimpleHashtable} has reached
	 * certain load factor ({@value #LOAD_FACTOR}), capacity is doubled by
	 * calling this method.
	 */
	@SuppressWarnings("unchecked")
	private void resize() {
		TableEntry<K, V>[] oldTable = table;
		table = new TableEntry[oldTable.length * 2];

		for (int i = 0; i < oldTable.length; i++) {
			TableEntry<K, V> entry = oldTable[i];
			if (entry == null) {
				continue;
			}

			// only 2 possible new slots for every entry in this slot
			TableEntry<K, V> tail1 = null, tail2 = null;
			while (entry != null) {
				int index = getIndex(entry.key);

				if (index == i) {
					if (tail1 == null) {
						table[index] = entry;
					} else {
						tail1.next = entry;
					}
					tail1 = entry;
				} else {
					if (tail2 == null) {
						table[index] = entry;
					} else {
						tail2.next = entry;
					}
					tail2 = entry;
				}

				entry = entry.next;
			}

			if (tail1 != null) {
				tail1.next = null;
			}
			if (tail2 != null) {
				tail2.next = null;
			}
		}

		modificationCount++;
	}

	/**
	 * Returns the value of the entry with given key.
	 * 
	 * @param key
	 *            key of the entry
	 * @return value of the entry or {@code null} if it doesn't exist in this
	 *         {@code SimpleHashtable}
	 */
	public V get(Object key) {
		TableEntry<K, V> entry = getEntry(key);

		if (entry != null) {
			return entry.value;
		} else {
			return null;
		}
	}

	/**
	 * @return number of currently stored entries in this
	 *         {@code SimpleHashtable}
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if entry with given key exists in this {@code SimpleHashtable}.
	 * 
	 * @param key
	 *            key of the entry
	 * @return {@code true} if it exists, {@code false} otherwise
	 */
	public boolean containsKey(Object key) {
		if (getEntry(key) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if entry with given value exists in this {@code SimpleHashtable}.
	 * 
	 * @param value
	 *            value of the entry
	 * @return {@code true} if it exists, {@code false} otherwise
	 */
	public boolean containsValue(Object value) {
		for (TableEntry<K, V> entry : this) {
			Object v = entry.value;
			if (value == v || (value != null) && value.equals(v)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Removes the entry with given key from this {@code SimpleHashtable}. If it
	 * doesn't exist, nothing happens.
	 * 
	 * @param key
	 *            key of the entry
	 */
	public void remove(Object key) {
		if (key == null) {
			return;
		}

		int index = getIndex(key);
		TableEntry<K, V> current = table[index];
		TableEntry<K, V> previous = null;

		while (current != null) {
			if (current.key.equals(key)) {
				if (previous == null) {
					table[index] = current.next;
				} else {
					previous.next = current.next;
				}

				size--;
				modificationCount++;
				return;
			}

			previous = current;
			current = current.next;
		}
	}

	/**
	 * Checks if this {@code SimpleHashtable} is empty.
	 * 
	 * @return {@code true} if it's empty; {@code false} otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Clears this {@code SimpleHashtable}.
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		if (size != 0) {
			table = new TableEntry[table.length];
			size = 0;
		}
	}

	@Override
	public String toString() {
		StringJoiner s = new StringJoiner(", ", "[", "]");
		for (TableEntry<K, V> entry : this) {
			s.add(entry.toString());
		}

		return s.toString();
	}

	/**
	 * Returns an iterator over entries in this {@code SimpleHashtable}.
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Iterator for this {@code SimpleHashtable}, remove() method is supported.
	 * 
	 * @author Dan
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

		/**
		 * Current entry.
		 */
		private TableEntry<K, V> current;

		/**
		 * Next entry to return.
		 */
		private TableEntry<K, V> next;

		/**
		 * Used for fail-fast iteration.
		 */
		private int expectedModCount;

		/**
		 * Current table slot.
		 */
		private int index;

		/**
		 * Creates a new {@code Iterator} for this {@code SimpleHashtable}.
		 */
		private IteratorImpl() {
			expectedModCount = modificationCount;
			index = 0;

			// prepare first
			if (size > 0) {
				while ((next = table[index]) == null) {
					index++;
				}
			}
		}

		@Override
		public boolean hasNext() {
			checkModCount();
			return next != null;
		}

		/**
		 * @throws ConcurrentModificationException
		 *             if this {@code SimpleHashtable} was modified during the
		 *             iteration
		 * @throws NoSuchElementException
		 *             if the iteration has no more elements
		 */
		@Override
		public TableEntry<K, V> next() {
			checkModCount();
			if (next == null) {
				throw new NoSuchElementException();
			}

			current = next;
			next = current.next;

			while (next == null) {
				index++;
				if (index == table.length) {
					break;
				}
				next = table[index];
			}

			return current;
		}

		/**
		 * Removes from this {@code SimpleHashtable} the last entry returned by
		 * this iterator. This method can be called only once per call to
		 * next().
		 * 
		 * @throws IllegalStateException
		 *             if the next method has not yet been called, or the remove
		 *             method has already been called after the last call to the
		 *             next method
		 * @throws ConcurrentModificationException
		 *             if this {@code SimpleHashtable} was modified, in any way
		 *             other than by calling this method, during the iteration
		 */
		@Override
		public void remove() {
			if (current == null) {
				throw new IllegalStateException();
			}
			checkModCount();

			SimpleHashtable.this.remove(current.key);
			current = null;
			expectedModCount = modificationCount;
		}

		/**
		 * Throws appropriate exception if this {@code SimpleHashtable} was
		 * modified during the iteration.
		 */
		private void checkModCount() {
			if (expectedModCount != modificationCount) {
				throw new ConcurrentModificationException();
			}
		}
	}
}
