package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Demonstration of {@link SmartScriptEngine}.
 *
 * @author Dan
 */
public class DemoSmartScriptEngine {

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		demo1();
		// demo2();
		// demo3();
		// demo4();
	}

	/**
	 * Demonstration 1.
	 */
	private static void demo1() {
		SmartScriptParser p = parse("webroot/scripts/osnovni.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		SmartScriptEngine engine = new SmartScriptEngine(p.getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies));
		try {
			engine.execute();
		} catch (RuntimeException e) {
			System.out.println("Error while executing: " + e.getMessage() + " Exiting.");
			System.exit(-1);
		}
	}

	/**
	 * Demonstration 2.
	 */
	private static void demo2() {
		SmartScriptParser p = parse("webroot/scripts/zbrajanje.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");

		SmartScriptEngine engine = new SmartScriptEngine(p.getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies));
		try {
			engine.execute();
		} catch (RuntimeException e) {
			System.out.println("Error while executing: " + e.getMessage() + " Exiting.");
			System.exit(-1);
		}
	}

	/**
	 * Demonstration 3.
	 */
	private static void demo3() {
		SmartScriptParser p = parse("webroot/scripts/brojPoziva.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);

		SmartScriptEngine engine = new SmartScriptEngine(p.getDocumentNode(), rc);
		try {
			engine.execute();
		} catch (RuntimeException e) {
			System.out.println("Error while executing: " + e.getMessage() + " Exiting.");
			System.exit(-1);
		}
		System.out.println();
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
	}

	/**
	 * Demonstration 4.
	 */
	private static void demo4() {
		SmartScriptParser p = parse("webroot/scripts/fibonacci.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		SmartScriptEngine engine = new SmartScriptEngine(p.getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies));
		try {
			engine.execute();
		} catch (RuntimeException e) {
			System.out.println("Error while executing: " + e.getMessage() + " Exiting.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Parses the given file.
	 * 
	 * @param filePath
	 *            file path
	 * @return {@code SmartScriptParser} containing parsed file
	 */
	private static SmartScriptParser parse(String filePath) {
		try {
			String docBody = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
			return new SmartScriptParser(docBody);
		} catch (IOException e) {
			System.out.println("An I/O exception occured: " + e.getMessage() + " Exiting.");
			System.exit(-1);
		} catch (SmartScriptParserException e) {
			System.out.println("Invalid file: " + e.getMessage() + " Exiting.");
			System.exit(-1);
		}
		return null;
	}

}
