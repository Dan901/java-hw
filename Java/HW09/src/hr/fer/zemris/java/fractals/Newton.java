package hr.fer.zemris.java.fractals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * This program renders fractals derived from Newton-Raphson iteration and uses
 * {@link FractalViewer} for visualization. <br>
 * Program will read from standard input roots of the polynomial on which the
 * calculation will be based.
 * 
 * @author Dan
 *
 */
public class Newton {

	/**
	 * This class represents an immutable complex number and offers basic
	 * operations over complex numbers. <br>
	 * Instances can be obtained by calling one of the constructors or by using
	 * static constants available in this class.
	 * 
	 * @author Dan
	 *
	 */
	public static class Complex {

		/**
		 * This complex number is: <b>0 + 0i</b>
		 */
		public static final Complex ZERO = new Complex(0, 0);

		/**
		 * This complex number is: <b>1 + 0i</b>
		 */
		public static final Complex ONE = new Complex(1, 0);

		/**
		 * This complex number is: <b>-1 + 0i</b>
		 */
		public static final Complex ONE_NEG = new Complex(-1, 0);

		/**
		 * This complex number is: <b>0 + i</b>
		 */
		public static final Complex IM = new Complex(0, 1);

		/**
		 * This complex number is: <b>0 - i</b>
		 */
		public static final Complex IM_NEG = new Complex(0, -1);

		/**
		 * Real part of this complex number.
		 */
		private final double real;

		/**
		 * Imaginary part of this complex number.
		 */
		private final double img;

		/**
		 * Creates a complex number <b>0 + 0i</b>
		 */
		public Complex() {
			this(0, 0);
		}

		/**
		 * Creates a complex number with given arguments.
		 * 
		 * @param re
		 *            real part
		 * @param im
		 *            imaginary part
		 */
		public Complex(double re, double im) {
			real = re;
			img = im;
		}

		/**
		 * @return the module of this complex number
		 */
		public double module() {
			return Math.sqrt(real * real + img * img);
		}

		/**
		 * Multiplies the given complex number with this number and returns the
		 * result as a new complex number.
		 * 
		 * @param c
		 *            number to multiply with
		 * @return the result of multiplication
		 */
		public Complex multiply(Complex c) {
			double re = real * c.real - img * c.img;
			double im = real * c.img + img * c.real;

			return new Complex(re, im);
		}

		/**
		 * Divides this number with the given complex number and returns the
		 * result as a new complex number.
		 * 
		 * @param c
		 *            number to divide with
		 * @return the result of division
		 */
		public Complex divide(Complex c) {
			if (c.equals(ZERO)) {
				throw new IllegalArgumentException("Cannot divide by zero.");
			}

			double module = c.module();
			double re = real * c.real + img * c.img;
			double im = -real * c.img + img * c.real;

			return new Complex(re / (module * module), im / (module * module));
		}

		/**
		 * Adds the given complex number to this number and returns the result
		 * as a new complex number.
		 * 
		 * @param c
		 *            number to add
		 * @return the result of addition
		 */
		public Complex add(Complex c) {
			return new Complex(real + c.real, img + c.img);
		}

		/**
		 * Subtracts the given complex number from this number and returns the
		 * result as a new complex number.
		 * 
		 * @param c
		 *            number to subtract
		 * @return the result of subtraction
		 */
		public Complex sub(Complex c) {
			return new Complex(real - c.real, img - c.img);
		}

		/**
		 * Multiplies this complex number with <b>-1</b>.
		 * 
		 * @return negated complex number
		 */
		public Complex negate() {
			return new Complex(-real, -img);
		}

		/**
		 * Raises this complex number to the n-th power and returns the result
		 * as a new complex number.
		 * 
		 * @param n
		 *            power
		 * @return the result of exponentiation
		 * @throws IllegalArgumentException
		 *             if n is negative
		 */
		public Complex power(int n) {
			if (n < 0) {
				throw new IllegalArgumentException("Power cannot be negative");
			}

			Complex result = Complex.ONE;
			for (int i = 0; i < n; i++) {
				result = result.multiply(this);
			}

			return result;
		}

		/**
		 * Calculates n roots from this complex number.
		 * 
		 * @param n
		 *            root number
		 * @return all roots in a {@code List}
		 * @throws IllegalArgumentException
		 *             if n is not positive
		 */
		public List<Complex> root(int n) {
			if (n <= 0) {
				throw new IllegalArgumentException("Root must be positive.");
			}

			List<Complex> roots = new ArrayList<>(n);

			double angle = Math.atan2(img, real);
			double realRoot = Math.pow(module(), (1.0 / n));
			for (int i = 0; i < n; i++) {
				double re = realRoot * (Math.cos((angle + 2 * i * Math.PI) / n));
				double im = realRoot * (Math.sin((angle + 2 * i * Math.PI) / n));
				roots.add(new Complex(re, im));
			}

			return roots;
		}

		@Override
		public String toString() {
			if (img == 0) {
				return String.format("%.2f", real);
			} else if (real == 0) {
				return String.format("%.2fi", img);
			} else {
				return String.format("%.2f%+.2fi", real, img);
			}
		}

	}

	/**
	 * This class represents an immutable model of root-based complex
	 * polynomial.
	 * 
	 * @author Dan
	 *
	 */
	public static class ComplexRootedPolynomial {

		/**
		 * Roots of this polynomial.
		 */
		private final Complex[] roots;

		/**
		 * Creates a complex polynomial with given roots.
		 * 
		 * @param roots
		 *            roots of the polynomial; at least one root
		 */
		public ComplexRootedPolynomial(Complex... roots) {
			if (roots.length == 0) {
				throw new IllegalArgumentException("Polynomial cannot be emtpy.");
			}
			this.roots = roots;
		}

		/**
		 * Computes polynomial value at given point.
		 * 
		 * @param z
		 *            point for computation
		 * @return value of this polynomial at a given point
		 */
		public Complex apply(Complex z) {
			return toComplexPolynom().apply(z);
		}

		/**
		 * Converts this root-based representation to {@link ComplexPolynomial}.
		 * 
		 * @return coefficient-based complex polynomial
		 */
		public ComplexPolynomial toComplexPolynom() {
			ComplexPolynomial result = new ComplexPolynomial(Complex.ONE);

			for (int i = 0; i < roots.length; i++) {
				ComplexPolynomial p = new ComplexPolynomial(roots[i].negate(), Complex.ONE);
				result = result.multiply(p);
			}

			return result;
		}

		/**
		 * Finds index of closest root for given complex number z that is within
		 * the threshold.
		 * 
		 * @param z
		 *            complex number
		 * @param threshold
		 *            threshold
		 * @return index of closest root for given complex number or -1 if there
		 *         is no such root within the {@code threshold}
		 */
		public int indexOfClosestRootFor(Complex z, double threshold) {
			List<Double> distances = Arrays.stream(roots).map(r -> r.sub(z).module()).collect(Collectors.toList());
			double min = distances.stream().mapToDouble(d -> d).min().getAsDouble();

			if (min <= threshold) {
				return distances.indexOf(min);
			} else {
				return -1;
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < roots.length; i++) {
				sb.append("(z - (" + roots[i] + "))");
			}

			return sb.toString();
		}

	}

	/**
	 * This class represents an immutable model of coefficient-based complex
	 * polynomial.
	 * 
	 * @author Dan
	 *
	 */
	public static class ComplexPolynomial {

		/**
		 * Coefficients of this polynomial.
		 */
		private final Complex[] factors;

		/**
		 * Creates a complex polynomial with given coefficients.
		 * 
		 * @param factors
		 *            coefficients; at least one coefficient
		 */
		public ComplexPolynomial(Complex... factors) {
			if (factors.length == 0) {
				throw new IllegalArgumentException("Polynomial cannot be emtpy.");
			}
			this.factors = factors;
		}

		/**
		 * @return order of this polynomial
		 */
		public short order() {
			return (short) (factors.length - 1);
		}

		/**
		 * Multiplies the given polynomial with this polynomial and returns the
		 * result as a new polynomial.
		 * 
		 * @param p
		 *            polynomial to multiply with
		 * @return the result of multiplication
		 */
		public ComplexPolynomial multiply(ComplexPolynomial p) {
			short order1 = order();
			short order2 = p.order();
			Complex[] factors = new Complex[order1 + order2 + 1];

			for (int i = 0; i < factors.length; i++) {
				factors[i] = Complex.ZERO;
			}

			for (int i = 0; i <= order1; i++) {
				for (int j = 0; j <= order2; j++) {
					factors[i + j] = factors[i + j].add(this.factors[i].multiply(p.factors[j]));
				}
			}

			return new ComplexPolynomial(factors);
		}

		/**
		 * Computes the first derivative of this polynomial and returns the
		 * result as a new polynomial.
		 * 
		 * @return first derivative of this polynomial
		 */
		public ComplexPolynomial derive() {
			Complex[] factors;

			if (order() == 0) {
				factors = new Complex[1];
				factors[0] = Complex.ZERO;
			} else {
				factors = new Complex[order()];
				for (int i = 0; i < factors.length; i++) {
					factors[i] = this.factors[i + 1].multiply(new Complex(i + 1, 0));
				}
			}

			return new ComplexPolynomial(factors);
		}

		/**
		 * Computes polynomial value at given point.
		 * 
		 * @param z
		 *            point for computation
		 * @return value of this polynomial at a given point
		 */
		public Complex apply(Complex z) {
			Complex value = Complex.ZERO;

			for (int i = 0; i < factors.length; i++) {
				value = value.add(z.power(i).multiply(factors[i]));
			}

			return value;
		}

		@Override
		public String toString() {
			StringJoiner sj = new StringJoiner(" + ");

			for (int i = factors.length - 1; i > 0; i--) {
				sj.add("(" + factors[i] + ")*z^" + i);
			}
			sj.add("(" + factors[0] + ")");

			return sj.toString();
		}
	}

	/**
	 * Convergence threshold.
	 */
	private static final double CONVERGENCE_THRESHOLD = 1E-3;

	/**
	 * Root threshold.
	 */
	private static final double ROOT_THRESHOLD = 2E-3;

	/**
	 * Polynomial entered by the user.
	 */
	private static ComplexRootedPolynomial polynomial;

	/**
	 * Derived polynomial.
	 */
	private static ComplexPolynomial derived;

	/**
	 * Implementation of {@link IFractalProducer} that renders fractals based on
	 * Newton-Raphson iteration. <br>
	 * Rendering task is split into {@code 8*numberOfAvailableProcessors} and
	 * divided between threads. Daemon threads are used.
	 * 
	 * @author Dan
	 *
	 */
	public static class IFractalProducerImpl implements IFractalProducer {

		/**
		 * {@link ThreadFactory} which produces daemon threads.
		 * 
		 * @author Dan
		 *
		 */
		private static class DaemonicThreadFactory implements ThreadFactory {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		}

		/**
		 * Thread pool.
		 */
		private ExecutorService pool;

		/**
		 * Creates a new {@link IFractalProducerImpl} for fractal rendering.
		 */
		public IFractalProducerImpl() {
			pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
					new DaemonicThreadFactory());
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer) {
			int m = 16 * 16 * 16;
			short[] data = new short[width * height];
			final int numberOfJobs = 8 * Runtime.getRuntime().availableProcessors();
			int rangeOfYPerJob = height / numberOfJobs;

			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < numberOfJobs; i++) {
				int yMin = i * rangeOfYPerJob;
				int yMax = (i + 1) * rangeOfYPerJob - 1;
				if (i == numberOfJobs - 1) {
					yMax = height - 1;
				}

				CalculationJob job = new CalculationJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data);
				results.add(pool.submit(job));
			}

			for (Future<Void> job : results) {
				while (true) {
					try {
						job.get();
						break;
					} catch (InterruptedException ignorable) {
					} catch (ExecutionException e) {
						throw new RuntimeException("Exception during fractal calculation.");
					}
				}

			}

			observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
		}

	}

	/**
	 * This class calculates a part of the fractal image.
	 * 
	 * @author Dan
	 *
	 */
	public static class CalculationJob implements Callable<Void> {

		/**
		 * Minimum value of the real part.
		 */
		private double reMin;
		/**
		 * Maximum value of the real part.
		 */
		private double reMax;
		/**
		 * Minimum value of the imaginary part.
		 */
		private double imMin;
		/**
		 * Maximum value of the imaginary part.
		 */
		private double imMax;
		/**
		 * Raster width used for image visualization.
		 */
		private int width;
		/**
		 * Raster height used for image visualization.
		 */
		private int height;
		/**
		 * Minimal y-axis value that is calculated by this job.
		 */
		private int yMin;
		/**
		 * Maximal y-axis value that is calculated by this job.
		 */
		private int yMax;
		/**
		 * Maximum number of iterations.
		 */
		private int m;
		/**
		 * Array with calculated results that need to be visualized.
		 */
		private short[] data;

		/**
		 * Creates a new {@link CalculationJob} with given arguments. <br>
		 * Only a part of the image from {@code yMin} to {@code yMax} is
		 * calculated by this job, meaning only a part od the {@code data} array
		 * is written on.
		 * 
		 * @param reMin
		 *            Minimum value of the real part.
		 * @param reMax
		 *            Maximum value of the real part.
		 * @param imMin
		 *            Minimum value of the imaginary part.
		 * @param imMax
		 *            Maximum value of the imaginary part.
		 * @param width
		 *            Raster width used for image visualization.
		 * @param height
		 *            Raster height used for image visualization.
		 * @param yMin
		 *            Minimal y-axis value that is calculated by this job.
		 * @param yMax
		 *            Maximal y-axis value that is calculated by this job.
		 * @param m
		 *            Maximum number of iterations.
		 * @param data
		 *            Array with calculated results that need to be visualized.
		 */
		public CalculationJob(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin,
				int yMax, int m, short[] data) {
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
		}

		@Override
		public Void call() throws Exception {
			int offset = yMin * width;

			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
					double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
					Complex c = new Complex(cre, cim);
					Complex zn = c;
					Complex zn1;
					double module;
					int iters = 0;

					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex fraction = numerator.divide(denominator);
						zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						zn = zn1;
						iters++;
					} while (iters < m && module > CONVERGENCE_THRESHOLD);

					int index = polynomial.indexOfClosestRootFor(zn1, ROOT_THRESHOLD);
					data[offset++] = (short) (index + 1);
				}
			}

			return null;
		}

	}

	/**
	 * Program entry point. <br>
	 * Reads user input from standard input and starts fractal rendering.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		List<Complex> roots = new ArrayList<>();
		int i = 1;
		while (true) {
			System.out.print("Root " + i + "> ");
			String line;
			try {
				line = reader.readLine().trim();
			} catch (IOException e) {
				System.err.println("An exception occured while reading from stdin: " + e.getMessage() + "\nExiting.");
				return;
			}

			if (line.equals("done")) {
				break;
			}

			try {
				roots.add(parse(line));
			} catch (NumberFormatException e) {
				System.out.println("Invalid complex number format. Try again.");
				continue;
			}
			i++;
		}
		System.out.println("Image of fractal will appear shortly. Thank you.");

		polynomial = new ComplexRootedPolynomial(roots.toArray(new Complex[roots.size()]));
		derived = polynomial.toComplexPolynom().derive();

		try {
			FractalViewer.show(new IFractalProducerImpl());
		} catch (RuntimeException e) {
			System.err.println("An exception occured: " + e.getMessage() + "\nExiting.");
		}
	}

	/**
	 * Parses the given string into a complex number from the form <b>a+ib</b>
	 * or <b>a-ib</b>.
	 * 
	 * @param s
	 *            {@code String} to be parsed into a complex number
	 * @return {@link Complex} parsed from the given {@code String}
	 * @throws NumberFormatException
	 *             if the given {@code String} cannot be parsed
	 */
	public static Complex parse(String s) {
		s = s.replaceAll("\\s", "");
		Pattern pattern = Pattern.compile("([+-]?i?\\d*(\\.\\d+)?)([+-]i\\d*(\\.\\d+)?)?");
		Matcher matcher = pattern.matcher(s);

		if (matcher.matches()) {
			String first = matcher.group(1);
			String second = matcher.group(3);

			double img;
			double real;
			if (second == null) {
				if (first.contains("i")) {
					img = parseImaginary(first);
					real = 0;
				} else {
					real = Double.parseDouble(first);
					img = 0;
				}
			} else {
				real = Double.parseDouble(first);
				img = parseImaginary(second);
			}

			return new Complex(real, img);
		} else {
			throw new NumberFormatException();
		}
	}

	/**
	 * Parses imaginary part as a double.
	 * 
	 * @param s
	 *            {@code String} to be parsed
	 * @return {@code double} value of the given imaginary part
	 * @throws NumberFormatException
	 *             if the given {@code String} cannot be parsed
	 */
	private static double parseImaginary(String s) {
		if (!s.contains("i")) {
			throw new NumberFormatException();
		}

		s = s.replaceFirst("i", "");
		if (s.isEmpty() || s.equals("-") || s.equals("+")) {
			s += "1";
		}
		return Double.parseDouble(s);
	}

}
