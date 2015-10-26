import java.io.IOException;
import java.util.LinkedHashMap;

import org.jfree.ui.RefineryUtilities;



public class Main_clustering {
	public static LinkedHashMap<String, LinkedHashMap<Integer, Double>> dataset_sse_k_plot = new LinkedHashMap<String, LinkedHashMap<Integer,Double>>();
	public static LinkedHashMap<String, LinkedHashMap<Integer, Double>> dataset_NMI_k_plot = new LinkedHashMap<String, LinkedHashMap<Integer,Double>>();
	
	
	public static void main(String[] args) throws IOException {

		if(args.length!=1){
			System.out.println("Provide Input : Main_Clustering datasetName");
			System.exit(0);
		}
		
		// datasetName, Epsilon for centroid start
		LinkedHashMap<String,Integer> datasets = new LinkedHashMap<String,Integer>();
		datasets.put(args[0]+".csv",100);
		
		/*"dermatologyData.csv
		 * datasets.put("ecoliData.csv",100);
		datasets.put("glassData.csv",100);
		datasets.put("soybeanData.csv",100);
		datasets.put("vowelsData.csv",100);
		datasets.put("yeastData.csv",100);*/
		Clustering_Models cm = new Clustering_Models();
		
		cm.KmeansAlgo(datasets);
		
		for (String dataset : dataset_sse_k_plot.keySet()){
			XYSeriesPlot demo = new XYSeriesPlot(dataset, dataset_sse_k_plot.get(dataset),"SSE");
	        demo.pack();
	 		RefineryUtilities.centerFrameOnScreen(demo);
	 		demo.setVisible(true);
		}
		
		for (String dataset : dataset_NMI_k_plot.keySet()){
			XYSeriesPlot demo = new XYSeriesPlot(dataset, dataset_NMI_k_plot.get(dataset),"NMI");
	        demo.pack();
	 		RefineryUtilities.centerFrameOnScreen(demo);
	 		demo.setVisible(true);
		}
		
	}

}
