package hr.fer.zemris.webapps.webapp_baza;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.webapps.webapp_baza.dao.DAOException;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOProvider;
import hr.fer.zemris.webapps.webapp_baza.polls.PollInfo;

/**
 * Obtains available polls and renders links for each one.
 * 
 * @author Dan
 */
@WebServlet({"/", "/index.html"})
public class PollsServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<PollInfo> polls;
		try {
			polls = DAOProvider.getDao().getPolls();
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}
		req.setAttribute("polls", polls);
		req.getRequestDispatcher("/WEB-INF/pages/polls.jsp").forward(req, resp);
	}

}
