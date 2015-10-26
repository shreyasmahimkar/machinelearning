import java.util.Random;


public class Utils {
	

	
	public static int positiveRandomNumberGen(int epsilon){
	    
	    Random random = new Random();
	    double randmNumber = 0;
	    for (int idx = 1; idx <= 1; ++idx){
	    	randmNumber = showRandomInteger(0, epsilon, random);
	    }
		return (int)randmNumber;
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
