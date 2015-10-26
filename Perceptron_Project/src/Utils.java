import java.util.Random;


public class Utils {
	
	static int epsilon = 100;
	
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

	
	public static double positiveRandomNumberGen(){
	    
	    Random random = new Random();
	    double randmNumber = 0;
	    for (int idx = 1; idx <= 1; ++idx){
	    	randmNumber = showRandomInteger(0, epsilon, random);
	    }
		return randmNumber/100;
	}
	
	private static double showRandomInteger(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    double randomNumber =  (int)(fraction + aStart);    
	    return randomNumber;
	  }
	
	public static double calculateEuclideanDistance(double[] array1, double[] array2)
    {
        double Sum = 0.0;
        for(int i=0;i<array1.length;i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        return Math.sqrt(Sum);
    }
	

}
