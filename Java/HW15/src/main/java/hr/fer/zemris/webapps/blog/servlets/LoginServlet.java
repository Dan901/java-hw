package hr.fer.zemris.webapps.blog.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hr.fer.zemris.webapps.blog.dao.DAOException;
import hr.fer.zemris.webapps.blog.dao.DAOProvider;
import hr.fer.zemris.webapps.blog.model.BlogUser;
import hr.fer.zemris.webapps.blog.servlets.forms.LoginForm;
import hr.fer.zemris.webapps.blog.servlets.util.ErrorUtil;

/**
 * Processes the login request. If the login if successful, session attributes
 * containing the user information are set, so that the future requests can be
 * handled knowing the user is logged in. If the login is not successful,
 * appropriate message is displayed.
 *
 * @author Dan
 */
@WebServlet("/servlets/login")
public class LoginServlet extends HttpServlet {

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
		if (!"Login".equals(req.getParameter("action"))) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/servlets/main");
			return;
		}

		HttpSession session = req.getSession();
		LoginForm form = new LoginForm();
		form.fillFromHttpReq(req);
		try {
			form.validate();
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}

		if (form.anyErrors()) {
			form.setPassword("");
			req.setAttribute("form", form);
			req.setAttribute("loggedIn", false);
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
		} else {
			BlogUser user;
			try {
				user = DAOProvider.getDAO().getBlogUser(form.getNick());
			} catch (DAOException e) {
				ErrorUtil.sendError(e.getMessage(), req, resp);
				return;
			}
			session.setAttribute("user_id", user.getId());
			session.setAttribute("user_fn", user.getFirstName());
			session.setAttribute("user_ln", user.getLastName());
			session.setAttribute("user_nick", user.getNick());
			resp.sendRedirect(req.getServletContext().getContextPath() + "/servlets/main");
		}
	}
}
