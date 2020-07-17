/*
ID: eddycyu
LANG: JAVA
TASK: subset
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class subset {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("subset.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("subset.out")))) {

            final int N = Integer.parseInt(f.readLine());

            // check if any solution exists (even sum only)
            final int totalSum = N * (N + 1) / 2;
            if (totalSum % 2 != 0) {
                out.println(0); // no solution is possible
                return;
            }

            // populate seed data; when N = 0, there is 1 way to create a
            // subset with a sum of 0
            long[] previousN = new long[totalSum + 1];
            previousN[0] = 1;

            // copy value from N-1 to N
            long[] currentN = new long[totalSum + 1];
            System.arraycopy(previousN, 0, currentN, 0, previousN.length - 1);

            // find solution
            for (int n = 1; n <= N; n++) {
                for (int i = 0; i <= (n * (n + 1) / 2); i++) {
                    if (n + i <= totalSum) {
                        // add value from N-1 to N
                        currentN[n + i] += previousN[i];
                    }
                }
                // update from N-1 values with values from N
                System.arraycopy(currentN, 0, previousN, 0, currentN.length - 1);
            }

            // output result (at subset sum, e.g. totalSum/2)
            out.println(currentN[totalSum / 2] / 2); // divide by 2 for double count
        }
    }
}
