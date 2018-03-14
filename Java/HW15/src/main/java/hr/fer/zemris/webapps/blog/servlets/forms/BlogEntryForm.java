package hr.fer.zemris.webapps.blog.servlets.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.webapps.blog.model.BlogEntry;

/**
 * {@link Form} for blog entries.
 *
 * @author Dan
 */
public class BlogEntryForm extends Form {

	/** ID of the entry. */
	private String id;

	/** Title of the entry. */
	private String title;

	/** Text of the entry. */
	private String text;

	@Override
	public void fillFromHttpReq(HttpServletRequest req) {
		id = prepare(req.getParameter("id"));
		title = prepare(req.getParameter("title"));
		text = prepare(req.getParameter("text"));
	}

	/**
	 * Fills this form with data from the given entry.
	 * 
	 * @param entry
	 *            {@code BlogEntry} object
	 */
	public void fillFromBlogEntry(BlogEntry entry) {
		if (entry.getId() == null) {
			id = "";
		} else {
			id = entry.getId().toString();
		}
		title = prepare(entry.getTitle());
		text = prepare(entry.getText());
	}

	/**
	 * Fills data from this form to the given entry.
	 * 
	 * @param entry
	 *            {@code BlogEntry} object
	 */
	public void fillToBlogEntry(BlogEntry entry) {
		entry.setTitle(title);
		entry.setText(text);
	}

	@Override
	public void validate() {
		errors.clear();

		if (title.isEmpty()) {
			errors.put("title", "Title is obligatory!");
		}

		if (text.isEmpty()) {
			errors.put("text", "Text is obligatory!");
		}
	}

	/**
	 * Getter for ID.
	 * 
	 * @return ID of the entry
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter for ID.
	 * 
	 * @param id
	 *            ID to set
	 */
	public void setId(String id) {
		this.id = id;
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

}
