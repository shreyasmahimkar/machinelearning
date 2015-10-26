   
import java.util.Arrays;   
   
public class Weak_Linear_Classifier implements Classification_Interface {   
   
    public TrainingData training_data;   
    public double threshold;   
    public double local_error;   
    public int sign;   
   
    Weak_Linear_Classifier(TrainingData data, boolean isdatasorted,boolean isRandom) {   
        if (!isdatasorted)   
            Arrays.sort(data.training_daata);   
        this.training_data = data;   
        linear_classification(isRandom);   
    }   
   
    private void linear_classification(boolean isRandom) {   
        int i, j, sign;   
        double threshold = 0, local_error;   
        this.local_error = 1;  
        for (i = 0; i < training_data.training_daata.length; i++) {   
            sign = 1; 
            if (isRandom){
            	int pos = Utils.positiveRandomNumberGen(training_data.training_daata.length-1);
            	threshold = training_data.training_daata[pos].data_point;
            }else{
            	if (i == 0)   
                    threshold = training_data.training_daata[i].data_point - 400;   
                else   
                    threshold = ((training_data.training_daata[i - 1].data_point + training_data.training_daata[i].data_point) / 2) - 0.001;
            }
   
            local_error = 0;   
            for (j = 0; j < training_data.training_daata.length; j++) {   
                if (training_data.training_daata[j].data_point < threshold   
                        && training_data.training_daata[j].label != -1.0) {   
                    local_error += training_data.training_daata[j].weight;   
                }   
                if (training_data.training_daata[j].data_point > threshold   
                        && training_data.training_daata[j].label != +1.0)   
                    local_error += training_data.training_daata[j].weight;   
            }   
            if (1 - local_error < local_error) {   
                local_error = 1 - local_error;   
                sign = -1;   
            }   
            if (local_error < this.local_error) {   
            	
            	this.threshold = threshold;   
                this.sign = sign;   
                this.local_error = local_error;   
   
            }   
        }   
    }   
   
    public int classify_point(double data) {   
   
        if ((data > this.threshold && this.sign == +1)   
                || (data < this.threshold && this.sign == -1))   
            return 1;   
        else   
            return -1;   
   
    }   
   
    public double testing(DataNode[] testdata) {   
        int good = 0;   
        for (int i = 0; i < testdata.length; i++)   
            if (classify_point(testdata[i].data_point) == testdata[i].label)   
                good++;   
        return (good * 100.0) / testdata.length;   
    }   
   
   
    public double find_best_parameter_T() {   
        return 0.0;   
    }   

   
}   