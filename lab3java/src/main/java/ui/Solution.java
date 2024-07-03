package ui;

public class Solution {
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Invalid number of arguments!");
            return;
        }
        DecisionTree decisionTree = new DecisionTree(args);
        System.out.println(decisionTree.printResult());

    }
}
