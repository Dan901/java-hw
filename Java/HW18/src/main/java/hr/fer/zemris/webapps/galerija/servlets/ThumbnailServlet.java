package hr.fer.zemris.webapps.galerija.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imgscalr.Scalr;

import hr.fer.zemris.webapps.galerija.model.ImageDB;

/**
 * Servlet that produces a thumbnail {@code JPG} image. <br>
 * Image name is expected as a parameter.
 *
 * @author Dan
 */
@WebServlet("/servlets/thumbnail")
public class ThumbnailServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	/** Size of the thumbnail image. */
	private static final int THUMBNAIL_SIZE = 150;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String imageName = req.getParameter("name");
		if (imageName == null || ImageDB.getImage(imageName) == null) {
			ErrorUtil.sendError("Requested image doesn't exist.", req, resp);
			return;
		}
		
		File thumbnailFolder = ImageDB.getThumbnailFolder().toFile();
		if(!thumbnailFolder.exists()){
			thumbnailFolder.mkdir();
		}
		
		File thumbnailFile = new File(thumbnailFolder, imageName);
		BufferedImage thumbnail;
		if(thumbnailFile.exists()){
			thumbnail = ImageIO.read(thumbnailFile);
		} else {
			BufferedImage original = ImageIO.read(ImageDB.getImageFolder().resolve(imageName).toFile());
			thumbnail = Scalr.resize(original, THUMBNAIL_SIZE);
			ImageIO.write(thumbnail, "jpg", thumbnailFile);
		}

		resp.setContentType("image/jpg");
		ImageIO.write(thumbnail, "jpg", resp.getOutputStream());
	}

}
