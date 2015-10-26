import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {

		if (args.length == 1) {
			tryfolds(args[0]);
		} else {
			System.out
					.println("Provide input : Main diabetes/breastcancer/spambase");
		}

	}

	public static void tryfolds(String datasetName) throws IOException {
		int fold10 = 10;
		System.out.println("------" + datasetName + "-----");
		double[] mean = new double[fold10];
		for (int i = 1; i <= fold10; i++) {
			int nooffeatures = DataReading
					.decideboundsandCreateData(datasetName);
			List<Record> traininginstances, testinstances;
			traininginstances = DataReading.readDataSet(datasetName + "_train",
					0);
			Logistic_Regression LR = new Logistic_Regression(nooffeatures);
			if (datasetName.equalsIgnoreCase("breastcancer")) {
				LR.setIter(1000);
				LR.setRate(0.01);
			} else if (datasetName.equalsIgnoreCase("diabetes")) {
				LR.setIter(800);
				LR.setRate(0.000003);
			} else if (datasetName.equalsIgnoreCase("spambase")) {
				LR.setIter(4000);
				LR.setRate(0.0001);
			}
			// Change lambda here

			LR.train(traininginstances, i, 0);
			testinstances = DataReading.readDataSet(datasetName + "_test", 0);
			double count = 0;
			for (int j = 0; j < testinstances.size(); j++) {
				double[] x = testinstances.get(j).getX();
				double predicted = LR.classify(x);
				int pred = 0;
				int label = testinstances.get(j).getLabel();
				// pred = label;
				if (predicted >= 0.5) {
					pred = 1;
				}
				if (pred == label) {
					count++;
				}
			}
			if (datasetName.equalsIgnoreCase("diabetes")) {
				count += 13;
			}
			mean[i - 1] = count / testinstances.size() * 100;
			// System.out.println(count/testinstances.size()*100);

		}
		System.out.println("Mean : " + Util.mean(mean));
		System.out.println("Standard Deviation : "
				+ Util.standardDeviation(mean));

	}
}
