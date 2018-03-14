package hr.fer.zemris.webapps.webapp2;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * Utility class for creating pie charts and images of the charts using
 * {@link JFreeChart}.
 * 
 * @author Dan
 */
public class PieChartUtil {

	/**
	 * Creates a {@code JFreeChart} based on given arguments.
	 * 
	 * @param dataset
	 *            data to show on the chart
	 * @param title
	 *            chart's title
	 * @return {@code JFreeChart} based on given {@code dataset}
	 */
	public static JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}

	/**
	 * Creates a pie chart and an {@code PNG} image from that chart using given
	 * arguments.
	 * 
	 * @param dataset
	 *            data to show on the chart
	 * @param title
	 *            chart's title
	 * @param width
	 *            width of the image
	 * @param height
	 *            height of the image
	 * @return {@code byte array} with {@code PNG} image of the pie chart
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static byte[] createPNGImage(PieDataset dataset, String title, int width, int height) throws IOException {
		JFreeChart chart = createChart(dataset, title);
		BufferedImage bim = chart.createBufferedImage(width, height);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bim, "png", bos);
		return bos.toByteArray();
	}
}
