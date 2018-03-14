package hr.fer.zemris.java.raytracer.model;

/**
 * This {@link GraphicalObject} represents a sphere. <br>
 * It is defined by a {@link Point3D} {@code center}, a {@code radius} and
 * coefficients for calculating diffuse and reflective components of light
 * intensity produced by a {@link LightSource} as seen from a viewer
 * perspective.
 * 
 * @author Dan
 *
 */
public class Sphere extends GraphicalObject {

	/**
	 * Center of this sphere.
	 */
	private Point3D center;
	/**
	 * Radius of this sphere.
	 */
	private double radius;
	/**
	 * Coefficient for diffuse component for red color.
	 */
	private double kdr;
	/**
	 * Coefficient for diffuse component for green color.
	 */
	private double kdg;
	/**
	 * Coefficient for diffuse component for blue color.
	 */
	private double kdb;
	/**
	 * Coefficient for reflective component for red color.
	 */
	private double krr;
	/**
	 * Coefficient for reflective component for green color.
	 */
	private double krg;
	/**
	 * Coefficient for reflective component for blue color.
	 */
	private double krb;
	/**
	 * Coefficient {@code n} for reflective component.
	 */
	private double krn;

	/**
	 * Creates a new {@link Sphere} with given arguments.
	 * 
	 * @param center
	 *            Center of this sphere.
	 * @param radius
	 *            Radius of this sphere; has to be positive.
	 * @param kdr
	 *            Coefficient for diffuse component for red color. Legal values
	 *            are [0.0,1.0].
	 * @param kdg
	 *            Coefficient for diffuse component for green color. Legal
	 *            values are [0.0,1.0].
	 * @param kdb
	 *            Coefficient for diffuse component for blue color. Legal values
	 *            are [0.0,1.0].
	 * @param krr
	 *            Coefficient for reflective component for red color. Legal
	 *            values are [0.0,1.0].
	 * @param krg
	 *            Coefficient for reflective component for green color. Legal
	 *            values are [0.0,1.0].
	 * @param krb
	 *            Coefficient for reflective component for blue color. Legal
	 *            values are [0.0,1.0].
	 * @param krn
	 *            Coefficient {@code n} for reflective component.
	 * @throws IllegalArgumentException
	 *             if any given value is illegal
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;

		if (radius <= 0) {
			throw new IllegalArgumentException("Radius has to be positive. Given value: " + radius);
		}
		this.radius = radius;

		this.kdr = checkCoefficient(kdr);
		this.kdg = checkCoefficient(kdg);
		this.kdb = checkCoefficient(kdb);
		this.krr = checkCoefficient(krr);
		this.krg = checkCoefficient(krg);
		this.krb = checkCoefficient(krb);
		this.krn = krn;
	}

	/**
	 * Checks if {@code arg} is in interval [0.0, 1.0].
	 * 
	 * @param arg
	 *            value to check
	 * @return given {@code arg}
	 * @throws IllegalArgumentException
	 *             if {@code arg} is not in specified interval
	 */
	private double checkCoefficient(Double arg) {
		if (arg < 0 || arg > 1) {
			throw new IllegalArgumentException("Coefficient has to be in interval [0,1]. Given value: " + arg);
		}
		return arg;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D distance = ray.start.sub(center);
		double scalar = ray.direction.scalarProduct(distance);
		double value = Math.pow(scalar, 2) - Math.pow(distance.norm(), 2) + radius * radius;
		if (value < 0) {
			return null;
		}

		double lambda1 = -scalar + Math.sqrt(value);
		double lambda2 = -scalar - Math.sqrt(value);
		double lambda;
		if (lambda1 < 0) {
			lambda = lambda2;
		} else if (lambda2 < 0) {
			lambda = lambda1;
		} else {
			lambda = Double.min(lambda1, lambda2);
		}

		if (lambda < 0) {
			return null;
		}

		Point3D point = ray.start.add(ray.direction.scalarMultiply(lambda));
		return new RayIntersectionImpl(point, ray.start.sub(point).norm(), true);
	}

	/**
	 * Implementation of {@link RayIntersection} that represents an intersection
	 * with a {@link Sphere}.
	 * 
	 * @author Dan
	 *
	 */
	private class RayIntersectionImpl extends RayIntersection {

		/**
		 * Creates a new {@link RayIntersectionImpl} with given arguments.
		 * 
		 * @param point
		 *            point of intersection between ray and a {@link Sphere}
		 * @param distance
		 *            distance between start of ray and intersection
		 * @param outer
		 *            specifies if intersection is outer
		 */
		protected RayIntersectionImpl(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		@Override
		public Point3D getNormal() {
			return getPoint().sub(center).normalize();
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}
	}

}
