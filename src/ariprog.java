/*
ID: eddycyu
LANG: JAVA
TASK: ariprog
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ariprog {

    public static void main(String[] args) throws IOException {

        try (final BufferedReader f = new BufferedReader(new FileReader("ariprog.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ariprog.out")))) {

            // length of arithmetic progression to find
            final int N = Integer.parseInt(f.readLine());

            // upper bound to limit the search to the bisquares
            final int M = Integer.parseInt(f.readLine());

            // find all possible bisquares for allowed values in arithmetic progression
            final boolean[] bisquares = getBisquares(M);

            // maximum bisquare value, which will be the upper bound for 'a' and 'b' in the
            // arithmetic progression equation (a, a+b, a+2b, ..., a+nb)
            final int maxBisquare = M * M + M * M;

            // search for valid arithmetic progressions
            final List<Result> results = getResults(N, maxBisquare, bisquares);

            // sort results
            Collections.sort(results);

            // output results
            if (results.size() == 0) {
                out.println("NONE");
            } else {
                for (Result result : results) {
                    out.print(result.first);
                    out.print(" ");
                    out.print(result.diff);
                    out.println();
                }
            }
        }
    }

    /**
     * Find all possible bisquares (p*p + q*q).
     *
     * @param M The upper bound for 'p' and 'q'.
     * @return Array of all possible bisquares.
     */
    private static boolean[] getBisquares(int M) {
        final boolean[] result = new boolean[M * M + M * M + 1];
        for (int p = 0; p <= M; p++) {
            for (int q = 0; q <= M; q++) {
                result[(p * p) + (q * q)] = true;
            }
        }
        return result;
    }

    /**
     * Get the results for all valid arithmetic progressions of length N whose
     * values are in the set of bisquares.
     *
     * @param N           The length of the arithmetic progression.
     * @param maxBisquare The upper bound for 'a' and 'b'.
     * @param bisquares   The set of bisquares.
     * @return List of results, which contain the first element and the
     * difference between consecutive elements in a valid progression.
     */
    private static List<Result> getResults(int N, int maxBisquare, boolean[] bisquares) {
        final List<Result> results = new ArrayList<>();
        for (int a = 0; a <= maxBisquare / 2; a++) {
            for (int b = 1; b <= maxBisquare / 2; b++) {
                // optimization: eliminate loops that would result in
                // invalid arithmetic progression
                int lastValue = (a + ((N - 1) * b));
                if ((lastValue > maxBisquare)) {
                    break;
                } else if (!bisquares[lastValue]) {
                    continue;
                }
                final int[] progression = getProgression(N, a, b, bisquares);
                // a value of zero for the last element in the sequence would
                // indicate that this is not a valid arithmetic progression
                if (progression[N - 1] != 0) {
                    final int first = progression[0];
                    final int diff = progression[1] - progression[0];
                    results.add(new Result(first, diff));
                }
                // else discard the invalid progression
            }
        }
        return results;
    }

    /**
     * Get arithmetic progression (a + nb) where values are in the set of bisquares.
     *
     * @param N         The length of the arithmetic progression.
     * @param a         The starting 'a' value.
     * @param b         The starting 'b' value.
     * @param bisquares The set of bisquares.
     * @return Arithmetic progression of length N where all values are in the set
     * of bisquares, else an incomplete arithmetic progress of length less than N
     */
    private static int[] getProgression(int N, int a, int b, boolean[] bisquares) {
        final int[] progression = new int[N];
        for (int n = 0; n < N; n++) {
            int value = a + (n * b);
            if (bisquares[value]) {
                progression[n] = value;
            } else {
                // optimization: terminate loop and return incomplete progression
                break;
            }
        }
        return progression;
    }

    public static class Result implements Comparable<Result> {
        private int first;
        private int diff;

        Result(int first, int diff) {
            super();
            this.first = first;
            this.diff = diff;
        }

        @Override
        public int compareTo(Result other) {
            if (diff < other.diff) {
                return -1;
            } else if (diff > other.diff) {
                return 1;
            } else {
                if (first < other.first) {
                    return -1;
                } else if (first > other.first) {
                    return 1;
                }
                return 0;
            }
        }
    }
}
