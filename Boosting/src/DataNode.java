public class DataNode implements Comparable {   
    public double data_point;   
   
    public double label;   
   
    public double weight;   
   
    DataNode(double data, double label, double weight) {   
        this.data_point = data;   
        this.label = label;   
        this.weight = weight;   
    }   
   
    public int compareTo(Object o) {   
        if (data_point == ((DataNode) o).data_point)   
            return 0;   
        if (data_point > ((DataNode) o).data_point)   
            return 1;   
        if (data_point < ((DataNode) o).data_point)   
            return -1;   
        return 0;   
    }   
}  