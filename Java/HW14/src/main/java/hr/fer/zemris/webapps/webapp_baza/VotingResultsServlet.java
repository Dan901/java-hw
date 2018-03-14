package hr.fer.zemris.webapps.webapp_baza;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
 * Obtains the results of voting for a poll whose ID is received as a parameter
 * and displays them.
 *
 * @author Dan
 */
@WebServlet("/voting-results")
public class VotingResultsServlet extends HttpServlet {

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

		PollInfo poll;
		List<PollOption> options;
		DAO dao = DAOProvider.getDao();
		try {
			poll = dao.getPoll(pollID);
			options = dao.getPollOptions(pollID);
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}
		
		if (poll == null) {
			ErrorUtil.sendError("Poll with id: " + pollIDStr + " doesn't exist!", req, resp);
			return;
		}
		req.setAttribute("poll", poll);
		
		Collections.sort(options, (o1, o2) -> -Long.compare(o1.getVotesCount(), o2.getVotesCount()));
		req.setAttribute("options", options);
		
		List<PollOption> winners;
		if(options.isEmpty()){
			winners = new ArrayList<>();
		} else {
			long maxVotes = options.stream().mapToLong(PollOption::getVotesCount).max().getAsLong();
			winners = options.stream().filter(o -> o.getVotesCount() == maxVotes).collect(Collectors.toList());
		}
		req.setAttribute("winners", winners);
		req.getRequestDispatcher("/WEB-INF/pages/results.jsp").forward(req, resp);
	}

}
