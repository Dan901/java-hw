package hr.fer.zemris.webapps.blog.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirects to the main servlet.
 *
 * @author Dan
 * @see MainServlet
 */
@WebServlet({"/", "/index.jsp", "/index.html"})
public class IndexServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servlets/main");
	}
}
