package hr.fer.zemris.webapps.blog.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Provider class for {@link EntityManagerFactory} object for an entire
 * web-application.
 *
 * @author Dan
 */
public class JPAEMFProvider {

	/** Factory for obtaining {@code EntityManager} objects. */
	private static EntityManagerFactory emf;

	/**
	 * Fetching the provider.
	 * 
	 * @return current {@code EntityManagerFactory} for obtaining
	 *         {@code EntityManager} objects
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets the current {@code EntityManagerFactory}.
	 * 
	 * @param emf
	 *            {@code EntityManagerFactory} for obtaining
	 *            {@code EntityManager} objects
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}