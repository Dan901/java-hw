package hr.fer.zemris.webapps.blog.dao.jpa;

import javax.persistence.EntityManager;

import hr.fer.zemris.webapps.blog.dao.DAOException;

/**
 * Provides {@code EntityManager} objects dependent of the current thread.
 * {@link ThreadLocal} is used for storage.
 *
 * @author Dan
 */
public class JPAEMProvider {

	/** Storage of {@code LocalData} for each thread. */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Gets the {@code EntityManager} for current thread and starts the
	 * transaction.
	 * 
	 * @return {@code EntityManager} only current thread has access to
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if (ldata == null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Commits the transaction and then closes the {@code EntityManager} linked
	 * to current thread.
	 * 
	 * @throws DAOException
	 *             if an error occurs
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if (ldata == null) {
			return;
		}

		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}

		try {
			ldata.em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}

		locals.remove();
		if (dex != null) {
			throw dex;
		}
	}

	/**
	 * Wrapper for data used by one thread.
	 *
	 * @author Dan
	 */
	private static class LocalData {

		/** {@code EntityManager} for one thread. */
		private EntityManager em;
	}

}