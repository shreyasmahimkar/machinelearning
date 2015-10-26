import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.jfree.ui.RefineryUtilities;

public class Main {

	/* Training matrices */
	static double[][] featurematrix;
	static double[][] norm_featurematrix;
	static double[][] norm_featurematrixT;
	static double[] classvector;
	static String[] trainingdata;
	static double[][] poly_featurematrix;
	static double[][] norm_poly_featurematrix;
	static double[][] norm_poly_featurematrixT;
	/* Testing matrices */
	static double[][] test_featurematrix;
	static double[][] test_norm_featurematrix;
	static double[][] test_norm_featurematrixT;
	static double[][] test_poly_featurematrix;
	static double[][] test_norm_poly_featurematrix;
	static String[] testingdata;
	static double[] test_actualop;
	/* Extra variables */

	static int nooffeatures;
	static double[] RMSE_test;
	static double[] SSE_test;
	static double[] RMSE_train;
	static double[] SSE_train;

	static double[][] RMSE_train_poly;
	static double[][] RMSE_test_poly;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int fold10 = 10;
		
		  /*System.out.println("---------------Q2-------------"); 
		  RMSE_test = new  double[fold10]; 
		  SSE_test = new double[fold10]; 
		  RMSE_train = new  double[fold10]; 
		  SSE_train = new double[fold10];
		  
		  for (int i=0;i<fold10;i++){
			  System.out.println("-----------fold"+(i+1)+"--------");
			  RunGradientDescentAlgo(args[0],i);
			  
		  } 
		  System.out.println("RMSE mean train : "+Util.mean(RMSE_train));
		  System.out.println("SSE mean train : "+Util.mean(SSE_train));
		  System.out.println("RMSE mean test : "+Util.mean(RMSE_test));
		  System.out.println("SSE mean test : "+Util.mean(SSE_test));*/
		 

		/*
		 * System.out.println("-------------Q3--------------------"); for(int
		 * i=0;i<fold10;i++){ RunNormalEquations(args[0]); }
		 */

		
		System.out.println("-------------Q5--------------------");
		int p = 0;
		if (args[0].equalsIgnoreCase("yachtData")) {
			System.out.println("-------Yacht-------");
			p = 7;
			RMSE_train_poly = new double[fold10][p];
			RMSE_test_poly = new double[fold10][p];
			for (int i = 0; i < fold10; i++) {
				constructFeaturesforPoly_yacht(args[0], i);
			}
			for (int m = 0; m < fold10; m++) {
				System.out.println("fold" + (m + 1));
				for (int n = 0; n < p; n++) {
					System.out.print("p = " + (n + 1) + " : "
							+ RMSE_train_poly[m][n]);
					System.out.println(" " + RMSE_test_poly[m][n]);
				}
			}

		} else if (args[0].equalsIgnoreCase("sin")) {
			p=15;
			RMSE_test = new  double[p]; 
			SSE_test = new double[p]; 
			RMSE_train = new  double[p]; 
			SSE_train = new double[p];
			  
			System.out.println("-------Sin-------");
			solveForsin();
		}
	}
	
	public static void solveForsin() throws IOException{
		// Read Training sin data
		int p=15;
		String datasetName;
		System.out.println("-------SinData-------");
		datasetName = "sinData";
		nooffeatures = 1;
		int inputtrainingdata = 100;
		int validationdata = 50;
		BufferedReader br = new BufferedReader(new FileReader(datasetName+ "_Train.csv"));
		String sCurrentLine;
		String [] tokens;
		featurematrix = new double[inputtrainingdata][nooffeatures];
		classvector = new double [inputtrainingdata];
		int k =0;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			featurematrix[k][0] = Double.parseDouble(tokens[0]);
			classvector[k] = Double.parseDouble(tokens[1]);
			k++;
		}
		// read the validation set
		br = new BufferedReader(new FileReader(datasetName+ "_Validation.csv"));
		test_poly_featurematrix = new double[validationdata][nooffeatures];
		test_actualop = new double [validationdata];
		k =0;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			test_poly_featurematrix[k][0] = Double.parseDouble(tokens[0]);
			test_actualop[k] = Double.parseDouble(tokens[1]);
			k++;
		}
		// Convert the training data to polynomia features 1 - 15
		// for each poly 1 -15 -> get the thetas 
					//Calculate the RMSE
					// Calculate the SSE
		for (int i = 1; i <= p; i++) {
			poly_featurematrix = new double[featurematrix.length][i*nooffeatures];
			for (int m = 0; m < featurematrix.length; m++) {
				int pow = 0;
				for (int n = 0; n < i * nooffeatures; n++) {
					if (n % nooffeatures == 0) {
						pow += 1;
					}
					poly_featurematrix[m][n] = Math.pow(featurematrix[m][n
							% nooffeatures], pow);
				}
			}
			//Invert the matrix only NOT normalized
			norm_poly_featurematrixT = new double[i * nooffeatures][poly_featurematrix.length];

			for (int m = 0; m < poly_featurematrix.length; m++) {
				for (int n = 0; n < i * nooffeatures; n++) {
					norm_poly_featurematrixT[n][m] = poly_featurematrix[m][n];
				}
			}
			
			LinearRegression lr = new LinearRegression(norm_poly_featurematrixT, classvector);
			lr.normalEquation();
			double rmse = Math.sqrt(lr.getCost() * 2);
			double sse = lr.getCost() * 2 * lr.y.size();
			RMSE_train[i-1] = rmse;
			SSE_train[i-1] = sse;
			System.out.println("----------"+i+"-----------");
			System.out.println("RMSE: "+rmse+" SSE: "+sse);
			
			
			double[] actual_pre = new double[test_poly_featurematrix.length];
			for (int m = 0; m < test_poly_featurematrix.length; m++) {
				actual_pre[m] = lr.predict(test_poly_featurematrix[m]) - test_actualop[m];
				
			}
			sse = Util.SSE(actual_pre);
			rmse = Util.RMSE(sse,actual_pre.length);
			
			RMSE_test[i - 1] = rmse;
			SSE_test[i-1]=sse;
			System.out.println("RMSE Test: "+rmse+" SSE Test: "+sse);
			
			
		}
		
		
	}

	public static void constructFeaturesforPoly_yacht(String argument, int pos)
			throws IOException {
		// p = 15 for sinusoid and p = 7 for yacht
		int p;
		String datasetName;

		int inputtrainingdata;
		int percent90;
		if (argument.equalsIgnoreCase("yachtData")) {
			p = 7;
			datasetName = "yachtData";
			nooffeatures = 6;
			inputtrainingdata = 308;
			percent90 = 270;
			createData(percent90, inputtrainingdata, datasetName);
			normalizeTrainingdata(); // Normalize the training features - piazza
			normalizeTestingdata(); // Normalize testing data
			double rmse[] = new double[p];
			for (int i = 1; i <= p; i++) {
				/* Start training data normalization */
				// System.out.println("----------pval---------:" + i);
				poly_featurematrix = new double[featurematrix.length][i
						* nooffeatures];

				for (int m = 0; m < featurematrix.length; m++) {
					int pow = 0;
					for (int n = 0; n < i * nooffeatures; n++) {
						if (n % nooffeatures == 0) {
							pow += 1;
						}
						poly_featurematrix[m][n] = Math.pow(featurematrix[m][n
								% nooffeatures], pow);
					}
				}

				double meantraining, stdevtraining;
				double[] temp;
				norm_poly_featurematrix = new double[poly_featurematrix.length][i
						* nooffeatures];

				for (int j = 0; j < i * nooffeatures; j++) {
					temp = new double[poly_featurematrix.length];
					for (int k = 0; k < poly_featurematrix.length; k++) {
						temp[k] = poly_featurematrix[k][j];
					}
					// Calculating mean and std dev for Normalizing the feature;
					meantraining = Util.mean(temp);
					stdevtraining = Util.standardDeviation(temp);
					for (int k = 0; k < poly_featurematrix.length; k++) {
						norm_poly_featurematrix[k][j] = (temp[k] - meantraining)
								/ stdevtraining;
					}
				}

				norm_poly_featurematrixT = new double[i * nooffeatures][norm_poly_featurematrix.length];

				for (int m = 0; m < norm_poly_featurematrix.length; m++) {
					for (int n = 0; n < i * nooffeatures; n++) {
						norm_poly_featurematrixT[n][m] = norm_poly_featurematrix[m][n];
					}
				}

				/*----Training data normalization complete----*/

				/*----Testing data start normalisation----*/

				test_poly_featurematrix = new double[test_featurematrix.length][i
						* nooffeatures];
				for (int m = 0; m < test_featurematrix.length; m++) {
					int pow = 0;
					for (int n = 0; n < i * nooffeatures; n++) {
						if (n % nooffeatures == 0) {
							pow += 1;
						}
						test_poly_featurematrix[m][n] = Math.pow(
								test_featurematrix[m][n % nooffeatures], pow);
					}
				}
				test_norm_poly_featurematrix = new double[test_poly_featurematrix.length][i
						* nooffeatures];

				for (int j = 0; j < i * nooffeatures; j++) {
					temp = new double[test_poly_featurematrix.length];
					for (int k = 0; k < test_poly_featurematrix.length; k++) {
						temp[k] = test_poly_featurematrix[k][j];
					}
					// Calculating mean and std dev for Normalizing the feature;
					meantraining = Util.mean(temp);
					stdevtraining = Util.standardDeviation(temp);
					for (int k = 0; k < test_poly_featurematrix.length; k++) {
						test_norm_poly_featurematrix[k][j] = (temp[k] - meantraining)
								/ stdevtraining;
					}
				}

				/*---Testing data normalization complete----*/

				LinearRegression lr = new LinearRegression(
						norm_poly_featurematrixT, classvector);
				lr.normalEquation();
				/*
				 * for (int k=0;k<lr.theta.length;k++){
				 * System.out.println(lr.theta[k]); }
				 */
				RMSE_train_poly[pos][i - 1] = Math.sqrt(lr.getCost() * 2);
				double[] actual_pre = new double[test_norm_poly_featurematrix.length];
				for (int k = 0; k < test_norm_poly_featurematrix.length; k++) {
					actual_pre[k] = lr.predict(test_norm_poly_featurematrix[k])
							- test_actualop[k];
					/*
					 * System.out.print(lr.predict(test_norm_poly_featurematrix[k
					 * ])); System.out.println(" "+test_actualop[k]);
					 */
				}

				RMSE_test_poly[pos][i - 1] = Util.RMSE(Util.SSE(actual_pre),
						actual_pre.length);
			}

		}

	}

	public static void RunNormalEquations(String argument) throws IOException {
		String datasetName;

		int inputtrainingdata;
		int percent90;
		if (argument.equalsIgnoreCase("housing")) {
			System.out.println("-------Housing-------");
			datasetName = "housing";
			nooffeatures = 13;
			inputtrainingdata = 506;
			percent90 = 455;
			createData(percent90, inputtrainingdata, datasetName);
			normalizeTrainingdata(); // Normalize the training features - piazza
			// call gradient descent to learn the data : max iter =1000
			LinearRegression lr = new LinearRegression(norm_featurematrixT,
					classvector);
			lr.normalEquation();
			for (int i = 0; i < lr.theta.length; i++) {
				System.out.println(lr.theta[i]);
			}
		} else if (argument.equalsIgnoreCase("yachtData")) {
			System.out.println("-------Yacht-------");
			datasetName = "yachtData";
			nooffeatures = 6;
			inputtrainingdata = 308;
			percent90 = 270;
			createData(percent90, inputtrainingdata, datasetName);
			normalizeTrainingdata(); // Normalize the training features - piazza
			// call gradient descent to learn the data : max iter =1000
			LinearRegression lr = new LinearRegression(norm_featurematrixT,
					classvector);
			lr.normalEquation();
			for (int i = 0; i < lr.theta.length; i++) {
				System.out.println(lr.theta[i]);
			}
			System.out.println("RMSE :" + Math.sqrt(lr.getCost() * 2));
			System.out.println("SSE :: " + lr.getCost() * 2 * lr.y.size());
		}
	}

	public static void RunGradientDescentAlgo(String argument, int pos)
			throws IOException {
		String datasetName;

		int inputtrainingdata;
		int percent90;
		if (argument.equalsIgnoreCase("housing")) {
			System.out.println("-------Housing-------");
			datasetName = "housing";
			nooffeatures = 13;
			inputtrainingdata = 506;
			percent90 = 455;
			createData(percent90, inputtrainingdata, datasetName);
			normalizeTrainingdata(); // Normalize the training features - piazza
			// call gradient descent to learn the data : max iter =1000
			LinearRegression lr = new LinearRegression(norm_featurematrixT,
					classvector);
			lr.setLearningRate(0.005);
			lr.setIterations(1000);
			lr.gradientDescent();
			// printing theta values
			/*
			 * for (int i=0;i<lr.theta.length;i++){
			 * System.out.println(lr.theta[i]); }
			 */
			normalizeTestingdata();
			double[] J = lr.getJ();
			double rmse;
			double sse;
			rmse = Math.sqrt(J[lr.iterations - 1] * 2);
			sse = J[lr.iterations - 1] * 2 * lr.y.size();
			System.out.println("RMSE Training :: " + rmse);
			System.out.println("SSE Training :: " + sse);

			RMSE_train[pos] = rmse;
			SSE_train[pos] = sse;
			// call predict store the actual and predicted values
			double[] actual_pre = new double[test_norm_featurematrix.length];
			for (int i = 0; i < test_norm_featurematrix.length; i++) {
				// System.out.println(lr.predict(test_norm_featurematrix[i])+" "+test_actualop[i]);
				actual_pre[i] = lr.predict(test_norm_featurematrix[i])
						- test_actualop[i];
			}
			// calculate SSE, RMSE for all 10 folds = mean RMSE,SSE
			rmse = Util.RMSE(Util.SSE(actual_pre), actual_pre.length);
			sse = Util.SSE(actual_pre);

			System.out.println("RMSE Test:: " + rmse);
			System.out.println("SSE Test:: " + sse);
			RMSE_test[pos] = rmse;
			SSE_test[pos] = sse;
			// plotting
			if (pos == 1) {
				PlotGradientdescent(lr.getJ());
			}

		} else if (argument.equalsIgnoreCase("yachtData")) {
			System.out.println("-------Yacht-------");
			datasetName = "yachtData";
			nooffeatures = 6;
			inputtrainingdata = 308;
			percent90 = 270;
			createData(percent90, inputtrainingdata, datasetName);
			normalizeTrainingdata(); // Normalize the training features - piazza
			// call gradient descent to learn the data : max iter =1000
			LinearRegression lr = new LinearRegression(norm_featurematrixT,
					classvector);
			lr.setLearningRate(0.01);
			lr.gradientDescent();
			for (int i = 0; i < lr.theta.length; i++) {
				System.out.println(lr.theta[i]);
			}
			// call predict store the actual and predicted values
			normalizeTestingdata();
			double[] J = lr.getJ();
			double rmse;
			double sse;
			rmse = Math.sqrt(J[lr.iterations - 1] * 2);
			sse = J[lr.iterations - 1] * 2 * lr.y.size();
			System.out.println("RMSE Training :: " + rmse);
			System.out.println("SSE Training :: " + sse);

			RMSE_train[pos] = rmse;
			SSE_train[pos] = sse;
			// call predict store the actual and predicted values
			double[] actual_pre = new double[test_norm_featurematrix.length];
			for (int i = 0; i < test_norm_featurematrix.length; i++) {
				// System.out.println(lr.predict(test_norm_featurematrix[i])+" "+test_actualop[i]);
				actual_pre[i] = lr.predict(test_norm_featurematrix[i])
						- test_actualop[i];
			}
			// calculate SSE, RMSE for all 10 folds = mean RMSE,SSE
			rmse = Util.RMSE(Util.SSE(actual_pre), actual_pre.length);
			sse = Util.SSE(actual_pre);

			System.out.println("RMSE Test:: " + rmse);
			System.out.println("SSE Test:: " + sse);
			RMSE_test[pos] = rmse;
			SSE_test[pos] = sse;
			// plotting
			if (pos == 1) {
				PlotGradientdescent(lr.getJ());
			}

		} else if (argument.equalsIgnoreCase("concreteData")) {
			System.out.println("-------Concrete-------");
			datasetName = "concreteData";
			nooffeatures = 8;
			inputtrainingdata = 1030;
			percent90 = 900;
			createData(percent90, inputtrainingdata, datasetName);
			normalizeTrainingdata(); // Normalize the training features - piazza
			// call gradient descent to learn the data : max iter =1000
			LinearRegression lr = new LinearRegression(norm_featurematrixT,
					classvector);
			lr.setLearningRate(0.007);
			lr.gradientDescent();
			// call predict store the actual and predicted values
			normalizeTestingdata();
			double[] J = lr.getJ();
			double rmse;
			double sse;
			rmse = Math.sqrt(J[lr.iterations - 1] * 2);
			sse = J[lr.iterations - 1] * 2 * lr.y.size();
			System.out.println("RMSE Training :: " + rmse);
			System.out.println("SSE Training :: " + sse);

			RMSE_train[pos] = rmse;
			SSE_train[pos] = sse;
			// call predict store the actual and predicted values
			double[] actual_pre = new double[test_norm_featurematrix.length];
			for (int i = 0; i < test_norm_featurematrix.length; i++) {
				// System.out.println(lr.predict(test_norm_featurematrix[i])+" "+test_actualop[i]);
				actual_pre[i] = lr.predict(test_norm_featurematrix[i])
						- test_actualop[i];
			}
			// calculate SSE, RMSE for all 10 folds = mean RMSE,SSE
			rmse = Util.RMSE(Util.SSE(actual_pre), actual_pre.length);
			sse = Util.SSE(actual_pre);

			System.out.println("RMSE Test:: " + rmse);
			System.out.println("SSE Test:: " + sse);
			RMSE_test[pos] = rmse;
			SSE_test[pos] = sse;
			// plotting
			if (pos == 1) {
				PlotGradientdescent(lr.getJ());
			}
		}
	}

	public static void PlotGradientdescent(double[] J) {
		XYSeriesDemo demo = new XYSeriesDemo("Gradient Descent Progress", J,
				"RMSE");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		demo = new XYSeriesDemo("Gradient Descent Progress", J, "COST");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

	public static void normalizeTestingdata() {
		String[] tokens;
		test_featurematrix = new double[testingdata.length][nooffeatures];
		test_actualop = new double[testingdata.length];
		for (int i = 0; i < testingdata.length; i++) {
			tokens = testingdata[i].split(",");
			int j = 0;
			// Coverting String to doouble and normalizing
			for (j = 0; j < nooffeatures; j++) {
				test_featurematrix[i][j] = Double.parseDouble(tokens[j]);
			}
			// Putting the last output values in a different vector
			test_actualop[i] = Double.parseDouble(tokens[j]);
		}

		double meantraining, stdevtraining;
		double[] temptraining, temptest;
		test_norm_featurematrix = new double[test_featurematrix.length][nooffeatures];
		for (int j = 0; j < nooffeatures; j++) {
			temptraining = new double[featurematrix.length];
			for (int i = 0; i < featurematrix.length; i++) {
				temptraining[i] = featurematrix[i][j];
			}
			// Calculating mean and std dev for Normalizing the feature;
			meantraining = Util.mean(temptraining);
			stdevtraining = Util.standardDeviation(temptraining);
			temptest = new double[test_featurematrix.length];
			for (int i = 0; i < test_featurematrix.length; i++) {
				temptest[i] = test_featurematrix[i][j];
			}

			for (int i = 0; i < test_featurematrix.length; i++) {
				test_norm_featurematrix[i][j] = (temptest[i] - meantraining)
						/ stdevtraining;
			}
		}

	}

	public static void normalizeTrainingdata() throws IOException {
		String[] tokens;
		featurematrix = new double[trainingdata.length][nooffeatures];
		classvector = new double[trainingdata.length];
		for (int i = 0; i < trainingdata.length; i++) {
			tokens = trainingdata[i].split(",");
			int j = 0;
			// Coverting String to doouble and normalizing
			for (j = 0; j < nooffeatures; j++) {
				featurematrix[i][j] = Double.parseDouble(tokens[j]);
			}
			// Putting the last output values in a different vector
			classvector[i] = Double.parseDouble(tokens[j]);
		}

		double meantraining, stdevtraining;
		double[] temp;
		norm_featurematrix = new double[featurematrix.length][nooffeatures];

		for (int j = 0; j < nooffeatures; j++) {
			temp = new double[featurematrix.length];
			for (int i = 0; i < featurematrix.length; i++) {
				temp[i] = featurematrix[i][j];
			}
			// Calculating mean and std dev for Normalizing the feature;
			meantraining = Util.mean(temp);
			stdevtraining = Util.standardDeviation(temp);
			for (int i = 0; i < featurematrix.length; i++) {
				norm_featurematrix[i][j] = (temp[i] - meantraining)
						/ stdevtraining;
			}
		}

		norm_featurematrixT = new double[nooffeatures][trainingdata.length];

		for (int i = 0; i < trainingdata.length; i++) {
			for (int j = 0; j < nooffeatures; j++) {
				norm_featurematrixT[j][i] = norm_featurematrix[i][j];
			}
		}

	}

	public static void createData(int percent90, int inputtrainingdata,
			String datasetName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(datasetName
				+ ".csv"));
		String sCurrentLine;
		int traincount = 0, testcount = 0;
		int splittraintest = 0;
		trainingdata = new String[percent90];
		testingdata = new String[inputtrainingdata - percent90];
		String[] fulldata = new String[inputtrainingdata];

		// In order to create random 10 datasets
		Integer[] arr = new Integer[inputtrainingdata];
		for (int j = 0; j < arr.length; j++) {
			arr[j] = j;
		}
		Collections.shuffle(Arrays.asList(arr));
		int fulldatacount = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			fulldata[fulldatacount] = sCurrentLine;
			fulldatacount++;
		}

		for (int k = 0; k < fulldata.length; k++) {
			if (traincount < percent90) {
				trainingdata[traincount] = fulldata[arr[splittraintest]];
				traincount++;

			} else if (testcount < inputtrainingdata - percent90) {
				testingdata[testcount] = fulldata[arr[splittraintest]];
				testcount++;
			}
			splittraintest++;
		}

		writetofile(datasetName + "_train.csv", trainingdata);
		writetofile(datasetName + "_test.csv", testingdata);
	}

	public static void writetofile(String fname, String[] data)
			throws IOException {
		File file = new File(fname);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		StringBuilder st = new StringBuilder();
		/*
		 * for (int i = 0; i < nooffeatures; i++) { st.append("c,"); }
		 * 
		 * bw.write(st.toString() + "?\n");
		 */
		for (int i = 0; i < data.length; i++) {
			bw.write(data[i] + "\n");
		}
		bw.close();
	}

}
