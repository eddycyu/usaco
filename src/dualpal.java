/*
ID: eddycyu
LANG: JAVA
TASK: dualpal
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class dualpal {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("dualpal.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("dualpal.out")))) {

            final String line = f.readLine();
            final String[] tokens = line.split(" ");
            final int N = Integer.parseInt(tokens[0]);
            final int S = Integer.parseInt(tokens[1]);

            // iterate through numbers larger than S
            final List<Integer> results = new ArrayList<>();
            for (int number = S + 1; number < Integer.MAX_VALUE; number++) {
                // check if we have already found N numbers strictly greater than S that are palindromic
                if (results.size() == N) {
                    break;
                }
                int countFoundForNumber = 0;
                // iterate through bases to find palindromes
                for (int base = 2; base <= 10; base++) {
                    final String valueBaseN = Integer.toString(number, base);
                    if (isPalindrome(valueBaseN)) {
                        countFoundForNumber++;
                        // check if we have already found palindromes in 2 different bases
                        if (countFoundForNumber == 2) {
                            results.add(number);
                            break;
                        }
                    }
                }
            }

            // output results
            for (Integer result : results) {
                out.println(result);
            }
        }
    }

    private static boolean isPalindrome(String input) {
        final StringBuilder buffer = new StringBuilder(input);
        return (buffer.reverse().toString().equals(input));
    }
}
