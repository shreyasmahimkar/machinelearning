import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYSeriesPlot extends ApplicationFrame {

	

	public XYSeriesPlot(String title, HashMap<Integer, Double> bernoulli,
			HashMap<Integer, Double> multinomial) {
		// TODO Auto-generated constructor stub
		super(title);
		final XYSeries ber = new XYSeries("Bernoulli");
		final XYSeries mult = new XYSeries("Multinomial");
		//add data to series
		
		for (Integer i : bernoulli.keySet()){
			ber.add(i, bernoulli.get(i));
		}
		
		for (Integer i : multinomial.keySet()){
			mult.add(i, multinomial.get(i));
		}
		
		
		final XYSeriesCollection data = new XYSeriesCollection(ber);
		data.addSeries(mult);
		
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Bernoulli adn Multinomial Model Plot", "Vocabulary Size", "Accuracy", data, PlotOrientation.VERTICAL,
				true, true, false);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(700, 570));
		setContentPane(chartPanel);
	}
	
}