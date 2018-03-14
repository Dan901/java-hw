package hr.fer.zemris.webapps.webapp2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Creates a {@code PNG} image containing a pie chart with data about usage of
 * different operating systems from a survey.
 * 
 * @author Dan
 */
@WebServlet(urlPatterns = { "/reportImage" })
public class ReportImageServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	/** Height of the image. */
	private static final int WIDTH = 500;

	/** Width of the image. */
	private static final int HEIGHT = 270;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PieDataset dataset = createDataset();
		byte[] image = PieChartUtil.createPNGImage(dataset, "Which operating system are you using?", WIDTH, HEIGHT);
		
		resp.setContentType("image/png");
		resp.setContentLength(image.length);
		resp.getOutputStream().write(image);
	}

	/**
	 * Creates s {@code PieDataset} for {@code JFreeChart} to show.<br>
	 * Data contains percentages of different operating systems in use.
	 * 
	 * @return {@code PieDataset} with OS usage data
	 */
	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 29);
		result.setValue("Mac", 20);
		result.setValue("Windows", 51);
		return result;
	}

}
