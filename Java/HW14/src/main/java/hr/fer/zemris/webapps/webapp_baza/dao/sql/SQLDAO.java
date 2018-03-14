package hr.fer.zemris.webapps.webapp_baza.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.webapps.webapp_baza.dao.DAO;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOException;
import hr.fer.zemris.webapps.webapp_baza.polls.PollInfo;
import hr.fer.zemris.webapps.webapp_baza.polls.PollOption;

/**
 * Implementation of {@link DAO} that uses an SQL language for obtaining the
 * data from a relational database.
 * 
 * @author Dan
 */
public class SQLDAO implements DAO {

	@Override
	public List<PollInfo> getPolls() throws DAOException {
		List<PollInfo> polls = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement("SELECT * FROM Polls ORDER BY id");
			rs = pst.executeQuery();
			while (rs != null && rs.next()) {
				PollInfo poll = new PollInfo(rs.getLong(1), rs.getString(2), rs.getString(3));
				polls.add(poll);
			}
		} catch (Exception e) {
			throw new DAOException("Error during obtaining polls!");
		} finally {
			try {
				pst.close();
				rs.close();
			} catch (Exception ignorable) {
			}
		}

		return polls;
	}
	
	@Override
	public PollInfo getPoll(long pollID) throws DAOException {
		PollInfo poll = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement("SELECT * FROM Polls WHERE id = ?");
			pst.setLong(1, pollID);
			rs = pst.executeQuery();
			if (rs != null && rs.next()) {
				poll = new PollInfo(rs.getLong(1), rs.getString(2), rs.getString(3));
			}
		} catch (Exception e) {
			throw new DAOException("Error during obtaining polls!");
		} finally {
			try {
				pst.close();
				rs.close();
			} catch (Exception ignorable) {
			}
		}
		
		return poll;
	}
	
	@Override
	public Long getPollFromOption(long id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = con.prepareStatement("SELECT pollID FROM PollOptions WHERE id = ?");
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs != null && rs.next()) {
				return rs.getLong(1);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DAOException("Error during obtaining polls!");
		} finally {
			try {
				pst.close();
				rs.close();
			} catch (Exception ignorable) {
			}
		}
	}

	@Override
	public List<PollOption> getPollOptions(long pollID) throws DAOException {
		List<PollOption> options = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(
					"SELECT id, optionTitle, optionLink, votesCount FROM PollOptions WHERE pollID = ? ORDER BY id");
			pst.setLong(1, pollID);
			rs = pst.executeQuery();
			while (rs != null && rs.next()) {
				PollOption option = new PollOption(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4));
				options.add(option);
			}
		} catch (Exception e) {
			throw new DAOException("Error during obtaining poll options!");
		} finally {
			try {
				pst.close();
				rs.close();
			} catch (Exception ignorable) {
			}
		}

		return options;
	}

	@Override
	public void incPollOptionVotes(long id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;

		try {
			pst = con.prepareStatement("UPDATE PollOptions SET votesCount = votesCount + 1 WHERE id = ?");
			pst.setLong(1, id);
			int n = pst.executeUpdate();
			if (n != 1) {
				throw new DAOException("Error during updating votes!");
			}
		} catch (Exception e) {
			throw new DAOException("Error during updating votes!");
		} finally {
			try {
				pst.close();
			} catch (Exception ignorable) {
			}
		}
	}

}
