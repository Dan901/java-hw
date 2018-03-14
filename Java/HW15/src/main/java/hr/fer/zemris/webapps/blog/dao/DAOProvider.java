package hr.fer.zemris.webapps.blog.dao;

import hr.fer.zemris.webapps.blog.dao.jpa.JPADAOImpl;

/**
 * Singleton class that knows which object that implements the {@link DAO}
 * interface should be returned as a provider for accessing the data persistence
 * layer.
 * 
 * @author Dan
 */
public class DAOProvider {

	/** Provider that implements the {@code DAO} interface. */
	private static DAO dao = new JPADAOImpl();

	/**
	 * Fetching the provider.
	 * 
	 * @return {@code DAO} implementation for accessing the data persistence
	 *         layer
	 */
	public static DAO getDAO() {
		return dao;
	}

}