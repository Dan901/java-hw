package hr.fer.zemris.webapps.webapp_baza.dao;

/**
 * Exception that is thrown by an implementation of {@link DAO} interface.
 * 
 * @author Dan
 */
public class DAOException extends RuntimeException {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code DAOException} with {@code null} as its detail
	 * message.
	 */
	public DAOException() {
		super();
	}

	/**
	 * Constructs a new runtime exception with the specified detail message,
	 * cause, suppression enabled or disabled, and writable stack trace enabled
	 * or disabled.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause
	 * @param enableSuppression
	 *            whether or not suppression is enabled or disabled
	 * @param writableStackTrace
	 *            whether or not the stack trace should be writable
	 */
	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message and
	 * cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructs a new runtime exception with the specified cause and a detail
	 * message of <tt>(cause==null ? null : cause.toString())</tt> (which
	 * typically contains the class and detail message of <tt>cause</tt>).
	 * 
	 * @param cause
	 *            the cause
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}