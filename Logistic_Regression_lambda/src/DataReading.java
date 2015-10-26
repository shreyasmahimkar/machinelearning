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
import java.util.List;
import java.util.Scanner;

public class DataReading {

    public static List<Record> readDataSet(String filename, int skipcols) throws FileNotFoundException {
        List<Record> dataset = new ArrayList<Record>();
        Scanner scanner = new Scanner(new File(filename+".csv"));
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] columns = line.split(",");

            // skip first columas and last column is the label
            int i = skipcols;
            double[] data = new double[columns.length-1-skipcols];
            for (i=skipcols; i<columns.length-1; i++) {
                data[i-skipcols] = Double.parseDouble(columns[i]);
            }
            int label = Integer.parseInt(columns[i]);
            Record instance = new Record(label, data);
            dataset.add(instance);
        }
        return dataset;
    }
    
    public static int decideboundsandCreateData(String datasetName) throws IOException{
    	int percent90,inputtrainingdata;
    	int nooffeatures = 0;
    	if(datasetName.equalsIgnoreCase("breastcancer")){
    		percent90 = 512;
    		inputtrainingdata = 569;
    		createData(percent90, inputtrainingdata, datasetName);
    		nooffeatures=30;
    	}else if(datasetName.equalsIgnoreCase("diabetes")){
    		percent90 = 700;
    		inputtrainingdata = 768;
    		createData(percent90, inputtrainingdata, datasetName);
    		nooffeatures=8;
    	} else if(datasetName.equalsIgnoreCase("spambase")){
    		percent90 = 4140;
    		inputtrainingdata = 4601;
    		createData(percent90, inputtrainingdata, datasetName);
    		nooffeatures=57;
    	}
    	return nooffeatures;
    }

	public static void createData(int percent90, int inputtrainingdata,
			String datasetName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(datasetName
				+ ".csv"));
		String sCurrentLine;
		int traincount = 0, testcount = 0;
		int splittraintest = 0;
		String[] trainingdata = new String[percent90];
		String[] testingdata = new String[inputtrainingdata - percent90];
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