
/**
 * Created by HAN on 2017. 9. 29..
 */

import java.util.Arrays;
import java.util.Scanner;

public class SingleKnapsack {
    Scanner sc;
    Knapsack kn;
    int maxWei, numProduct;
    double[][] data;

    public SingleKnapsack(){
        sc = new Scanner(System.in);
        kn = new Knapsack();
        maxWei = 0;
        numProduct =0;
        data = new double[100][3];
    }
    void inputData(){
        System.out.printf("number of Products: ");
        if(sc.hasNext()) {
            numProduct = Integer.parseInt(sc.next());
        }
        //input maximum volume
        System.out.printf("number of Maximum Weight: ");
        if(sc.hasNext()) {
            maxWei = Integer.parseInt(sc.next());

        }
        System.out.printf("numproduct : %d, maxVol: %d\n",numProduct, maxWei);

        //input weight and value
        for(int i = 0 ; i < numProduct; i++){
            System.out.printf("product %d weight: ",i);
            if(sc.hasNext()) {
                //weight
                data[i][2] = Double.parseDouble(sc.next());
            }
            System.out.printf("product %d value: ",i);
            if(sc.hasNext()) {
                //value
                data[i][1] = Double.parseDouble(sc.next());
            }
            data[i][0] = data[i][1]/data[i][2];

        }
    }

    void sortData(){
        for(int i = 0; i < numProduct; i++){
            for(int j = 1 ; j < numProduct-i; j++){
                if(data[j-1][0]<data[j][0]){
                    double[] temp = data[j];
                    data[j] = data[j-1];
                    data[j-1] = temp;
                }
            }
        }
        for(int i = 0 ; i < numProduct; i++){
            System.out.printf("%f, %f, %f\n", data[i][0], data[i][1], data[i][2]);
        }

    }
    int[] greedy(double data[][], int num, int max){
        double weight = 0;
        int[] ret = new int[num];

        //pack things that have high val/wieght
        for(int i = 0 ; i < num; i++){
            if(weight + data[i][2] <= max) {
                weight += data[i][2];
                ret[i] = 1;
            }
            else break;
        }
        return ret;
    }

    int[] neighbor(double data[][], int num, int max, int current[]){
        System.out.println("currnet node : "+Arrays.toString(current));
        double maxVal=0;
        // calculate Cost(X(t))
        for(int i = 0 ; i < num; i++)
            if(current[i]==1) maxVal+= data[i][1];


        //Define neighbor as flipping 2 elements from current state;

        for(int i = 0; i < num ; i ++){
            // flip i th element
            current[i] = 1-current[i];
            for(int j = i+1 ; j < num ; j++){
                // flip jth element
                current[j] = 1-current[j];
                double neighborVal = kn.calVal(data, num, current);
                //print neightbor
                System.out.println("See Neighborhood: "+ Arrays.toString(current));
//                System.out.println("---------maxVal: "+maxVal+", neighborVal: "+neighborVal+", weight: "+calWeight(data, num, current));
                if(neighborVal > maxVal && kn.calWeight(data, num, current) <= max) {
                    // X(t+1) = this neighbor.
                    return neighbor(data, num, max, current);
                }

                current[j] = 1 - current[j];
            }
            current[i] = 1-current[i];
        }
        return current;
    }
    public void playSingleGreedy(){
        System.out.println("Single Knapsack Problem");

        //PART 1 : GET INPUT
        inputData();

        //PART2 : Sort by Value/Weight
        //sort **maybe can make with array.sort
        sortData();

        //PART3 : Get Initial State from Greedy Algorighm.
        int result[] = greedy(data, numProduct, maxWei);

        //PART4 : Result
        System.out.println(Arrays.toString(result));
        System.out.println(kn.calVal(data, numProduct, result));
    }
    public void playSingleNeighbor(){
        System.out.println("Single Knapsack Problem");

        //PART 1 : GET INPUT
        inputData();

        //PART2 : Sort by Value/Weight
        //sort **maybe can make with array.sort
        sortData();

        //PART3 : Get Initial State from Greedy Algorighm.
        int greedy[] = greedy(data, numProduct, maxWei);

        //PART4 : Neighborhood Search Algorighm.
        int result[] = neighbor(data, numProduct, maxWei, greedy);

        //PART5 : Result
        // System.out.println(Arrays.toString(greedy));
        System.out.println(Arrays.toString(result));
        System.out.println(kn.calVal(data, numProduct, result));
    }
}
