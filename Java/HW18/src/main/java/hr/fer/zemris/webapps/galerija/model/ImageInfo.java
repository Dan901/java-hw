package hr.fer.zemris.webapps.galerija.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Contains information about one image:
 * <ul>
 * <li>image name (also the file name)</li>
 * <li>description</li>
 * <li>tags associated with the image</li>
 * </ul>
 *
 * @author Dan
 */
public class ImageInfo {

	/** Name of the image. */
	private String name;

	/** Description of the image. */
	private String description;

	/** tags associated with the image. */
	private Set<String> tags;

	/**
	 * Creates a new {@code ImageInfo} with given arguments.
	 * 
	 * @param name
	 *            name of the image
	 * @param description
	 *            description of the image
	 */
	public ImageInfo(String name, String description) {
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
		tags = new TreeSet<>();
	}

	/**
	 * Getter for name.
	 * 
	 * @return name of the image
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for name.
	 * 
	 * @param name
	 *            name of the image to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for description.
	 * 
	 * @return description of the image
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description.
	 * 
	 * @param description
	 *            description of the image to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for tags.
	 * 
	 * @return unmodifiable {@code Set} of tags associated with this image
	 */
	public Set<String> getTags() {
		return Collections.unmodifiableSet(tags);
	}

	/**
	 * Adds a new tag to this image.
	 * 
	 * @param tag
	 *            tag to add
	 */
	public void addTag(String tag) {
		tags.add(Objects.requireNonNull(tag));
	}
}
