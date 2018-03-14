package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * {@code RequestContext} holds context for executing smart scripts. It also
 * generates a HTML header before the result is written. <br>
 * Constructor expects the {@code OutputStream} for writing, {@code Map} for
 * each parameter type and a {@code List} of cookies, which are modeled by the
 * {@link RCCookie} class. <br>
 * Header properties are set to default values and can be changed with various
 * setter methods. <br>
 * Two methods for writing data are available: {@link #write(byte[])} and
 * {@link #write(String)}, and before anything is written header is generated
 * and written.
 *
 * @author Dan
 */
public class RequestContext {

	/**
	 * This class models one cookie used by {@code RequestContext} class.
	 *
	 * @author Dan
	 */
	public static class RCCookie {

		/** Name of this {@code RCCookie}. */
		private String name;

		/** Value of this {@code RCCookie}. */
		private String value;

		/** Domain of this {@code RCCookie}. */
		private String domain;

		/** Path of this {@code RCCookie}. */
		private String path;

		/** Maximum age of this {@code RCCookie}. */
		private Integer maxAge;

		/** Cookie type. */
		private String type;

		/**
		 * Creates a new {@code RCCookie} with given arguments.
		 * 
		 * @param name
		 *            name of the cookie; cannot be {@code null}
		 * @param value
		 *            value of the cookie; cannot be {@code null}
		 * @param maxAge
		 *            maximum age of the cookie
		 * @param domain
		 *            domain of the cookie
		 * @param path
		 *            path of the cookie
		 * @param type
		 *            type of the cookie, for example: {@code HttpOnly}
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path, String type) {
			this.name = Objects.requireNonNull(name);
			this.value = Objects.requireNonNull(value);
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
			this.type = type;
		}

		/**
		 * @return the name of this {@code RCCookie}
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the value of this {@code RCCookie}
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return the domain of this {@code RCCookie}
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * @return the path of this {@code RCCookie}
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @return the maximum age of this {@code RCCookie}
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

		/**
		 * @return the type of this {@code RCCookie}
		 */
		public String getType() {
			return type;
		}
	}

	/** Default encoding used for the data, not for the header. */
	private static final String DEFAULT_ENCODING = "UTF-8";

	/** {@code Charset} used for encoding the header. */
	private static final Charset HEADER_CHARSET = StandardCharsets.ISO_8859_1;

	/** Default status code. */
	private static final int DEFAULT_STATUS_CODE = 200;

	/** Default status text. */
	private static final String DEFAULT_STATUS_TEXT = "OK";

	/** Default MIME type. */
	private static final String DEFAULT_MIME_TYPE = "text/html";

	/** {@code OutputStream} for writing header and data. */
	private OutputStream outputStream;

	/** {@code Charset} used for encoding data. */
	private Charset charset;

	/** {@code String} representation of the charset. */
	private String encoding;

	/** Current status code. */
	private int statusCode;

	/** Current status text. */
	private String statusText;

	/** Current MIME type. */
	private String mimeType;

	/** Current content length. */
	private Long contentLength;

	/** Parameters read-only {@code Map}. */
	private Map<String, String> parameters;

	/** Temporary parameters {@code Map}. */
	private Map<String, String> temporaryParameters;

	/** Persistent parameters {@code Map}. */
	private Map<String, String> persistentParameters;

	/** {@code List} of cookies. */
	private List<RCCookie> outputCookies;

	/** {@code true} if the header is already generated and written. */
	private boolean headerGenerated;

	/**
	 * Creates a new {@code RequestContext} with given parameters and default
	 * header values.
	 * 
	 * @param outputStream
	 *            {@code OutputStream} for writing header and data; cannot be
	 *            {@code null}
	 * @param parameters
	 *            read-only {@code Map} with parameters
	 * @param persistentParameters
	 *            {@code Map} with persistent parameters
	 * @param outputCookies
	 *            {@code List} with cookies
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this.outputStream = Objects.requireNonNull(outputStream, "Output stream must not be null!");
		this.parameters = parameters;
		this.persistentParameters = persistentParameters;
		this.outputCookies = outputCookies;

		setEncoding(DEFAULT_ENCODING);
		setStatusCode(DEFAULT_STATUS_CODE);
		setStatusText(DEFAULT_STATUS_TEXT);
		setMimeType(DEFAULT_MIME_TYPE);
	}

	/**
	 * Sets the data encoding.
	 * 
	 * @param encoding
	 *            new encoding
	 * @throws RuntimeException
	 *             if the header is already written
	 */
	public void setEncoding(String encoding) {
		checkHeader();
		this.encoding = encoding;
		charset = Charset.forName(encoding);
	}

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode
	 *            new status code
	 * @throws RuntimeException
	 *             if the header is already written
	 */
	public void setStatusCode(int statusCode) {
		checkHeader();
		this.statusCode = statusCode;
	}

	/**
	 * Sets the status text.
	 * 
	 * @param statusText
	 *            new status text
	 * @throws RuntimeException
	 *             if the header is already written
	 */
	public void setStatusText(String statusText) {
		checkHeader();
		this.statusText = statusText;
	}

	/**
	 * Sets the MIME type.
	 * 
	 * @param mimeType
	 *            new MIME type
	 * @throws RuntimeException
	 *             if the header is already written
	 */
	public void setMimeType(String mimeType) {
		checkHeader();
		this.mimeType = mimeType;
	}

	/**
	 * Sets the content length of the data that will be send.
	 * 
	 * @param contentLength
	 *            content length
	 * @throws RuntimeException
	 *             if the header is already written
	 */
	public void setContentLength(Long contentLength) {
		checkHeader();
		this.contentLength = contentLength;
	}

	/**
	 * Returns the parameter with given name.
	 * 
	 * @param name
	 *            name of the parameter
	 * @return parameter with given name or {@code null} if no such mapping
	 *         exists
	 */
	public String getParameter(String name) {
		if (parameters == null) {
			return null;
		}
		return parameters.get(name);
	}

	/**
	 * Returns the read-only {@code Set} with names of all parameters.
	 * 
	 * @return names of all parameters or {@code null} if no parameters exist
	 */
	public Set<String> getParameterNames() {
		if (parameters == null) {
			return null;
		}
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Returns the persistent parameter with given name.
	 * 
	 * @param name
	 *            name of the persistent parameter
	 * @return persistent parameter with given name or {@code null} if no such
	 *         mapping exists
	 */
	public String getPersistentParameter(String name) {
		if (persistentParameters == null) {
			return null;
		}
		return persistentParameters.get(name);
	}

	/**
	 * Returns the read-only {@code Set} with names of all persistent
	 * parameters.
	 * 
	 * @return names of all persistent parameters or {@code null} if no
	 *         persistent parameters exist
	 */
	public Set<String> getPersistentParameterNames() {
		if (persistentParameters == null) {
			return null;
		}
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Adds a new persistent parameter.
	 * 
	 * @param name
	 *            name of the persistent parameter; cannot be {@code null}
	 * @param value
	 *            value of the persistent parameter
	 */
	public void setPersistentParameter(String name, String value) {
		if (persistentParameters == null) {
			persistentParameters = new HashMap<>();
		}
		persistentParameters.put(Objects.requireNonNull(name), value);
	}

	/**
	 * Removes the persistent parameter with given name or does nothing if is
	 * doesn't exist.
	 * 
	 * @param name
	 *            name of the persistent parameter
	 */
	public void removePersistentParameter(String name) {
		if (persistentParameters == null) {
			return;
		}
		persistentParameters.remove(name);
	}

	/**
	 * Returns the temporary parameter with given name.
	 * 
	 * @param name
	 *            name of the temporary parameter
	 * @return temporary parameter with given name or {@code null} if no such
	 *         mapping exists
	 */
	public String getTemporaryParameter(String name) {
		if (temporaryParameters == null) {
			return null;
		}
		return temporaryParameters.get(name);
	}

	/**
	 * Returns the read-only {@code Set} with names of all temporary parameters.
	 * 
	 * @return names of all temporary parameters or {@code null} if no temporary
	 *         parameters exist
	 */
	public Set<String> getTemporaryParameterNames() {
		if (temporaryParameters == null) {
			return null;
		}
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Adds a new temporary parameter.
	 * 
	 * @param name
	 *            name of the temporary parameter; cannot be {@code null}
	 * @param value
	 *            value of the temporary parameter
	 */
	public void setTemporaryParameter(String name, String value) {
		if (temporaryParameters == null) {
			temporaryParameters = new HashMap<>();
		}
		temporaryParameters.put(Objects.requireNonNull(name), value);
	}

	/**
	 * Removes the temporary parameter with given name or does nothing if is
	 * doesn't exist.
	 * 
	 * @param name
	 *            name of the temporary parameter
	 */
	public void removeTemporaryParameter(String name) {
		if (temporaryParameters == null) {
			return;
		}
		temporaryParameters.remove(name);
	}

	/**
	 * Adds a new {@code RCCookie}.
	 * 
	 * @param rcCookie
	 *            cookie to add; cannot be {@code null}
	 * @throws RuntimeException
	 *             if the header is already written
	 */
	public void addRCCookie(RCCookie rcCookie) {
		checkHeader();
		if (outputCookies == null) {
			outputCookies = new ArrayList<>();
		}
		outputCookies.add(Objects.requireNonNull(rcCookie));
	}

	/**
	 * Writes the given data to the {@code OutputStream} given in the
	 * constructor. If the header wasn't already generated, than the header is
	 * written first.
	 * 
	 * @param data
	 *            {@code bytes} to write
	 * @return this {@code RequestContext} object
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (data == null) {
			throw new IllegalArgumentException("Data cannot be null.");
		}
		if (!headerGenerated) {
			writeHeader();
		}
		outputStream.write(data);
		return this;
	}

	/**
	 * Writes the given data, using the last set encoding (or the default one),
	 * to the {@code OutputStream} given in the constructor. If the header
	 * wasn't already generated, than the header is written first.
	 * 
	 * @param text
	 *            {@code String} to write
	 * @return this {@code RequestContext} object
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public RequestContext write(String text) throws IOException {
		if (text == null) {
			throw new IllegalArgumentException("Text cannot be null.");
		}
		return write(text.getBytes(charset));
	}

	/**
	 * Checks if the header is already generated and written.
	 * 
	 * @throws RuntimeException
	 *             if the header is already written
	 */
	private void checkHeader() {
		if (headerGenerated) {
			throw new RuntimeException("Header is already generated!");
		}
	}

	/**
	 * Generates and writes the header to the {@link #outputStream}.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void writeHeader() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 ").append(statusCode).append(' ').append(statusText).append("\r\n");
		sb.append("Server: DPS\r\n");
		sb.append("Content-Type: ").append(mimeType);
		if (mimeType.startsWith("text/")) {
			sb.append(";charset=").append(encoding);
		}
		sb.append("\r\n");
		if (contentLength != null) {
			sb.append("Content-Length: ").append(contentLength).append("\r\n");
		}
		if (outputCookies != null) {
			outputCookies.forEach(c -> {
				sb.append("Set-Cookie: ").append(c.name).append("=\"").append(c.value).append("\"");
				if (c.domain != null) {
					sb.append("; Domain=").append(c.domain);
				}
				if (c.path != null) {
					sb.append("; Path=").append(c.path);
				}
				if (c.maxAge != null) {
					sb.append("; Max-Age=").append(c.maxAge);
				}
				if (c.type != null) {
					sb.append("; ").append(c.type);
				}
				sb.append("\r\n");
			});
		}
		sb.append("\r\n");

		outputStream.write(sb.toString().getBytes(HEADER_CHARSET));
		headerGenerated = true;
	}

}
