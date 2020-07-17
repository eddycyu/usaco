/*
ID: eddycyu
LANG: JAVA
TASK: ttwo
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ttwo {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("ttwo.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ttwo.out")))) {

            // forest is 10x10 grid
            // [0][0] = upper left corner
            final String[][] grid = new String[10][10];

            // starting direction (0 = north, 1 = east, 2 = south, 3 = west)
            int farmerDirection = 0;
            int cowsDirection = 0;

            // location index
            int farmerI = -1;
            int farmerJ = -1;
            int cowsI = -1;
            int cowsJ = -1;

            // read grid (remember starting index locations for Farmer John and cows)
            for (int i = 0; i < 10; i++) {
                String[] tokens = f.readLine().split("");
                for (int j = 0; j < 10; j++) {
                    grid[i][j] = tokens[j];
                    if (grid[i][j].equals("F")) {
                        farmerI = i;
                        farmerJ = j;
                    } else if (grid[i][j].equals("C")) {
                        cowsI = i;
                        cowsJ = j;
                    }
                }
            }

            // used for keeping track of previously visited location and direction (for detecting cycles)
            final boolean[][] farmerVisited = new boolean[10][10];
            final int[][] farmerVisitedDirection = new int[10][10];
            final boolean[][] cowsVisited = new boolean[10][10];
            final int[][] cowsVisitedDirection = new int[10][10];

            // record visit and direction at starting location
            farmerVisited[farmerI][farmerJ] = true;
            farmerVisitedDirection[farmerI][farmerJ] = farmerDirection;
            cowsVisited[cowsI][cowsJ] = true;
            cowsVisitedDirection[cowsI][cowsJ] = cowsDirection;

            // solve
            int minutes = 0;
            while (true) {
                minutes++;

                /*
                 * process Farmer John
                 */

                // facing north on upper row or facing east on right column
                // or facing south on bottom row or facing west on left column;
                // cannot move forward; just rotate
                if (((farmerDirection == 0) && (farmerI == 0))
                        || ((farmerDirection == 1) && (farmerJ == 9))
                        || ((farmerDirection == 2) && (farmerI == 9))
                        || ((farmerDirection == 3) && (farmerJ == 0))) {
                    farmerDirection = (farmerDirection + 1) % 4;
                }
                // facing north with obstacle in the way or facing east with obstacle in the way
                // or facing south with obstacle in the way or facing west with obstacle in the way;
                // cannot move forward; just rotate
                else if (((farmerDirection == 0) && (grid[farmerI - 1][farmerJ].equals("*")))
                        || ((farmerDirection == 1) && (grid[farmerI][farmerJ + 1].equals("*")))
                        || ((farmerDirection == 2) && (grid[farmerI + 1][farmerJ].equals("*")))
                        || ((farmerDirection == 3) && (grid[farmerI][farmerJ - 1].equals("*")))) {
                    farmerDirection = (farmerDirection + 1) % 4;
                }
                // can move forward
                else {
                    if (farmerDirection == 0) {
                        farmerI -= 1;
                    } else if (farmerDirection == 1) {
                        farmerJ += 1;
                    } else if (farmerDirection == 2) {
                        farmerI += 1;
                    } else if (farmerDirection == 3) {
                        farmerJ -= 1;
                    }
                }

                /*
                 * process cows
                 */

                // facing north on upper row or facing east on right column
                // or facing south on bottom row or facing west on left column;
                // cannot move forward; just rotate
                if (((cowsDirection == 0) && (cowsI == 0))
                        || ((cowsDirection == 1) && (cowsJ == 9))
                        || ((cowsDirection == 2) && (cowsI == 9))
                        || ((cowsDirection == 3) && (cowsJ == 0))) {
                    cowsDirection = (cowsDirection + 1) % 4;
                }
                // facing north with obstacle in the way or facing east with obstacle in the way
                // or facing south with obstacle in the way or facing west with obstacle in the way;
                // cannot move forward; just rotate
                else if (((cowsDirection == 0) && (grid[cowsI - 1][cowsJ].equals("*")))
                        || ((cowsDirection == 1) && (grid[cowsI][cowsJ + 1].equals("*")))
                        || ((cowsDirection == 2) && (grid[cowsI + 1][cowsJ].equals("*")))
                        || ((cowsDirection == 3) && (grid[cowsI][cowsJ - 1].equals("*")))) {
                    cowsDirection = (cowsDirection + 1) % 4;
                }
                // can move forward
                else {
                    if (cowsDirection == 0) {
                        cowsI -= 1;
                    } else if (cowsDirection == 1) {
                        cowsJ += 1;
                    } else if (cowsDirection == 2) {
                        cowsI += 1;
                    } else if (cowsDirection == 3) {
                        cowsJ -= 1;
                    }
                }

                // check if cycle
                if ((farmerVisited[farmerI][farmerJ] && (farmerVisitedDirection[farmerI][farmerJ] == farmerDirection))
                        && (cowsVisited[cowsI][cowsJ] && (cowsVisitedDirection[cowsI][cowsJ] == cowsDirection))) {
                    out.println(0);
                    break;
                } else {
                    // else record visit at new location and direction
                    farmerVisited[farmerI][farmerJ] = true;
                    farmerVisitedDirection[farmerI][farmerJ] = farmerDirection;
                    cowsVisited[cowsI][cowsJ] = true;
                    cowsVisitedDirection[cowsI][cowsJ] = cowsDirection;
                }

                // check if same location
                if ((farmerI == cowsI) && (farmerJ == cowsJ)) {
                    out.println(minutes);
                    break;  // terminate loop
                }
            }
        }
    }
}