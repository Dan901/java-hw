package hr.fer.zemris.webapps.webapp_baza.dao.sql;

import java.sql.Connection;

/**
 * Provides connection dependent on the current thread. <br>
 * Connections are stored in a {@link ThreadLocal} object.
 * 
 * @author Dan
 */
public class SQLConnectionProvider {

	/** All currently stored connections. */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Sets the connection for current thread, or removes it if the argument is
	 * {@code null}.
	 * 
	 * @param con
	 *            connection to the database
	 */
	public static void setConnection(Connection con) {
		if (con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Gets the connection for current thread.
	 * 
	 * @return connection to the database that is safe to use
	 */
	public static Connection getConnection() {
		return connections.get();
	}

}