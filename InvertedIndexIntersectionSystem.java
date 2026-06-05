package elasticsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvertedIndexIntersectionSystem {

    /**
     * Executes a two-pointer merge-style intersection on two sorted arrays.
     * Time Complexity: O(|a| + |b|)
     */
    public static List<Integer> intersectTwoLists(int[] a, int[] b) {
        List<Integer> out = new ArrayList<>();
        int i = 0, j = 0;

        // Linear sweep through both lists using two independent pointers
        while (i < a.length && j < b.length) {
            if (a[i] == b[j]) {
                out.add(a[i]);
                i++;
                j++;
            } else if (a[i] < b[j]) {
                i++; // Advance pointer in list 'a' since its current value is too small
            } else {
                j++; // Advance pointer in list 'b' since its current value is too small
            }
        }
        return out;
    }

    /**
     * Intersects multiple posting lists using the optimal Shortest-List-First policy.
     */
    public static List<Integer> intersectMultiTermQuery(int[][] postingLists) {
        if (postingLists == null || postingLists.length == 0) {
            return new ArrayList<>();
        }

        // 1. Sort the input posting lists based on their length (Shortest-List-First Optimization)
        System.out.println("\n[Optimizer] Analyzing list lengths for query planning...");
        Arrays.sort(postingLists, (list1, list2) -> Integer.compare(list1.length, list2.length));

        for (int k = 0; k < postingLists.length; k++) {
            System.out.println(" -> List " + k + " length: " + postingLists[k].length + " elements.");
        }

        // 2. Convert the initial shortest primitive array to an Integer list to hold intermediate results
        List<Integer> intermediateResult = new ArrayList<>();
        for (int val : postingLists[0]) {
            intermediateResult.add(val);
        }

        // 3. Iteratively intersect intermediate results with subsequent sorted posting lists
        for (int k = 1; k < postingLists.length; k++) {
            // Convert intermediate List back to a primitive array for optimized execution
            int[] intermediateArray = intermediateResult.stream().mapToInt(Integer::intValue).toArray();
            int[] nextTargetList = postingLists[k];

            System.out.println("\n[Executing Interaction Step " + k + "]");
            System.out.println("  Intermediate List: " + intermediateResult);
            System.out.println("  Next Target List:  " + Arrays.toString(nextTargetList));

            intermediateResult = intersectTwoLists(intermediateArray, nextTargetList);
        }

        return intermediateResult;
    }

    public static void main(String[] args) {
        // Miniature sample datasets from the Elasticsearch Case Study
        int[] listMachine = {1, 4, 7, 12, 15, 20, 24, 30};
        int[] listLearning = {2, 4, 8, 12, 18, 24, 30};
        int[] listTutorial = {4, 6, 9, 12, 17, 24, 30, 35};

        System.out.println("=== Starting Elasticsearch Query Planner Simulation ===");

        // Grouping posting lists for multi-term evaluation: 'machine AND learning AND tutorial'
        int[][] queryPostingLists = {listMachine, listLearning, listTutorial};

        // Execute query intersection
        List<Integer> finalMatchedDocuments = intersectMultiTermQuery(queryPostingLists);

        // Print final matched output results
        System.out.println("\n=======================================================");
        System.out.println("Final Document Match Intersection: " + finalMatchedDocuments);
        System.out.println("Total Matching Documents Found:    " + finalMatchedDocuments.size());
        System.out.println("=======================================================");
    }
}
