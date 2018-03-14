package hr.fer.zemris.webapps.blog.servlets.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.webapps.blog.model.BlogComment;

/**
 * {@link Form} for blog comments.
 *
 * @author Dan
 */
public class BlogCommentForm extends Form {

	/** Email of the user that posted this comment. */
	private String email;

	/** Message of the comment. */
	private String message;

	@Override
	public void fillFromHttpReq(HttpServletRequest req) {
		email = prepare(req.getParameter("email"));
		message = prepare(req.getParameter("message"));
	}

	/**
	 * Fills this form with data from the given comment
	 * 
	 * @param comment
	 *            {@code BlogComment} object
	 */
	public void fillFromBlogComment(BlogComment comment) {
		email = prepare(comment.getUsersEMail());
		message = prepare(comment.getMessage());
	}

	/**
	 * Fills data from this form to the given comment
	 * 
	 * @param comment
	 *            {@code BlogComment} object
	 */
	public void fillToBlogComment(BlogComment comment) {
		comment.setUsersEMail(email);
		comment.setMessage(message);
	}

	@Override
	public void validate() {
		errors.clear();

		if (email.isEmpty()) {
			errors.put("email", "Email is obligatory!");
		}

		if (message.isEmpty()) {
			errors.put("message", "Message is obligatory!");
		}
	}

	/**
	 * Getter for user's email.
	 * 
	 * @return user's email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter for user's email.
	 * 
	 * @param email
	 *            user's email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

}
