/*
ID: eddycyu
LANG: JAVA
TASK: preface
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class preface {

    final static private int I = 1;
    final static private int V = 5;
    final static private int X = 10;
    final static private int L = 50;
    final static private int C = 100;
    final static private int D = 500;
    final static private int M = 1000;

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("preface.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("preface.out")))) {

            // number of pages in the preface of a book
            final int N = Integer.parseInt(f.readLine());

            // 0 = I, 1 = V, 2 = X, 3 = L, 4 = C, 5 = D, 6 = M
            int[] counts = new int[7];

            // count number of roman numeral letters
            for (int page = 1; page <= N; page++) {
                countLetters(page, counts);
            }

            // output
            if (counts[0] > 0) {
                out.println("I " + counts[0]);
            }
            if (counts[1] > 0) {
                out.println("V " + counts[1]);
            }
            if (counts[2] > 0) {
                out.println("X " + counts[2]);
            }
            if (counts[3] > 0) {
                out.println("L " + counts[3]);
            }
            if (counts[4] > 0) {
                out.println("C " + counts[4]);
            }
            if (counts[5] > 0) {
                out.println("D " + counts[5]);
            }
            if (counts[6] > 0) {
                out.println("M " + counts[6]);
            }
        }
    }

    private static void countLetters(int number, int[] counts) {
        while (number > 0) {
            if (number >= M) {
                number -= M;
                counts[6]++; // M
            } else if (number >= D) {
                if (number >= 900) {
                    number -= 900;
                    counts[4]++; // C
                    counts[6]++; // M
                } else {
                    number -= D;
                    counts[5]++; // D
                }
            } else if (number >= C) {
                if (number >= 400) {
                    number -= 400;
                    counts[4]++; // C
                    counts[5]++; // D
                } else {
                    number -= C;
                    counts[4]++; // C
                }
            } else if (number >= L) {
                if (number >= 90) {
                    number -= 90;
                    counts[2]++; // X
                    counts[4]++; // C
                } else {
                    number -= L;
                    counts[3]++; // L
                }
            } else if (number >= X) {
                if ((number >= 40)) {
                    number -= 40;
                    counts[2]++; // X
                    counts[3]++; // L
                } else {
                    number -= X;
                    counts[2]++; // X
                }
            } else if (number >= V) {
                if (number >= 9) {
                    number -= 9;
                    counts[0]++; // I
                    counts[2]++; // X
                } else {
                    number -= V;
                    counts[1]++; // V
                }
            } else {
                if (number >= 4) {
                    number -= 4;
                    counts[0]++; // I
                    counts[1]++; // V
                } else {
                    number -= I;
                    counts[0]++; // I
                }
            }
        }
    }
}
