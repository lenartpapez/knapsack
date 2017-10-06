import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by HAN on 2017. 9. 29..
 */
public class MultiKnapsack {

    Scanner sc;
    Knapsack kn;
    int maxWei[], numProduct, numKnapsack;
    double[][] data;

    public MultiKnapsack(){
        sc = new Scanner(System.in);
        kn = new Knapsack();
        maxWei = new int[100];
        numProduct = 0;
        numKnapsack = 0;
        data = new double[100][3];
    }

    void inputData(){
        System.out.print("Number of products: ");
        if(sc.hasNext()) {
            numProduct = Integer.parseInt(sc.next());
        }
        System.out.print("Number of knapsacks: ");
        if(sc.hasNext()) {
            numKnapsack = Integer.parseInt(sc.next());
        }
        //input maximum volume
        for(int i = 0 ; i < numKnapsack; i ++){
            System.out.printf("Capacity of knapsack %d: ", i+1);
            if(sc.hasNext()) {
                maxWei[i] = Integer.parseInt(sc.next());
            }
        }


        //input price and weight
        for(int i = 0 ; i < numProduct; i++){
            System.out.printf("Price of item %d: ", i+1);
            if(sc.hasNext()) {
                //price  
                data[i][1] = Double.parseDouble(sc.next());
            }
            System.out.printf("Weight of item %d: ", i+1);
            if(sc.hasNext()) {
                //weight
                data[i][2] = Double.parseDouble(sc.next());
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
        // for(int i = 0 ; i < numProduct; i++){
        //     System.out.printf("%f, %f, %f\n", data[i][0], data[i][1], data[i][2]);
        // }
    }

    int[] greedy(double data[][], int num, int numKnapsack, int[] maxWei){
        // ret notates which product is in which knapsack. -1 means not included in any.
        int ret[] = new int[numProduct];
        int retIter[] = new int[numProduct];
        for(int i = 0 ; i < numProduct; i++) ret[i] = -1;

        double weight[] = new double[numKnapsack];
//        System.out.println("init weight");
//        System.out.println(Arrays.toString(weight));
        //pack things that have high val/wieght
        while(true) {
            for (int i = 0, j = 0; i < num; i++) {
                if(ret[i] != -1) continue;

//                System.out.println("Every weight");
//                System.out.println(Arrays.toString(weight));

                int k = j;
                do{
//                    System.out.println("k " + k +": "+weight[k] +" + "+ data[i][2]+" compares "+ maxWei[k]);
                    if(weight[k] + data[i][2] <= maxWei[k]){
//                        System.out.println("k : " + k +", i : "+i);
                        weight[k] += data[i][2];
                        ret[i] = k;
                        break;
                    }
                    k++;
                }while(k%numKnapsack!=j);
            }
            if(Arrays.equals(ret, retIter)) break;
            System.arraycopy( ret, 0, retIter, 0, ret.length );
        }

        return ret;
    }

    public void playMultiKnapsackGame(){
        //PART 1 : GET INPUT


        inputData();

        //PART2 : Sort by Value/Weight
        //sort **maybe can make with array.sort
        sortData();

        //PART3 : Get Initial State from Greedy Algorighm.
        int greedy[] = greedy(data, numProduct, numKnapsack, maxWei);
        int firstKnapsackTotal = 0;
        int secondKnapsackTotal = 0;
        int totalProfit = 0;
        System.out.println();
        for(int i = 0 ; i < numProduct; i++) {
            String knapsack = "";
            if(greedy[i] == 1) {
                knapsack = "in knapsack 2";
                secondKnapsackTotal += data[i][2];
                totalProfit += data[i][1];
            } else if(greedy[i] == -1) {
                knapsack = "not in any knapsacks";
            } else {
                knapsack = "in knapsack 1";
                firstKnapsackTotal += data[i][2];
                totalProfit += data[i][1];
            }
            System.out.printf("Item with price: %.0f and weight: %.0f is %s\n", data[i][1], data[i][2], knapsack);
        }
        System.out.println(Arrays.toString(greedy));
        System.out.printf("\nFirst knapsack total weight: %d\n", firstKnapsackTotal);  
        System.out.printf("Second knapsack total weight: %d\n", secondKnapsackTotal);
        System.out.printf("Total profit in knapsacks: %d\n", totalProfit);
    }
}
