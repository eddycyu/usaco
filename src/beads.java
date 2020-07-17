/*
ID: eddycyu
LANG: JAVA
TASK: beads
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class beads {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("beads.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("beads.out")))) {

            final int RED = 0;
            final int BLUE = 1;

            // number of beads in necklace
            final int N = Integer.parseInt(f.readLine());

            // string of N characters representing the necklace, each of which is r, b, or w
            final String necklace = f.readLine();

            // concatenate two copies of the necklace to simulate a circular necklace in linear format
            final String s = necklace + necklace;
            final char[] a = s.toCharArray();

            // compute left side using dynamic programming
            int[][] left = new int[N * 2][2];
            left[0][RED] = 0;       // populate seed value for RED
            left[0][BLUE] = 0;      // populate seed value for BLUE
            for (int i = 1; i < N * 2; i++) {
                if (a[i - 1] == 'r') {
                    left[i][RED] = left[i - 1][RED] + 1;
                    left[i][BLUE] = 0;
                } else if (a[i - 1] == 'b') {
                    left[i][BLUE] = left[i - 1][BLUE] + 1;
                    left[i][RED] = 0;
                } else {
                    // increment for both since 'w' can be either RED or BLUE
                    left[i][RED] = left[i - 1][RED] + 1;
                    left[i][BLUE] = left[i - 1][BLUE] + 1;
                }
            }

            // compute right side using dynamic programming
            int[][] right = new int[(N * 2) + 1][2];
            right[2 * N][RED] = 0;  // populate seed value for RED
            right[2 * N][BLUE] = 0; // populate seed value for BLUE
            for (int i = (N * 2) - 1; i >= 0; i--) {
                if (a[i] == 'r') {
                    right[i][RED] = right[i + 1][RED] + 1;
                    right[i][BLUE] = 0;
                } else if (a[i] == 'b') {
                    right[i][BLUE] = right[i + 1][BLUE] + 1;
                    right[i][RED] = 0;
                } else {
                    // increment for both since 'w' can be either RED or BLUE
                    right[i][RED] = right[i + 1][RED] + 1;
                    right[i][BLUE] = right[i + 1][BLUE] + 1;
                }
            }

            // find the maximum value for (left[i] + right[i])
            int max = 0;
            for (int i = 0; i < N * 2; i++) {
                max = Math.max(max,
                        Math.max(left[i][RED], left[i][BLUE])
                                + Math.max(right[i][RED], right[i][BLUE]));
            }

            // check for case where necklace is of a single color, in which
            // case the max length would be N
            max = Math.min(N, max);

            // output
            out.println(max);
        }
    }
}
