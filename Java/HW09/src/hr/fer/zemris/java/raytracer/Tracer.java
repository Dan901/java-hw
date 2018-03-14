package hr.fer.zemris.java.raytracer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * This class is used for calculation of each pixel's RGB components. <br>
 * After initialization with given {@link Scene} and required vectors, method
 * {@link #calculatePixelRGB(int, int, short[])} can be called for any pixel
 * (point) within the screen. <br>
 * Phong reflection model and Ray-casting algorithm are used.
 * 
 * @author Dan
 *
 */
public class Tracer {

	/**
	 * Ambient component that is added to each seen pixel's RGB values.
	 */
	private static final int AMBIENT_COMPONENT = 15;

	/**
	 * {@link Scene} containing {@code GraphicalObjects} and
	 * {@code LightSources}.
	 */
	private Scene scene;
	/**
	 * Point of the observer.
	 */
	private Point3D eye;
	/**
	 * Normalized vector defining y-axis, based on viewUp vector.
	 */
	private Point3D yAxis;
	/**
	 * Normalized vector defining x-axis, based on viewUp vector.
	 */
	private Point3D xAxis;
	/**
	 * Upper left corner of the screen.
	 */
	private Point3D screenCorner;
	/**
	 * Horizontal width of the observed space.
	 */
	private double horizontal;
	/**
	 * Vertical height of the observed space.
	 */
	private double vertical;
	/**
	 * Number of pixels per screen row.
	 */
	private int width;
	/**
	 * Number of pixels per screen column.
	 */
	private int height;

	/**
	 * Creates a new {@link Tracer} with given arguments.
	 * 
	 * @param scene
	 *            {@link Scene} containing {@code GraphicalObjects} and
	 *            {@code LightSources}.
	 * @param eye
	 *            Point of the observer.
	 * @param view
	 *            Position that is observed; center of the screen.
	 * @param viewUp
	 *            Specification of view-up vector which is used to determine
	 *            y-axis for the screen.
	 * @param horizontal
	 *            Horizontal width of the observed space.
	 * @param vertical
	 *            Vertical height of the observed space.
	 * @param width
	 *            Number of pixels per screen row.
	 * @param height
	 *            Number of pixels per screen column.
	 * @throws IllegalArgumentException
	 *             if the {@code view-up} vector is collinear with the
	 *             {@code eye-view} vector
	 */
	public Tracer(Scene scene, Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width,
			int height) {
		this.scene = Objects.requireNonNull(scene);
		this.eye = Objects.requireNonNull(eye);
		this.horizontal = horizontal;
		this.vertical = vertical;
		this.width = width;
		this.height = height;

		Point3D og = view.sub(eye).normalize();
		if (og.scalarProduct(viewUp) == og.norm() * viewUp.norm()) {
			throw new IllegalArgumentException("The view-up vector is collinear with O-G vector.");
		}

		Point3D viewUpNorm = viewUp.normalize();
		yAxis = viewUpNorm.sub(og.scalarMultiply(og.scalarProduct(viewUpNorm))).normalize();
		xAxis = og.vectorProduct(yAxis).normalize();
		screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2)).add(yAxis.scalarMultiply(vertical / 2));
	}

	/**
	 * Calculates RGB components of given pixel from the observed space, based
	 * on the {@link #scene}.
	 * 
	 * @param x
	 *            x coordinate of the pixel
	 * @param y
	 *            y coordinate of the pixel
	 * @param rgb
	 *            array for result storage with {@code length} at least 3
	 */
	public void calculatePixelRGB(int x, int y, short[] rgb) {
		Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(x * horizontal / (width - 1)))
				.sub(yAxis.scalarMultiply(y * vertical / (height - 1)));
		Ray ray = Ray.fromPoints(eye, screenPoint);

		for (int i = 0; i < rgb.length; i++) {
			rgb[i] = 0;
		}

		Optional<RayIntersection> optS = getClosestIntersection(scene.getObjects(), ray);
		if (!optS.isPresent()) {
			return;
		}
		RayIntersection s = optS.get();

		for (int i = 0; i < rgb.length; i++) {
			rgb[i] = AMBIENT_COMPONENT;
		}

		scene.getLights().forEach(new Consumer<LightSource>() {
			@Override
			public void accept(LightSource src) {
				Ray ray2 = new Ray(src.getPoint(), s.getPoint().sub(src.getPoint()).normalize());
				Optional<RayIntersection> optS2 = getClosestIntersection(scene.getObjects(), ray2);
				if (!optS2.isPresent()) {
					return;
				}
				RayIntersection s2 = optS2.get();

				double distance = src.getPoint().sub(s.getPoint()).norm();
				if (s2.getDistance() + 1E-3 < distance) {
					return;
				}

				Point3D l = ray2.direction.negate();
				Point3D n = s.getNormal();
				double product = Double.max(n.scalarProduct(l), 0);
				rgb[0] += (short) (src.getR() * s.getKdr() * product);
				rgb[1] += (short) (src.getG() * s.getKdg() * product);
				rgb[2] += (short) (src.getB() * s.getKdb() * product);

				Point3D v = ray.start.sub(s.getPoint());
				v.modifyNormalize();
				Point3D r = n.scalarMultiply(2 * n.scalarProduct(l)).sub(l);
				r.modifyNormalize();
				double product2 = Math.pow(Double.max(r.scalarProduct(v), 0), s.getKrn());

				rgb[0] += (short) (src.getR() * s.getKrr() * product2);
				rgb[1] += (short) (src.getG() * s.getKrg() * product2);
				rgb[2] += (short) (src.getB() * s.getKrb() * product2);

			}
		});

	}

	/**
	 * Calculates the closest intersection of given {@code ray} and any
	 * {@code GraphicalObject}.
	 * 
	 * @param objects
	 *            {@code List} of {@code GraphicalObjects} to check
	 * @param ray
	 *            ray to check
	 * @return closest {@link RayIntersection} or {@code null} if none is found
	 *         wrapped in {@link Optional}
	 */
	private Optional<RayIntersection> getClosestIntersection(List<GraphicalObject> objects, Ray ray) {
		return objects.stream().map(o -> o.findClosestRayIntersection(ray)).filter(Objects::nonNull)
				.min((r1, r2) -> Double.compare(r1.getDistance(), r2.getDistance()));
	}

}
