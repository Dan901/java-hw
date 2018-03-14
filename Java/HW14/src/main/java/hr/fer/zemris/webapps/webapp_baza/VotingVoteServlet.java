package hr.fer.zemris.webapps.webapp_baza;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.webapps.webapp_baza.dao.DAO;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOException;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOProvider;

/**
 * Registers a single vote for an option whose ID is received as a parameter and
 * calls the servlet responsible for displaying results.
 *
 * @author Dan
 */
@WebServlet("/voting-vote")
public class VotingVoteServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idStr = req.getParameter("id");
		if (idStr == null) {
			ErrorUtil.sendError("ID parameter is missing!", req, resp);
			return;
		}
		
		long id = Long.parseLong(idStr);
		Long pollID;
		DAO dao = DAOProvider.getDao();
		try {
			dao.incPollOptionVotes(id);
			pollID = dao.getPollFromOption(id);
		} catch(DAOException e){
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}
		
		if(pollID == null){
			ErrorUtil.sendError("This poll option is not linked with any poll!", req, resp);
		} else {
			resp.sendRedirect(req.getContextPath() + "/voting-results?pollID=" + pollID);
		}
	}

}
