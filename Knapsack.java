import java.util.Arrays;

/**
 * Created by HAN on 2017. 9. 28..
 */

public class Knapsack {

    public Knapsack() {

    }

    double calWeight(double data[][], int num, int arr[]){
        double ret = 0;
        for(int i = 0 ; i < num ; i ++){
            if(arr[i]==1) ret += data[i][2];
        }
        return ret;
    }
    double calVal(double data[][], int num, int arr[]){
        double ret = 0;
        for(int i = 0 ; i < num ; i ++){
            if(arr[i]==1) ret += data[i][1];
        }
        return ret;
    }

}
