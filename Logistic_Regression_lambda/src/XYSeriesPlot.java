import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
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
		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;
		for(Integer i : lr.keySet()){
			series.add(i, lr.get(i));
			
			if(lr.get(i)>max){
				max = lr.get(i);
			}
			if(lr.get(i)<min){
				min = lr.get(i);
			}
			
		}
		
		// Scaling
		max +=2;
		min -= 2;
		
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Gradient Descent", "Iterations", "Log loss", data, PlotOrientation.VERTICAL,
				true, true, false);
		
		chart.getXYPlot().getRangeAxis().setRange(min, max);
		
		
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(700, 570));
		setContentPane(chartPanel);
	}
	
}