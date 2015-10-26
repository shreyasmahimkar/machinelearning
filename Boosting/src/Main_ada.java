import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.jfree.ui.RefineryUtilities;

public class Main_ada {

	public static void main(String[] args) throws IOException {
		
		if(args.length!=1){
			System.out.println("Provide Input : Main_ada diabetes/breastcancer/spambase");
			System.exit(0);
		}
		
		String dataSetName = args[0];
		int boostiter = 20;
		// int pos = 1;
		DataReader.readData(dataSetName + ".csv");
		DataReader.createData(DataReader.instances * 80 / 100,
				DataReader.instances, dataSetName);

		// Optimal Decision Stumps
		//OptimalDecisionStumps(dataSetName,boostiter);
		OpDecisionStumps(dataSetName, boostiter);
		// Random Decision Stumps
		RandomDecisionStumps(dataSetName, boostiter);
		
		
	}
	
	public static void RandomDecisionStumps(String dataSetName, int boostiter) throws NumberFormatException, IOException{
		//Select a feature Randomly.
		// Select a Threshold randomly
		int feature = Utils.positiveRandomNumberGen(DataReader.features-2);
		System.out.println(feature);
		TrainingData data_prep = new TrainingData(dataSetName+"_train.csv", feature, DataReader.features-1);
		Adaboosting_Algo adab = new Adaboosting_Algo(data_prep, 0);
		Weak_Linear_Classifier[] strong = adab.adaboost(boostiter, true);
		LinkedHashMap<Integer, Double> loc_err = new LinkedHashMap<Integer, Double>();
		int num = 1;
		/* Local Round Error start */
		for (Weak_Linear_Classifier s: strong ){
		    	System.out.println("::::"+s.local_error);
		    	loc_err.put(num, s.local_error);
		    	num+=1;
		}
		XYSeriesPlot demo = new XYSeriesPlot(dataSetName, loc_err,"Local Error");
        demo.pack();
 		RefineryUtilities.centerFrameOnScreen(demo);
 		demo.setVisible(true);
 		/* Local Round Error End */
 		/*Training Error Start*/
 		loc_err = new LinkedHashMap<Integer, Double>();
 		for(int i=0;i<boostiter;i++){
 			data_prep = new TrainingData(dataSetName+"_train.csv", feature, DataReader.features-1);
 			adab = new Adaboosting_Algo(data_prep, 0);
 			strong = adab.adaboost(i, true);
 			System.out.println(100 - adab.testing(data_prep.training_daata));
 			loc_err.put(i+1, 100 - adab.testing(data_prep.training_daata));
 		}
 		demo = new XYSeriesPlot(dataSetName, loc_err,"Training Error");
        demo.pack();
 		RefineryUtilities.centerFrameOnScreen(demo);
 		demo.setVisible(true);
 		/*Training Error End*/
 		
 		/*Testing Error Start*/
 		DataNode[] testData = new DataNode[DataReader.instances - DataReader.instances * 80 / 100];
 		BufferedReader br = new BufferedReader(new FileReader(dataSetName+ "_test.csv"));
		String sCurrentLine;
		String[] tokens = null;
		int i = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			double data = Double.parseDouble(tokens[feature]);
			double label = Integer
					.parseInt(tokens[DataReader.features - 1]) == 0 ? -1
					: 1;
			testData[i] = new DataNode(data, label, 0);
			i += 1;
		}
		for(int i1=0;i1<boostiter;i1++){
 			data_prep = new TrainingData(dataSetName+"_train.csv", feature, DataReader.features-1);
 			adab = new Adaboosting_Algo(data_prep, 0);
 			strong = adab.adaboost(i1, true);
 			System.out.println(100 - adab.testing(testData));
 			loc_err.put(i1+1, 100 - adab.testing(testData));
 		}
		demo = new XYSeriesPlot(dataSetName, loc_err,"Testing Error");
        demo.pack();
 		RefineryUtilities.centerFrameOnScreen(demo);
 		demo.setVisible(true);
		
 		/*Testing Error End*/
	}
	
	public static void OpDecisionStumps(String dataSetName,int boostiter) throws NumberFormatException, IOException{
		TrainingData[] data_prep = new TrainingData[DataReader.features - 1];
		Adaboosting_Algo[] adab = new Adaboosting_Algo[DataReader.features - 1];
		int bestfeature = Integer.MIN_VALUE;
		double maxvalue = Double.NEGATIVE_INFINITY;
		for (int k = 0; k < DataReader.features - 1; k++) {
			data_prep[k] = new TrainingData(dataSetName + "_train.csv", k,
					DataReader.features - 1);
			adab[k] = new Adaboosting_Algo(data_prep[k],0);
			adab[k].strong_classifier = adab[k].adaboost(1, false);
			double accuracy = adab[k].testing(data_prep[k].training_daata);
			//System.out.println(k + ":"+accuracy);
			if (accuracy > maxvalue){
				maxvalue = accuracy;
				bestfeature = k; 
			}
		}
		System.out.println(bestfeature);
		/*Local Round Error Start*/
		TrainingData data_prep1 = new TrainingData(dataSetName+"_train.csv", bestfeature, DataReader.features-1);
		Adaboosting_Algo adab1 = new Adaboosting_Algo(data_prep1, 0);
		Weak_Linear_Classifier[] strong = adab1.adaboost(boostiter, false);
		LinkedHashMap<Integer, Double> loc_err = new LinkedHashMap<Integer, Double>();
		int num = 1;
		/* Local Round Error start */
		for (Weak_Linear_Classifier s: strong ){
		    	System.out.println("::::"+s.local_error);
		    	loc_err.put(num, s.local_error);
		    	num+=1;
		}
		XYSeriesPlot demo = new XYSeriesPlot(dataSetName, loc_err,"Local Error");
        demo.pack();
 		RefineryUtilities.centerFrameOnScreen(demo);
 		demo.setVisible(true);
 		/* Local Round Error End */
 		/*Training Error Start*/
 		loc_err = new LinkedHashMap<Integer, Double>();
 		for(int i=0;i<boostiter;i++){
 			data_prep1 = new TrainingData(dataSetName+"_train.csv", bestfeature, DataReader.features-1);
 			adab1 = new Adaboosting_Algo(data_prep1, 0);
 			strong = adab1.adaboost(i, false);
 			System.out.println(100 - adab1.testing(data_prep1.training_daata));
 			loc_err.put(i+1, 100 - adab1.testing(data_prep1.training_daata));
 		}
 		demo = new XYSeriesPlot(dataSetName, loc_err,"Training Error");
        demo.pack();
 		RefineryUtilities.centerFrameOnScreen(demo);
 		demo.setVisible(true);
 		/*Training Error End*/
 		
 		/*Testing Error Start*/
 		DataNode[] testData = new DataNode[DataReader.instances - DataReader.instances * 80 / 100];
 		BufferedReader br = new BufferedReader(new FileReader(dataSetName+ "_test.csv"));
		String sCurrentLine;
		String[] tokens = null;
		int i = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			double data = Double.parseDouble(tokens[bestfeature]);
			double label = Integer
					.parseInt(tokens[DataReader.features - 1]) == 0 ? -1
					: 1;
			testData[i] = new DataNode(data, label, 0);
			i += 1;
		}
		for(int i1=0;i1<boostiter;i1++){
 			data_prep1 = new TrainingData(dataSetName+"_train.csv", bestfeature, DataReader.features-1);
 			adab1 = new Adaboosting_Algo(data_prep1, 0);
 			strong = adab1.adaboost(i1, true);
 			System.out.println(100 - adab1.testing(testData));
 			loc_err.put(i1+1, 100 - adab1.testing(testData));
 		}
		demo = new XYSeriesPlot(dataSetName, loc_err,"Testing Error");
        demo.pack();
 		RefineryUtilities.centerFrameOnScreen(demo);
 		demo.setVisible(true);
		
 		/*Testing Error End*/
 		
	}

}
