package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This program expects one command line argument: path to the file with data
 * for a bar chart. Program reads data from given file, creates a new
 * {@link BarChartComponent} and displays it.
 * 
 * @author Dan
 *
 */
public class BarChartDemo extends JFrame {

	/**	*/
	private static final long serialVersionUID = 1L;

	/**
	 * Program entry point. Creates a new frame {@link BarChartDemo} once the
	 * data is read.
	 * 
	 * @param args
	 *            path to the file with data
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("One argument exepcted.");
			return;
		}

		BarChart model;
		Path path = Paths.get(args[0]);
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8));
			model = getModel(reader);
		} catch (IOException e) {
			System.out.println("An IOException occured: " + e.getMessage());
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid file format: " + e.getMessage());
			return;
		}

		SwingUtilities.invokeLater(() -> {
			new BarChartDemo(model, path.toAbsolutePath().toString()).setVisible(true);
		});
	}

	/**
	 * Reads from given {@link BufferedReader} and creates a new
	 * {@link BarChart} model with read data.
	 * 
	 * @param reader
	 *            for reading data
	 * @return new {@code BarChart} model
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private static BarChart getModel(BufferedReader reader) throws IOException {
		String xLabel = reader.readLine().trim();
		String yLabel = reader.readLine().trim();
		List<XYValue> values = Stream.of(reader.readLine().trim()).map(line -> line.split(" ")).flatMap(Arrays::stream)
				.map(s -> XYValue.fromString(s)).collect(Collectors.toList());
		int yMin = Integer.parseInt(reader.readLine().trim());
		int yMax = Integer.parseInt(reader.readLine().trim());
		int gap = Integer.parseInt(reader.readLine().trim());

		return new BarChart(values, xLabel, yLabel, yMin, yMax, gap);
	}

	/** Path to the file from which the data was read. */
	private String path;

	/** Model with all the data that needs to be displayed. */
	private BarChart model;

	/**
	 * Creates a new frame {@link BarChartDemo} with given arguments.
	 * 
	 * @param model
	 *            model with all the data that needs to be displayed
	 * @param path
	 *            path to the file from which the data was read
	 */
	public BarChartDemo(BarChart model, String path) {
		this.model = model;
		this.path = path;

		setLocation(100, 100);
		setSize(800, 600);
		setTitle("Bar Chart");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		BarChartComponent comp = new BarChartComponent(model);

		JLabel label = new JLabel(path);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font(null, Font.PLAIN, 15));
		label.setBackground(Color.WHITE);
		label.setOpaque(true);

		setLayout(new BorderLayout());
		add(comp, BorderLayout.CENTER);
		add(label, BorderLayout.PAGE_START);
	}
}
