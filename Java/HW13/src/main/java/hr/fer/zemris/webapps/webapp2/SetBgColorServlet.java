package hr.fer.zemris.webapps.webapp2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the background color for the whole web-application to the color it
 * received in the parameter {@code color}.
 *
 * @author Dan
 */
@WebServlet(urlPatterns = { "/setcolor" })
public class SetBgColorServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = req.getParameter("color");
		if (color != null) {
			req.getSession().setAttribute("pickedBgCol", color);
			resp.sendRedirect(req.getContextPath() + "/index.jsp");
		}
	}
}
