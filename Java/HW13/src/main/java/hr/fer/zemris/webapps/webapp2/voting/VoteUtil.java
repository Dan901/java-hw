package hr.fer.zemris.webapps.webapp2.voting;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for dealing with file that contains the number of votes for
 * each band.
 * 
 * @author Dan
 */
public class VoteUtil {

	/**
	 * Reads the file with number of votes each band and parses it.
	 * 
	 * @param filePath
	 *            {@code Path} of the file containing the number of votes for
	 *            each band
	 * @return {@code Map} with band's id number as key and number of votes as
	 *         value
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static Map<Integer, Integer> getAllVotes(Path filePath) throws IOException {
		return Files.readAllLines(filePath).stream().map(line -> line.split("\t")).filter(elems -> elems.length == 2)
				.collect(Collectors.toMap(e -> Integer.parseInt(e[0]), e -> Integer.parseInt(e[1])));
	}

	/**
	 * Writes the given information about votes to the file.
	 * 
	 * @param votes
	 *            {@code Map} with band's id number as key and number of votes
	 *            as value; this {@code Map} can be obtained by calling
	 *            {@link #getAllVotes(Path)} method
	 * @param filePath
	 *            {@code Path} of the file to write in
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void updateVotes(Map<Integer, Integer> votes, Path filePath) throws IOException {
		Writer writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);
		for (Map.Entry<Integer, Integer> vote : votes.entrySet()) {
			writer.write(vote.getKey() + "\t" + vote.getValue() + System.lineSeparator());
		}
		writer.close();
	}
}
