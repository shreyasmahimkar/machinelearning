import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Vector;
   
public class TrainingData {     
    DataNode[] training_daata;   
   
    TrainingData(String filename, int dataindex, int labelindex) {   
        double[] training_data = load_training_data(filename, dataindex);   
        double[] labels = load_training_data(filename, labelindex);   
        // initialize weights   
        double[] weights = new double[training_data.length];   
        Arrays.fill(weights, (1.0 / training_data.length));   
        // iterativelly combine everything in the datastructure   
        training_daata = new DataNode[training_data.length];   
        for (int i = 0; i < training_data.length; i++) {   
            training_daata[i] = new DataNode(training_data[i], labels[i], weights[i]);   
             
        }   
    }   
   
   
    public double[] load_training_data(String filename, int dataindex) {   
        int i = 0;   
        double output[];   
        String[] dataline;   
        Vector datavector = new Vector();   
   
        try {   
            BufferedReader filereader = new BufferedReader(new FileReader(filename));   
   
            while (true) {   
                String line = filereader.readLine();   
                if (line == null)   
                    break;   
                i++;   
                dataline = line.split(",");   
                if (dataline[dataindex].trim().equals("0"))   
                    datavector.add(new Double(-1.0));   
                else if (dataline[dataindex].trim().equals("1"))   
                    datavector.add(new Double(1.0));   
                else if (!dataline[dataindex].matches("\\d*\\.?\\d*"))   
                    System.out.println("Warning: Found wrong data, removed");   
                else   
                    datavector.add(dataline[dataindex]);   
   
            }   
            filereader.close();   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
        output = new double[datavector.size()];   
        for (i = 0; i < datavector.size(); i++)   
            output[i] = new Double(datavector.get(i).toString()).doubleValue();   
        return output;   
    }   
}   