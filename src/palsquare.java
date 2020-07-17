/*
ID: eddycyu
LANG: JAVA
TASK: palsquare
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class palsquare {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("palsquare.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("palsquare.out")))) {

            // base
            final int B = Integer.parseInt(f.readLine());

            // iterate through all numbers from 1 to 300 to find palindrome
            for (int n = 1; n <= 300; n++) {
                final int squaredValue = n * n;
                final String squaredValueBaseN = Integer.toString(squaredValue, B);
                if (isPalindrome(squaredValueBaseN)) {
                    final String indexBaseN = Integer.toString(n, B);
                    out.println(indexBaseN.toUpperCase() + " " + squaredValueBaseN.toUpperCase());
                }
            }
        }
    }

    private static boolean isPalindrome(String input) {
        final StringBuilder buffer = new StringBuilder(input);
        return (buffer.reverse().toString().equals(input));
    }
}
