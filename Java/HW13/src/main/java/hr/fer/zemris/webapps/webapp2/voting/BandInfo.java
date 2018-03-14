package hr.fer.zemris.webapps.webapp2.voting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@code BandInfo} contains information about a single band. <br>
 * Static methods for creating {@code BandInfo} objects from {@code String} and
 * from files are available.
 *
 * @author Dan
 */
public class BandInfo {

	/** This band's id number. */
	private int id;

	/** This band's name. */
	private String name;

	/** URL to an example song by this band. */
	private String songURL;

	/** Number of votes for this band. */
	private int votes;

	/**
	 * Creates a new {@code BandInfo} with given arguments.
	 * 
	 * @param id
	 *            band's id number
	 * @param name
	 *            band's name
	 * @param songURL
	 *            URL to an example song by the band
	 * @param votes
	 *            number of votes for this band
	 */
	public BandInfo(int id, String name, String songURL, int votes) {
		this.id = id;
		this.name = name;
		this.songURL = songURL;
		this.votes = votes;
	}

	/**
	 * @return this band's id number
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return this band's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return URL to an example song by this band
	 */
	public String getSongURL() {
		return songURL;
	}

	/**
	 * @return number of votes for this band
	 */
	public int getVotes() {
		return votes;
	}

	/**
	 * Sets the number of votes for this band.
	 * 
	 * @param votes
	 *            number of votes
	 */
	public void setVotes(int votes) {
		this.votes = votes;
	}

	/**
	 * Creates a new {@code BandInfo} from given {@code String}. <br>
	 * Number of votes for the band is set to 0.
	 * 
	 * @param str
	 *            {@code String} with band's id, name and URL to an example song
	 *            separated by a tab "{@code \t}"
	 * @return a new {@code BandInfo} from given {@code str} or {@code null} if
	 *         for any reason {@code str} is not in valid format
	 */
	public static BandInfo fromString(String str) {
		if (str == null) {
			return null;
		}

		String[] elems = str.split("\t");
		if (elems.length != 3) {
			return null;
		}
		return new BandInfo(Integer.parseInt(elems[0]), elems[1], elems[2], 0);
	}

	/**
	 * Creates a {@code Map} from a file, where every line contains information
	 * about a single band. <br>
	 * Lines are processed by {@link #fromString(String)} method.
	 * 
	 * @param filePath
	 *            {@code Path} of the file
	 * @return {@code Map} with band's id number as a key and {@code BandInfo}
	 *         as a value
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static Map<Integer, BandInfo> getBands(Path filePath) throws IOException {
		return Files.readAllLines(filePath).stream().map(BandInfo::fromString)
				.sorted((b1, b2) -> Integer.compare(b1.getId(), b2.getId())).filter(Objects::nonNull)
				.collect(Collectors.toMap(b -> b.getId(), b -> b));
	}

	/**
	 * Same as {@link #getBands(Path)} method but uses a second file to get the
	 * number of votes for each band.
	 * 
	 * @param bandsFile
	 *            {@code Path} of the file with information about bands
	 * @param votesFile
	 *            {@code Path} of the file with number of votes for each band
	 * @return {@code Map} with band's id number as a key and {@code BandInfo}
	 *         as a value
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static Map<Integer, BandInfo> getBandsWithVotes(Path bandsFile, Path votesFile) throws IOException {
		Map<Integer, BandInfo> bandsMap = getBands(bandsFile);
		Map<Integer, Integer> votesMap = VoteUtil.getAllVotes(votesFile);

		votesMap.forEach((id, votes) -> {
			if (bandsMap.containsKey(id)) {
				bandsMap.get(id).setVotes(votes);
			}
		});

		return bandsMap;
	}
}
