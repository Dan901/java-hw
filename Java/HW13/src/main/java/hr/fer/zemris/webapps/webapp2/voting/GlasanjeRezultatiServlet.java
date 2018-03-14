package hr.fer.zemris.webapps.webapp2.voting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet displays voting results stored in a file.
 *
 * @author Dan
 */
@WebServlet(urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bandsFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		Path bandsFile = Paths.get(bandsFileName);

		String votesFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path votesFile = Paths.get(votesFileName);
		File file = votesFile.toFile();
		if (!file.exists()) {
			file.createNewFile();
		}

		Map<Integer, BandInfo> bandsMap = BandInfo.getBandsWithVotes(bandsFile, votesFile);
		List<Map.Entry<Integer, BandInfo>> bandsList = new ArrayList<>(bandsMap.entrySet());
		Collections.sort(bandsList, (e1, e2) -> -Integer.compare(e1.getValue().getVotes(), e2.getValue().getVotes()));
		
		List<Map.Entry<Integer, BandInfo>> winners = new ArrayList<>();
		if(!bandsList.isEmpty()){
			int maxVotes = bandsList.get(0).getValue().getVotes();
			for (Map.Entry<Integer,BandInfo> entry : bandsList) {
				if(entry.getValue().getVotes() == maxVotes){
					winners.add(entry);
				}
			}
		}
		
		getServletContext().setAttribute("results", bandsList);
		getServletContext().setAttribute("winners", winners);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}
