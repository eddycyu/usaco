/*
ID: eddycyu
LANG: JAVA
TASK: milk3
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utilize Depth First Search (DFS) to solve this problem.
 */
class milk3 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("milk3.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("milk3.out")))) {

            final String line = f.readLine();
            final String[] tokens = line.split(" ");
            final int maxA = Integer.parseInt(tokens[0]);
            final int maxB = Integer.parseInt(tokens[1]);
            final int maxC = Integer.parseInt(tokens[2]);

            final Set<Integer> result = new HashSet<>();

            /* start with everything in C; do all possible distributions from C to A and B */

            // distribute amount from C to AB (fill up A first, then B)
            final Tuple tuple1 = new Tuple(maxA, maxB, maxC, result);
            tuple1.setValues(0, 0, maxC);
            tuple1.distributeToABFromC(maxC);

            // distribute amount from C to BA (fill up B first, then A)
            final Tuple tuple2 = new Tuple(maxA, maxB, maxC, result);
            tuple2.setValues(0, 0, maxC);
            tuple2.distributeToBAFromC(maxC);

            // distribute amount from C to A
            final Tuple tuple3 = new Tuple(maxA, maxB, maxC, result);
            tuple3.setValues(0, 0, maxC);
            tuple3.distributeToAFromC(maxC);

            // distribute amount from C to B
            final Tuple tuple4 = new Tuple(maxA, maxB, maxC, result);
            tuple4.setValues(0, 0, maxC);
            tuple4.distributeToBFromC(maxC);

            // sort result
            List<Integer> sortedResult = new ArrayList<>(result);
            Collections.sort(sortedResult);

            // print result
            out.println(sortedResult.toString()
                    .replace("[", "").replace("]", "").replace(",", ""));
        }
    }

    public static class Tuple {

        // keeps track of paths that have already been searched;
        // this is an optimization to avoid repeat searches
        private static final Map<String, Tuple> seen = new HashMap<>();

        // reference to results
        private final Set<Integer> result;

        private final int maxA;
        private final int maxB;
        private final int maxC;

        private int a;
        private int b;
        private int c;

        Tuple(int maxA, int maxB, int maxC, Set<Integer> result) {
            this.maxA = maxA;
            this.maxB = maxB;
            this.maxC = maxC;
            this.result = result;
        }

        void setValues(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        private Tuple cloneTuple() {
            final Tuple cloneTuple = new Tuple(maxA, maxB, maxC, result);
            cloneTuple.setValues(a, b, c);
            return cloneTuple;
        }

        private boolean isTerminated() {
            return a == 0;
        }

        private int setA(int amount) {
            final int diff = (a + amount) - maxA;
            if (diff > 0) {
                a = maxA;
                return diff;
            } else {
                a = a + amount;
                return 0;
            }
        }

        private int setB(int amount) {
            final int diff = (b + amount) - maxB;
            if (diff > 0) {
                b = maxB;
                return diff;
            } else {
                b = b + amount;
                return 0;
            }
        }

        private int setC(int amount) {
            final int diff = (c + amount) - maxC;
            if (diff > 0) {
                c = maxC;
                return diff;
            } else {
                c = c + amount;
                return 0;
            }
        }

        void distributeToAFromB(int amount) {
            // distribute amount
            b = setA(amount);

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "AFB")) {
                return;
            } else {
                seen.put(toString() + "AFB", this);
            }

            search();
        }

        void distributeToAFromC(int amount) {
            // distribute amount
            c = setA(amount);

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "AFC")) {
                return;
            } else {
                seen.put(toString() + "AFC", this);
            }

            search();
        }

        void distributeToBFromA(int amount) {
            // distribute amount
            a = setB(amount);

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "BFA")) {
                return;
            } else {
                seen.put(toString() + "BFA", this);
            }

            search();
        }

        void distributeToBFromC(int amount) {
            // distribute amount
            c = setB(amount);

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "BFC")) {
                return;
            } else {
                seen.put(toString() + "BFC", this);
            }

            search();
        }

        void distributeToCFromA(int amount) {
            // distribute amount
            a = setC(amount);

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "CFA")) {
                return;
            } else {
                seen.put(toString() + "CFA", this);
            }

            search();
        }

        void distributeToCFromB(int amount) {
            // distribute amount
            b = setC(amount);

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "CFB")) {
                return;
            } else {
                seen.put(toString() + "CFB", this);
            }

            search();
        }

        void distributeToABFromC(int amount) {
            // distribute amount
            int diff = setA(amount);
            if (diff > 0) {
                diff = setB(diff);
            }
            c = diff;

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "AB")) {
                return;
            } else {
                seen.put(toString() + "AB", this);
            }

            search();
        }

        void distributeToBAFromC(int amount) {
            // distribute amount
            int diff = setB(amount);
            if (diff > 0) {
                diff = setA(diff);
            }
            c = diff;

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "BA")) {
                return;
            } else {
                seen.put(toString() + "BA", this);
            }

            search();
        }

        void distributeToBCFromA(int amount) {
            // distribute amount
            int diff = setB(amount);
            if (diff > 0) {
                diff = setC(diff);
            }
            a = diff;

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "BC")) {
                return;
            } else {
                seen.put(toString() + "BC", this);
            }

            search();
        }

        void distributeToCBFromA(int amount) {
            // distribute amount
            int diff = setC(amount);
            if (diff > 0) {
                diff = setB(diff);
            }
            a = diff;

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "CB")) {
                return;
            } else {
                seen.put(toString() + "CB", this);
            }

            search();
        }

        void distributeToACFromB(int amount) {
            // distribute amount
            int diff = setA(amount);
            if (diff > 0) {
                diff = setC(diff);
            }
            b = diff;

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "AC")) {
                return;
            } else {
                seen.put(toString() + "AC", this);
            }

            search();
        }

        void distributeToCAFromB(int amount) {
            // distribute amount
            int diff = setC(amount);
            if (diff > 0) {
                diff = setA(diff);
            }
            b = diff;

            // optimization to remove redundant search path
            if (seen.containsKey(toString() + "CA")) {
                return;
            } else {
                seen.put(toString() + "CA", this);
            }

            search();
        }

        /**
         * Depth First Search
         */
        private void search() {

            // if terminated (e.g. A is 0), then add C to the result
            if (isTerminated()) {
                result.add(c);
            }

            // try all possible search patterns for other results;
            // NOTE: optimization to only search when amount to distribute is greater than zero

            if (a > 0) {
                final Tuple tuple1 = cloneTuple();
                final Tuple tuple2 = cloneTuple();
                final Tuple tuple3 = cloneTuple();
                final Tuple tuple4 = cloneTuple();
                tuple1.distributeToBCFromA(a);
                tuple2.distributeToCBFromA(a);
                tuple3.distributeToBFromA(a);
                tuple4.distributeToCFromA(a);
            }
            if (b > 0) {
                final Tuple tuple5 = cloneTuple();
                final Tuple tuple6 = cloneTuple();
                final Tuple tuple7 = cloneTuple();
                final Tuple tuple8 = cloneTuple();
                tuple5.distributeToACFromB(b);
                tuple6.distributeToCAFromB(b);
                tuple7.distributeToAFromB(b);
                tuple8.distributeToCFromB(b);
            }
            if (c > 0) {
                final Tuple tuple9 = cloneTuple();
                final Tuple tuple10 = cloneTuple();
                final Tuple tuple11 = cloneTuple();
                final Tuple tuple12 = cloneTuple();

                tuple9.distributeToABFromC(c);
                tuple10.distributeToBAFromC(c);
                tuple11.distributeToAFromC(c);
                tuple12.distributeToBFromC(c);
            }
        }

        @Override
        public String toString() {
            return "" + a + "," + b + "," + c;
        }
    }
}
