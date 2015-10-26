import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYSeriesPlot extends ApplicationFrame {
	

	public XYSeriesPlot(String title, double[] rmsevsLambda, int features) {
		// TODO Auto-generated constructor stub
		super(title);
		final XYSeries series = new XYSeries("RMSE vs lambda");
		
		double lambda = 0;
		for(int k=0; k<rmsevsLambda.length; k++){
			series.add(lambda,rmsevsLambda[k]);
			lambda += 0.2;
		}
		
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Ridge Regression Error plot", "Lambda", "Features:"+Integer.toString(features), data, PlotOrientation.VERTICAL,
				true, true, false);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(700, 570));
		setContentPane(chartPanel);
	}
	
}