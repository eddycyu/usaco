/*
ID: eddycyu
LANG: JAVA
TASK: fracdec
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class fracdec {

    public static void main(String[] args) throws IOException {

        try (
                final BufferedReader f = new BufferedReader(new FileReader("fracdec.in"));
                final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("fracdec.out")))
        ) {
            final String[] tokens = f.readLine().split(" ");
            final int N = Integer.parseInt(tokens[0]);
            final int D = Integer.parseInt(tokens[1]);

            // buffer containing the decimal part of the decimal representation of the fraction
            // e.g. for 1.2345, buffer would contain 2345
            final StringBuilder buffer = new StringBuilder();

            // keep track of which remainders have been seen before;
            // value will be the index location in the buffer where the remainder was first seen
            // e.g. seen[remainder]=index location in buffer where remainder was first seen
            final int[] seen = new int[100000 + 1];
            Arrays.fill(seen, -1);  // initialize default value to -1

            // find decimal representation without repeated sequence
            final int wholeNumberPart = N / D;
            int remainder = N % D;
            while ((remainder != 0) && (seen[remainder] == -1)) {
                seen[remainder] = buffer.length(); // location in buffer where remainder was first seen
                //remainder *= 10;                                  // multiply by 10 (slower, but works)
                remainder = (remainder << 1) + (remainder << 3);    // multiply by 10 (bit operation is faster)
                buffer.append(remainder / D);                       // append (remainder/D) to buffer
                remainder = remainder % D;                          // next remainder
            }
            final String decimalPart = buffer.toString();

            // construct result
            final StringBuilder result = new StringBuilder();
            if (decimalPart.equals("")) {
                result.append(wholeNumberPart).append(".0");
            } else {
                result.append(wholeNumberPart).append(".");
                final String[] digits = decimalPart.split("");
                int openingParenthesisIndex = seen[remainder];
                for (int i = 0; i < digits.length; i++) {
                    if (openingParenthesisIndex == i) {
                        result.append("(");
                        result.append(digits[i]);
                    } else {
                        result.append(digits[i]);
                    }
                }
                // add closing parenthesis is there is an opening parenthesis
                if (openingParenthesisIndex != -1) {
                    result.append(")");
                }
            }

            // output result (up to 76 characters per line)
            final String resultString = result.toString();
            for (int i = 0; i < resultString.length(); i += 76) {
                out.println(resultString.substring(i, Math.min(i + 76, resultString.length())));
            }
        }
    }
}