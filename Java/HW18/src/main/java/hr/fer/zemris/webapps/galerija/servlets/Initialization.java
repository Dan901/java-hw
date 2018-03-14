package hr.fer.zemris.webapps.galerija.servlets;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import hr.fer.zemris.webapps.galerija.model.ImageDB;

/**
 * Initializes the web-application.<br>
 * Calls {@link ImageDB#load(Path)} method.
 *
 * @author Dan
 */
@WebListener
public class Initialization implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ImageDB.load(Paths.get(sce.getServletContext().getRealPath("/WEB-INF/")));
		} catch (IOException e) {
			throw new RuntimeException("Cannot start the application.", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
