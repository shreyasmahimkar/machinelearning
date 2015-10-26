import java.util.ArrayList;
import java.util.List;

public class Perceptron {
	double[] weight, input;
	double loc_err, glo_err;
	int i, iter, output;
	static int theta = 0;
	double[] alpha;
	double R;
	static double gamma = 0.02;
	static double b=0;

	
	public void trainRBFKernalPerceptron(int m, int features, double learning_rate,
			int iterations){
		
		alpha = new double[m]; // the instance misclassified number of time
		R = calculatemaxR(m);
		
		iter = 0;
		b = 0;
		do {
			iter++;
			glo_err = 0;
			String[] x;
			
			for (int instance = 0; instance < m; instance++) {
				x = DataSplitter.trainingdata[instance].split(",");
				input = new double[x.length];
				for (int j = 0; j < x.length; j++) {
					input[j] = Double.parseDouble(x[j]);
				}
				
				output = predict_RBF_Output(theta, alpha, input);
				double yi = input[features - 1];
				loc_err = yi-output;
				
				if(loc_err!=0){
					//System.out.println(loc_err + ":: "+iter);
					alpha[instance]+=1;
					b+=yi*R;
				}
				glo_err+=(loc_err*loc_err);
			}
		} while (glo_err != 0 && iter < iterations);

		
	}
	
	public static int predict_RBF_Output(int theta, double[] alpha,
			double[] input) {

		double sum = 0;
		
		for (int i = 0; i < alpha.length - 1; i++) {
			String[] x = DataSplitter.trainingdata[i].split(",");
			double yi = Double.parseDouble(x[x.length-1]);
			sum += alpha[i] *yi* calculateRBF(x, input);
		}
		sum += b;

		return (sum > theta) ? 1 : -1;
	}
	
	public static double calculateRBF(String[] inputinner,double[] input){
		double RBFout = 0;
		double[] input2 = new double[inputinner.length];
		for (int i=0;i<inputinner.length;i++){
			input2[i]=Double.parseDouble(inputinner[i]);
		}
		
		RBFout = Utils.calculateEuclideanDistance(input2, input);
		RBFout = -gamma*RBFout*RBFout;
		
		return Math.exp(RBFout);
	}
	
	public void test_RBF_Perceptron(int testdata, int features,int fold) {
		String[] x;
		double count = 0;
		for (int instance = 0; instance < testdata; instance++) {
			x = DataSplitter.testingdata[instance].split(",");
			double yi = Double.parseDouble(x[features - 1]);
			input = new double[x.length];
			for (int j = 0; j < x.length; j++) {
				input[j] = Double.parseDouble(x[j]);
			}
			double yi_pred = predict_RBF_Output(theta, alpha, input);

			if (yi_pred == yi) {
				count++;
			}

		}
		//System.out.println(count / testdata*100);
		Main_Perceptron.rbfmean[fold] = count / testdata*100;

	}


	
	
	
	
	// m training examples, features
	public void trainPerceptron(int m, int features, double learning_rate,
			int iterations) throws InterruptedException {

		weight = new double[features]; // also has bias
		for (int i = 0; i < weight.length; i++) {
			weight[i] = Utils.positiveRandomNumberGen();
		}
		// for (int iter = 0; iter < iterations; iter++) { // Convergence check
		// Repeat this until convergence!

		iter = 0;
		do {
			iter++;
			glo_err = 0;
			String[] x;

			for (int instance = 0; instance < m; instance++) {
				x = DataSplitter.trainingdata[instance].split(",");
				input = new double[x.length];
				for (int j = 0; j < x.length; j++) {
					input[j] = Double.parseDouble(x[j]);
				}
				output = predictOutput(theta, weight, input);
				double yi = Double.parseDouble(x[features - 1]);
				loc_err = yi - output;

				// Now we update the weights and the bias
				for (int j = 0; j < weight.length - 1; j++) {
					weight[j] += learning_rate * loc_err * input[j];
				}
				// update bias
				weight[weight.length - 1] += learning_rate * loc_err;
				glo_err += (loc_err * loc_err);
			}
		} while (glo_err != 0 && iter < iterations);
		// }

	}
	
	public void testPerceptron(int testdata, int features, int fold) {
		String[] x;
		double count = 0;
		for (int instance = 0; instance < testdata; instance++) {
			x = DataSplitter.testingdata[instance].split(",");
			double yi = Double.parseDouble(x[features - 1]);
			input = new double[x.length];
			for (int j = 0; j < x.length; j++) {
				input[j] = Double.parseDouble(x[j]);
			}
			double yi_pred = predictOutput(theta, weight, input);

			if (yi_pred == yi) {
				count++;
			}

		}
		//System.out.println(count / testdata*100);
		Main_Perceptron.per[fold] = count / testdata*100;

	}

	public static int predictOutput(int theta, double[] weights, double[] input) {

		double sum = 0;
		for (int i = 0; i < weights.length - 1; i++) {
			sum += weights[i] * input[i];
		}
		sum += weights[weights.length - 1];

		return (sum > theta) ? 1 : -1;
	}

		

	
	
	public void train_dual_perceptron(int m, int features,
			double learning_rate, int iterations){
		alpha = new double[m]; // the instance misclassified number of time
		R = calculatemaxR(m);
		
		iter = 0;
		b = 0;
		do {
			iter++;
			glo_err = 0;
			String[] x;
			
			for (int instance = 0; instance < m; instance++) {
				x = DataSplitter.trainingdata[instance].split(",");
				input = new double[x.length];
				for (int j = 0; j < x.length; j++) {
					input[j] = Double.parseDouble(x[j]);
				}
				
				output = predict_dual_Output(theta, alpha, input);
				double yi = input[features - 1];
				loc_err = yi-output;
				
				if(loc_err!=0){
					//System.out.println(loc_err + ":: "+iter);
					alpha[instance]+=1;
					b+=yi*R;
				}
				glo_err+=(loc_err*loc_err);
			}
		} while (glo_err != 0 && iter < iterations);
		
		
	}
	
	

	public static double calculatemaxR(int m /* number of training examples */) {
		List<Double> norms = new ArrayList<Double>();
		String[] x;

		for (int instance = 0; instance < m; instance++) {
			x = DataSplitter.trainingdata[instance].split(",");
			double sum_sq = 0;
			for (String val : x) {
				double x_val = Double.parseDouble(val);
				sum_sq += x_val * x_val;

			}
			norms.add(Math.sqrt(sum_sq));
		}

		double max = Double.NEGATIVE_INFINITY;
		for (Double d : norms) {
			if (d > max) {
				max = d;
			}
		}

		return max;

	}
	
	
	
	public void test_dual_Perceptron(int testdata, int features, int fold) {
		String[] x;
		double count = 0;
		for (int instance = 0; instance < testdata; instance++) {
			x = DataSplitter.testingdata[instance].split(",");
			double yi = Double.parseDouble(x[features - 1]);
			input = new double[x.length];
			for (int j = 0; j < x.length; j++) {
				input[j] = Double.parseDouble(x[j]);
			}
			double yi_pred = predict_dual_Output(theta, alpha, input);

			if (yi_pred == yi) {
				count++;
			}

		}
		//System.out.println(count / testdata*100);
		Main_Perceptron.dualper[fold] = count / testdata*100;

	}
	
	public static double calculateDotProduct(String[] inputinner,double[] input){
		double dotpro = 0;
		for (int j = 0; j < input.length - 1; j++) {
			dotpro += Double.parseDouble(inputinner[j])*input[j];
		}
		
		return dotpro;
	}


	public static int predict_dual_Output(int theta, double[] alpha,
			double[] input) {

		double sum = 0;
		
		for (int i = 0; i < alpha.length - 1; i++) {
			String[] x = DataSplitter.trainingdata[i].split(",");
			double yi = Double.parseDouble(x[x.length-1]);
			sum += alpha[i] *yi* calculateDotProduct(x, input);
		}
		sum += b;

		return (sum > theta) ? 1 : -1;
	}

}
