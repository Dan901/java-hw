package hr.fer.zemris.webapps.webapp2;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creates a table with sine and cosine values for all angles in interval
 * {@code [a,b]} where {@code a} and {@code b} are URL parameters. <br>
 * If any parameter is left out, default values are used.
 *
 * @author Dan
 */
@WebServlet(urlPatterns = { "/trigonometric" })
public class TrigonometricServlet extends HttpServlet {

	/**
	 * Encapsulation of sine and cosine values for an angle.
	 *
	 * @author Dan
	 */
	public static class TrigValues {

		/** Sine value. */
		private double sinValue;

		/** Cosine value. */
		private double cosValue;

		/**
		 * Creates a new {@code TrigValues} with given argument.
		 * 
		 * @param angle
		 *            angle in radians to calculate sine an cosine values for
		 */
		public TrigValues(double angle) {
			sinValue = Math.sin(angle);
			cosValue = Math.cos(angle);
		}

		/**
		 * @return sine value of this {@code TrigValues}
		 */
		public double getSinValue() {
			return sinValue;
		}

		/**
		 * @return cosine value of this {@code TrigValues}
		 */
		public double getCosValue() {
			return cosValue;
		}
	}

	/** */
	private static final long serialVersionUID = 1L;

	/** Default lower angle boundary. */
	private static final int DEAFULT_A = 0;

	/** Default upper angle boundary. */
	private static final int DEAFULT_B = 360;

	/** Maximum difference between lower and upper boundaries. */
	private static final int LIMIT = 720;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String aStr = req.getParameter("a");
		String bStr = req.getParameter("b");
		int a, b;
		try {
			if (aStr == null) {
				a = DEAFULT_A;
			} else {
				a = Integer.parseInt(aStr);
			}
			if (bStr == null) {
				b = DEAFULT_B;
			} else {
				b = Integer.parseInt(bStr);
			}
		} catch (NumberFormatException e) {
			req.setAttribute("message", "a and b parameters have to be numbers!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		if (a > b) {
			int temp = a;
			a = b;
			b = temp;
		}
		b = Integer.min(b, a + LIMIT);

		Map<Integer, TrigValues> values = new TreeMap<>();
		for (int i = a; i <= b; i++) {
			double rad = i * Math.PI / 180;
			TrigValues tv = new TrigValues(rad);
			values.put(i, tv);
		}

		req.setAttribute("values", values);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
}
