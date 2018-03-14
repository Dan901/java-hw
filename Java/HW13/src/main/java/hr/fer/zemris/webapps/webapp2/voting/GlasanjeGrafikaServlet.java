package hr.fer.zemris.webapps.webapp2.voting;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import hr.fer.zemris.webapps.webapp2.PieChartUtil;

/**
 * This servlet creates a {@code PNG} image containing a pie chart with voting
 * results.
 *
 * @author Dan
 */
@WebServlet(urlPatterns = { "/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;
	
	/** Height of the image. */
	private static final int WIDTH = 600;

	/** Width of the image. */
	private static final int HEIGHT = 400;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Path bandsFile = Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
		Path votesFile = Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));
		Map<Integer, BandInfo> bandsMap = BandInfo.getBandsWithVotes(bandsFile, votesFile);
		
		PieDataset dataset = createDataset(bandsMap);
		byte[] image = PieChartUtil.createPNGImage(dataset, "", WIDTH, HEIGHT);
		resp.setContentType("image/png");
		resp.setContentLength(image.length);
		resp.getOutputStream().write(image);
	}

	/**
	 * Creates s {@code PieDataset} for {@code JFreeChart} to show.<br>
	 * Data should contain voting results.
	 * 
	 * @param bandsMap
	 *            {@code Map} with voting results
	 * @return {@code PieDataset} with OS usage data
	 */
	private PieDataset createDataset(Map<Integer, BandInfo> bandsMap) {
		DefaultPieDataset result = new DefaultPieDataset();
		bandsMap.forEach((id, bi) -> {
			result.setValue(bi.getName(), bi.getVotes());
		});
		return result;
	}
}
