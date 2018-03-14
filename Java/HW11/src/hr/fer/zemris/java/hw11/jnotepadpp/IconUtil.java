package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Utility class for getting {@link Icon} objects from files in source
 * directory.
 * 
 * @author Dan
 *
 */
public class IconUtil {

	/**
	 * Gets an {@link ImageIcon} from an image file in directory: "icons".
	 * 
	 * @param name
	 *            name of the icon with file extension
	 * @return an {@code ImageIcon}
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalArgumentException
	 *             if file with given name doesn'e exist
	 */
	public static Icon getIcon(String name) throws IOException {
		InputStream is = IconUtil.class.getResourceAsStream("icons/" + name);
		if (is == null) {
			throw new IllegalArgumentException("Icon with given name doesn't exist");
		}

		byte[] data = readAllBytes(is);
		is.close();
		return new ImageIcon(data);
	}

	/**
	 * Converts given {@link InputStream} to a {@code byte} array.
	 * 
	 * @param is
	 *            stream to read
	 * @return new {@code byte} array containing data from given stream
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private static byte[] readAllBytes(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int n;

		while ((n = is.read(data)) != -1) {
			buffer.write(data, 0, n);
		}

		return buffer.toByteArray();
	}
}
