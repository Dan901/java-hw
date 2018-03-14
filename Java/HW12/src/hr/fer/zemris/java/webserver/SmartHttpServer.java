package hr.fer.zemris.java.webserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * {@code SmartHttpServer} is a simple web server. Program expects a single
 * command line argument: path to the server configuration file. <br>
 * All relevant server parameters are read from the configuration file. <br>
 * This server supports only {@code GET} method. If requested file is in the
 * server root folder than it's content will be sent. Specifically if the file
 * is a smart script than it will be executed and the result will be sent. <br>
 * Apart from file requests if the requested path starts with
 * {@code "/ext/worker"}, server will assume that the {@code worker} is an
 * existing {@link IWebWorker} in workers package that can process the request.
 * If the requested {@code IWebWorker} is in the workers configuration file,
 * than the {@code "/ext"} part is not needed. <br>
 * Once started server can be terminated by typing 'stop'.
 *
 * @author Dan
 */
public class SmartHttpServer {

	/**
	 * Program entry point. Starts the server thread.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Server configuration file path expected!");
			return;
		}

		SmartHttpServer server;
		try {
			server = new SmartHttpServer(args[0]);
		} catch (RuntimeException e) {
			System.out.println("Error occured: " + e.getMessage());
			return;
		}
		server.start();
		System.out.println("Server started. Enter 'stop' to terminate the server.");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String line;
			do {
				line = br.readLine().trim().toLowerCase();
			} while (!line.equals("stop"));
			server.stop();
			System.out.println("Stopping server. It may take a while.");
		} catch (IOException e) {
			System.out.println("Error occured: " + e.getMessage());
		}
	}

	/**
	 * Entry in a server's session {@code Map}.
	 *
	 * @author Dan
	 */
	private static class SessionMapEntry {

		/** Session ID. */
		private String sid;

		/** Time until this {@code SessionMapEntry} is valid. */
		private long validUntil;

		/** {@code Map} with session dependent parameters. */
		private Map<String, String> map;

		/**
		 * Creates a new {@code SessionMapEntry} with given arguments.
		 * 
		 * @param sid
		 *            session id; cannot be {@code null}
		 * @param validUntil
		 *            time until the {@code SessionMapEntry} is valid
		 * @param map
		 *            {@code Map} with session dependent parameters; cannot be
		 *            {@code null}
		 */
		public SessionMapEntry(String sid, long validUntil, Map<String, String> map) {
			this.sid = Objects.requireNonNull(sid);
			this.validUntil = validUntil;
			this.map = Objects.requireNonNull(map);
		}
	}

	/** Length of the session ID. */
	private static final int SID_LENGTH = 20;

	/** {@code Characters} that can be used to generate session ID. */
	private static final char[] SID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	/** Address on which address server listens. */
	private String address;

	/** Port on which address server listens. */
	private int port;

	/** Number of threads used for thread pool. */
	private int workerThreads;

	/** Duration of user session in seconds. */
	private int sessionTimeout;

	/** {@code Map} with MIME types mapped to a file extension. */
	private Map<String, String> mimeTypes;

	/** {@code Map} with {@code IWebWorkers} mapped to their class names. */
	private Map<String, IWebWorker> workersMap;

	/** {@code Thread} for main server work. */
	private ServerThread serverThread;

	/** Thread pool with worker threads. */
	private ExecutorService threadPool;

	/** {@code Path} of the root directory from which the files are served. */
	private Path documentRoot;

	/**
	 * {@code Map} with {@code SessionMapEntries} mapped to their session IDs.
	 */
	private Map<String, SessionMapEntry> sessions;

	/** {@code Random} for generating session IDs. */
	private Random sessionRandom;

	/** {@code true} if the {@link #serverThread} is running. */
	private boolean running;

	/**
	 * Creates a new {@code SmartHttpServer} with given argument. <br>
	 * Also a new {@code Thread} for cleaning up expired session mappings is
	 * created.
	 * 
	 * @param configFileName
	 *            path of the file with server configuration properties.
	 */
	public SmartHttpServer(String configFileName) {
		mimeTypes = new HashMap<>();
		workersMap = new HashMap<>();
		sessions = new HashMap<>();
		sessionRandom = new Random();

		try {
			loadServerProperties(configFileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Timer cleanUp = new Timer(true);
		cleanUp.schedule(new CleanSessionMap(), CleanSessionMap.INTERVAL, CleanSessionMap.INTERVAL);
	}

	/**
	 * Starts the server {@code Thread} and initializes the thread pool.
	 */
	protected synchronized void start() {
		if (!running) {
			serverThread = new ServerThread();
			serverThread.start();
			threadPool = Executors.newFixedThreadPool(workerThreads);
			running = true;
		}
	}

	/**
	 * Stops the server {@code Thread} and the thread pool.
	 */
	protected synchronized void stop() {
		if (running) {
			serverThread.termiate();
			threadPool.shutdown();
			running = false;
		}
	}

	/**
	 * Loads server properties from given file.
	 * 
	 * @param configFileName
	 *            path of the file with server configuration properties
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void loadServerProperties(String configFileName) throws IOException {
		Properties p = new Properties();
		InputStream is = Files.newInputStream(Paths.get(configFileName));
		p.load(is);

		address = p.getProperty("server.address");
		port = Integer.parseInt(p.getProperty("server.port"));
		workerThreads = Integer.parseInt(p.getProperty("server.workerThreads"));
		documentRoot = Paths.get(p.getProperty("server.documentRoot"));
		sessionTimeout = Integer.parseInt(p.getProperty("session.timeout"));
		loadMimeTypes(p.getProperty("server.mimeConfig"));
		loadWorkers(p.getProperty("server.workers"));
	}

	/**
	 * Loads MIME types mappings from given file.
	 * 
	 * @param mimeFileName
	 *            path of the file with MIME types mapped to a file extension
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void loadMimeTypes(String mimeFileName) throws IOException {
		Properties p = new Properties();
		InputStream is = Files.newInputStream(Paths.get(mimeFileName));
		p.load(is);

		@SuppressWarnings("rawtypes")
		Enumeration e = p.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			mimeTypes.put(key, p.getProperty(key));
		}
	}

	/**
	 * Loads {@code IWebWorker} mappings from given file.
	 * 
	 * @param workersFileName
	 *            path of the file with {@code IWebWorkers} mapped to a path
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalArgumentException
	 *             if more workers share the same path
	 */
	private void loadWorkers(String workersFileName) throws IOException {
		Properties p = new Properties();
		InputStream is = Files.newInputStream(Paths.get(workersFileName));
		p.load(is);

		@SuppressWarnings("rawtypes")
		Enumeration e = p.propertyNames();
		while (e.hasMoreElements()) {
			String path = (String) e.nextElement();
			if (workersMap.containsKey(path)) {
				throw new IllegalArgumentException("Workers map already contains path: " + path);
			}
			String fqcn = p.getProperty(path);
			workersMap.put(path, getWebWorker(fqcn));
		}
	}

	/**
	 * Gets an {@code IWebWorker} from given FQCN.
	 * 
	 * @param fqcn
	 *            fully qualified class name
	 * @return {@code IWebWorker} represented by given FQCN
	 * @throws IllegalArgumentException
	 *             if given FQCN is invalid
	 */
	private IWebWorker getWebWorker(String fqcn) {
		try {
			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			Object newObject = referenceToClass.newInstance();
			return (IWebWorker) newObject;
		} catch (Exception e) {
			throw new IllegalArgumentException("Given FQCN is invalid: " + fqcn);
		}
	}

	/**
	 * {@code Thread} that is responsible for creating {@code ClientWorkers} for
	 * each request. <br>
	 * Call {@link #termiate()} when server needs to be stopped.
	 *
	 * @author Dan
	 */
	private class ServerThread extends Thread {

		/** {@code true} if server needs to be stopped. */
		private boolean stop;

		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket()) {
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
				serverSocket.setSoTimeout(1000);

				while (!stop) {
					Socket client;
					try {
						client = serverSocket.accept();
					} catch (SocketTimeoutException e) {
						continue;
					}
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Stops the server.
		 */
		private void termiate() {
			stop = true;
		}
	}

	/**
	 * {@code ClientWorker} processes one HTTP request.
	 *
	 * @author Dan
	 */
	private class ClientWorker implements Runnable {

		/** Default MIME type. */
		private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

		/** {@code Socket} for communicating with the client. */
		private Socket csocket;

		/** {@code InputStream} with the client's request. */
		private PushbackInputStream istream;

		/** {@code OutputStream} to write the response. */
		private OutputStream ostream;

		/** HTTP version. */
		private String version;

		/** HTTP requested method. */
		private String method;

		/** {@code Map} with parameters from the request. */
		private Map<String, String> params;

		/** {@code Map} with session persistent parameters. */
		private Map<String, String> permParams;

		/** {@code List} with cookies to send. */
		private List<RCCookie> outputCookies;

		/** Session ID. */
		private String sid;

		/**
		 * Creates a new {@code ClientWorker} with given argument.
		 * 
		 * @param csocket
		 *            {@code Socket} for communicating with the client
		 */
		private ClientWorker(Socket csocket) {
			this.csocket = csocket;
			params = new HashMap<>();
			outputCookies = new ArrayList<>();
		}

		@Override
		public void run() {
			try {
				serve();
				csocket.close();
			} catch (Exception e) {
				System.err.println("Discarding request due to error: " + e.getMessage());
				throw new RuntimeException(e);
			}
		}

		/**
		 * Process the client's request and sends a response.
		 * 
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		private void serve() throws IOException {
			istream = new PushbackInputStream(csocket.getInputStream(), 4);
			ostream = csocket.getOutputStream();

			List<String> headers = readRequest();
			if (headers == null) {
				sendError(400, "Bad request");
				return;
			}

			String[] firstLine = headers.isEmpty() ? null : headers.get(0).split(" ");
			if (firstLine == null || firstLine.length != 3) {
				sendError(400, "Bad request");
				return;
			}

			method = firstLine[0].toUpperCase();
			if (!method.equals("GET")) {
				sendError(405, "Method Not Allowed");
				return;
			}

			version = firstLine[2].toUpperCase();
			if (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
				sendError(505, "HTTP Version Not Supported");
				return;
			}

			checkSession(headers);

			String requestedPath = firstLine[1];
			String[] pathElems = requestedPath.split("\\?", 2);
			if (pathElems.length == 2) {
				try {
					parseParameters(pathElems[1]);
				} catch (IllegalArgumentException e) {
					sendError(400, "Bad request");
					return;
				}
			}

			RequestContext rc = new RequestContext(ostream, params, permParams, outputCookies);
			rc.setStatusCode(200);
			rc.setStatusText("OK");

			String pathStr = pathElems[0];
			if (pathStr.startsWith("/ext")) {
				String workerName = pathStr.substring(5);
				IWebWorker worker = getWebWorker("hr.fer.zemris.java.webserver.workers." + workerName);
				worker.processRequest(rc);
				return;
			}

			if (workersMap.containsKey(pathStr)) {
				workersMap.get(pathStr).processRequest(rc);
				return;
			}

			if (pathStr.startsWith("/")) {
				pathStr = pathStr.substring(1);
			}
			Path path = documentRoot.resolve(pathStr);
			if (!path.startsWith(documentRoot)) {
				sendError(403, "Forbidden");
				return;
			}
			File file = path.toFile();
			if (!file.exists() || !file.isFile() || !file.canRead()) {
				sendError(404, "Not Found");
				return;
			}

			int index = file.getName().lastIndexOf('.');
			if (index != -1) {
				String ext = file.getName().substring(index + 1);
				if (ext.equals("smscr")) {
					executeScript(path, rc);
					return;
				}

				if (mimeTypes.containsKey(ext)) {
					rc.setMimeType(mimeTypes.get(ext));
				} else {
					rc.setMimeType(DEFAULT_MIME_TYPE);
				}
			}

			rc.setContentLength(file.length());
			rc.write(Files.readAllBytes(path));
		}

		/**
		 * Initializes the {@link #sid} and {@link #permParams} depending on
		 * received cookies. <br>
		 * If no session cookie is received or the session is to old, new
		 * {@code SessionMapEntry} is created.
		 * 
		 * @param headers
		 *            request split into lines
		 * @throws IllegalArgumentException
		 *             if line with cookies has an invalid format
		 */
		private void checkSession(List<String> headers) {
			String sidCandidate = null;
			for (String line : headers) {
				if (!line.startsWith("Cookie:")) {
					continue;
				}
				for (String cookie : line.substring(7).trim().split(";")) {
					String[] cookieElems = cookie.split("=", 2);
					if (cookieElems.length != 2) {
						throw new IllegalArgumentException("Invalid cookie format!");
					}
					String name = cookieElems[0];
					if (name.equals("sid")) {
						sidCandidate = getCookieValue(cookieElems[1]);
						break;
					}
				}
			}

			synchronized (SmartHttpServer.this) {
				if (sidCandidate != null && sessions.containsKey(sidCandidate)) {
					if (sessions.get(sidCandidate).validUntil < System.currentTimeMillis()) {
						sessions.remove(sidCandidate);
					} else {
						sid = sidCandidate;
					}
				}
				if (sid == null) {
					do {
						sid = generateSID();
					} while (sessions.containsKey(sid));
					SessionMapEntry entry = new SessionMapEntry(sid, 0, new ConcurrentHashMap<>());
					sessions.put(sid, entry);
					RCCookie cookie = new RCCookie("sid", sid, null, address, "/", "HttpOnly");
					outputCookies.add(cookie);
				}
			}

			SessionMapEntry entry = sessions.get(sid);
			entry.validUntil = System.currentTimeMillis() + sessionTimeout * 1000;
			permParams = entry.map;
		}

		/**
		 * Generates a random session ID.
		 * 
		 * @return random session ID
		 */
		private String generateSID() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < SID_LENGTH; i++) {
				char c = SID_CHARS[sessionRandom.nextInt(SID_CHARS.length)];
				sb.append(c);
			}
			return sb.toString();
		}

		/**
		 * Removes the surrounding quotes from given {@code String} if they
		 * exist.
		 * 
		 * @param value
		 *            value of the cookie
		 * @return given {@code String} without surrounding quotes
		 */
		private String getCookieValue(String value) {
			if (value.startsWith("\"") && value.endsWith("\"")) {
				value = value.substring(1, value.length() - 1);
			}
			return value;
		}

		/**
		 * Executes a smart script file using {@link SmartScriptEngine}.
		 * 
		 * @param path
		 *            path of the smart script file
		 * @param rc
		 *            context for executing the smart script
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		private void executeScript(Path path, RequestContext rc) throws IOException {
			String docBody = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
			SmartScriptParser parser = new SmartScriptParser(docBody);
			SmartScriptEngine engine = new SmartScriptEngine(parser.getDocumentNode(), rc);
			engine.execute();
		}

		/**
		 * Parses the parameters from a request and puts them in {@link #params}
		 * {@code Map}.
		 * 
		 * @param paramString
		 *            {@code String} with all received parameters
		 */
		private void parseParameters(String paramString) {
			for (String param : paramString.split("&")) {
				String[] paramElems = param.split("=", 2);
				if (paramElems.length != 2) {
					throw new IllegalArgumentException("Invalid parameter definition.");
				}
				params.put(paramElems[0], paramElems[1]);
			}
		}

		/**
		 * Splits the request into lines.
		 * 
		 * @return the request as a {@code List} of lines; or {@code null} if
		 *         request has an invalid format
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		private List<String> readRequest() throws IOException {
			byte[] request = getRequest();
			if (request == null) {
				return null;
			}
			String requestStr = new String(request, StandardCharsets.ISO_8859_1);
			List<String> headers = new ArrayList<>();
			String currentLine = null;

			for (String s : requestStr.split("\r\n")) {
				if (s.isEmpty()) {
					break;
				}
				char c = s.charAt(0);
				if (c == 9 || c == 32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}

			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}

		/**
		 * Reads the whole request until specified termination sequence is
		 * reached.
		 * 
		 * @return the request as a {@code byte} array; or {@code null} if
		 *         request has an invalid format
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		private byte[] getRequest() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[4];

			while (true) {
				if (istream.read(buf) < 4) {
					return null;
				}
				if (buf[0] == 13 && buf[1] == 10 && buf[2] == 13 && buf[3] == 10) {
					break;
				} else {
					bos.write(buf[0]);
					istream.unread(buf, 1, 3);
				}
			}

			return bos.toByteArray();
		}

		/**
		 * Sends an error response to the client.
		 * 
		 * @param statusCode
		 *            error status code
		 * @param statusText
		 *            error status text
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		private void sendError(int statusCode, String statusText) throws IOException {
			RequestContext rc = new RequestContext(ostream, null, null, null);
			rc.setStatusCode(statusCode);
			rc.setStatusText(statusText);
			rc.setMimeType("text/plain");
			rc.setContentLength(0L);
			rc.write(new byte[0]);
		}
	}

	/**
	 * {@code TimerTask} responsible for cleaning up expired session mappings.
	 *
	 * @author Dan
	 */
	private class CleanSessionMap extends TimerTask {

		/** Delay between each run. */
		private static final int INTERVAL = 5 * 60 * 1000;

		@Override
		public void run() {
			synchronized (SmartHttpServer.this) {
				sessions.entrySet().removeIf(e -> {
					return e.getValue().validUntil < System.currentTimeMillis();
				});
			}
		}
	}

}
