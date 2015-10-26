import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYSeriesPlot extends ApplicationFrame {

	/**
	 * A demonstration application showing an XY series containing a null value.
	 *
	 * @param title
	 *            the frame title.
	 */
	public XYSeriesPlot(final String title, HashMap<Integer, Double> lr) {

		super(title);
		final XYSeries series = new XYSeries("Progress");
		for(Integer i : lr.keySet()){
			series.add(i, lr.get(i));
		}
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Gradient Descent", "Iterations", "Log loss", data, PlotOrientation.VERTICAL,
				true, true, false);
		

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(700, 570));
		setContentPane(chartPanel);
	}
	
}