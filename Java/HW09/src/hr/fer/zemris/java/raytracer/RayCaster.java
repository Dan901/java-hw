package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Implementation of simple ray-caster. <br>
 * Calculations are based on predefined {@link Scene} obtained by
 * {@link RayTracerViewer#createPredefinedScene()} method and done by an
 * instance of {@link Tracer}.
 * 
 * @author Dan
 *
 */
public class RayCaster {

	/**
	 * Program entry point. Starts calculation and rendering with
	 * {@link SequentialIRayTracerProducer}.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(new SequentialIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Sequential implementation of {@link IRayTracerProducer}. Calculation for
	 * each pixel's RGB component is done by an instance of {@link Tracer}.
	 * 
	 * @author Dan
	 *
	 */
	private static class SequentialIRayTracerProducer implements IRayTracerProducer {

		@Override
		public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width,
				int height, long requestNo, IRayTracerResultObserver observer) {

			System.out.println("Započinjem izračune...");
			short[] red = new short[width * height];
			short[] green = new short[width * height];
			short[] blue = new short[width * height];

			Scene scene = RayTracerViewer.createPredefinedScene();
			Tracer tracer = new Tracer(scene, eye, view, viewUp, horizontal, vertical, width, height);

			short[] rgb = new short[3];
			int offset = 0;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					tracer.calculatePixelRGB(x, y, rgb);

					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					offset++;
				}
			}

			System.out.println("Izračuni gotovi...");
			observer.acceptResult(red, green, blue, requestNo);
			System.out.println("Dojava gotova...");
		}

	}
}
