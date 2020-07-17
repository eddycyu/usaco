/*
ID: eddycyu
LANG: JAVA
TASK: concom
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class concom {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("concom.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("concom.out")))) {

            // the number of input triples
            final int N = Integer.parseInt(f.readLine());

            // parse triples ([i][j]=p)
            final int[][] triples = new int[100 + 1][100 + 1];
            final boolean[][] owns = new boolean[100 + 1][100 + 1];
            String[] tokens;
            int i, j, p;
            for (int n = 0; n < N; n++) {
                tokens = f.readLine().split(" ");
                i = Integer.parseInt(tokens[0]);
                j = Integer.parseInt(tokens[1]);
                p = Integer.parseInt(tokens[2]);
                triples[i][j] = p;
                // handles if company A owns more than 50% of company B
                if (triples[i][j] > 50) {
                    owns[i][j] = true;
                }
            }

            // works for test 1,2,3,4,5,6,7 but fails test 8
            // if i own > 50% of j, then find everything that j owns (k) and have i also own it (k)
            // and if j owns > 50% of k, then find everything that k owns (l) and have i also own it (l)
            for (i = 1; i <= 100; i++) {
                for (j = 1; j <= 100; j++) {
                    if (i == j) {
                        continue; // skip
                    }
                    if (triples[i][j] > 50) {
                        for (int k = 1; k <= 100; k++) {
                            if (j == k) {
                                continue; // skip
                            }
                            if (triples[j][k] > 0) {
                                triples[i][k] += triples[j][k];
                                if (triples[i][k] > 50) {
                                    owns[i][k] = true;
                                }
                                if (triples[j][k] > 50) {
                                    for (int l = 1; l <= 100; l++) {
                                        if (k == l) {
                                            continue; // skip
                                        }
                                        if (triples[k][l] > 0) {
                                            triples[i][l] += triples[k][l];
                                            if (triples[i][l] > 50) {
                                                owns[i][l] = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // works for test 1,2,3,4,5,6,7,8 but fails test 9
            // handles company A = company B (part 1)
            // if i owns 100% of j, then find everything that i owns (k) and have j also own it (k)
            for (i = 1; i <= 100; i++) {
                for (j = 1; j <= 100; j++) {
                    if (i == j) {
                        continue; // skip
                    }
                    if (triples[i][j] == 100) {
                        for (int k = 1; k <= 100; k++) {
                            if (owns[i][k]) {
                                owns[j][k] = true;
                            }
                        }
                    }
                }
            }

            // works for test 1,2,3,4,5,6,7,8,9
            // handles company A = company B (part 2)
            // if i owns 100% of j, then find everything that j owns (k) and have i also own it (k)
            for (i = 1; i <= 100; i++) {
                for (j = 1; j <= 100; j++) {
                    if (i == j) {
                        continue; // skip
                    }
                    if (triples[i][j] == 100) {
                        for (int k = 1; k <= 100; k++) {
                            if (owns[j][k]) {
                                owns[i][k] = true;
                            }
                        }
                    }
                }
            }

            // output
            for (i = 1; i <= 100; i++) {
                for (j = 1; j <= 100; j++) {
                    if (i == j) {
                        continue; // skip
                    }
                    if (owns[i][j]) {
                        out.print(i);
                        out.print(" ");
                        out.println(j);
                    }
                }
            }
        }
    }
}
