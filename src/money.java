/*
ID: eddycyu
LANG: JAVA
TASK: money
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class money {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("money.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("money.out")))) {
            String[] tokens = f.readLine().split(" ");
            final int V = Integer.parseInt(tokens[0]);  // number of coins in the system
            final int N = Integer.parseInt(tokens[1]);  // amount of money to construct

            // parse coin values
            final int[] coins = new int[V];
            int v = 0;
            String line;
            while ((v < V) && (line = f.readLine()) != null) {
                tokens = line.split(" ");
                for (String token : tokens) {
                    coins[v++] = Integer.parseInt(token);
                }
            }

            // compute using dynamic programming
            final long count = solve(N, coins);

            // output
            out.println(count);
        }
    }

    private static long solve(int N, int[] coins) {
        // the number of different ways in which to get a sum of N
        final long[] ways = new long[N + 1];

        // base case: the number of different ways in which to get a
        // sum of 0 is always 1
        ways[0] = 1;

        // loop over every coin value
        for (int coin : coins) {
            // loop over every sum value
            for (int n = 1; n < ways.length; n++) {
                if (n >= coin) {
                    ways[n] += ways[n - coin];
                }
            }
        }

        return ways[N];
    }
}
