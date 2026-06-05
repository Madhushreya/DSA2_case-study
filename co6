package cargo_optimization;

import java.util.*;

public class CargoKnapsackEngine {

    /**
     * Solves the 0/1 Knapsack problem using a 2D Dynamic Programming matrix.
     * @param weights Array of item weights in tons
     * @param values Array of item financial payout values in thousands
     * @param W Maximum weight capacity of the logistics truck
     * @return List of 1-based indices representing selected cargo consignments
     */
    public static List<Integer> knapsack01(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n + 1][W + 1];

        // 1. Build the dynamic programming lookup tables
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                // Skip item case
                dp[i][w] = dp[i - 1][w];
                
                // Take item case if it fits within current weight bounds
                if (weights[i - 1] <= w) {
                    int takeValue = dp[i - 1][w - weights[i - 1]] + values[i - 1];
                    if (takeValue > dp[i][w]) {
                        dp[i][w] = takeValue;
                    }
                }
            }
        }

        System.out.println("Computed Global Maximum Cargo Profit Value: ₹" + dp[n][W] + "k");

        // 2. Back-trace path recovery module to identify chosen components
        List<Integer> chosen = new ArrayList<>();
        int w = W;
        for (int i = n; i >= 1; i--) {
            // Check if the value changed from the previous item row
            if (dp[i][w] != dp[i - 1][w]) {
                chosen.add(i); // Record the 1-based index identity
                w -= weights[i - 1]; // Reduce remaining weight capacity
            }
        }
        
        // Reverse the list to restore chronological insertion order
        Collections.reverse(chosen);
        return chosen;
    }

    public static void main(String[] args) {
        // Data arrays mapped from the cargo problem specification
        String[] labels = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int[] weights = {5, 8, 3, 10, 4, 6, 7, 2};
        int[] values  = {40, 50, 20, 70, 30, 35, 45, 15};
        int maxTruckCapacity = 24;

        System.out.println("=== BANGALORE-TO-MUMBAI TRUCK ROUTING ENGINE ===");
        List<Integer> optimalCargoList = knapsack01(weights, values, maxTruckCapacity);

        System.out.println("\nSelected Cargo Consignments Details:");
        int totalWeight = 0;
        int totalValue = 0;
        
        for (int index : optimalCargoList) {
            int actualIdx = index - 1;
            System.out.println("  Consignment " + labels[actualIdx] + " -> Weight: " 
                               + weights[actualIdx] + " tons, Value: ₹" + values[actualIdx] + "k");
            totalWeight += weights[actualIdx];
            totalValue += values[actualIdx];
        }
        
        System.out.println("\nFinal Manifest Validation Metrics:");
        System.out.println("  Total Manifest Weight Allocation: " + totalWeight + " / " + maxTruckCapacity + " tons");
        System.out.println("  Total Manifest Financial Yield: ₹" + totalValue + "k");
    }
}
