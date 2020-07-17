/*
ID: eddycyu
LANG: JAVA
TASK: transform
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class transform {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("transform.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("transform.out")))) {

            final int N = Integer.parseInt(f.readLine());

            // create starting pattern
            final char[][] startPattern = new char[N][N];
            for (int y = 0; y < N; y++) {
                final String input = f.readLine();
                final char[] chars = input.toCharArray();
                for (int x = 0; x < N; x++) {
                    startPattern[x][y] = chars[x];
                }
            }
            final String startString = patternToString(startPattern, N);

            // create ending pattern
            final char[][] endPattern = new char[N][N];
            for (int y = 0; y < N; y++) {
                final String input = f.readLine();
                final char[] chars = input.toCharArray();
                for (int x = 0; x < N; x++) {
                    endPattern[x][y] = chars[x];
                }
            }
            final String endString = patternToString(endPattern, N);

            // generate transformations
            final char[][] transform1 = rotate90(startPattern, N);         // rotate 90 degree
            final char[][] transform2 = rotate90(transform1, N);           // rotate 180 degree
            final char[][] transform3 = rotate90(transform2, N);           // rotate 270 degree
            final char[][] transform4 = rotateHorizontal(startPattern, N); // rotate horizontal
            final char[][] transform5a = rotate90(transform4, N);          // rotate horizontal + 90 degrees
            final char[][] transform5b = rotate90(transform5a, N);         // rotate horizontal + 180 degrees
            final char[][] transform5c = rotate90(transform5b, N);         // rotate horizontal + 270 degrees

            // determine which transformation was used
            int answer = 7; // default: invalid transformation
            final String transform1String = patternToString(transform1, N);
            final String transform2String = patternToString(transform2, N);
            final String transform3String = patternToString(transform3, N);
            final String transform4String = patternToString(transform4, N);
            final String transform5aString = patternToString(transform5a, N);
            final String transform5bString = patternToString(transform5b, N);
            final String transform5cString = patternToString(transform5c, N);
            if (endString.equals(transform1String)) {
                answer = 1;
            } else if (endString.equals(transform2String)) {
                answer = 2;
            } else if (endString.equals(transform3String)) {
                answer = 3;
            } else if (endString.equals(transform4String)) {
                answer = 4;
            } else if (endString.equals(transform5aString)
                    || endString.equals(transform5bString)
                    || endString.equals(transform5cString)) {
                answer = 5;
            } else if (endString.equals(startString)) {
                answer = 6;
            } // else 7 - invalid transformation

            // output answer
            out.println(answer);
        }
    }

    private static String patternToString(char[][] pattern, int length) {
        final StringBuilder buffer = new StringBuilder();
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                buffer.append(pattern[x][y]);
            }
        }
        return buffer.toString();
    }

    private static char[][] rotate90(char[][] pattern, int length) {
        final char[][] result = new char[length][length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                result[length - 1 - y][x] = pattern[x][y];
            }
        }
        return (result);
    }

    private static char[][] rotateHorizontal(char[][] pattern, int length) {
        final char[][] result = new char[length][length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                result[length - 1 - x][y] = pattern[x][y];
            }
        }
        return (result);
    }
}
