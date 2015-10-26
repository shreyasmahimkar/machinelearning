import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class DataReader {
	
	public static void readData(String datasetName) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(datasetName));
		String sCurrentLine;
		String [] tokens = null;
		int instances = 0;
		
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			instances++;
		}
		br.close();
		Clustering_Models.features = tokens.length;
		Clustering_Models.instances = instances; 
		
		Clustering_Models.dataset = new double[Clustering_Models.instances][Clustering_Models.features];
		br = new BufferedReader(new FileReader(datasetName));
		instances = 0;
		while ((sCurrentLine = br.readLine()) != null) {
			tokens = sCurrentLine.split(",");
			for (int j=0;j<Clustering_Models.features;j++){
				Clustering_Models.dataset[instances][j] = Double.parseDouble(tokens[j]);
			}
			instances++;
		}
		
	}
}
