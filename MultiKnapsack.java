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
    int[] actualweights;

    public MultiKnapsack(){
        endProfit = 0;
        sc = new Scanner(System.in);
        kn = new Knapsack();
        maxWei = new int[100];
        numProduct = 0;
        numKnapsack = 0;
        data = new double[100][3];
        actualweights = new int[100];

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
                actualweights[i] = 0;
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

    void printData(){
        for(int i = 0 ; i < numProduct; i++){
            System.out.printf("[%d] %f. price %f, weight %f\n",i, data[i][0],data[i][1],data[i][2]);
        }
    }

    int[] greedy2(double data[][], int num, int numKnapsack, int[] maxWei, int start){
        // put item first in "start" knapsack
        // ret notates which product is in which knapsack. -1 means not included in any.
        int ret[] = new int[numProduct];
        int retIter[] = new int[numProduct];
        for(int i = 0 ; i < numProduct; i++) ret[i] = -1;

        double weight[] = new double[numKnapsack];
//        System.out.println("init weight");
//        System.out.println(Arrays.toString(weight));
        //pack things that have high val/wieght
        while(true) {
            for (int i = 0, j = start; i < num; i++) {
                if(ret[i] != -1) continue;
                int k = j;
                do{
                    // System.out.println("k : "+k);
                    if(weight[k] + data[i][2] <= maxWei[k]){
                        weight[k] += data[i][2];
                        ret[i] = k;
                        break;
                    }
                    k++;
                    if(k>=numKnapsack) k%=numKnapsack;
                }while(k!=j);
            }
            if(Arrays.equals(ret, retIter)) break;
            System.arraycopy( ret, 0, retIter, 0, ret.length );
        }

        return ret;
    }

//*** original greedy
    int[] greedy(double data[][], int num, int numKnapsack, int[] maxWei){
        // ret notates which product is in which knapsack. -1 means not included in any.
        int ret[] = new int[numProduct];
        int retIter[] = new int[numProduct];
        for(int i = 0 ; i < numProduct; i++) ret[i] = -1;

        double weight[] = new double[numKnapsack];
        //pack things that have high val/wieght
        while(true) {
            for (int i = 0, j = 0; i < num; i++) {
                if(ret[i] != -1) continue;
                int k = j;
                do{
                    if(weight[k] + data[i][2] <= maxWei[k]){
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
        for(int i = 0; i < numProduct; i++) {
            for(int j = 1; j < numProduct; j++) {
                int[] vcopy = new int[numProduct];
                System.arraycopy(vector, 0, vcopy, 0, numProduct);
                int temp = vcopy[i];
                vcopy[i] = vcopy[j];
                vcopy[j] = temp;
                if(!neighbors.contains(vcopy))
                    neighbors.add(vcopy);
            }
        }
        return neighbors;
    }

    public int[] neighborSearch(int[] vector){
        System.out.println("searching..."+Arrays.toString(vector));
        System.out.println(calculateProfit(vector)[0]);

        ArrayList<int[]> neighbors = generateNeighbors(vector);
        endProfit = calculateProfit(vector)[0];
        for(int[] n: neighbors){
            if(feasable(n, endProfit)){
                return neighborSearch(n);
            }
        }
        System.out.println("return..."+Arrays.toString(vector));
        return vector;
    }

    public int[] calculateProfit(int[] vector) {
        int[] profits = new int[numKnapsack + 1];
        for(int i = 0 ; i < numProduct; i++) {
            if(vector[i] != -1) {
                profits[0] += data[i][1];
                profits[vector[i]+1] += data[i][2];
            }
        }
        return profits;
    }


    public boolean feasable(int[] vector, int totalProfit) {
        for(int i = 0 ; i < numKnapsack; i++) {
            actualweights[i] = 0;
        }
        int profit = 0;
        for(int i = 0 ; i < numProduct; i++) {
            if(vector[i] != -1) {
                actualweights[vector[i]] += data[i][2];
                profit += data[i][1];
            }
        }

        for(int i = 0 ; i < numKnapsack; i++) {
            if(actualweights[i] > maxWei[i]) return false;
        }

        if(profit > totalProfit) return true;
        return false;
    }

    public int[] shuffleKnapsacks(int[] maxWei) {
        int knaps[] = new int[maxWei.length];
        int j = numKnapsack - 1;
        for(int i = 0; i < j; i++) {
            int temp = maxWei[i];
            knaps[i] = maxWei[j];
            knaps[j] = temp;
            j--;
        }
        return knaps;
    }

    void shuffleData() {
        Random random = new Random();

        for (int i = numProduct - 1; i > 0; i--) {
            int m = random.nextInt(i + 1);
            double temp[] = data[i];
            data[i] = data[m];
            data[m] = temp;
        }
    }
    public void playGreedyMultiKnapsackGame(){
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

        //PART3 : first with greedy
        int ret[] = greedy(data, numProduct, numKnapsack, maxWei);
        int maxProfit = calculateProfit(ret)[0];
        System.out.println("After Greedy: "+Arrays.toString(ret)+" "+maxProfit);
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
        printData();

        //PART3 : first with greedy
        int greedy[] = greedy(data, numProduct, numKnapsack, maxWei);
        int ret[] = neighborSearch(greedy);
        int maxProfit = calculateProfit(ret)[0];
        System.out.println("After Greedy & Neighbor : "+Arrays.toString(ret)+" "+maxProfit);


        //PART4 : several times with shuffled data
        for(int i = 0 ; i < 10; i++){
            System.out.println("Shuffle ["+i+"]");
            shuffleData();
            // printData();
            int greedyShuffle[] = greedy(data, numProduct, numKnapsack, maxWei);
            int retShuffle[] = neighborSearch(greedyShuffle);
            int shuffleProfit = calculateProfit(retShuffle)[0];
            if(maxProfit < shuffleProfit){
                maxProfit = shuffleProfit;
                System.arraycopy(ret, 0, retShuffle, 0, retShuffle.length);
                System.out.println("RET changed  : "+Arrays.toString(ret)+" "+maxProfit);
            }
        }
        System.out.println("After 10 times of Shuffled Data : "+ Arrays.toString(ret)+" "+maxProfit);


        //** one with greedy2
        // for(int i = 0 ; i < numKnapsack; i++){
        //     int greedy[] = greedy2(data, numProduct, numKnapsack, maxWei, i);
        //     int tempRet[] = neighborSearch(greedy);
        //     int currentProfit = calculateProfit(tempRet)[0];
        //     System.out.println("["+i+"] "+Arrays.toString(ret)+" "+calculateProfit(ret)[0]);
        //
        //     if(currentProfit > maxProfit){
        //         System.arraycopy( tempRet, 0, ret, 0, ret.length );
        //         maxProfit = currentProfit;
        //     }
        //     System.out.println("current return : "+Arrays.toString(ret)+" "+calculateProfit(ret)[0]);
        // }
        // System.out.println("returned"+Arrays.toString(ret)+" "+calculateProfit(ret)[0]);
        //** END OF one with greedy2



        // ** initial state from greedy algorithm
        // int greedy[] = greedy(data, numProduct, numKnapsack, maxWei);
        // int knaps[] = shuffleKnapsacks(maxWei);
        // endProfit = calculateProfit(greedy)[0];
        // int greedy2[] = greedy(data, numProduct, numKnapsack, knaps);
        // int weights[] = new int[numKnapsack];
        // for(int i = 0; i < weights.length; i++) {
        //     weights[i] = calculateProfit(greedy)[i+1];
        // }
        // System.out.println();
        //
        // // first improvement
        // ArrayList<int[]> neighbors = generateNeighbors(greedy);
        // ArrayList<int[]> neighbors2 = generateNeighbors(greedy2);
        // for(int i = 0; i < Math.min(neighbors.size(), neighbors2.size()); i++) {
        //     int[] n = neighbors.get(i);
        //     int[] n2 = neighbors2.get(i);
        //     if(calculateProfit(n2)[0] > calculateProfit(n)[0]) {
        //         greedy2 = n;
        //         n = n2;
        //     }
        //     if(feasable(n, endProfit)) {
        //         greedy = n;
        //         endProfit = calculateProfit(n)[0];
        //         for(int j = 0; j < weights.length; j++) {
        //             weights[j] = calculateProfit(greedy)[j+1];
        //         }
        //         for(int j = 0 ; j < numProduct; j++) {
        //             if(greedy[j] == -1) {
        //                 for(int k = 0; k < numKnapsack; k++) {
        //                     if(weights[k] + data[j][2] <= maxWei[k]) {
        //                         weights[k] += data[j][2];
        //                         endProfit += data[j][1];
        //                         greedy[j] = 0;
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // }
        //
        // System.out.println("Profits after one improvement: " + Arrays.toString(calculateProfit(greedy)));
        //
        // // second improvement
        // neighbors = generateNeighbors(greedy);
        // neighbors2 = generateNeighbors(greedy2);
        // for(int i = 0; i < Math.min(neighbors.size(), neighbors2.size()); i++) {
        //     int[] n = neighbors.get(i);
        //     int[] n2 = neighbors2.get(i);
        //     if(calculateProfit(n2)[0] > calculateProfit(n)[0]) {
        //         greedy2 = n;
        //         n = n2;
        //     }
        //     if(feasable(n, endProfit)) {
        //         greedy = n;
        //         endProfit = calculateProfit(n)[0];
        //         for(int j = 0; j < weights.length; j++) {
        //             weights[j] = calculateProfit(greedy)[j+1];
        //         }
        //         for(int j = 0 ; j < numProduct; j++) {
        //             if(greedy[j] == -1) {
        //                 for(int k = 0; k < numKnapsack; k++) {
        //                     if(weights[k] + data[j][2] <= maxWei[k]) {
        //                         weights[k] += data[j][2];
        //                         endProfit += data[j][1];
        //                         greedy[j] = 0;
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // }
        //
        // System.out.println("\nProfits after second improvement: " + Arrays.toString(calculateProfit(greedy)));
        // System.out.println();

        // for(int i = 0 ; i < numProduct; i++) {
        //     String knapsack = "";
        //     if(greedy[i] != -1) {
        //         knapsack = String.format("in knapsack %d", greedy[i]+1);
        //     } else {
        //         knapsack = "not in any knapsacks";
        //     }
        //     System.out.printf("Item with price: %.0f and weight: %.0f is %s\n", data[i][1], data[i][2], knapsack);
        // }
        // System.out.printf("\nTotal profit in knapsacks: %d\n", endProfit);
        // int k = 0;
        // for(int i : weights) {
        //     System.out.printf("Knapsack %d weight: %d\n", k, i);
        //     k++;
        // }

    }
}
