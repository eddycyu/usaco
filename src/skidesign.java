/*
ID: eddycyu
LANG: JAVA
TASK: skidesign
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class skidesign {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("skidesign.in"));
             final Scanner scanner = new Scanner(f);
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("skidesign.out")))) {

            // read input
            final int hillCount = scanner.nextInt();
            final int[] hills = new int[hillCount];
            for (int i = 0; i < hillCount; i++) {
                hills[i] = scanner.nextInt();
            }

            int minimumAmount = Integer.MAX_VALUE;
            if (hillCount == 1) {
                // nothing to do; there's only one hill!
                minimumAmount = 0;
            } else {
                // sort the hills by elevation (ascending order)
                Arrays.sort(hills);

                // iterate through the hills to find two hills with elevation difference > 17;
                // compute the cost to make the changes; keep track of the cheapest cost
                int changeCount = 0;
                for (int i = 0; i < hillCount - 1; i++) {
                    int highestElevationMax = -1;
                    int lowestElevationMin = -1;
                    for (int j = i + 1; j < hillCount; j++) {
                        int amount = 0;
                        final int lowestElevation = hills[i];
                        final int highestElevation = hills[j];
                        int diff = highestElevation - lowestElevation;
                        if (diff > 17) {
                            changeCount++;
                            final int lowerChange = (diff - 17) / 2; // round down
                            final int higherChange = (int) Math.ceil((diff - 17.0) / 2.0); // round up
                            highestElevationMax = hills[j] - higherChange;
                            lowestElevationMin = hills[i] + lowerChange;
                            // compute cost for change
                            for (int k = 0; k < hillCount; k++) {
                                if (hills[k] < lowestElevationMin) {
                                    final int change = lowestElevationMin - hills[k];
                                    amount += (change * change);
                                } else if (hills[k] > highestElevationMax) {
                                    final int change = hills[k] - highestElevationMax;
                                    amount += (change * change);
                                }
                                // speed optimization to eliminate unnecessary work
                                if (amount >= minimumAmount) {
                                    break; // no need to calculate any further for this loop
                                }
                            }
                            // keep track of the cheapest cost
                            minimumAmount = Math.min(minimumAmount, amount);
                        }
                    }
                }
                if (changeCount == 0) {
                    // all hills are within 17
                    minimumAmount = 0;
                }
            }

            // output result
            out.println(minimumAmount);
        }
    }
}