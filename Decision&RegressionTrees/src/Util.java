import java.util.Arrays;


public class Util{
	
	/**
	 * calculateVarianceForallAttrs 
	 * @param testcases
	 * @param attr
	 * @return variance per attribute
	 */
	public static double calculateVarianceForallAttrs(int [] testcases, int attr){
		
		double [] values = new double[testcases.length];
		for (int i = 0; i<testcases.length;i++){
			values[i] = Double.parseDouble(DecisionTreeMaking.datamatrix[testcases[i]][attr]);
		}
		
		return variance(values);
		
	}
	
	
    /**
     * Variance: the square of the standard deviation. A measure of the degree
     * of spread among a set of values; a measure of the tendency of individual
     * values to vary from the mean value.
     *
     * @param values
     * @return
     */
    public static double variance(double[] values) {
        double std = standardDeviation(values);
        return std * std;
    }
    /**
     * Standard deviation is a statistical measure of spread or variability.The
     * standard deviation is the root mean square (RMS) deviation of the values
     * from their arithmetic mean.
     *
     * <b>standardDeviation</b> normalizes values by (N-1), where N is the sample size.  This is the
     * sqrt of an unbiased estimator of the variance of the population from
     * which X is drawn, as long as X consists of independent, identically
     * distributed samples.
     *
     * @param values
     * @return
     */
    public static strictfp double standardDeviation(double[] values) {
        double mean = mean(values);
        double dv = 0D;
        for (double d : values) {
            double dm = d - mean;
            dv += dm * dm;
        }
        return Math.sqrt(dv / (values.length - 1));
    }

    /**
     * Calculate the mean of an array of values
     *
     * @param values The values to calculate
     * @return The mean of the values
     */
    public static strictfp double mean(double[] values) {
        return sum(values) / values.length;
    }
    /**
     * Sum up all the values in an array
     *
     * @param values an array of values
     * @return The sum of all values in the Array
     */
    public static strictfp double sum(double[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }
        else {
            double sum = 0;
            for (int i = 0; i < values.length; i++) {
                sum += values[i];
            }
            return sum;
        }
    }
}

   
    