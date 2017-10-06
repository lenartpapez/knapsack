import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //Single Knapsack Problem
        SingleKnapsack single = new SingleKnapsack();
        MultiKnapsack multi = new MultiKnapsack();

        //input number of testcases
        int testcase = 0;
        System.out.print("Number of testcases (usually 1): ");
        if(sc.hasNext()) {
            testcase = Integer.parseInt(sc.next());
        }

        //play game "testcase" times.
        while(testcase-->0) {
//            single.playSingleKnapsackGame();
            multi.playMultiKnapsackGame();
        }
    }
}
