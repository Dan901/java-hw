package hr.fer.zemris.webapps.galerija.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.webapps.galerija.model.ImageDB;

/**
 * Servlet that produces a full size {@code JPG} image. <br>
 * Image name is expected as a parameter.
 *
 * @author Dan
 */
@WebServlet("/servlets/image")
public class ImageServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String imageName = req.getParameter("name");
		if (imageName == null || ImageDB.getImage(imageName) == null) {
			ErrorUtil.sendError("Requested image doesn't exist.", req, resp);
			return;
		}
		
		BufferedImage image = ImageIO.read(ImageDB.getImageFolder().resolve(imageName).toFile());
		resp.setContentType("image/jpg");
		ImageIO.write(image, "jpg", resp.getOutputStream());
	}
}
