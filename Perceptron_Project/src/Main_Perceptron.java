import java.io.IOException;


public class Main_Perceptron {
	public static double[] per;
	public static double[] dualper;
	public static double[] rbfmean;
	public static void main(String[] args) throws IOException, InterruptedException {
		
		if(args.length!=1){
			System.out.println("Provide arguments: Main_Perceptron perceptronData/twoSpirals");
			System.exit(0);
		}
		
		
		int percent90 = 0;
		int inputtrainingdata = 0;
		String datasetName = "";
		double learning_rate = 0;
		int features = 0;
		int fold10 =10;
		per = new double[fold10];
		dualper = new double[fold10];
		rbfmean = new double[fold10];
		int iterations=0;
		if (args[0].equalsIgnoreCase("perceptronData")){
			percent90 = 900;
			inputtrainingdata = 1000;
			datasetName = "perceptronData";
			learning_rate = 0.1;
			features = 5;
			iterations = 100;
		} 
		else if(args[0].equalsIgnoreCase("twoSpirals")){
			percent90 = 900;
			inputtrainingdata = 1000;
			datasetName = "twoSpirals";
			learning_rate = 0.1;
			features = 3;
			iterations = 5;
		}
		
		for (int i=0; i<fold10;i++){
			DataSplitter.createData(percent90, inputtrainingdata, datasetName);
			Perceptron p = new Perceptron();
			if (args[0].equalsIgnoreCase("perceptronData")){
				p.trainPerceptron(percent90, features, learning_rate,iterations);
				p.testPerceptron(100, features,i);
				p.train_dual_perceptron(percent90, features, learning_rate, iterations);
				p.test_dual_Perceptron(100, features,i);
				
			}else{
				p.train_dual_perceptron(percent90, features, learning_rate, iterations);
				p.test_dual_Perceptron(100, features,i);
				
				
				p.trainRBFKernalPerceptron(percent90, features, learning_rate, iterations);
				p.test_RBF_Perceptron(100, features,i);
			}
			System.out.println(i);
		}
		
		if (args[0].equalsIgnoreCase("perceptronData")){
			System.out.println("----Perceptron----");
			System.out.println(Utils.mean(Main_Perceptron.per));
			System.out.println("----Dual Perceptron----");
			System.out.println(Utils.mean(Main_Perceptron.dualper));
			
		}else{
			System.out.println("----Dual Perceptron Linear Kernel----");
			System.out.println(Utils.mean(Main_Perceptron.dualper));
			System.out.println("----Dual Perceptron RBF Kernel----");
			System.out.println(Utils.mean(Main_Perceptron.rbfmean));
		}
		
		
		
	}

}
