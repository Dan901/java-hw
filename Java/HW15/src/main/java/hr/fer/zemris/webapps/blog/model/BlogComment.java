package hr.fer.zemris.webapps.blog.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Model of one blog comment.
 *
 * @author Dan
 */
@Entity
@Table(name = "blog_comments")
@Cacheable(true)
public class BlogComment {

	/** ID of the comment. */
	@Id
	@GeneratedValue
	private Long id;

	/** {@code BlogEntry} this comment belongs to. */
	@ManyToOne
	@JoinColumn(nullable = false)
	private BlogEntry blogEntry;

	/** Email of the user that posted this comment. */
	@Column(length = 100, nullable = false)
	private String usersEMail;

	/** Message of the comment. */
	@Column(length = 4096, nullable = false)
	private String message;

	/** {@code Date} of the comment creation. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date postedOn;

	/**
	 * Getter for ID.
	 * 
	 * @return ID of the comment
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for ID.
	 * 
	 * @param id
	 *            ID to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for blog entry.
	 * 
	 * @return {@code BlogEntry} this comment belongs to
	 */
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Setter for blog entry this comment belongs to.
	 * 
	 * @param blogEntry
	 *            blog entry to set
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Getter for user's email.
	 * 
	 * @return user's email
	 */
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Setter for user's email.
	 * 
	 * @param usersEMail
	 *            user's email to set
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Getter for message.
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for message.
	 * 
	 * @param message
	 *            message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter for {@code Date} of the comment creation.
	 * 
	 * @return {@code Date} of the comment creation
	 */
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Setter for {@code Date} of the comment creation.
	 * 
	 * @param postedOn
	 *            {@code Date} of the comment creation to set
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}