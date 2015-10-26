import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYSeriesDemo extends ApplicationFrame {

	/**
	 * A demonstration application showing an XY series containing a null value.
	 *
	 * @param title
	 *            the frame title.
	 */
	public XYSeriesDemo(final String title, double[] J,String cost_rmse) {

		super(title);
		final XYSeries series = new XYSeries("Random Data");
		
		
		if (cost_rmse.equalsIgnoreCase("RMSE")){
			for(int i=0;i<J.length;i++){
				series.add(i,Math.sqrt(J[i]*2));
			}
		}else if(cost_rmse.equalsIgnoreCase("COST")){
			for(int i=0;i<J.length;i++){
				series.add(i,J[i]);
			}
		}
		
		
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Gradient Descent", "Iterations", cost_rmse, data, PlotOrientation.VERTICAL,
				true, true, false);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(700, 570));
		setContentPane(chartPanel);
	}
}