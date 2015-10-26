   
import java.util.Arrays;
   
public class Adaboosting_Algo implements Classification_Interface {   
   
    public TrainingData tr_data; 
    Weak_Linear_Classifier[] strong_classifier;   
    Adaboosting_Algo(TrainingData data, int num_boostiteration) {   
        this.tr_data = data;      
    }   
   
    //  Adaboost algo start   
    public Weak_Linear_Classifier[] adaboost(int NUM_boostiterations,boolean isRandom) {   
        int i, j;   
        double sum_of_weights = 0, alpha;   
        strong_classifier = new Weak_Linear_Classifier[NUM_boostiterations];   
        Arrays.sort(tr_data.training_daata);   
        for (i = 0; i < NUM_boostiterations; i++) {   
            // Weak Classifier 
            strong_classifier[i] = new Weak_Linear_Classifier(tr_data, true, isRandom);   
            // If error is 0 then break   
            if (strong_classifier[i].local_error == 0)   
                break;   
            // Updating weights 
            sum_of_weights = 0;   
            alpha = Math.log((1 - strong_classifier[i].local_error) / strong_classifier[i].local_error) / 2;   
            for (j = 0; j < tr_data.training_daata.length; j++) {   
                tr_data.training_daata[j].weight = tr_data.training_daata[j].weight   
                        * Math.exp(-alpha   
                                * strong_classifier[i].classify_point(tr_data.training_daata[j].data_point)   
                                * tr_data.training_daata[j].label);   
                sum_of_weights += tr_data.training_daata[j].weight;   
            }   
   
            //normalizing weights   
            for (j = 0; j < tr_data.training_daata.length; j++)   
                tr_data.training_daata[j].weight = tr_data.training_daata[j].weight   
                        / sum_of_weights;   
        } 
        /*for (Linear s: strong ){
        	System.out.println("::::"+(1-2*Math.sqrt(s.error*(1-s.error))));
        }*/
    
        return strong_classifier;   
    }   
   
    public int classify_point(double data_point) {   
        double sum = 0;   
        for (int i = 0; i < strong_classifier.length; i++) {   
            double alpha = (Math.log((1 - strong_classifier[i].local_error) / strong_classifier[i].local_error)) / 2;   
            if ((data_point > strong_classifier[i].threshold && strong_classifier[i].sign == +1)   
                    || (data_point < strong_classifier[i].threshold && strong_classifier[i].sign == -1))   
                sum += alpha;   
            else   
                sum -= alpha;   
        }   
        
        if (sum > 0)   
            return 1;   
        else   
            return -1;   
    }   
   
    public double testing(DataNode[] testing_data) {   
        int good = 0;   
        for (int i = 0; i < testing_data.length; i++)   
            if (classify_point(testing_data[i].data_point) == testing_data[i].label)   
                good++;   
        return (good * 100.0) / testing_data.length;   
    }
    
    public double find_best_parameter_T() {   
        int best_T = 0, same_perf = 0;   
        double perf, best_perf = 0;   
        double thresholdper = 5;        
        for (int i = 0;; i++) {   
            Adaboosting_Algo boost = new Adaboosting_Algo(tr_data, i); 
            perf = boost.testing(tr_data.training_daata);   
            if (perf <= best_perf + thresholdper 
            		&& perf >= best_perf - thresholdper )   
                same_perf++;   
            else   
                same_perf = 0;   
            if (perf > best_perf) {   
                best_perf = perf;   
                best_T = i;   
            }   
            if (same_perf == 2){ 
            	break;   
            }
        }   
        System.out.println("Best parameter T is : " + best_T +":"+ perf);   
        return best_T;   
   
    }   
   
}   