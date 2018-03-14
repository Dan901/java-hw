package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Implementation of simple ray-caster that uses parallelization for faster
 * calculations. <br>
 * Calculations are based on predefined {@link Scene} obtained by
 * {@link RayTracerViewer#createPredefinedScene()} method and done by an
 * instance of {@link Tracer}. <br>
 * Only difference between {@link RayCaster} is that this is a multi-threaded
 * program.
 * 
 * @author Dan
 *
 */
public class RayCasterParallel {

	/**
	 * Program entry point. Starts calculation and rendering with
	 * {@link ParallelIRayTracerProducer}.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(new ParallelIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Parallel implementation of {@link IRayTracerProducer}. Calculation for
	 * each pixel's RGB component is done by an instance of {@link Tracer}. <br>
	 * {@link ForkJoinPool} is used for parallelization and
	 * {@link CalculationJob} splits the whole task into smaller jobs.
	 * 
	 * @author Dan
	 *
	 */
	private static class ParallelIRayTracerProducer implements IRayTracerProducer {

		@Override
		public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width,
				int height, long requestNo, IRayTracerResultObserver observer) {

			System.out.println("Započinjem izračune...");
			short[] red = new short[width * height];
			short[] green = new short[width * height];
			short[] blue = new short[width * height];

			Scene scene = RayTracerViewer.createPredefinedScene();
			Tracer tracer = new Tracer(scene, eye, view, viewUp, horizontal, vertical, width, height);

			ForkJoinPool pool = new ForkJoinPool();
			pool.invoke(new CalculationJob(tracer, red, green, blue, 0, height - 1, width, height));
			pool.shutdown();

			System.out.println("Izračuni gotovi...");
			observer.acceptResult(red, green, blue, requestNo);
			System.out.println("Dojava gotova...");
		}

	}

	/**
	 * This class represents one part of the task that needs to be completed by
	 * this program. Task is split recursively on y-axis until specified
	 * threshold is reached.
	 * 
	 * @author Dan
	 *
	 */
	private static class CalculationJob extends RecursiveAction {

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * Maximum number of rows per job.
		 */
		private static final int THRESHOLD = 10;

		/**
		 * {@link Tracer} for calculations.
		 */
		private Tracer tracer;
		/**
		 * Array with red components for each pixel.
		 */
		private short[] red;
		/**
		 * Array with green components for each pixel.
		 */
		private short[] green;
		/**
		 * Array with blue components for each pixel.
		 */
		private short[] blue;
		/**
		 * Minimal y-axis value that is calculated by this job.
		 */
		private int yMin;
		/**
		 * Maximal y-axis value that is calculated by this job.
		 */
		private int yMax;
		/**
		 * Number of pixels per screen row.
		 */
		private int width;
		/**
		 * Number of pixels per screen column.
		 */
		private int height;

		/**
		 * Creates a new {@link CalculationJob} with given arguments.
		 * 
		 * @param tracer
		 *            {@link Tracer} for calculations.
		 * @param red
		 *            Array for storing red components for each pixel.
		 * @param green
		 *            Array for storing green components for each pixel.
		 * @param blue
		 *            Array for storing blue components for each pixel.
		 * @param yMin
		 *            Minimal y-axis value that is calculated by this job
		 *            (inclusive).
		 * @param yMax
		 *            Maximal y-axis value that is calculated by this job
		 *            (inclusive).
		 * @param width
		 *            Number of pixels per screen row.
		 * @param height
		 *            Number of pixels per screen column.
		 */
		public CalculationJob(Tracer tracer, short[] red, short[] green, short[] blue, int yMin, int yMax, int width,
				int height) {
			this.tracer = tracer;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.yMin = yMin;
			this.yMax = yMax;
			this.width = width;
			this.height = height;
		}

		@Override
		protected void compute() {
			if (yMax - yMin + 1 <= THRESHOLD) {
				computeDirect();
			} else {
				int half = yMin + (yMax - yMin) / 2;
				invokeAll(new CalculationJob(tracer, red, green, blue, yMin, half, width, height),
						new CalculationJob(tracer, red, green, blue, half + 1, yMax, width, height));
			}
		}

		/**
		 * Used for this {@code CalculationJob's} computation. <br>
		 * Computes pixels from {@link #yMin} to {@link #yMax}.
		 */
		private void computeDirect() {
			short[] rgb = new short[3];
			int offset = yMin * width;

			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					tracer.calculatePixelRGB(x, y, rgb);

					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					offset++;
				}
			}
		}
	}
}
