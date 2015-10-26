import java.util.Arrays;

public class NueralNetworkAutoEncoder  {
    private static GraphNetwork nueral_network;
    private static double training_examples[][];
    private final static int full_size_training       =   8;
    private final static int hidden_nodes            =   1;
    private final static int output_nodes           =   8;
    private final static int input_nodes            =   8;
    //MSE for terminating condition
    private final static double mse_terminating_cond            =   0.25;
    private final static int                iterations          =   1000000000;
    private final static double             eta                     =   0.1;
    private final static double             alpha                   =   0.01;

    public static void main(String args[]) throws Exception {
        initializeTrainingExamples();
        nueral_network = new GraphNetwork(input_nodes,hidden_nodes,output_nodes);
        startAutoencodertrain();
        startAutoencodertest();
    }

    private static void initializeTrainingExamples(){
        training_examples = new double[full_size_training][input_nodes];
        for (int i=0; i<full_size_training; i++){
            for (int j=0; j<input_nodes; j++){
                training_examples[i][j] = (j==i ? 1 : 0);
            }
        }
    }
    private static void startAutoencodertrain() throws Exception{
        
	    int i, j, k; 
	    double mse;
	    for (i=0, mse=mse_terminating_cond; (i<iterations && mse>=mse_terminating_cond); i++) {
	    	System.out.println("MSE : "+ mse);
	        for (j=0, k=0, mse=0; j<full_size_training; j++, k=(int)(Math.random()*full_size_training)){
	            mse += nueral_network.BackpropAlgo(training_examples[j],training_examples[j], eta, alpha);
	        }
	    } 
	    
	    System.out.println("iterations: "+i);
        
    }
    private static void startAutoencodertest() throws Exception{
        int accuracy=0;
        
        for (int i=0; i<full_size_training; i++) {
            for (int j=0; j<input_nodes; j++)
                System.out.print((int)training_examples[i][j]+" ");
            System.out.print("\t-->\t");
            double pred_output[]=nueral_network.BackPropAlgotest(training_examples[i]);
            double in_nodes[] = new double[input_nodes];
            for(int j=0; j<input_nodes; j++){
                in_nodes[j]=pred_output[j]>0.5?1:0;
                System.out.print((pred_output[j]>0.5?1:0)+" ");
            }
            System.out.println();
            if (Arrays.equals(in_nodes,training_examples[i]))
                accuracy++;
        }
        System.out.print(accuracy+" inputs out of "+full_size_training+" was reproduced");
        
    }
}