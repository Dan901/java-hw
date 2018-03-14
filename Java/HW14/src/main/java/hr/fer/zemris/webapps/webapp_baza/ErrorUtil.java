package hr.fer.zemris.webapps.webapp_baza;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility class for displaying error messages to the user.
 *
 * @author Dan
 */
public class ErrorUtil {

	/**
	 * Sends an error message as a response.
	 * 
	 * @param message
	 *            error message
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
	public static void sendError(String message, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("message", message);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}
}
