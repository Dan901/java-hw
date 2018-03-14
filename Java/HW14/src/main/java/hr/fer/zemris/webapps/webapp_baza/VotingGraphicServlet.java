package hr.fer.zemris.webapps.webapp_baza;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import hr.fer.zemris.webapps.webapp_baza.dao.DAO;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOException;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOProvider;
import hr.fer.zemris.webapps.webapp_baza.polls.PollOption;

/**
 * Creates a {@code PNG} image containing a pie chart with voting results for a
 * poll whose ID is received as a parameter.
 *
 * @author Dan
 */
@WebServlet("/voting-graphic")
public class VotingGraphicServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	/** Height of the image. */
	private static final int WIDTH = 600;

	/** Width of the image. */
	private static final int HEIGHT = 400;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pollIDStr = req.getParameter("pollID");
		if (pollIDStr == null) {
			ErrorUtil.sendError("PollID parameter is missing!", req, resp);
			return;
		}
		long pollID = Long.parseLong(pollIDStr);

		List<PollOption> options;
		DAO dao = DAOProvider.getDao();
		try {
			options = dao.getPollOptions(pollID);
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}

		DefaultPieDataset dataset = new DefaultPieDataset();
		options.forEach(option -> {
			dataset.setValue(option.getTitle(), option.getVotesCount());
		});

		JFreeChart chart = ChartFactory.createPieChart3D("", dataset);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);

		BufferedImage bim = chart.createBufferedImage(WIDTH, HEIGHT);
		resp.setContentType("image/png");
		ImageIO.write(bim, "png", resp.getOutputStream());
	}

}
