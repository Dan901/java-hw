package hr.fer.zemris.webapps.blog.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.webapps.blog.model.BlogUser;
import hr.fer.zemris.webapps.blog.servlets.forms.RegistrationForm;

/**
 * Creates a new {@link RegistrationForm} and sends it to the user for filling
 * out.
 *
 * @author Dan
 */
@WebServlet("/servlets/register")
public class RegistrationServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogUser user = new BlogUser();
		RegistrationForm form = new RegistrationForm();
		form.fillFromUser(user);

		req.setAttribute("form", form);
		req.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(req, resp);
	}
}
