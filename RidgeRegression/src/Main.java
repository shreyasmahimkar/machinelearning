import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.jfree.ui.RefineryUtilities;

public class Main {
	static int nooffeatures;
	static String[] trainingdata;
	static String[] testingdata;
	static double[][] featurematrix;
	static double[] classvector;
	
	static double[][] test_featurematrix;
	static double[][] test_poly_featurematrix;
	static double [] test_actualop;
	
	static double[][] poly_featurematrix;
	static double[][] norm_poly_featurematrixT;
	
	static double[] rmse_10fold_train;
	static double[] rmse_10fold_test;
	static double[] rmse_lambda_train;
	static double[] rmse_lambda_test;
	
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		if(args[0].equalsIgnoreCase("4")){
			ridgeregression(4);
			PlotRMSEvsLambda("Train" ,4, rmse_lambda_train);
			PlotRMSEvsLambda("Test" ,4, rmse_lambda_test);
		}else if(args[0].equalsIgnoreCase("9")){
			ridgeregression(9);
			PlotRMSEvsLambda("Train" ,9, rmse_lambda_train);
			PlotRMSEvsLambda("Test" ,9, rmse_lambda_test);
		}
		
		// plot mean RMSE train and test vs lambda on different plots
		

	}
	
	public static void ridgeregression(int features) throws IOException{
		int fold10 = 10;
		rmse_10fold_train = new double[fold10];
		rmse_10fold_test = new double[fold10];
		
		double maxlambda = 10;
		int count = 0;
		for (double lam=0; lam<=maxlambda ; lam=lam+0.2){
			count++;
		}
		rmse_lambda_train = new double[count];
		rmse_lambda_test = new double[count];
		count =0;
		// for lambda 0 - 10 +0.2 plot
		for (double lam=0; lam<=maxlambda ; lam=lam+0.2){
			// for 10 fold cross validation calculate mean RMSE train and test
			for(int k=0;k<fold10;k++){
				// Split data into training and testing csv
				splitData();
				// add 4 new features to Sinusoid data
				putdataintomatrices();
				addfeatures(features);
				multipleLambdas(lam,k);
			}
			rmse_lambda_train[count] = Util.mean(rmse_10fold_train);
			rmse_lambda_test[count] = Util.mean(rmse_10fold_test);
			count++;
		}
		
		
		//DisplayInCSV(features);
		System.out.println();
	}
	
	public static void PlotRMSEvsLambda(String title, int features, double[] values){
		XYSeriesPlot demo = new XYSeriesPlot("Ridge Regression "+title, values,features);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		
	}
	
	
	public static void DisplayInCSV(int features){
		double j=0;
		System.out.println("----------train----------" + features);
		for (int i=0; i<rmse_lambda_train.length;i++){
			
			System.out.println("lambda = "+j+" :: "+rmse_lambda_train[i]);
			j = j +0.2;
		}
		System.out.println("----------test----------" + features);
		j=0;
		for (int i=0; i<rmse_lambda_test.length;i++){
			
			System.out.println("lambda = "+j+" :: "+rmse_lambda_train[i]);
			j = j +0.2;
		}
		
		/*for (int i = 0 ; i<poly_featurematrix.length;i++){
			for (int j=0; j< features; j++){
				System.out.print(poly_featurematrix[i][j]+",");
			}
			System.out.print(classvector[i]);
			System.out.println();
		}*/
	}
	
	
	public static void addfeatures(int i){
		poly_featurematrix = new double[featurematrix.length][i	* nooffeatures];
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
		// Invert the matrix only NOT normalized
		norm_poly_featurematrixT = new double[i * nooffeatures][poly_featurematrix.length];
		
		for (int n = 0; n < i * nooffeatures; n++) {
			double[] temp = new double[poly_featurematrix.length];
			double mean;
			for (int m = 0; m < poly_featurematrix.length; m++) {
				temp[m] = poly_featurematrix[m][n];
			}
			mean = Util.mean(temp);
			
			for (int m = 0; m < poly_featurematrix.length; m++) {
				norm_poly_featurematrixT[n][m] = poly_featurematrix[m][n] - mean;
			}	
		}
		
		
		/*-----------------Testing matrices------------------------------*/
		test_poly_featurematrix = new double[test_featurematrix.length][i	* nooffeatures];
		for (int m = 0; m < test_featurematrix.length; m++) {
			int pow = 0;
			for (int n = 0; n < i * nooffeatures; n++) {
				if (n % nooffeatures == 0) {
					pow += 1;
				}
				test_poly_featurematrix[m][n] = Math.pow(test_featurematrix[m][n
						% nooffeatures], pow);
			}
		}
		
		
		for (int n = 0; n < i * nooffeatures; n++) {
			double[] temp = new double[test_poly_featurematrix.length];
			double mean;
			for (int m = 0; m < test_poly_featurematrix.length; m++) {
				temp[m] = test_poly_featurematrix[m][n];
			}
			mean = Util.mean(temp);
			
			for (int m = 0; m < test_poly_featurematrix.length; m++) {
				test_poly_featurematrix[m][n] = test_poly_featurematrix[m][n] - mean;
			}	
		}
		
	}
	
	public static void multipleLambdas(double lambda,int fold){
		Ridge_Regression lr = new Ridge_Regression(norm_poly_featurematrixT, classvector);
		lr.normalEquation(lambda);
		double rmse = Math.sqrt(lr.getCost() * 2);
		rmse_10fold_train[fold] = rmse;	
		double[] actual_pre = new double[test_poly_featurematrix.length];
		for (int m = 0; m < test_poly_featurematrix.length; m++) {
			actual_pre[m] = lr.predict(test_poly_featurematrix[m])
					- test_actualop[m];

		}
		double sse = Util.SSE(actual_pre);
		rmse = Util.RMSE(sse, actual_pre.length);
		rmse_10fold_test[fold] = rmse;
	}

	public static void putdataintomatrices() throws NumberFormatException, IOException {
		String datasetName;
		datasetName = "sin";
		nooffeatures = 1;
		int inputtrainingdata = 90;
		int validationdata = 10;
		BufferedReader br = new BufferedReader(new FileReader(datasetName+ "_train.csv"));
		String sCurrentLine;
		String[] tokens;
		featurematrix = new double[inputtrainingdata][nooffeatures];
		classvector = new double[inputtrainingdata];
		int k = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			featurematrix[k][0] = Double.parseDouble(tokens[0]);
			classvector[k] = Double.parseDouble(tokens[1]);
			k++;
		}
		// read the validation set
		br = new BufferedReader(new FileReader(datasetName + "_test.csv"));
		test_featurematrix = new double[validationdata][nooffeatures];
		test_actualop = new double[validationdata];
		k = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			test_featurematrix[k][0] = Double.parseDouble(tokens[0]);
			test_actualop[k] = Double.parseDouble(tokens[1]);
			k++;
		}
	}

	public static void splitData() throws IOException {
		String datasetName;
		int inputtrainingdata;
		int percent90;
		datasetName = "sin";
		nooffeatures = 1;
		inputtrainingdata = 100;
		percent90 = 90;
		createData(percent90, inputtrainingdata, datasetName);
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
