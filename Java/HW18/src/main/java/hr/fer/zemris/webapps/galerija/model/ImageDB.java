package hr.fer.zemris.webapps.galerija.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Emulator of a database containing all available images and environment
 * information, such as folder paths.<br>
 * Everything is available in a static way and method {@link #load(Path)} should
 * be called before other methods can be used.
 *
 * @author Dan
 * @see ImageInfo
 */
public class ImageDB {

	/** Name of the file containing information about all images. */
	private static final String DESCRIPTION_FILE = "opisnik.txt";

	/** Name of the folder with full size images. */
	private static final String IMAGE_FOLDER = "slike";

	/** Name of the folder with thumbnails. */
	private static final String THUMBNAIL_FOLDER = "thumbnails";

	/**
	 * {@code Path} to the folder where other needed folders and files are
	 * expected.
	 */
	private static Path rootFolder;

	/** {@code Map} with all images with image names as key. */
	private static Map<String, ImageInfo> images;

	/**
	 * Loads all images to the memory. Each image is represented by an
	 * {@link ImageInfo} object.
	 * 
	 * @param path
	 *            {@code Path} of the root folder where image folder and
	 *            description file can be found
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void load(Path path) throws IOException {
		rootFolder = Objects.requireNonNull(path);
		images = new HashMap<>();

		try (BufferedReader reader = Files.newBufferedReader(path.resolve(DESCRIPTION_FILE), StandardCharsets.UTF_8);) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}

				ImageInfo image = new ImageInfo(line, reader.readLine().trim());
				String[] tags = reader.readLine().trim().split(",");
				Arrays.stream(tags).map(String::trim).forEach(t -> image.addTag(t));
				images.put(image.getName(), image);
			}
		}
	}

	/**
	 * Gets an {@link ImageInfo} object for image with given name.
	 * 
	 * @param name
	 *            name of the image
	 * @return {@code ImageInfo} with information about an image with given name
	 */
	public static ImageInfo getImage(String name) {
		return images.get(name);
	}

	/**
	 * Gets all available images.
	 * 
	 * @return unmodifiable {@code Collection} of all available images
	 */
	public static Collection<ImageInfo> getImages() {
		if (images == null) {
			throw new IllegalStateException("Images have to be loaded first.");
		}
		return Collections.unmodifiableCollection(images.values());
	}

	/**
	 * Gets the image folder.
	 * 
	 * @return {@code Path} to the folder with full size images
	 */
	public static Path getImageFolder() {
		return rootFolder.resolve(IMAGE_FOLDER);
	}

	/**
	 * Gets the thumbnail folder.
	 * 
	 * @return {@code Path} to the folder with thumbnails
	 */
	public static Path getThumbnailFolder() {
		return rootFolder.resolve(THUMBNAIL_FOLDER);
	}
}
