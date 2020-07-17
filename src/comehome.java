/*
ID: eddycyu
LANG: JAVA
TASK: comehome
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class comehome {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("comehome.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("comehome.out")))) {

            // number of paths
            final int numberOfPaths = Integer.parseInt(f.readLine());

            // distance between pastures [A-Z,a-z][A-Z,a-z] where Z = barn
            final int[][] distance = new int[52][52];

            // initially set large distance between pastures;
            // 10,000,001 is safe to use since all path distances
            // are <= 1,000 and there are <= 10,000 paths
            for (int i = 0; i < distance.length; i++) {
                for (int j = 0; j < distance.length; j++) {
                    distance[i][j] = 10000001;
                }
            }
            // set distance for self looping paths to zero
            for (int i = 0; i < 26; i++) {
                distance[i][i] = 0;
            }

            // set known distance between pastures
            for (int i = 0; i < numberOfPaths; i++) {
                final String[] tokens = f.readLine().split(" ");
                final int pastureA = charToInt(tokens[0].charAt(0));
                final int pastureB = charToInt(tokens[1].charAt(0));
                final int dist = Integer.parseInt(tokens[2]);
                // keep shortest distance
                if (distance[pastureA][pastureB] > dist) {
                    distance[pastureA][pastureB] = dist;
                    distance[pastureB][pastureA] = dist;
                }
            }

            // use Floyd-Warshall algorithm to solve all pair shortest path
            for (int k = 0; k < distance.length; k++) {
                for (int i = 0; i < distance.length; i++) {
                    for (int j = 0; j < distance.length; j++) {
                        if (distance[i][j] >= (distance[i][k] + distance[k][j])) {
                            distance[i][j] = distance[i][k] + distance[k][j];
                        }
                    }
                }
            }

            // output pasture with cow that arrives back first and length of shortest path
            int pasture = Integer.MAX_VALUE;
            int shortestDistance = -1;
            for (int i = 0; i < 25; i++) {
                int dist = distance[25][i];
                if ((shortestDistance == -1) || (dist < shortestDistance)) {
                    pasture = i;
                    shortestDistance = dist;
                }
            }
            out.println(intToChar(pasture) + " " + shortestDistance);
        }
    }

    private static int charToInt(char character) {
        final int number = character - 65;
        if ((number >= 0) && (number <= 25)) {
            // uppercase (pasture with cow) where Z is barn
            // values 0 (A) to 25 (Z)
            return number;
        } else if ((number >= 32) && (number <= 57)) {
            // lowercase (pasture with no cow)
            // values 26 (a) to 51 (z)
            return number - 6;
        } else {
            return -1; // invalid
        }
    }

    private static char intToChar(int number) {
        if ((number >= 0) && (number <= 25)) {
            // uppercase (pasture with cow) where Z is barn
            // values 0 (A) to 25 (Z)
            return (char) (number + 65);
        } else if ((number >= 26) && (number <= 51)) {
            // lowercase (pasture with no cow)
            // values 26 (a) to 51 (z)
            return (char) (number + 65 + 6);
        } else {
            return ' '; // invalid
        }
    }
}