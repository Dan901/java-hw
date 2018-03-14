package hr.fer.zemris.webapps.blog.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.webapps.blog.dao.DAOException;
import hr.fer.zemris.webapps.blog.dao.DAOProvider;
import hr.fer.zemris.webapps.blog.model.BlogUser;
import hr.fer.zemris.webapps.blog.servlets.forms.LoginForm;
import hr.fer.zemris.webapps.blog.servlets.util.ErrorUtil;

/**
 * Main servlet that displays the home page of the web-application containing
 * login/register options and a list of all registered authors with links to their
 * blogs.
 *
 * @author Dan
 */
@WebServlet("/servlets/main")
public class MainServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	/**
	 * Processes the request according to this servlet's job.
	 * 
	 * @param req
	 *            an {@code HttpServletRequest} object that contains the request
	 *            the client has made of the servlet
	 * @param resp
	 *            an {@code HttpServletResponse} object that contains the
	 *            response the servlet sends to the client
	 * @throws ServletException
	 *             if the request could not be handled
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean loggedIn = (boolean) req.getAttribute("loggedIn");
		if (!loggedIn) {
			LoginForm form = new LoginForm();
			req.setAttribute("form", form);
		}

		List<BlogUser> users;
		try {
			users = DAOProvider.getDAO().getAllUsers();
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}

		req.setAttribute("users", users);
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
}
