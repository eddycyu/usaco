/*
ID: eddycyu
LANG: JAVA
TASK: prefix
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class prefix {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("prefix.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("prefix.out")))) {

            // parse primitives and store in a map of primitives keyed by primitive length
            final Map<Integer, Set<String>> primitivesByLength = new HashMap<>();
            for (String line = f.readLine(); (line != null) && !line.equals("."); line = f.readLine()) {
                final String[] primitivesArray = line.split(" ");
                for (final String primitive : primitivesArray) {
                    final int primitiveLength = primitive.length();
                    primitivesByLength.putIfAbsent(primitiveLength, new HashSet<>());
                    final Set<String> primitivesSet = primitivesByLength.get(primitiveLength);
                    primitivesSet.add(primitive);
                }
            }

            // sorted list of available primitive lengths
            final List<Integer> primitiveLengths = new ArrayList<>(primitivesByLength.keySet());
            Collections.sort(primitiveLengths);

            // parse sequence
            final StringBuilder builder = new StringBuilder();
            for (String line = f.readLine(); (line != null) && !line.isEmpty(); line = f.readLine()) {
                builder.append(line);
            }
            final String sequence = builder.toString();

            // solve
            reducePrimitives(primitivesByLength, primitiveLengths);
            int maxLength = solve(primitivesByLength, primitiveLengths, sequence);

            // output result
            out.println(maxLength);
        }
    }

    private static void reducePrimitives(Map<Integer, Set<String>> primitivesByLength,
                                         List<Integer> primitiveLengths) {
        // reduce the number or primitives by eliminating repeating primitives
        // that already exist in a simplified form. For example, 'ab' and
        // 'ababab' is reduced to 'ab'.
        for (int primitiveLength1 : primitiveLengths) {
            for (int primitiveLength2 : primitiveLengths) {
                if (primitiveLength1 == primitiveLength2) {
                    continue;
                }
                // primitiveLength2 is not a multiple of primitiveLength1
                if (primitiveLength2 % primitiveLength1 != 0) {
                    continue;
                }
                // else they are multiples; see if primitiveLength2 can be eliminated
                for (String primitive1 : primitivesByLength.get(primitiveLength1)) {
                    // construct comparison string from primitive1
                    final StringBuilder builder = new StringBuilder();
                    final int multiples = primitiveLength2 / primitiveLength1;
                    for (int i = 0; i < multiples; i++) {
                        builder.append(primitive1);
                    }
                    final String compareString = builder.toString();
                    // remove if they are equal
                    primitivesByLength.get(primitiveLength2).removeIf(primitive2 -> primitive2.equals(compareString));
                }
            }
        }
        // remove any empty lists of primitives
        for (int primitivesLength : primitiveLengths) {
            if (primitivesByLength.get(primitivesLength).isEmpty()) {
                primitivesByLength.remove(primitivesLength);
            }
        }
        // update sorted list of primitive lengths
        primitiveLengths.clear();
        primitiveLengths.addAll(primitivesByLength.keySet());
        Collections.sort(primitiveLengths);
    }

    private static int solve(Map<Integer, Set<String>> primitivesByLength,
                             List<Integer> primitiveLengths, String sequence) {
        // iterate through the primitives and mark ('1') any portion of the
        // sequence that matches with the primitives
        int[] bits = new int[sequence.length()];
        for (int primitiveLength : primitiveLengths) {
            for (String primitive : primitivesByLength.get(primitiveLength)) {
                int startIndex = 0;
                int foundIndex;
                while ((startIndex < sequence.length()
                        && (foundIndex = sequence.indexOf(primitive, startIndex)) != -1)) {
                    Arrays.fill(bits, foundIndex, foundIndex + primitiveLength, 1);
                    startIndex = foundIndex + 1;
                }
            }
        }
        // count longest length of contiguous 1's
        int maxLength = 0;
        for (int bit : bits) {
            if (bit == 1) {
                maxLength++;
            } else {
                break;
            }
        }
        return maxLength;
    }
}
