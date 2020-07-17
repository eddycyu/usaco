/*
ID: eddycyu
LANG: JAVA
TASK: holstein
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class holstein {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("holstein.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("holstein.out")))) {

            // number of types of vitamins
            final int V = Integer.parseInt(f.readLine());

            // minimum requirement for each vitamin
            final int[] minimum = new int[V];
            final String[] tokens = f.readLine().split(" ");
            for (int i = 0; i < V; i++) {
                minimum[i] = Integer.parseInt(tokens[i]);
            }

            // number of types of feeds
            final int G = Integer.parseInt(f.readLine());

            // amount of each vitamin found in one scoop of each feed
            final int[][] feeds = new int[G][V];
            for (int g = 0; g < G; g++) {
                final Scanner scanner = new Scanner(f.readLine());
                for (int v = 0; v < V; v++) {
                    feeds[g][v] = scanner.nextInt();
                }
                scanner.close();
            }

            // find solution
            final Set<Integer> seen = new HashSet<>();
            final List<Result> results = new ArrayList<>();
            for (int i = 0; i < G; i++) {
                final int[] scoops = new int[G];
                final int[] amounts = new int[V];
                addScoop(i, scoops, amounts, feeds);
                findScoops(scoops, amounts, minimum, feeds, G, seen, results);
            }

            // output result
            final Result bestResult = results.get(results.size() - 1);
            out.print(bestResult.scoopCount);
            for (int i = 0; i < bestResult.scoops.length; i++) {
                if (bestResult.scoops[i] == 1) {
                    out.print(" " + (i + 1));
                }
            }
            out.println();
        }
    }

    private static boolean findScoops(int[] scoops, int[] amounts, int[] minimum,
                                      int[][] feeds, int feedCount,
                                      Set<Integer> seen, List<Result> results) {
        if (isMeetsMinimum(amounts, minimum)) {
            final Result result = new Result(scoops, amounts);
            results.add(result);
            return true; // found a solution
        }

        // optimization: try adding another scoop, but only if we don't
        // already have an answer with fewer scoops
        int scoopCount = getScoopCount(scoops);
        int minimumScoopCount = getMinimumScoopCount(results);
        if ((minimumScoopCount > 0) && (scoopCount == minimumScoopCount)) {
            return true; // already found a better solution previously
        }

        for (int i = 0; i < feedCount; i++) {
            final int[] newScoops = Arrays.copyOf(scoops, scoops.length);
            final int[] newAmounts = Arrays.copyOf(amounts, amounts.length);
            if (newScoops[i] != 1) {
                addScoop(i, newScoops, newAmounts, feeds);
                // optimization: make sure we didn't already compute this combination before
                final int newScoopKey = scoopsToKey(newScoops);
                if (!seen.contains(newScoopKey)) {
                    seen.add(newScoopKey);
                    // check if the new scoop combo is a solution
                    if (findScoops(newScoops, newAmounts, minimum, feeds, feedCount, seen, results)) {
                        // found solution; no need to try next combo in this loop
                        break;
                    }
                }
            }
        }
        return false; // keep looking for a solution
    }

    private static void addScoop(int index, int[] scoops, int[] amounts, int[][] feeds) {
        scoops[index] = 1;
        for (int i = 0; i < amounts.length; i++) {
            amounts[i] += feeds[index][i];
        }
    }

    private static boolean isMeetsMinimum(int[] amounts, int[] minimum) {
        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] < minimum[i]) {
                return false;
            }
        }
        return true;
    }

    private static int getScoopCount(int[] scoops) {
        int scoopCount = 0;
        for (int scoop : scoops) {
            if (scoop == 1) {
                scoopCount++;
            }
        }
        return scoopCount;
    }

    private static int getMinimumScoopCount(List<Result> results) {
        if (results.isEmpty()) {
            return 0;
        }
        Result bestResult = results.get(results.size() - 1);
        return bestResult.scoopCount;
    }

    private static int scoopsToKey(int[] scoops) {
        int key = 0;
        for (int i = 0; i < scoops.length; i++) {
            if (scoops[i] == 1) {
                key = key | (1 << i);
            }
        }
        return key;
    }

    static class Result {
        int[] scoops;
        int[] amount;
        int scoopCount;

        private Result() {
            super();
        }

        Result(int[] scoops, int[] amount) {
            this();
            this.scoops = scoops;
            this.amount = amount;
            for (int scoop : scoops) {
                if (scoop == 1) {
                    scoopCount++;
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!Result.class.isAssignableFrom(obj.getClass())) {
                return false;
            }
            final Result other = (Result) obj;
            return Arrays.equals(this.scoops, other.scoops);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            for (int scoop : scoops) {
                hash = 53 * hash + scoop;
            }
            return hash;
        }
    }
}
