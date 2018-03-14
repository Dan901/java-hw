package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This worker creates an HTML table with all received parameters.
 *
 * @author Dan
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("text/html");

		//@formatter:off
		StringBuilder sb = new StringBuilder(
				"<html>\r\n" +
				"  <head>\r\n" + 
				"    <title>Ispis parametara</title>\r\n" +
				"  </head>\r\n" + 
				"  <body>\r\n" + 
				"    <h3>Predani parametri:</h3>\r\n" + 
				"    <table border='1'>\r\n");
		
		for(String name : context.getParameterNames()){
			String value = context.getParameter(name);
			sb.append("      <tr><td>")
			.append(name)
			.append("</td><td>")
			.append(value)
			.append("</td></tr>\r\n");
		}
		sb.append(
				"    </table>\r\n" + 
				"  </body>\r\n" + 
				"</html>\r\n");
		//@formatter:on

		try {
			context.write(sb.toString());
		} catch (IOException e) {
			System.err.println("Discarding request due to error: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
