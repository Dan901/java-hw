package hr.fer.zemris.webapps.webapp2.voting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet processes vote for one band. <br>
 * Band's id number is expected as a URL parameter and vote is added to the
 * current number of stored votes.
 *
 * @author Dan
 */
@WebServlet(urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idStr = req.getParameter("id");
		if (idStr == null) {
			req.setAttribute("message", "ID parameter expected!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path path = Paths.get(fileName);
		File file = path.toFile();
		if (!file.exists()) {
			file.createNewFile();
		}

		int id = Integer.parseInt(idStr);
		Map<Integer, Integer> votes = VoteUtil.getAllVotes(path);
		votes.compute(id, (k, v) -> v == null ? 1 : v + 1);
		VoteUtil.updateVotes(votes, path);

		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
}
