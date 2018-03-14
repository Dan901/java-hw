package hr.fer.zemris.webapps.webapp2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This {@code ServletContextListener} just sets the current time, at the moment
 * of initialization, into {@code ServletContext's} attributes.
 *
 * @author Dan
 */
@WebListener
public class StartTimeListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("startTime", System.currentTimeMillis());
	}

}
