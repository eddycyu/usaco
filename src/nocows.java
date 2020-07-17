/*
ID: eddycyu
LANG: JAVA
TASK: nocows
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * https://gist.github.com/shahril96/c6d9b4f447cd6e015fe4f546ba514306
 */
public class nocows {

    public static void main(String[] args) throws IOException {

        try (final BufferedReader f = new BufferedReader(new FileReader("nocows.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("nocows.out")))) {

            final String line = f.readLine();
            final String[] tokens = line.split(" ");
            final int N = Integer.parseInt(tokens[0]);
            final int K = Integer.parseInt(tokens[1]);

            // trees[n][k] = the number of trees with node count (n) and height (k)
            int[][] trees = new int[200 + 1][100 + 1];

            /*
             * use dynamic programming to calculate number of pedigrees
             *
             * n,k
             * ---
             * 0,0 = 0
             * 1,k = 1
             * 2,k = 0
             *
             * 3,1 = 1,0 * 1,0 = 0 * 0 = 0
             * 3,2 = 1,1 * 1,1 = 1 * 1 = 1
             * 3,3 = 1,2 * 1,2 = 0 * 0 = 0
             * ...
             * 3,k = 1,k-1 * 1,k-1 = 0
             *
             * 4,1 = (1,0 * 2,0) + (2,0 * 1,0) = (0 * 0) + (0 * 0) = 0
             * 4,2 = (1,1 * 2,1) + (2,1 * 1,1) = (1 * 0) + (0 * 1) = 0
             * 4,3 = (1,2 * 2,2) + (2,2 * 1,2) = (1 * 0) + (1 * 0) = 0
             * ...
             * 4,k = (1,k-1 * 2,k-1) + (2,k-1 * 1,k-1) = 0
             *
             * 5,1 = (1,0 * 3,0) + (2,0 * 2,0) + (3,0 * 1,0) = (1 * 0) + (0 * 0) + (0 * 0) = 0
             * 5,2 = (1,1 * 3,1) + (2,1 * 2,1) + (3,1 * 1,1) = (1 * 0) + (0 * 0) + (0 * 0) = 0
             * 5,3 = (1,2 * 3,2) + (2,2 * 2,2) + (3,2 * 1,2) = (1 * 1) + (0 * 0) + (1 * 1) = 2
             * ...
             */
            for (int n = 1; n <= N; n++) {
                for (int k = 1; k <= K; k++) {
                    if (n == 1) {
                        trees[n][k] = 1;
                    } else {
                        for (int m = 1; m <= n - 2; m++) {
                            /*
                             * m = number of nodes in left subtree
                             * n-m-1 = number of nodes in right subtree
                             *
                             * sum up while iterating from 1 node on left subtree (and rest in right subtree)
                             * until 1 node in right subtree (and rest in left subtree)
                             */
                            trees[n][k] += trees[m][k - 1] * trees[n - m - 1][k - 1];
                            trees[n][k] %= 9901;
                        }
                    }
                }
            }

            // output result
            int result = (trees[N][K] - trees[N][K - 1] + 9901) % 9901;
            out.println(result);
        }
    }
}
