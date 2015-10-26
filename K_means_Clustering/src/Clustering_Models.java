import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Clustering_Models {

	public static double[][] dataset; // this also contains the output
	public static int features; // This contains the output also
	public static int instances;
	public static double[][] K;
	public static double[][] euclideanDistance; // m x k -> m instances k
												// clusters
	public static int maxclusters = 20;
	public static int maxiterations = 1000;
	public static int[] outputpredictedclusters; // for each instance in dataset
	public static int clusters = 0;
	public static double[] sse;
	public static double maxsse = 10;
	public static int iter;
	public static double class_entropy = 0;
	public static double cluster_entropy = 0.0;
	public static LinkedHashMap<Integer, Double> Entropy = null;

	public static double Calculate_Class_Entropy() {
		double entropy = 0.0;
		Entropy = new LinkedHashMap<Integer, Double>();
		for (int i = 0; i < dataset.length; i++) {
			int a = (int) dataset[i][features - 1];
			if (Entropy.get(a) != null) {
				Entropy.put(a, Entropy.get(a) + 1);
			} else {
				Entropy.put(a, 1.0);
			}
		}
		double sum = 0.0;
		for (Integer ks : Entropy.keySet()) {
			sum += Entropy.get(ks);
		}

		for (Integer ks : Entropy.keySet()) {
			Entropy.put(ks, Entropy.get(ks) / sum);
		}

		for (Integer ks : Entropy.keySet()) {
			entropy += (-Entropy.get(ks) * Math.log(Entropy.get(ks)
					/ Math.log(2)));
		}

		return entropy;
	}

	public static double calculate_Cluster_Entropy(int[] op_clusters) {
		double cluster_entropy = 0.0;
		Entropy = new LinkedHashMap<Integer, Double>();
		for (int i = 0; i < dataset.length; i++) {
			int a = op_clusters[i];
			if (Entropy.get(a) != null) {
				Entropy.put(a, Entropy.get(a) + 1);
			} else {
				Entropy.put(a, 1.0);
			}
		}
		double sum = 0.0;
		for (Integer ks : Entropy.keySet()) {
			sum += Entropy.get(ks);
		}

		for (Integer ks : Entropy.keySet()) {
			Entropy.put(ks, Entropy.get(ks) / sum);
		}

		for (Integer ks : Entropy.keySet()) {
			cluster_entropy += (-Entropy.get(ks) * Math.log(Entropy.get(ks)
					/ Math.log(2)));
		}

		return cluster_entropy;
	}

	public void KmeansAlgo(LinkedHashMap<String, Integer> datasets)
			throws IOException {
		// Read Datasets and for each data set do the following
		for (String datasetName : datasets.keySet()) {
			DataReader.readData(datasetName);
			// Calculate Entropy of the Class Labels here before clustering
			class_entropy = Calculate_Class_Entropy();
			// Inititialize number of clusters start with 1
			clusters = 1;
			// Randomly Initialize the centroids
			do {
				K = new double[clusters][features - 1];

				outputpredictedclusters = new int[instances];
				initCentroids(dataset, clusters);
				euclideanDistance = new double[instances][clusters];
				double prev_sse = 0;
				double curr_sse = 0;
				double NMI=0.0;
				do {
					sse = new double[clusters];
					iter++;
					for (int i = 0; i < instances; i++) {
						for (int j = 0; j < clusters; j++) {
							euclideanDistance[i][j] = Utils
									.calculateEuclideanDistance(dataset[i],
											K[j]);
						}
					}

					// Assign clusters 1,2,3 ...
					// Calculate the minimum value is in which position of the
					// euclidean distance
					// Assign cluster = pos + 1
					for (int i = 0; i < instances; i++) {
						outputpredictedclusters[i] = calcminimum(euclideanDistance[i]);
					}
					// Calculate SSE
					for (int i = 0; i < instances; i++) {
						int pos = outputpredictedclusters[i];
						double answer = euclideanDistance[i][pos];
						sse[pos] += answer * answer;

					}

					// Recalculate Centroid
					for (int kcenter = 0; kcenter < clusters; kcenter++) {
						double[] newk = new double[features - 1];
						double numinstances = 0;
						for (int i = 0; i < instances; i++) {
							int temp = outputpredictedclusters[i];
							if (kcenter == temp) {
								for (int j = 0; j < features - 1; j++) {
									newk[j] += dataset[i][j];
								}
								numinstances++;
							}
						}

						for (int j = 0; j < features - 1; j++) {
							K[kcenter][j] = newk[j] / numinstances;
						}

					}
					// Calculate NMI
					cluster_entropy = calculate_Cluster_Entropy(outputpredictedclusters);
					double numerator = class_entropy - calculate_Conditional_Entropy(outputpredictedclusters);
					NMI = (2*numerator)/(class_entropy+cluster_entropy);
					

					for (double d : sse) {
						curr_sse += d;
					}
					System.out
							.println("NMI:"+NMI+" sse:" + curr_sse + " iteration:" + iter);
					if (prev_sse == curr_sse || Double.isNaN(curr_sse)) {
						break;
					}
					prev_sse = curr_sse;
					curr_sse = 0;
				} while (iter <= maxiterations);
				// The SSE did not turn to 0
				System.out.println("cluster:" + clusters);
				if (Main_clustering.dataset_sse_k_plot.get(datasetName) != null) {
					Main_clustering.dataset_sse_k_plot.get(datasetName).put(
							clusters, prev_sse);
					Main_clustering.dataset_NMI_k_plot.get(datasetName).put(clusters, NMI);
				} else {
					Main_clustering.dataset_sse_k_plot.put(datasetName,
							new LinkedHashMap<Integer, Double>());
					Main_clustering.dataset_NMI_k_plot.put(datasetName, new LinkedHashMap<Integer, Double>());
					Main_clustering.dataset_sse_k_plot.get(datasetName).put(
							clusters, prev_sse);
					Main_clustering.dataset_NMI_k_plot.get(datasetName).put(clusters, NMI);
				}

				clusters += 1;
				iter = 0;

			} while (/* sse>maxsse && */clusters <= maxclusters);

			System.out.println(clusters);

			// Calculate SSE and NMI
			// Based on SSE increase number of clusters
		}

	}

	public static double calculate_Conditional_Entropy(int[] op_clusters) {
		double final_cond_entropy = 0.0;
		List<Double> c_e = new ArrayList<Double>();
		double num_clusters = clusters;
		
		for (int i = 0; i < op_clusters.length; i++) {
			double cond_entropy = 0.0;
			double sum = 0.0;
			// Pick those from the data
			List<Integer> pos = new ArrayList<Integer>();
			// 
			LinkedHashMap<Integer, Double> lhm = new LinkedHashMap<Integer, Double>();
			for (int j = 0; j < op_clusters.length; j++) {
				if (op_clusters[i] == i){
					sum+=1;
					pos.add(i);
				}
			}
			for(Integer loc : pos){
				int a = (int)dataset[loc][features-1];
				if(lhm.get(loc)!=null){
					lhm.put(a,lhm.get(loc)+1);
				}else{
					lhm.put(a, 1.0);
				}
			}
			
			for(Integer iter : lhm.keySet()){
				lhm.put(iter, lhm.get(iter)/sum);
			}
			
			for(Integer iter : lhm.keySet()){
				cond_entropy += lhm.get(iter)*Math.log(lhm.get(iter))/Math.log(2);
			}
			
			cond_entropy = (-1/num_clusters) * cond_entropy;
			c_e.add(cond_entropy);
		}

		for(Double d : c_e){
			final_cond_entropy += d;
		}
		

		return final_cond_entropy;
	}

	public static int calcminimum(double[] arraymin) {
		int pos = 0;
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < arraymin.length; i++) {
			if (arraymin[i] < min) {
				min = arraymin[i];
				pos = i;
			}
		}
		return pos;
	}

	/*
	 * public static void initCentroids(int epsilon) { for (int i = 0; i <
	 * K.length; i++) { for (int j = 0; j < K[0].length; j++) { K[i][j] =
	 * Utils.positiveRandomNumberGen(epsilon); } } }
	 */

	public static void initCentroids(double[][] dataset, int centroids) {
		int[] pos;
		if (centroids == 1) {
			K[0] = dataset[0];
		} else {
			K[0] = dataset[0]; // 1 centroid here
			double[] euclideandist = new double[dataset.length];
			for (int i = 0; i < dataset.length; i++) {
				euclideandist[i] = Utils.calculateEuclideanDistance(dataset[i],
						K[0]);
			}
			pos = Utils.top_max_calc(euclideandist, centroids - 1);
			int j = 1;
			for (int p : pos) {
				K[j] = dataset[p];
				j = j + 1;
			}

		}

	}

}
