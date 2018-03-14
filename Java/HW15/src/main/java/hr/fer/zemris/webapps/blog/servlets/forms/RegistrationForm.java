package hr.fer.zemris.webapps.blog.servlets.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.webapps.blog.dao.DAOProvider;
import hr.fer.zemris.webapps.blog.model.BlogUser;
import hr.fer.zemris.webapps.blog.servlets.util.PasswordUtil;

/**
 * {@link Form} for user registration.
 *
 * @author Dan
 */
public class RegistrationForm extends Form {

	/** First name of the user. */
	private String firstName;

	/** Last name of the user. */
	private String lastName;

	/** Nick of the user. */
	private String nick;

	/** Email of the user. */
	private String email;

	/** Plain password of the user. */
	private String password;

	@Override
	public void fillFromHttpReq(HttpServletRequest req) {
		firstName = prepare(req.getParameter("firstName"));
		lastName = prepare(req.getParameter("lastName"));
		nick = prepare(req.getParameter("nick"));
		email = prepare(req.getParameter("email"));
		password = prepare(req.getParameter("password"));
	}

	/**
	 * Fills this form with data from the given user.
	 * 
	 * @param user
	 *            {@code BlogUser} object
	 */
	public void fillFromUser(BlogUser user) {
		firstName = prepare(user.getFirstName());
		lastName = prepare(user.getLastName());
		nick = prepare(user.getNick());
		email = prepare(user.getEmail());
		password = "";
	}

	/**
	 * Fills data from this form to the given user.
	 * 
	 * @param user
	 *            {@code BlogUser} object
	 */
	public void fillToUser(BlogUser user) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setNick(nick);
		user.setEmail(email);
		user.setPasswordHash(PasswordUtil.calculateHash(password));
	}

	@Override
	public void validate() {
		errors.clear();

		if (firstName.isEmpty()) {
			errors.put("firstName", "First name is obligatory!");
		}

		if (lastName.isEmpty()) {
			errors.put("lastName", "Last name is obligatory!");
		}

		if (nick.isEmpty()) {
			errors.put("nick", "Nick is obligatory!");
		} else if (DAOProvider.getDAO().getBlogUser(nick) != null) {
			errors.put("nick", "Nick already exists!");
		}

		if (password.isEmpty()) {
			errors.put("password", "Password is obligatory!");
		}

		if (email.isEmpty()) {
			errors.put("email", "email is obligatory!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if (l < 3 || p == -1 || p == 0 || p == l - 1) {
				errors.put("email", "email format is invalid!");
			}
		}
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
	 * Getter for password.
	 * 
	 * @return plain password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for password.
	 * 
	 * @param password
	 *            password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
