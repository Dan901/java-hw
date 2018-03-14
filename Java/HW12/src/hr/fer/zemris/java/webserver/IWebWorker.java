package hr.fer.zemris.java.webserver;

/**
 * Interface for any worker that can process a request.
 *
 * @author Dan
 */
public interface IWebWorker {

	/**
	 * Processes a request and creates a content for the client.
	 * 
	 * @param context
	 *            for sending content to the client
	 */
	void processRequest(RequestContext context);
}
