package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.Random;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This worker creates a game where the user has to guess a number.
 *
 * @author Dan
 */
public class Igra implements IWebWorker {

	/** Key for storing a generated number. */
	private static final String NUMBER = "broj";

	/** Key for storing remaining number of attempts. */
	private static final String REMAINING = "preostalo";

	/** Key for storing user's guess. */
	private static final String GUESS = "kandidat";

	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("text/html");
		String response;

		String number = context.getPersistentParameter(NUMBER);
		if (number == null) {
			int n = new Random().nextInt(100);
			context.setPersistentParameter(NUMBER, Integer.toString(n));
			context.setPersistentParameter(REMAINING, "7");
			response = getResponse("Pogodite broj između 0 i 100!", true);
		} else {
			String guessedStr = context.getParameter(GUESS);
			if (guessedStr == null) {
				response = getResponse("Morate unijeti broj!", true);
			} else {
				int guessed = Integer.parseInt(guessedStr);
				int remaining = Integer.parseInt(context.getPersistentParameter(REMAINING));
				int n = Integer.parseInt(context.getPersistentParameter(NUMBER));
				int compare = Integer.compare(n, guessed);
				if (remaining > 1) {
					if (compare == 0) {
						context.removePersistentParameter(NUMBER);
						context.removePersistentParameter(REMAINING);
						response = getResponse("Bravo, pogodili ste!", false);
					} else {
						remaining--;
						context.setPersistentParameter(REMAINING, Integer.toString(remaining));
						String message = "Traženi broj je " + (compare < 0 ? "manji" : "veći.")
								+ " Preostalo pokušaja: " + remaining;
						response = getResponse(message, true);
					}
				} else {
					if (compare == 0) {
						response = getResponse("Bravo, pogodili ste!", false);
					} else {
						response = getResponse("Traženi broj je bio: " + n, false);
					}
					context.removePersistentParameter(NUMBER);
					context.removePersistentParameter(REMAINING);
				}
			}
		}

		try {
			context.write(response);
		} catch (IOException e) {
			System.err.println("Discarding request due to error: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Forms a HTML response.
	 * 
	 * @param message
	 *            message to include
	 * @param textField
	 *            {@code true} if the response has a text field for user input
	 * @return {@code String} containing generated response
	 */
	private String getResponse(String message, boolean textField) {
		StringBuilder sb = new StringBuilder();
		//@formatter:off
		sb.append("<html>\r\n" +
				"  <head>\r\n" + 
				"    <title>Pogodi broj</title>\r\n" +
				"  </head>\r\n" + 
				"  <body>\r\n" +
				"<h2>" + message + "</h2>");
		if(textField){
			sb.append("<form action=\"/ext/Igra\">" + 
					"Što mislite koji je broj? <input type=\"text\" name=\"" + GUESS + "\"><br>" + 
					"<input type=\"submit\"></form>");
		}
		sb.append("  </body>\r\n" + 
				"</html>\r\n");
		//@formatter:on
		
		return sb.toString();
	}

}
