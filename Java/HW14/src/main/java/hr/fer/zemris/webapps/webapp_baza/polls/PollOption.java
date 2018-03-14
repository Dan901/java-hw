package hr.fer.zemris.webapps.webapp_baza.polls;

import java.util.Objects;

/**
 * Model for options that are available in a poll for voting. Poll is modeled by
 * {@link PollInfo} class.
 * 
 * @author Dan
 */
public class PollOption {

	/** Option's ID number. */
	private long id;

	/** Title of the option. */
	private String title;

	/** Link that is somehow relevant to this option. */
	private String link;

	/** Number of votes this option received. */
	private long votesCount;

	/**
	 * Creates a new {@code PollOption} with given arguments.
	 * 
	 * @param id
	 *            option's ID number
	 * @param title
	 *            title of the option
	 * @param link
	 *            link that is somehow relevant to this option (an example
	 *            maybe)
	 * @param votesCount
	 *            number of votes this option received
	 */
	public PollOption(long id, String title, String link, long votesCount) {
		this.id = id;
		this.title = Objects.requireNonNull(title);
		this.link = Objects.requireNonNull(link);
		this.votesCount = votesCount;
	}

	/**
	 * @return number of votes this option received
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * @return option's ID number
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return title of this option
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return link that is somehow relevant to this option
	 */
	public String getLink() {
		return link;
	}

}
