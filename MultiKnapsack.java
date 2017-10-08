import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;
import java.util.Random;


/**
 * Created by HAN on 2017. 9. 29..
 */
public class MultiKnapsack {

    int endProfit;
    Scanner sc;
    Knapsack kn;
    int maxWei[], numProduct, numKnapsack;
    double[][] data;

    public MultiKnapsack(){
        endProfit = 0;
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

    public ArrayList<int[]> generateNeighbors(int[] vector) {
        ArrayList<int[]> neighbors = new ArrayList<>();
        for(int i = 0; i < numProduct - 1; i++) {
            for(int j = 0; j < numProduct; j++) {
                int[] vcopy = new int[numProduct];
                System.arraycopy(vector, 0, vcopy, 0, numProduct);
                if(vcopy[i] == 0 && vcopy[j] == 1) {
                    int temp = vcopy[i];
                    vcopy[i] = vcopy[j];
                    vcopy[j] = temp;
                    if(!neighbors.contains(vcopy))
                        neighbors.add(vcopy);
                } else if(vcopy[i] == 1 && vcopy[j] == 0) {
                    int temp = vcopy[i];
                    vcopy[i] = vcopy[j];
                    vcopy[j] = temp;
                    if(!neighbors.contains(vcopy))
                        neighbors.add(vcopy);
                }
            }
        }
        return neighbors;
    }

    public int[] calculateProfit(int[] vector) {
        int[] profits = new int[3];
        for(int i = 0 ; i < numProduct; i++) {
            if(vector[i] == 0) {
                profits[0] += data[i][1];
                profits[1] += data[i][2];
            } else if(vector[i] == 1) {
                profits[0] += data[i][1];
                profits[2] += data[i][2];
            }
        }
        return profits;
    }


    public boolean feasible(int[] vector, int totalProfit) {
        int profit = 0;
        int firstKnapsackTotal = 0;
        int secondKnapsackTotal = 0;
        for(int i = 0 ; i < numProduct; i++) {
            if(vector[i] == 0) {
                firstKnapsackTotal += data[i][2];
                profit += data[i][1];
            } else if(vector[i] == 1) {
                secondKnapsackTotal += data[i][2];
                profit += data[i][1];
            } else {

            }
        }

        if(firstKnapsackTotal <= maxWei[0] && secondKnapsackTotal <= maxWei[1] &&
         profit >= totalProfit) return true;
        return false;
    }


    public void playMultiKnapsackGame(){
        //PART 1 : GET INPUT
        inputData();

        //PART2 : Sort by Value/Weight
        Arrays.sort(data, new Comparator<double[]>() {
            @Override
            public int compare(final double[] one, final double[] two) {
                final double ratio1 = one[0];
                final double ratio2 = two[0];
                if(ratio1 < ratio2) {
                    return 1;
                } else if(ratio1 > ratio2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });


        //PART3 : Get Initial State from Greedy Algorighm.
        int greedy[] = greedy(data, numProduct, numKnapsack, maxWei);
        endProfit = calculateProfit(greedy)[0];
        System.out.println("\nProfits after just greedy: " + Arrays.toString(calculateProfit(greedy)));
        int firstKnapsackTotal = calculateProfit(greedy)[1];
        int secondKnapsackTotal = calculateProfit(greedy)[2];
        System.out.println();
        ArrayList<int[]> neighbors = generateNeighbors(greedy);
        for(int[] n : neighbors) {
            if(feasible(n, endProfit)) {
                greedy = n;
                endProfit = calculateProfit(n)[0];
                firstKnapsackTotal = calculateProfit(greedy)[1];
                secondKnapsackTotal = calculateProfit(greedy)[2];
                for(int i = 0 ; i < numProduct; i++) {
                    if(greedy[i] == -1) {
                        if(firstKnapsackTotal + data[i][2] <= maxWei[0]) {
                            firstKnapsackTotal += data[i][2];
                            endProfit += data[i][1];
                            greedy[i] = 0;
                        } else if(secondKnapsackTotal + data[i][2] <= maxWei[1]) {
                            secondKnapsackTotal += data[i][2];
                            endProfit += data[i][1];
                            greedy[i] = 1;
                        }
                    }
                }
            }
        }

        System.out.println("Final choice after neighborhood search: " + Arrays.toString(greedy) + "\n");
        for(int i = 0 ; i < numProduct; i++) {
            String knapsack = "";
            if(greedy[i] == 1) {
                knapsack = "in knapsack 2";
            } else if(greedy[i] == -1) {
                knapsack = "not in any knapsacks";
            } else {
                knapsack = "in knapsack 1";
            }
            System.out.printf("Item with price: %.0f and weight: %.0f is %s\n", data[i][1], data[i][2], knapsack);
        }
        System.out.printf("\nTotal profit in knapsacks: %d\n", endProfit);
        System.out.printf("First knapsack total weight: %d\n", firstKnapsackTotal);  
        System.out.printf("Second knapsack total weight: %d\n", secondKnapsackTotal);
    }
}
