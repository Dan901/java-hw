package hr.fer.zemris.webapps.blog.dao;

import java.util.List;

import hr.fer.zemris.webapps.blog.model.BlogEntry;
import hr.fer.zemris.webapps.blog.model.BlogUser;

/**
 * Interface towards the data persistence layer. <br>
 * This interface supports basic operations needed for persisting blog data.
 * 
 * @author Dan
 */
public interface DAO {

	/**
	 * Gets the blog entry with given ID.
	 * 
	 * @param id
	 *            ID number of the entry
	 * @return {@code BlogEntry} with given ID; or {@code null} if the entry
	 *         doesn't exist
	 * @throws DAOException
	 *             if any kind of error occurs
	 */
	BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Gets the user with given nick.
	 * 
	 * @param nick
	 *            users' nick
	 * @return {@code BlogUser} with given nick; or {@code null} if the user
	 *         doesn't exist
	 * @throws DAOException
	 *             if any kind of error occurs
	 */
	BlogUser getBlogUser(String nick) throws DAOException;

	/**
	 * Persist the given object.
	 * 
	 * @param object
	 *            object to persist
	 * @throws DAOException
	 *             if any kind of error occurs
	 */
	void addObject(Object object) throws DAOException;

	/**
	 * Gets all registered users.
	 * 
	 * @return all users as a {@code List} of {@code BlogUser} objects
	 * @throws DAOException
	 *             if any kind of error occurs
	 */
	List<BlogUser> getAllUsers() throws DAOException;

}