package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Memory;

/**
 * Implementation of interface {@link Memory} that uses {@code Object array} for
 * storing values.
 * 
 * @author Dan
 *
 */
public class MemoryImpl implements Memory {

	/**
	 * Array for storing values in memory.
	 */
	private Object[] memory;

	/**
	 * Creates a new {@code MemoryImpl} with given argument.
	 * 
	 * @param size
	 *            size of the memory
	 */
	public MemoryImpl(int size) {
		memory = new Object[size];
	}

	@Override
	public void setLocation(int location, Object value) {
		if (location < 0 || location >= memory.length) {
			throw new IndexOutOfBoundsException("Invalid memory location: " + location);
		}

		memory[location] = value;
	}

	@Override
	public Object getLocation(int location) {
		if (location < 0 || location >= memory.length) {
			throw new IndexOutOfBoundsException("Invalid memory location: " + location);
		}

		return memory[location];
	}

}
