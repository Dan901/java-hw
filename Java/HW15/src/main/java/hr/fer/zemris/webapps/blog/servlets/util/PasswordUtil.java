package hr.fer.zemris.webapps.blog.servlets.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

/**
 * Utility class for dealing with passwords. <br>
 *
 * @author Dan
 */
public class PasswordUtil {

	/**
	 * Calculates a {@code SHA-1} hash of the given password.
	 * 
	 * @param password
	 *            {@code String} containing plain password
	 * @return {@code SHA-1} hash of the given password
	 */
	public static String calculateHash(String password) {
		MessageDigest sha1;
		try {
			sha1 = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		byte[] digest = sha1.digest(password.getBytes(StandardCharsets.UTF_8));
		return DatatypeConverter.printHexBinary(digest);
	}
}
