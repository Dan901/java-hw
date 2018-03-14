package hr.fer.zemris.webapps.webapp_baza.polls;

import java.util.Objects;

/**
 * Model of one poll. Poll options that are available for voting are modeled by
 * {@link PollOption} class.
 * 
 * @author Dan
 */
public class PollInfo {

	/** Polls' ID number. */
	private long id;

	/** Title of the poll. */
	private String title;

	/** Message to show with poll options. */
	private String message;

	/**
	 * Creates a new {@code PollInfo} with given arguments.
	 * 
	 * @param id
	 *            poll's ID number
	 * @param title
	 *            title of the poll
	 * @param message
	 *            message to show with poll options
	 */
	public PollInfo(long id, String title, String message) {
		this.id = id;
		this.title = Objects.requireNonNull(title);
		this.message = Objects.requireNonNull(message);
	}

	/**
	 * @return poll's ID number
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return title of this poll
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return message to show with poll options
	 */
	public String getMessage() {
		return message;
	}

}
