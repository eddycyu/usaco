/*
ID: eddycyu
LANG: JAVA
TASK: barn1
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class barn1 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("barn1.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("barn1.out")))) {

            String line = f.readLine();
            final String[] tokens = line.split(" ");
            final int maxBoards = Integer.parseInt(tokens[0]);
            final int totalStalls = Integer.parseInt(tokens[1]);
            final int occupiedStalls = Integer.parseInt(tokens[2]);

            // mark stalls with 'true' if occupied
            final boolean[] blockedStalls = new boolean[totalStalls];
            final List<Integer> occupiedStallsList = new ArrayList<>();
            for (int i = 0; i < occupiedStalls; i++) {
                line = f.readLine();
                final int stallNumber = Integer.parseInt(line);
                blockedStalls[stallNumber - 1] = true;
                occupiedStallsList.add(stallNumber);
            }

            // sort the occupied stalls list (in case it is not provided in sorted order)
            Collections.sort(occupiedStallsList);

            // record the gaps
            final List<Gap> gaps = new ArrayList<>();
            int previousOccupiedStallNumber = 0;
            for (Integer currentOccupiedStallNumber : occupiedStallsList) {
                if (currentOccupiedStallNumber - previousOccupiedStallNumber > 1) {
                    gaps.add(
                            new Gap(previousOccupiedStallNumber + 1,
                                    currentOccupiedStallNumber - 1,
                                    previousOccupiedStallNumber + 1 == 1));
                }
                previousOccupiedStallNumber = currentOccupiedStallNumber;
            }

            // record right-most outer gap (if any)
            if (previousOccupiedStallNumber < totalStalls) {
                gaps.add(new Gap(previousOccupiedStallNumber + 1, totalStalls, true));
            }

            // sort the gaps by length
            Collections.sort(gaps);

            // calculate boards required
            int boardsRequired = 0;
            if (blockedStalls[0] && blockedStalls[blockedStalls.length - 1]) {
                // no exterior gaps (all interior gaps)
                boardsRequired = gaps.size() + 1;
            } else if ((blockedStalls[0] && !blockedStalls[blockedStalls.length - 1])
                    || (!blockedStalls[0] && blockedStalls[blockedStalls.length - 1])) {
                // only one exterior gap
                boardsRequired = gaps.size();
            } else {
                // both exterior gaps
                boardsRequired = gaps.size() - 1;
            }

            // reduce gaps to reduce boards
            while (boardsRequired > maxBoards) {
                // get shortest gap
                final Gap gap = gaps.remove(0);
                // mark stalls
                for (int i = gap.startingStallNumber - 1; i < gap.endingStallNumber; i++) {
                    blockedStalls[i] = true;
                }
                boardsRequired--;
            }

            // calculate total number of stalls blocked
            int totalBlocked = 0;
            for (boolean blockedStall : blockedStalls) {
                if (blockedStall) {
                    totalBlocked++;
                }
            }

            // output result
            out.println(totalBlocked);
        }
    }

    /**
     * Identifies a gap between empty stalls. For example:
     * <p>
     * Gep between stalls 3 and 5 = Gap(4, 4).
     * Gap between stalls 6 and 9 = Gap(7, 8)
     */
    static class Gap implements Comparable<Gap> {
        int startingStallNumber = 0;
        int endingStallNumber = 0;
        int length = 0;
        boolean outerGap = false;

        private Gap() {
            super();
        }

        Gap(int startingStallNumber, int endingStallNumber, boolean outerGap) {
            this();
            this.startingStallNumber = startingStallNumber;
            this.endingStallNumber = endingStallNumber;
            this.outerGap = outerGap;
            this.length = endingStallNumber - startingStallNumber + 1;
        }

        @Override
        public int compareTo(Gap other) {
            // sort outer gaps to the end
            if (this.outerGap && !other.outerGap) {
                return (1);
            } else if (!this.outerGap && other.outerGap) {
                return (-1);
            } else {
                return (this.length - other.length);
            }
        }
    }
}