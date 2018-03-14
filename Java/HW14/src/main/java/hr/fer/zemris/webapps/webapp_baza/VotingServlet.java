package hr.fer.zemris.webapps.webapp_baza;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.webapps.webapp_baza.dao.DAO;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOException;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOProvider;
import hr.fer.zemris.webapps.webapp_baza.polls.PollInfo;
import hr.fer.zemris.webapps.webapp_baza.polls.PollOption;

/**
 * Obtains available options for a poll whose ID number is received as a
 * parameter and renders a link for voting on each option.
 *
 * @author Dan
 */
@WebServlet("/voting")
public class VotingServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pollIDStr = req.getParameter("pollID");
		if (pollIDStr == null) {
			ErrorUtil.sendError("PollID parameter is missing!", req, resp);
			return;
		}
		long pollID = Long.parseLong(pollIDStr);

		DAO dao = DAOProvider.getDao();
		PollInfo poll;
		try {
			poll = dao.getPoll(pollID);
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}
		
		if (poll == null) {
			ErrorUtil.sendError("Poll with id: " + pollIDStr + " doesn't exist!", req, resp);
			return;
		}
		req.setAttribute("poll", poll);

		List<PollOption> options;
		try {
			options = dao.getPollOptions(pollID);
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}
		req.setAttribute("options", options);
		req.getRequestDispatcher("/WEB-INF/pages/voting.jsp").forward(req, resp);
	}
}
