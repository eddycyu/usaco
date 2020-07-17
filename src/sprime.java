/*
ID: eddycyu
LANG: JAVA
TASK: sprime
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class sprime {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("sprime.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("sprime.out")))) {

            final int N = Integer.parseInt(f.readLine());

            // find min and max value ranges
            int min = 1;
            for (int n = 1; n < N; n++) {
                min = min * 10;
            }
            int max = min * 10;
            max -= 1;

            // note1: use 'char' instead of 'int' to reduce memory usage
            // note2: only store computed values up to (max/10) to reduce memory usage
            final char[] sprimes = new char[max / 10];

            // initialize default values to '2' where '0' = not super prime,
            // '1' = super prime, and '2' = don't know if super prime or not
            Arrays.fill(sprimes, '2');

            // find all superprimes of length N
            for (int i = min + 1; i <= max; i += 2) {
                if (isSuperPrime(i, sprimes)) {
                    out.println(i);
                }
            }
        }
    }

    private static boolean isSuperPrime(int number, char[] sprimes) {
        if (number >= 10) {
            int nextNumber = number / 10;

            // optimization: check if slice is superprime (first check against known superprimes)
            if (sprimes[nextNumber - 1] != '2') {
                return sprimes[nextNumber - 1] == '1' && isPrime(number);
            }

            // otherwise check if slice is superprime by computing
            final boolean result = isSuperPrime(nextNumber, sprimes);
            if (sprimes[nextNumber - 1] == '2') {
                sprimes[nextNumber - 1] = result ? '1' : '0';
            } else {
                sprimes[nextNumber - 1] = (result && (sprimes[nextNumber - 1] == '1')) ? '1' : '0';
            }
            return result && isPrime(number);
        } else {
            // no more slices; just check if number is prime
            return isPrime(number);
        }
    }

    private static boolean isPrime(int number) {
        // special case: for numbers 3 and under, only 2 and 3 are prime
        if (number <= 3) {
            return number > 1;
        }
        // all primes greater than 3 are of the form 6k[+/-]1;
        // eliminate numbers divisible by 2 or 3
        if ((number % 2 == 0) || (number % 3 == 0)) {
            return false;
        }
        // and only check for divisors up to sqrt(number)
        for (int i = 5; i * i <= number; i += 6) {
            if ((number % i == 0) || ((number % (i + 2)) == 0)) {
                return false;
            }
        }
        return true;
    }
}
