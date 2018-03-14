package hr.fer.zemris.webapps.blog.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Model of one blog user.
 *
 * @author Dan
 */
@Entity
@Table(name = "blog_users")
@Cacheable(true)
public class BlogUser {

	/** ID of the user. */
	@Id
	@GeneratedValue
	private Long id;

	/** First name of the user. */
	@Column(nullable = false, length = 30)
	private String firstName;

	/** Last name of the user. */
	@Column(nullable = false, length = 50)
	private String lastName;

	/** Nick of the user. */
	@Column(nullable = false, length = 30, unique = true)
	private String nick;

	/** Email of the user. */
	@Column(nullable = false, length = 100)
	private String email;

	/** Hashed password of the user. */
	@Column(nullable = false, length = 40)
	private String passwordHash;

	/** All blog entries of the user. */
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("createdAt")
	private List<BlogEntry> entries;

	/**
	 * Creates a new {@code BlogUser}.
	 */
	public BlogUser() {
		entries = new ArrayList<>();
	}

	/**
	 * Getter for ID.
	 * 
	 * @return ID of the user
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
	 * Getter for first name.
	 * 
	 * @return first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter for first name.
	 * 
	 * @param firstName
	 *            first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter for last name.
	 * 
	 * @return last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter for last name.
	 * 
	 * @param lastName
	 *            last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter for nick.
	 * 
	 * @return nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Setter for nick.
	 * 
	 * @param nick
	 *            nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Getter for email.
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter for email.
	 * 
	 * @param email
	 *            email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter for hashed password.
	 * 
	 * @return hashed password
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Setter for hashed password.
	 * 
	 * @param passwordHash
	 *            hashed password to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * Getter for blog entries.
	 * 
	 * @return all blog entries
	 */
	public List<BlogEntry> getEntries() {
		return entries;
	}

	/**
	 * Setter for blog entries.
	 * 
	 * @param entries
	 *            blog entries to set
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
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
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
