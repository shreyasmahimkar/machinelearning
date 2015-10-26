import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class TestDT {

	public static LinkedHashMap<Integer, ArrayList<Double>> MSE = new LinkedHashMap<Integer, ArrayList<Double>>();

	public static void main(String[] args) throws FileNotFoundException,
			IOException, InterruptedException {

		if (args.length != 1) {
			System.out.println("You must call TestDT as follows:\n\n"
					+ "  java TestDT iris/spambase/housing");
		}

		int percent90 = 0, maxnmin = 0;
		String datasetName = "";
		if (args[0].equalsIgnoreCase("iris")) {
			System.out.println("-------Iris-------");
			datasetName = "iris";
			DecisionTreeMaking.nooffeatures = 4;
			DecisionTreeMaking.inputtrainingdata = 150;
			percent90 = 135;
			maxnmin = 21;

		} else if (args[0].equalsIgnoreCase("spambase")) {
			System.out.println("-------Spambase-------");
			datasetName = "spambase";
			DecisionTreeMaking.nooffeatures = 57;
			DecisionTreeMaking.inputtrainingdata = 4601;
			percent90 = 4141;
			maxnmin = 26;

		} else if (args[0].equalsIgnoreCase("housing")) {
			System.out.println("-------Housing-------");
			datasetName = "housing";
			DecisionTreeMaking.nooffeatures = 13;
			DecisionTreeMaking.inputtrainingdata = 506;
			percent90 = 455;
			maxnmin = 21; // change this
		}

		LinkedHashMap<Integer, ArrayList<Double>> average = new LinkedHashMap<Integer, ArrayList<Double>>();
		ArrayList<Double> inneraverage = new ArrayList<Double>();
		
		
		
		int fold10 = 10;
		// 10 fold cross validation
		for (int i = 0; i < fold10; i++) {
			System.out.println("In fold no:"+i);
			BufferedReader br = new BufferedReader(new FileReader(datasetName
					+ ".csv"));
			// Continuos variable check

			String sCurrentLine;
			int traincount = 0, testcount = 0;
			int splittraintest = 0;
			String[] trainingdata = new String[percent90];
			String[] testingdata = new String[DecisionTreeMaking.inputtrainingdata
					- percent90];
			String[] fulldata = new String[DecisionTreeMaking.inputtrainingdata];

			// In order to create random 10 datasets
			Integer[] arr = new Integer[DecisionTreeMaking.inputtrainingdata];
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

				} else if (testcount < DecisionTreeMaking.inputtrainingdata
						- percent90) {
					testingdata[testcount] = fulldata[arr[splittraintest]];
					testcount++;
				}
				splittraintest++;
			}

			writetofile(datasetName + "_train.csv", trainingdata);
			writetofile(datasetName + "_test.csv", testingdata);

			if (args[0].equalsIgnoreCase("iris")
					|| args[0].equalsIgnoreCase("spambase")) {
				for (int nmin = 5; nmin < maxnmin; nmin = nmin + 5) {
					double sum = 0;
					DecisionTreeMaking id3 = new DecisionTreeMaking(datasetName
							+ "_train.csv", DecisionTreeMaking.nooffeatures,
							percent90, ",");
					int threshold = (int) Math
							.ceil((double) (percent90 * nmin / 100.0));
					// System.out.println("create decision tree");
					id3.createDecisionTree(threshold);
					sum += id3.calculate_accuracy(datasetName + "_test.csv",
							DecisionTreeMaking.inputtrainingdata - percent90,
							",");
					// System.out.println(sum +"----");
					//System.out.println(sum + ":" + nmin);
					inneraverage = new ArrayList<Double>();
					if (average.get(nmin) == null) {
						inneraverage.add(sum);
						average.put(nmin, inneraverage);

					} else {
						inneraverage = average.get(nmin);
						inneraverage.add(sum);
						average.put(nmin, inneraverage);
					}
					System.out.print(nmin+" ");

				}
				
				//System.out.println("-------");

				// System.out.println(average.entrySet());
			} else if (args[0].equalsIgnoreCase("housing")) {

				for (int nmin = 5; nmin < maxnmin; nmin = nmin + 5) {
					DecisionTreeMaking id3 = new DecisionTreeMaking(datasetName
							+ "_train.csv", DecisionTreeMaking.nooffeatures,
							percent90, ",");
					int threshold = (int) Math
							.ceil((double) (percent90 * nmin / 100.0));
					id3.generateRegressionTree(threshold);
					id3.findRAccuracy(datasetName + "_test.csv",DecisionTreeMaking.inputtrainingdata - percent90,",",nmin);
					//System.out.println("------"+nmin+"--------");

				}
				double [] mean_calc;
				ArrayList<Double> temp;
				for (Entry<Integer, ArrayList<Double>> e : MSE.entrySet()) {
					temp = e.getValue();
					mean_calc = new double[temp.size()];
					int k=0;
					for (Double d : temp) {
						mean_calc[k] = d;
						k++;
						
					}
					//System.out.println(e.getKey()+"---"+Util.mean(mean_calc));
					inneraverage = new ArrayList<Double>();
					if (average.get(e.getKey()) == null) {
						inneraverage.add(Util.mean(mean_calc));
						average.put(e.getKey(), inneraverage);

					} else {
						inneraverage = average.get(e.getKey());
						inneraverage.add(Util.mean(mean_calc));
						average.put(e.getKey(), inneraverage);
					}
				}
				MSE = new LinkedHashMap<Integer, ArrayList<Double>>();
				//System.out.println("-----fold:"+i+"-------");
			}
		}

		if (args[0].equalsIgnoreCase("iris")
				|| args[0].equalsIgnoreCase("spambase")) {
			double sum = 0;
			double [] sd_calc;
			
			ArrayList<Double> temp;
			for (Entry<Integer, ArrayList<Double>> e : average.entrySet()) {
				temp = e.getValue();
				sd_calc = new double[temp.size()];
				int k=0;
				for (Double d : temp) {
					sum += d;
					sd_calc[k] = d*100;
					k++;
					System.out.println(Math.round(d*100));
				}
				System.out.println(e.getKey() + "--" + sum / fold10 * 100+"--"+Util.standardDeviation(sd_calc));
				sum = 0;
			}
		}else if(args[0].equalsIgnoreCase("housing")){
			double sum = 0;
			double [] sd_calc;
			
			ArrayList<Double> temp;
			for (Entry<Integer, ArrayList<Double>> e : average.entrySet()) {
				temp = e.getValue();
				sd_calc = new double[temp.size()];
				int k=0;
				for (Double d : temp) {
					sum += d;
					sd_calc[k] = d;
					k++;
					System.out.println(Math.round(d));
				}
				System.out.println(e.getKey() + "--" + sum / fold10+"--"+Util.standardDeviation(sd_calc));
				sum = 0;
			}
			
		}

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

		for (int i = 0; i < DecisionTreeMaking.nooffeatures; i++) {
			st.append("c,");
		}

		bw.write(st.toString() + "?\n");
		for (int i = 0; i < data.length; i++) {
			bw.write(data[i] + "\n");
		}
		bw.close();
	}
}
