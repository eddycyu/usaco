/*
ID: eddycyu
LANG: JAVA
TASK: numtri
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class numtri {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("numtri.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("numtri.out")))) {

            // number of rows
            final int R = Integer.parseInt(f.readLine());

            // construct triangle; [0][0] is upper left corner
            final int[][] triangle = new int[R][R];
            for (int i = 0; i < R; i++) {
                final String line = f.readLine();
                final String[] tokens = line.split(" ");
                for (int j = 0; j < tokens.length; j++) {
                    triangle[i][j] = Integer.parseInt(tokens[j]);
                }
            }

            // initialize values in sums to -1
            final int[][] sums = new int[R][R];
            for (int row = 0; row < sums.length; row++) {
                for (int col = 0; col < sums.length; col++) {
                    sums[row][col] = -1;
                }
            }

            // find max sum
            sum(0, 0, triangle, sums);

            // output result
            out.println(sums[0][0]);
        }
    }

    private static int sum(int row, int col, int[][] triangle, int[][] sums) {
        // special case for last row of triangle
        if (row == (triangle.length - 1)) {
            // the sum is just the value in the triangle
            setSum(row, col, triangle[row][col], sums);
            return sums[row][col];
        }

        // check if sum is already calculated for this node at [row][col];
        // if so (e.g. not -1), then we already know the maximum sum for this
        // node; there is no need to calculate it again;
        //
        // this is an important optimization to eliminate duplicate calculations
        // that have already been performed; otherwise it will take too long to
        // compute when you have a large number of rows
        final int nodeSum = sums[row][col];
        if (nodeSum != -1) {
            return nodeSum;
        }

        // keep traversing down the rows to find the maximum sum
        setSum(row, col, triangle[row][col] + sum(row + 1, col, triangle, sums), sums);
        setSum(row, col, triangle[row][col] + sum(row + 1, col + 1, triangle, sums), sums);

        // return the maximum sum for this node at [row][col]
        return sums[row][col];
    }

    private static void setSum(int row, int col, int sum, int[][] sums) {
        // only save the largest sum
        if (sum > sums[row][col]) {
            sums[row][col] = sum;
        }
    }
}
