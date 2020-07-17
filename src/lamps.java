/*
ID: eddycyu
LANG: JAVA
TASK: lamps
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class lamps {

    /**
     * NOTE:
     * <p>
     * There are 4^C (e.g. 4^10000) possible combinations, which is way too big! So, that means, there
     * must be some optimization in order to solve this problem.
     * <p>
     * The trick is to realize that (1) the order of the button click does not matter (e.g. clicking button 1
     * and then button 2 is the same as clicking button 2 and then button 1), and that (2) clicking the same
     * button more than once will just cancel out that button click. Hence, you can reduce the total
     * combinations by ignoring the button click order and by only having at most 4 clicks per combo.
     */
    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("lamps.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("lamps.out")))) {

            // number of lamps
            final int N = Integer.parseInt(f.readLine());

            // total number of button presses
            final int C = Integer.parseInt(f.readLine());

            // final lamp configuration
            final int[] finalLampConfiguration = new int[N]; // 0 = off, 1 = on, 3 = off or on
            Arrays.fill(finalLampConfiguration, 3);
            final Scanner scanner = new Scanner(f);
            int lampNumber;
            while ((lampNumber = scanner.nextInt()) != -1) {
                finalLampConfiguration[lampNumber - 1] = 1; // ON
            }
            while ((lampNumber = scanner.nextInt()) != -1) {
                finalLampConfiguration[lampNumber - 1] = 0; // OFF
            }

            // generate all possible click combinations
            final int maxClicks = Math.min(C, 4); // max of 4 clicks
            final int[] combo = new int[maxClicks];
            final List<int[]> combos = new ArrayList<>();
            generateClickCombos(combo, 0, maxClicks, combos);

            // compute the results of the combos
            final List<int[]> results = new ArrayList<>();
            computeResults(N, combos, results);

            // find results which are consistent with the final lamp configuration
            final Set<String> validResults = new HashSet<>();
            for (int[] result : results) {
                boolean isMatch = true;
                for (int i = 0; i < finalLampConfiguration.length; i++) {
                    if (finalLampConfiguration[i] == 3) {
                        continue; // any state is allowed; check next lamp
                    }
                    if (finalLampConfiguration[i] != result[i]) {
                        // desired state is not matched
                        isMatch = false;
                        break;
                    }
                    // else - desired state is matched
                }
                if (isMatch) {
                    validResults.add(Arrays.toString(result).replaceAll("[^0-9]", ""));
                }
            }

            // output result
            if (validResults.size() == 0) {
                out.println("IMPOSSIBLE");
            } else {
                // sort the results
                String[] validResultsArray = validResults.toArray(new String[0]);
                Arrays.sort(validResultsArray);
                for (String resultString : validResultsArray) {
                    out.println(resultString);
                }
            }
        }
    }

    private static void generateClickCombos(int[] combo, int currentClickCount,
                                            int maxClicks, List<int[]> combos) {
        // combo generation is complete
        if (currentClickCount == maxClicks) {
            combos.add(Arrays.copyOf(combo, combo.length));
            return;
        }

        // else find next button (button1 to button4) for next click in combo
        for (int i = 1; i <= 4; i++) {
            combo[currentClickCount] = i;
            generateClickCombos(combo, (currentClickCount + 1), maxClicks, combos);
        }
    }

    private static void computeResults(int N, List<int[]> combos, List<int[]> results) {
        // iterate through the combos and compute the resulting configuration of the lamps
        for (int[] combo : combos) {
            final int[] lamps = new int[N]; // 0 = off, 1 = on
            Arrays.fill(lamps, 1); // all lamps start in ON state
            // check which button is pressed for each click
            for (int button : combo) {
                if (button == 1) {
                    // button1 : all lamps flip state
                    for (int y = 0; y < lamps.length; y++) {
                        lamps[y] = (lamps[y] + 1) % 2;
                    }
                } else if (button == 2) {
                    // button2 : all odd lamps flip state
                    for (int y = 0; y < lamps.length; y = y + 2) {
                        lamps[y] = (lamps[y] + 1) % 2;
                    }
                } else if (button == 3) {
                    // button3: all even lamps flip state
                    for (int y = 1; y < lamps.length; y = y + 2) {
                        lamps[y] = (lamps[y] + 1) % 2;
                    }
                } else if (button == 4) {
                    // button4: all 3xK+1 lamps flip state, where K=click
                    for (int k = 0; k < lamps.length; k++) {
                        int j = (3 * k) + 1;
                        // make sure we don't exceed array bounds
                        if (j - 1 < lamps.length) {
                            lamps[j - 1] = (lamps[j - 1] + 1) % 2;
                        }
                    }
                }
            }
            results.add(Arrays.copyOf(lamps, lamps.length));
        }
    }
}
