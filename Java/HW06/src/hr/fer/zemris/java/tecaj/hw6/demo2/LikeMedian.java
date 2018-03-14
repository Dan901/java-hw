package hr.fer.zemris.java.tecaj.hw6.demo2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This generic class calculates median value of given elements. Elements are
 * added by calling {@link #add(Comparable)} and median value is calculates by
 * calling {@link #get()} method.
 * <p>
 * {@link Iterable} interface is implemented and elements are returned in order
 * in which they were added.
 * <p>
 * {@code null} elements are not supported
 * 
 * @author Dan
 *
 * @param <T>
 *            type of the elements, have to implement {@link Comparable}
 *            interface
 */
public class LikeMedian<T extends Comparable<T>> implements Iterable<T> {

	/**
	 * Storage of elements in original order.
	 */
	private List<T> elements;

	/**
	 * Creates a new {@code LikeMedian} with no elements stored.
	 */
	public LikeMedian() {
		elements = new LinkedList<T>();
	}

	/**
	 * Adds a new element to this {@code LikeMedian}.
	 * 
	 * @param element
	 *            element to be added
	 */
	public void add(T element) {
		elements.add(Objects.requireNonNull(element));
	}

	/**
	 * Calculates median element based on currently stored elements.
	 * 
	 * @return median element in form of {@link Optional}
	 */
	public Optional<T> get() {
		if (elements.isEmpty()) {
			return Optional.empty();
		}

		List<T> sortedElements = elements.parallelStream().sorted().collect(Collectors.toList());
		int index = (sortedElements.size() - 1) / 2;
		return Optional.of(sortedElements.get(index));
	}

	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}
}
