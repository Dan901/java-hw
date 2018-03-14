package hr.fer.zemris.webapps.blog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Model of one blog entry.
 *
 * @author Dan
 */
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {

	/** ID of the entry. */
	@Id
	@GeneratedValue
	private Long id;

	/** Comments of the entry. */
	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	private List<BlogComment> comments;

	/** {@code Date} of the entry creation. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date createdAt;

	/** {@code Date} of the last entry modification. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date lastModifiedAt;

	/** Title of the entry. */
	@Column(nullable = false, length = 60)
	private String title;

	/** Text of the entry. */
	@Column(nullable = false, length = 4096)
	private String text;

	/** Creator of the entry. */
	@ManyToOne
	@JoinColumn(nullable = false)
	private BlogUser creator;

	/**
	 * Creates a new {@code BlogEntry}.
	 */
	public BlogEntry() {
		comments = new ArrayList<>();
	}

	/**
	 * Getter for ID.
	 * 
	 * @return ID of the entry
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
	 * Getter for comments.
	 * 
	 * @return comments
	 */
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Setter for comments.
	 * 
	 * @param comments
	 *            comments to set
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Getter for creation {@code Date}.
	 * 
	 * @return creation {@code Date}
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Setter for creation {@code Date}.
	 * 
	 * @param createdAt
	 *            creation {@code Date} to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter for last modification {@code Date}.
	 * 
	 * @return last modification {@code Date}
	 */
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Setter for last modification {@code Date}.
	 * 
	 * @param lastModifiedAt
	 *            last modification {@code Date} to set
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Getter for title.
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for title.
	 * 
	 * @param title
	 *            title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for text.
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter for text.
	 * 
	 * @param text
	 *            text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Getter for creator.
	 * 
	 * @return creator of the entry
	 */
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Setter for creator.
	 * 
	 * @param creator
	 *            creator to set
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}