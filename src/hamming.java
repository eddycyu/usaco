/*
ID: eddycyu
LANG: JAVA
TASK: hamming
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class hamming {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("hamming.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("hamming.out")))) {

            // read input
            final String[] tokens = f.readLine().split(" ");
            final int N = Integer.parseInt(tokens[0]);
            final int B = Integer.parseInt(tokens[1]);
            final int D = Integer.parseInt(tokens[2]);
            final int maxCodewordValue = (int) Math.pow(2, B) - 1;

            // find solution
            final int[] codewords = new int[maxCodewordValue + 1];
            for (int i = 0; i < codewords.length; i++) {
                codewords[i] = i;
            }
            for (int i = 0; i < codewords.length; i++) {
                final List<Integer> result = new ArrayList<>();
                result.add(codewords[i]);
                findSolution(codewords[i], Arrays.copyOfRange(codewords, i + 1, codewords.length), N, B, D, result);
                // we're guaranteed to find a solution; print the first one we find
                if (result.size() >= N) {
                    for (int j = 0; j < result.size(); j++) {
                        out.print(result.get(j));
                        if (((j + 1) % 10 == 0) || (j == result.size() - 1)) {
                            out.println();
                        } else if (j < result.size() - 1) {
                            out.print(" ");
                        }
                    }
                    break;
                }
            }
        }
    }

    private static void findSolution(int baseCodeword, int[] codewords,
                                     int maxCodewords, int maxBits, int hammingDistance,
                                     List<Integer> result) {
        final int[] subset = new int[codewords.length];
        Arrays.fill(subset, -1);

        // filter out the codewords (into subset) that are hamming distance
        // from baseCodeword
        int index = 0;
        for (int codeword : codewords) {
            if (isHamming(baseCodeword, codeword, maxBits, hammingDistance)) {
                subset[index++] = codeword;
            }
        }

        // if subset is not empty, then add the first value from subset into result
        if (subset[0] != -1) {
            result.add(subset[0]);
            // optimization: don't need to find more than required solution size
            if (result.size() == maxCodewords) {
                return;
            }
        }

        // continue recursion
        if (subset[1] != -1) {
            findSolution(subset[0], Arrays.copyOfRange(subset, 1, index),
                    maxCodewords, maxBits, hammingDistance, result);
        }
    }

    /**
     * Check if codewordA is hamming distance from codewordB.
     */
    private static boolean isHamming(int codewordA, int codewordB, int maxBits, int hammingDistance) {
        int count = 0;
        for (int i = 0; i < maxBits; i++) {
            final int bit = 1 << i;
            final boolean a = (codewordA & bit) == bit;
            final boolean b = (codewordB & bit) == bit;
            if ((a && !b) || (!a && b)) {
                if (++count == hammingDistance) {
                    return true;
                }
            }
        }
        return false;
    }
}
