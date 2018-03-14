package hr.fer.zemris.webapps.galerija.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import hr.fer.zemris.webapps.galerija.model.ImageDB;
import hr.fer.zemris.webapps.galerija.model.ImageInfo;

/**
 * This class returns information from {@link ImageDB} in {@code JSON} format
 * using {@code REST}.
 *
 * @author Dan
 */
@Path("/images")
public class ImageProvider {

	/**
	 * Gets all available image tags.
	 * 
	 * @return {@code Set} of all image tags
	 */
	@Path("/tag")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getTags() {
		Set<String> tags = new TreeSet<>();
		ImageDB.getImages().forEach(i -> tags.addAll(i.getTags()));
		return tags;
	}

	/**
	 * Gets names of all images which contain given tag.
	 * 
	 * @param tag
	 *            image tag
	 * @return {@code List} of all images which contain given tag
	 */
	@Path("/tag/{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getImagesForTag(@PathParam("tag") String tag) {
		List<String> names = new ArrayList<>();
		ImageDB.getImages().parallelStream().filter(i -> i.getTags().contains(tag))
				.forEach(i -> names.add(i.getName()));
		return names;
	}

	/**
	 * Gets an {@link ImageInfo} object of an image with given name.
	 * 
	 * @param name
	 *            name of the image
	 * @return image with given name
	 */
	@Path("/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ImageInfo getImage(@PathParam("name") String name) {
		return ImageDB.getImage(name);
	}
}
