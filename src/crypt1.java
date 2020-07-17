/*
ID: eddycyu
LANG: JAVA
TASK: crypt1
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

class crypt1 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("crypt1.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("crypt1.out")))) {

            // parse inputs
            final int N = Integer.parseInt(f.readLine());
            final String[] tokens = f.readLine().split(" ");
            final List<Integer> digitsList = new ArrayList<>();
            final Set<String> digitsSet = new HashSet<>();
            for (String token : tokens) {
                digitsList.add(Integer.parseInt(token));
                digitsSet.add(token);
            }

            // sort the digits in ascending order
            Collections.sort(digitsList);

            int resultCount = 0;
            for (int i = 0; i < digitsList.size(); i++) {
                for (int j = 0; j < digitsList.size(); j++) {
                    for (int k = 0; k < digitsList.size(); k++) {
                        for (int l = 0; l < digitsList.size(); l++) {
                            for (int m = 0; m < digitsList.size(); m++) {
                                int e = digitsList.get(m);
                                int d = digitsList.get(l);
                                int c = digitsList.get(k);
                                int b = digitsList.get(j);
                                int a = digitsList.get(i);

                                // check for valid length of p1 and p2 AND valid numbers
                                int abc = (a * 100) + (b * 10) + c;
                                int p1 = abc * e;
                                int p2 = abc * d;
                                if ((p1 > 999) || (p2 > 999)
                                        || !containsValidDigits(p1, digitsSet)
                                        || !containsValidDigits(p2, digitsSet)) {
                                    continue; // try next number
                                }

                                // check for valid length of sum of p1 and p2 AND valid numbers
                                int sum = p1 + 10 * p2;
                                if ((sum > 9999) || !containsValidDigits(sum, digitsSet)) {
                                    continue; // try next number
                                }

                                resultCount++;
                            }
                        }
                    }
                }
            }

            // output result
            out.println(resultCount);
        }
    }

    private static boolean containsValidDigits(int number, Set<String> validDigits) {
        final String[] digitArray = Integer.toString(number).split("");
        for (String digitString : digitArray) {
            if (!validDigits.contains(digitString)) {
                return false;
            }
        }
        return true;
    }
}