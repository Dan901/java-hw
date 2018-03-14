package hr.fer.zemris.webapps.blog.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.webapps.blog.dao.DAOException;
import hr.fer.zemris.webapps.blog.dao.DAOProvider;
import hr.fer.zemris.webapps.blog.model.BlogUser;
import hr.fer.zemris.webapps.blog.servlets.forms.RegistrationForm;
import hr.fer.zemris.webapps.blog.servlets.util.ErrorUtil;

/**
 * Processes the request for registration of a new user.<br>
 * If the filled out {@link RegistrationForm} is without errors, the new
 * {@link BlogUser} is created and stored, and if not the form is returned.
 *
 * @author Dan
 */
@WebServlet("/servlets/new")
public class NewUserServlet extends HttpServlet {

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
		req.setCharacterEncoding("UTF-8");

		if (!"Save".equals(req.getParameter("action"))) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/");
			return;
		}

		RegistrationForm form = new RegistrationForm();
		form.fillFromHttpReq(req);
		form.validate();

		if (form.anyErrors()) {
			form.setPassword("");
			req.setAttribute("form", form);
			req.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(req, resp);
			return;
		}

		BlogUser user = new BlogUser();
		form.fillToUser(user);
		try {
			DAOProvider.getDAO().addObject(user);
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}

		resp.sendRedirect(req.getServletContext().getContextPath() + "/servlets/main");
	}
}
