package hr.fer.zemris.webapps.webapp_baza.dao;

import java.util.List;

import hr.fer.zemris.webapps.webapp_baza.polls.PollInfo;
import hr.fer.zemris.webapps.webapp_baza.polls.PollOption;

/**
 * Interface towards the data persistence layer. <br>
 * This interface is for working with polls and offers methods for obtaining
 * polls, poll options and updating the number of votes.
 * 
 * @author Dan
 */
public interface DAO {

	/**
	 * Gets all available polls.
	 * 
	 * @return all available polls as a {@code List} of {@code PollInfo} objects
	 * @throws DAOException
	 *             if any kind of exception occurs
	 */
	List<PollInfo> getPolls() throws DAOException;

	/**
	 * Gets the poll with given ID.
	 * 
	 * @param pollID
	 *            poll's ID number
	 * @return a {@code PollInfo} with given ID; or {@code null} if the poll
	 *         doesn't exist
	 * @throws DAOException
	 *             if any kind of exception occurs
	 */
	PollInfo getPoll(long pollID) throws DAOException;

	/**
	 * Gets the poll ID number from poll option whose ID is given.
	 * 
	 * @param id
	 *            poll option's ID number
	 * @return ID number of the poll that contains the poll options with given
	 *         {@code id}; or {@code null} if the poll options is not linked
	 *         with any poll
	 * @throws DAOException
	 *             if any kind of exception occurs
	 */
	Long getPollFromOption(long id) throws DAOException;

	/**
	 * Gets all options for one poll.
	 * 
	 * @param pollID
	 *            ID number of the poll whose options are required
	 * @return all poll options for the poll with given ID number as a
	 *         {@code List} of {@code PollOption} objects
	 * @throws DAOException
	 *             if any kind of exception occurs
	 */
	List<PollOption> getPollOptions(long pollID) throws DAOException;

	/**
	 * Increases the number of votes for a single {@code PollOption} by 1.
	 * 
	 * @param id
	 *            poll option's id
	 * @throws DAOException
	 *             if any kind of exception occurs
	 */
	void incPollOptionVotes(long id) throws DAOException;
}
