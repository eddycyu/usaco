/*
ID: eddycyu
LANG: JAVA
TASK: namenum
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

public class namenum {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("namenum.in"));
             final BufferedReader dict = new BufferedReader(new FileReader("dict.txt"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("namenum.out")))) {

            // get serial number
            final String serialNumber = f.readLine();

            // load dictionary of names
            final Set<String> names = new HashSet<>();
            String line;
            while ((line = dict.readLine()) != null) {
                names.add(line);
            }

            // translate serial number to words
            final List<String> words = new ArrayList<>();
            final char[] digits = serialNumber.toCharArray();
            translate(digits, 0, "", words, names);

            // output answer
            if (words.size() == 0) {
                out.println("NONE");
            } else {
                Collections.sort(words);
                for (String word : words) {
                    out.println(word);
                }
            }
        }
    }

    private static void translate(char[] digits, int index, String word,
                                  List<String> words, Set<String> dict) {
        final String[] letters = getLetters(digits[index]);
        for (String letter : letters) {
            if (index == (digits.length - 1)) {
                final String name = word + letter;
                if (dict.contains(name)) {
                    words.add(name);
                }
            } else {
                translate(digits, index + 1, word + letter, words, dict);
            }
        }
    }

    private static String[] getLetters(char digit) {
        switch (digit) {
            case '2': {
                return new String[]{"A", "B", "C"};
            }
            case '3': {
                return new String[]{"D", "E", "F"};
            }
            case '4': {
                return new String[]{"G", "H", "I"};
            }
            case '5': {
                return new String[]{"J", "K", "L"};
            }
            case '6': {
                return new String[]{"M", "N", "O"};
            }
            case '7': {
                return new String[]{"P", "R", "S"};
            }
            case '8': {
                return new String[]{"T", "U", "V"};
            }
            case '9': {
                return new String[]{"W", "X", "Y"};
            }
            default: {
                return new String[]{};
            }
        }
    }
}
