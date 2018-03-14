package hr.fer.zemris.webapps.blog.dao.jpa;

import java.util.List;

import javax.persistence.NoResultException;

import hr.fer.zemris.webapps.blog.dao.DAO;
import hr.fer.zemris.webapps.blog.dao.DAOException;
import hr.fer.zemris.webapps.blog.model.BlogEntry;
import hr.fer.zemris.webapps.blog.model.BlogUser;

/**
 * Implementation of {@link DAO} based on Java Persistence API.
 * 
 * @author Dan
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		try {
			return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	@Override
	public BlogUser getBlogUser(String nick) throws DAOException {
		try {
			return (BlogUser) JPAEMProvider.getEntityManager()
					.createQuery("select u from BlogUser as u where u.nick=:nick").setParameter("nick", nick)
					.setHint("org.hibernate.cacheable", true).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void addObject(Object object) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().persist(object);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlogUser> getAllUsers() throws DAOException {
		try {
			return JPAEMProvider.getEntityManager().createQuery("select u from BlogUser as u").getResultList();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
}