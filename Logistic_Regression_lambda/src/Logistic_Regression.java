import java.util.HashMap;
import java.util.List;

import org.jfree.ui.RefineryUtilities;


public class Logistic_Regression {

    private double learning_rate;
    
    public static HashMap<Integer, Double> plotIters = new HashMap<Integer, Double>();

    private double[] weights;

    private int ITERATIONS;

    public void setIter(int iterations){
    	this.ITERATIONS = iterations;
    }
    
    public void setRate(double rate){
    	this.learning_rate = rate;
    }
    
    public Logistic_Regression(int n) {
        weights = new double[n];
    }

    private double sigmoidFunc(double z) {
        return 1 / (1 + Math.exp(-z));
    }

    public void train(List<Record> instances, int fold, double lambda) {
        for (int n=0; n<ITERATIONS; n++) {
            double logloss = 0.0;
            for (int i=0; i<instances.size(); i++) {
                double[] x = instances.get(i).getX();
                double predicted = classify(x);
                int label = instances.get(i).getLabel();
                weights[0] = weights[0] - learning_rate * (predicted - label ) * x[0];
                for (int j=1; j<weights.length; j++) {
                    weights[j] = weights[j] - learning_rate * ((predicted - label ) * x[j] - lambda * weights[j]);
                }
                logloss += Math.log(1 + Math.exp((-1)*predicted*label));
            }
            if(fold == 1){
            	plotIters.put(n+1, logloss);
            }
            //System.out.println(logloss);
        }
        if(fold == 1){
        	XYSeriesPlot demo = new XYSeriesPlot("D", plotIters);
            demo.pack();
     		RefineryUtilities.centerFrameOnScreen(demo);
     		demo.setVisible(true);
        }
       
    }

    public double classify(double[] x) {
        double logit = .0;
        for (int i=0; i<weights.length;i++)  {
            logit += weights[i] * x[i];
        }
        return sigmoidFunc(logit);
    }

}