package hr.fer.zemris.webapps.blog.servlets.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract form that matches the web-representation of a domain object. <br>
 * Every attribute of the domain object should be represented as a
 * {@code String}.<br>
 * Following features are available:
 * <ul>
 * <li>Filling the form from an HTTP request using
 * {@link #fillFromHttpReq(HttpServletRequest)}</li>
 * <li>Validating if the constraints of the attributes are met using
 * {@link #validate()}</li>
 * </ul>
 *
 * @author Dan
 */
public abstract class Form {

	/**
	 * Map containing error messages where key of the error message is the
	 * attribute name.
	 */
	protected final Map<String, String> errors;

	/**
	 * Creates a new {@code Form}.
	 */
	public Form() {
		errors = new HashMap<>();
	}

	/**
	 * Gets the error message for an attribute name.
	 * 
	 * @param attribute
	 *            attribute name whose error message is requested
	 * @return error message for the given attribute; or {@code null} if no
	 *         error message is present
	 */
	public String getError(String attribute) {
		return errors.get(attribute);
	}

	/**
	 * Checks are there any errors in this {@code Form}.
	 * 
	 * @return {@code true} if at least one attribute has an error message
	 *         attached to it
	 */
	public boolean anyErrors() {
		return !errors.isEmpty();
	}

	/**
	 * Checks is there an error message for the given attribute name.
	 * 
	 * @param attribute
	 *            attribute name to check
	 * @return {@code true} if the given attribute has an error message attached
	 *         to it
	 */
	public boolean hasError(String attribute) {
		return errors.containsKey(attribute);
	}

	/**
	 * Modifies the given {@code String}.
	 * 
	 * @param s
	 *            {@code String} to modify
	 * @return empty {@code String} if {@code s} is {@code null}; otherwise
	 *         trimmed version of {@code s}
	 */
	protected String prepare(String s) {
		if (s == null) {
			return "";
		} else {
			return s.trim();
		}
	}

	/**
	 * Fills this {@code Form} from a HTTP request
	 * 
	 * @param req
	 *            {@code HttpServletRequest} with necessary parameters
	 */
	abstract public void fillFromHttpReq(HttpServletRequest req);

	/**
	 * Validates if all attributes meet their constraints.
	 */
	abstract public void validate();
}
