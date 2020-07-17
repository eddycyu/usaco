/*
ID: eddycyu
LANG: JAVA
TASK: runround
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class runround {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("runround.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("runround.out")))) {

            final int M = Integer.parseInt(f.readLine());
            for (int m = M + 1; m < 999999999; m++) {
                final String number = Integer.toString(m);
                if (isRunround(number)) {
                    out.println(m);
                    break;
                }
            }

        }
    }

    private static boolean isRunround(String number) {
        boolean[] seen = new boolean[10];
        boolean[] visitedIndex = new boolean[number.length() + 1];
        int index = 0;
        int increment = Character.getNumericValue(number.charAt(index));
        int count = 0;

        do {
            // 0 is an invalid increment
            if (increment == 0) {
                return false;
            }
            // check if number wraps around to itself
            if (increment % number.length() == 0) {
                return false;
            }
            // check if increment is repeated
            if (seen[increment]) {
                return false;
            }
            seen[increment] = true;

            // advance index
            index = (index + increment) % (number.length());

            // check if next index has been visited before
            if (visitedIndex[index]) {
                return false;
            }
            visitedIndex[index] = true;
            increment = Character.getNumericValue(number.charAt(index));
        } while (++count < number.length());

        return true;
    }
}
