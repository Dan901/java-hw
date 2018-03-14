package hr.fer.zemris.java.tecaj.hw6.demo2;

import java.util.Optional;

/**
 * Demonstrations of {@link LikeMedian} on {@code Strings}.
 * 
 * @author Dan
 *
 */
public class MedianDemo2 {

	/**
	 * Demonstration.
	 * @param args not used
	 */
	public static void main(String[] args) {
		LikeMedian<String> likeMedian = new LikeMedian<String>();
		likeMedian.add("Joe");
		likeMedian.add("Jane");
		likeMedian.add("Adam");
		likeMedian.add("Zed");
		Optional<String> result = likeMedian.get();
		System.out.println(result.get()); // Writes: Jane

	}
}
