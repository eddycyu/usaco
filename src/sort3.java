/*
ID: eddycyu
LANG: JAVA
TASK: sort3
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class sort3 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("sort3.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("sort3.out")))) {

            final int N = Integer.parseInt(f.readLine());
            final int[] records = new int[N];
            for (int i = 0; i < N; i++) {
                records[i] = Integer.parseInt(f.readLine());
            }

            final int numberOf1 = countNumberOfRecordsOf(records, 1, 0, N - 1);
            final int numberOf2 = countNumberOfRecordsOf(records, 2, 0, N - 1);
            final int numberOf3 = N - numberOf1 - numberOf2;

            final int index1Start = 0;
            final int index1End = numberOf1 - 1;
            final int index2Start = numberOf1;
            final int index2End = numberOf1 + numberOf2 - 1;
            final int index3Start = numberOf1 + numberOf2;
            final int index3End = N - 1;

            // determine how many of x are in bucket y (num2In1, num3In1, num1In2, ...)
            int num2In1 = countNumberOfRecordsOf(records, 2, index1Start, index1End);
            int num3In1 = countNumberOfRecordsOf(records, 3, index1Start, index1End);

            int num1In2 = countNumberOfRecordsOf(records, 1, index2Start, index2End);
            int num3In2 = countNumberOfRecordsOf(records, 3, index2Start, index2End);

            int num1In3 = countNumberOfRecordsOf(records, 1, index3Start, index3End);
            int num2In3 = countNumberOfRecordsOf(records, 2, index3Start, index3End);

            // distribute the numbers to their respective buckets
            int exchanges = 0;
            while ((num2In1 + num3In1 + num1In2 + num3In2 + num1In3 + num2In3) > 0) {

                if (num1In2 <= num2In1) {
                    exchanges += num1In2;
                    num2In1 -= num1In2;
                    num1In2 = 0;
                } else {
                    exchanges += num2In1;
                    num1In2 -= num2In1;
                    num2In1 = 0;
                }

                if (num1In3 <= num3In1) {
                    exchanges += num1In3;
                    num3In1 -= num1In3;
                    num1In3 = 0;
                } else {
                    exchanges += num3In1;
                    num1In3 -= num3In1;
                    num3In1 = 0;
                }

                if (num2In3 <= num3In2) {
                    exchanges += num2In3;
                    num3In2 -= num2In3;
                    num2In3 = 0;
                } else {
                    exchanges += num3In2;
                    num2In3 -= num3In2;
                    num3In2 = 0;
                }

                if (((num2In1 == num3In2) && (num2In1 == num1In3))
                        && ((num2In3 == 0) && (num3In1 == 0) && (num1In2 == 0))) {
                    exchanges += (2 * num2In1);
                    num2In1 = num3In2 = num1In3 = 0;

                } else if (((num3In1 == num1In2) && (num3In1 == num2In3))
                        && ((num3In2 == 0) && (num1In3 == 0) && (num2In1 == 0))) {
                    exchanges += (2 * num3In1);
                    num3In1 = num1In2 = num2In3 = 0;
                }
            }

            out.println(exchanges);
        }
    }

    private static int countNumberOfRecordsOf(int[] records, int number, int indexStart, int indexEnd) {
        int count = 0;
        for (int i = indexStart; i <= indexEnd; i++) {
            if (records[i] == number) {
                count++;
            }
        }
        return count;
    }
}
