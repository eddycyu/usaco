/*
ID: eddycyu
LANG: JAVA
TASK: frac1
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class frac1 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("frac1.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("frac1.out")))) {

            final int N = Integer.parseInt(f.readLine());

            // generate the fractions
            final Set<Fraction> fractions = new HashSet<>();
            for (int denominator = 1; denominator <= N; denominator++) {
                for (int numerator = 0; numerator <= denominator; numerator++) {
                    fractions.add(new Fraction(numerator, denominator));
                }
            }

            // sort the results
            final List<Fraction> sortedFractions = new ArrayList<>(fractions);
            Collections.sort(sortedFractions);

            // output results
            for (Fraction fraction : sortedFractions) {
                out.println(fraction.numerator + "/" + fraction.denominator);
            }
        }
    }

    static class Fraction implements Comparable<Fraction> {
        int numerator;
        int denominator;
        int value;

        private Fraction() {
            super();
        }

        Fraction(int numerator, int denominator) {
            this();
            this.numerator = numerator;
            this.denominator = denominator;

            // since numbers are from 1 to 160, multiply by 100000 to avoid decimals
            // with enough precision
            this.value = (int) (100000 * ((float) numerator / (float) denominator));
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!Fraction.class.isAssignableFrom(obj.getClass())) {
                return false;
            }

            final Fraction other = (Fraction) obj;
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + this.value;
            return hash;
        }

        @Override
        public int compareTo(Fraction other) {
            if (this.value < other.value) {
                return -1;
            } else if (this.value > other.value) {
                return 1;
            }
            return 0;
        }
    }
}
