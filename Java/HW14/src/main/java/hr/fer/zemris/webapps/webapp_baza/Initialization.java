package hr.fer.zemris.webapps.webapp_baza;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Initializes the web application. <br>
 * Creates a connection poll {@code ComboPooledDataSource} for accessing the
 * database and creates necessary tables (from files) if they don't already
 * exist.
 * 
 * @author Dan
 */
@WebListener
public class Initialization implements ServletContextListener {

	/** Name of the file with information about polls. */
	private static final String POLLS_FILE = "polls.txt";

	//@formatter:off
	/** SQL statement for creating {@code Polls} table. */
	private static final String SQL_CREATE_POLLS = "CREATE TABLE Polls" + 
								"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
								"title VARCHAR(150) NOT NULL," +
								"message CLOB(2048) NOT NULL)";

	/** SQL statement for creating {@code PollOptions} table. */
	private static final String SQL_CREATE_POLL_OPTIONS = "CREATE TABLE PollOptions" + 
								"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
								"optionTitle VARCHAR(100) NOT NULL," +
								"optionLink VARCHAR(150) NOT NULL," + 
								"pollID BIGINT," + 
								"votesCount BIGINT," + 
								"FOREIGN KEY (pollID) REFERENCES Polls(id))";
	//@formatter:on

	/**
	 * {@code ServletContext} extracted from {@code ServletContextEvent} for all
	 * methods to use during initialization.
	 */
	private ServletContext sc;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sc = sce.getServletContext();
		Path path = Paths.get(sc.getRealPath("/WEB-INF/dbsettings.properties"));

		Properties config;
		try {
			InputStream is = Files.newInputStream(path);
			config = new Properties();
			config.load(is);
		} catch (IOException e) {
			throw new RuntimeException("Configuration file not found!");
		}

		String host = config.getProperty("host");
		String port = config.getProperty("port");
		String dbName = config.getProperty("name");
		String user = config.getProperty("user");
		String password = config.getProperty("password");

		if (host == null || port == null || dbName == null || user == null || password == null) {
			throw new RuntimeException("Configuration file is missing necessary properites!");
		}

		String connectionURL = "jdbc:derby://" + host + ":" + port + "/" + dbName + ";user=" + user + ";password="
				+ password;

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Error during poll initialization!", e);
		}
		cpds.setJdbcUrl(connectionURL);
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		Connection con = null;
		try {
			con = cpds.getConnection();
			createTables(con);
			populateTables(con);
		} catch (SQLException | IOException e) {
			throw new RuntimeException("Error during creating poll tables!");
		} finally {
			try {
				con.close();
			} catch (Exception ignorable) {
			}
		}
	}

	/**
	 * Creates tables using {@link #SQL_CREATE_POLLS} and
	 * {@link #SQL_CREATE_POLL_OPTIONS} statements if they don't already exist.
	 * 
	 * @param con
	 *            connection to the database
	 * @throws SQLException
	 *             if an SQL error occurs during creating tables
	 */
	private void createTables(Connection con) throws SQLException {
		PreparedStatement pst = con.prepareStatement(SQL_CREATE_POLLS);
		createTable(pst);
		pst = con.prepareStatement(SQL_CREATE_POLL_OPTIONS);
		createTable(pst);
	}

	/**
	 * Executes the given statement for creating a table. If the table already
	 * exist nothing is changed.
	 * 
	 * @param pst
	 *            {@code PreparedStatement} for creating a table
	 * @throws SQLException
	 *             if an SQL error occurs during executing the statement
	 */
	private void createTable(PreparedStatement pst) throws SQLException {
		try {
			pst.execute();
		} catch (SQLException e) {
			if (!tableAlreadyExists(e)) {
				throw e;
			}
		} finally {
			try {
				pst.close();
			} catch (Exception ignorable) {
			}
		}
	}

	/**
	 * Checks if given exception is caused by trying to create an existing
	 * table.
	 * 
	 * @param e
	 *            {@code SQLException} to check
	 * @return {@code true} if given exception is caused by trying to create an
	 *         existing table; {@code false} otherwise
	 */
	private boolean tableAlreadyExists(SQLException e) {
		return e.getSQLState().equals("X0Y32");
	}

	/**
	 * Checks if {@code Polls} table is empty, and if it is, calls
	 * {@link #insertFromFiles(Connection)} method.
	 * 
	 * @param con
	 *            connection to the database
	 * @throws SQLException
	 *             if an SQL error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void populateTables(Connection con) throws SQLException, IOException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement("SELECT * FROM Polls");
			rs = pst.executeQuery();
			if (!rs.next()) {
				insertFromFiles(con);
			}
		} finally {
			try {
				pst.close();
				rs.close();
			} catch (Exception ignorable) {
			}
		}
	}

	/**
	 * Inserts polls into {@code Polls} table from a predefined file and for
	 * each poll calls {@link #insertPollOptions(Connection, long, String)}
	 * method.
	 * 
	 * @param con
	 *            connection to the database
	 * @throws SQLException
	 *             if an SQL error occurs during insertion
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void insertFromFiles(Connection con) throws SQLException, IOException {
		Path pollsPath = Paths.get(sc.getRealPath("/WEB-INF/" + POLLS_FILE));
		List<String> polls = Files.readAllLines(pollsPath);
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement("INSERT INTO Polls (title, message) VALUES (?,?)",
					Statement.RETURN_GENERATED_KEYS);

			for (String poll : polls) {
				String[] elems = poll.split("\t");
				if (elems.length != 3) {
					throw new RuntimeException("Invalid polls file format!");
				}
				pst.setString(1, elems[1]);
				pst.setString(2, elems[2]);
				pst.executeUpdate();
				rs = pst.getGeneratedKeys();
				if (rs != null && rs.next()) {
					long pollID = rs.getLong(1);
					insertPollOptions(con, pollID, elems[0]);
				} else {
					throw new RuntimeException("Error during inserting into tables!");
				}
			}
		} finally {
			try {
				pst.close();
				rs.close();
			} catch (Exception ignorable) {
			}
		}
	}

	/**
	 * Inserts poll options for one poll into {@code PollOptions} table from the
	 * given file.
	 * 
	 * @param con
	 *            connection to the database
	 * @param pollID
	 *            ID of the poll whose options are inserted
	 * @param fileName
	 *            name of the file with poll options
	 * @throws SQLException
	 *             if an SQL error occurs during insertion
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void insertPollOptions(Connection con, long pollID, String fileName) throws SQLException, IOException {
		Path filePath = Paths.get(sc.getRealPath("/WEB-INF/" + fileName));
		List<String> options = Files.readAllLines(filePath);
		PreparedStatement pst = null;

		try {
			pst = con.prepareStatement(
					"INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES (?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			pst.setLong(3, pollID);
			pst.setInt(4, 0);

			for (String option : options) {
				String[] elems = option.split("\t");
				if (elems.length != 3) {
					throw new RuntimeException("Invalid poll options file format!");
				}
				pst.setString(1, elems[1]);
				pst.setString(2, elems[2]);
				pst.executeUpdate();
			}
		} finally {
			try {
				pst.close();
			} catch (Exception ignorable) {
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}