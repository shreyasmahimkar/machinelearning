import java.util.Random;


public class Utils {
	
	public static double calculateEuclideanDistance(double[] array1, double[] array2)
    {
        double Sum = 0.0;
        // -1 for the dataset also contains the output values
        for(int i=0;i<array1.length - 1;i++) {
           Sum = Sum + Math.pow(Math.abs(array1[i]-array2[i]),2.0);
        }
        
        return Math.sqrt(Sum);
    }
	
	public static int[] top_max_calc(double[] values, int num_max){
		double[] max=new double[num_max];
		for(int i=0;i<num_max;i++){
			max[i] = Double.NEGATIVE_INFINITY;
		}
		
		int[] pos=new int[num_max];
		for(int i=0;i<num_max;i++){
			pos[i] = num_max+1;
		}
		for(int k=0;k<num_max;k++){
			for(int i=0;i<values.length;i++){
				if(values[i]>max[k] && isnotInArray(i, pos)){
					max[k] = values[i];
					pos[k] = i;
				}
			}
			
		}
		
		return pos;
	}
	
	public static boolean isnotInArray(int val, int[] values){
		
		for (int v : values){
			if (v == val){
				return false;
			}
		}
		return true;
	}
	
	public static double positiveRandomNumberGen(int epsilon){
	    
	    Random random = new Random();
	    double randmNumber = 0;
	    for (int idx = 1; idx <= 1; ++idx){
	    	randmNumber = showRandomInteger(0, epsilon, random);
	    }
		return randmNumber/100;
	}
	
	public static double bothpositive_negRandomNumberGen(int epsilon){
	    
	    Random random = new Random();
	    double randmNumber = 0;
	    for (int idx = 1; idx <= 1; ++idx){
	    	randmNumber = showRandomInteger(-epsilon, epsilon, random);
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

}
