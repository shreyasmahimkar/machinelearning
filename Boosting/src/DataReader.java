import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;


public class DataReader {
	public static int features;
	public static int instances;
	public static double [][] dataset;
	public static String[] trainingdata;
	public static String[] testingdata;
	
	
	public static void readData(String datasetName) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(datasetName));
		String sCurrentLine;
		String [] tokens = null;
		int insts = 0;
		
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			insts++;
		}
		br.close();
		features = tokens.length;
		instances = insts; 
		
		dataset = new double[instances][features];
		br = new BufferedReader(new FileReader(datasetName));
		insts = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			for (int j=0;j<features;j++){
				dataset[insts][j] = Double.parseDouble(tokens[j]);
			}
			insts++;
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
		} else { // If it exists overwrite
			file.delete();
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		StringBuilder st = new StringBuilder();

		for (int i = 0; i < data.length; i++) {
			bw.write(data[i] + "\n");
		}
		bw.close();
	}

}
