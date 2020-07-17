/*
ID: eddycyu
LANG: JAVA
TASK: combo
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class combo {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("combo.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("combo.out")))) {

            final int maxDialNumber = Integer.parseInt(f.readLine());
            final String[] farmerCombo = f.readLine().split(" ");
            final String[] masterCombo = f.readLine().split(" ");
            final List<Integer> farmerComboList = new ArrayList<>();
            for (String combo : farmerCombo) {
                farmerComboList.add(Integer.parseInt(combo));
            }
            final List<Integer> masterComboList = new ArrayList<>();
            for (String combo : masterCombo) {
                masterComboList.add(Integer.parseInt(combo));
            }

            // generate all possible settings for Farmer John's lock
            final Set<String> possibleCombos = new HashSet<>();
            for (int i = farmerComboList.get(0) - 2; i <= farmerComboList.get(0) + 2; i++) {
                for (int j = farmerComboList.get(1) - 2; j <= farmerComboList.get(1) + 2; j++) {
                    for (int k = farmerComboList.get(2) - 2; k <= farmerComboList.get(2) + 2; k++) {
                        final int dial1 = (i + 50) % (maxDialNumber);
                        final int dial2 = (j + 50) % (maxDialNumber);
                        final int dial3 = (k + 50) % (maxDialNumber);
                        possibleCombos.add("" + (dial1 == 0 ? 50 : dial1)
                                + "," + (dial2 == 0 ? 50 : dial2)
                                + "," + (dial3 == 0 ? 50 : dial3));
                    }
                }
            }

            // generate all possible settings for master lock
            for (int i = masterComboList.get(0) - 2; i <= masterComboList.get(0) + 2; i++) {
                for (int j = masterComboList.get(1) - 2; j <= masterComboList.get(1) + 2; j++) {
                    for (int k = masterComboList.get(2) - 2; k <= masterComboList.get(2) + 2; k++) {
                        final int dial1 = (i + 50) % (maxDialNumber);
                        final int dial2 = (j + 50) % (maxDialNumber);
                        final int dial3 = (k + 50) % (maxDialNumber);
                        possibleCombos.add("" + (dial1 == 0 ? 50 : dial1)
                                + "," + (dial2 == 0 ? 50 : dial2)
                                + "," + (dial3 == 0 ? 50 : dial3));
                    }
                }
            }

            // output
            out.println(possibleCombos.size());
        }
    }
}