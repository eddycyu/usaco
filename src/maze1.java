/*
ID: eddycyu
LANG: JAVA
TASK: maze1
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class maze1 {

    public static void main(String[] args) throws IOException {

        try (
                final BufferedReader f = new BufferedReader(new FileReader("maze1.in"));
                final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("maze1.out")))
        ) {
            // read width and height of maze
            String[] tokens = f.readLine().split(" ");
            final int W = Integer.parseInt(tokens[0]);
            final int H = Integer.parseInt(tokens[1]);

            // read maze[row][col]; value is true if cell is empty, else false if cell is a wall
            boolean[][] maze = new boolean[H * 2 + 1][W * 2 + 1];
            for (int row = 0; row < (H * 2 + 1); row++) {
                tokens = f.readLine().split("");
                for (int col = 0; col < (W * 2 + 1); col++) {
                    if (col < tokens.length) {
                        if (tokens[col].equals(" ")) {
                            maze[row][col] = true; // cell is empty
                        } else {
                            maze[row][col] = false; // cell is a wall
                        }
                    } else {
                        // ends with spaces
                        maze[row][col] = true; // cell is empty
                    }
                }
            }

            // solve (find best solution for each empty cell)
            int[][] results = new int[H * 2 + 1][W * 2 + 1];
            boolean[][] visited = new boolean[H * 2 + 1][W * 2 + 1];
            int[][] distance = new int[H * 2 + 1][W * 2 + 1];   // 0 = unknown, -1 = dead end, 1+ = steps to exit
            for (int row = 1; row < (H * 2 + 1); row += 2) {
                for (int col = 1; col < (W * 2 + 1); col += 2) {
                    if (maze[row][col]) {

                        // keep the minimum number of steps to reach an exit from current cell
                        int newCount = solve(W, H, row, col, maze, distance, visited);
                        if ((newCount < distance[row][col]) || (distance[row][col] <= 0)) {
                            distance[row][col] = newCount;
                        }

                        for (int i = 1; i < (H * 2 + 1); i += 2) {
                            for (int j = 1; j < (W * 2 + 1); j += 2) {
                                // reset all -1 (dead end) back to 0 (unknown)
                                if (distance[i][j] == -1) {
                                    distance[i][j] = 0;
                                }
                                // keep track of best results
                                if ((distance[i][j] != 0) && ((results[i][j] == 0) || (distance[i][j] < results[i][j]))) {
                                    results[i][j] = distance[i][j];
                                }
                            }
                        }
                    }
                }
            }

            // output highest value
            int result = 0;
            for (int row = 1; row < (H * 2 + 1); row += 2) {
                for (int col = 1; col < (W * 2 + 1); col += 2) {
                    if (results[row][col] > result) {
                        result = results[row][col];
                    }
                }
            }
            out.println(result);
            System.out.println(result);
        }
    }

    private static int solve(int W, int H, int row, int col, boolean[][] maze, int[][] distance,
                             boolean[][] visited) {

        //printMaze(W, H, row, col, maze, distance, visited);

        // reached outside of maze
        if ((row < 0) || (row >= (2 * H + 1)) || (col < 0) || (col >= (2 * W + 1))) {
            return 1;
        }

        // reach a cell that leads to a dead end
        if (distance[row][col] == -1) {
            return 0;
        }

        // reached a cell with known distance to exit
        //if (distance[row][col] > 0) {
        //    return distance[row][col];
        //}

        boolean isDeadEnd = true;

        // try moving N
        if (((row >= 3) && maze[row - 1][col] && (distance[row - 2][col] != -1) && !visited[row - 2][col])    // inner cell not visited before
                || ((row == 1) && maze[row - 1][col])) {                    // outer cell with open wall
            isDeadEnd = false;
            visited[row][col] = true;   // mark current cell as visited
            int newCount = solve(W, H, row - 2, col, maze, distance, visited);
            if ((newCount == -1) && (distance[row][col] <= 0)) {
                distance[row][col] = newCount; // dead-end
            }
            // keep the minimum number of steps to reach an exit from current cell
            else if ((newCount > 0) && ((newCount < distance[row][col]) || (distance[row][col] <= 0))) {
                distance[row][col] = newCount;
            }
            visited[row][col] = false;  // un-mark current cell as visited
            if (row == 3 && col == 1) {
                System.out.println("here");
            }
        }
        // try moving E
        if (((col < (2 * W + 1) - 2) && maze[row][col + 1] && (distance[row][col + 2] != -1) && !visited[row][col + 2])   // inner cell not visited before
                || ((col == (2 * W + 1) - 2) && maze[row][col + 1])) {                  // outer cell with open wall
            isDeadEnd = false;
            visited[row][col] = true;   // mark current cell as visited
            int newCount = solve(W, H, row, col + 2, maze, distance, visited);
            if ((newCount == -1) && (distance[row][col] <= 0)) {
                distance[row][col] = newCount; // dead-end
            }
            // keep the minimum number of steps to reach an exit from current cell
            else if ((newCount > 0) && ((newCount < distance[row][col]) || (distance[row][col] <= 0))) {
                distance[row][col] = newCount;
            }
            visited[row][col] = false;  // un-mark current cell as visited
        }
        // try moving S
        if (((row < (2 * H + 1) - 2) && maze[row + 1][col] && (distance[row + 2][col] != -1) && !visited[row + 2][col])   // inner cell not visited before
                || ((row == ((2 * H + 1) - 2)) && maze[row + 1][col])) {                // outer cell with open wall
            isDeadEnd = false;
            visited[row][col] = true;   // mark current cell as visited
            int newCount = solve(W, H, row + 2, col, maze, distance, visited);
            if ((newCount == -1) && (distance[row][col] <= 0)) {
                distance[row][col] = newCount; // dead-end
            }
            // keep the minimum number of steps to reach an exit from current cell
            else if ((newCount > 0) && ((newCount < distance[row][col]) || (distance[row][col] <= 0))) {
                distance[row][col] = newCount;
            }
            visited[row][col] = false;  // un-mark current cell as visited
        }
        // try moving W
        if (((col >= 3) && maze[row][col - 1] && (distance[row][col - 2] != -1) && !visited[row][col - 2])    // inner cell not visited before
                || ((col == 1) && maze[row][col - 1])) {                    // outer cell with open wall
            isDeadEnd = false;
            visited[row][col] = true;   // mark current cell as visited
            int newCount = solve(W, H, row, col - 2, maze, distance, visited);
            if ((newCount == -1) && (distance[row][col] <= 0)) {
                distance[row][col] = newCount; // dead-end
            }
            // keep the minimum number of steps to reach an exit from current cell
            else if ((newCount > 0) && ((newCount < distance[row][col]) || (distance[row][col] <= 0))) {
                distance[row][col] = newCount;
            }
            visited[row][col] = false;  // un-mark current cell as visited
        }

        // couldn't go anywhere; this cell is a dead end if there is no path to an exit
        if (isDeadEnd) {
            distance[row][col] = -1;
            return -1;
        }

        // this cell contains a path to an exit
        if (distance[row][col] > 0) {
            return distance[row][col] + 1;
        } else {
            return distance[row][col];
        }
    }

    private static void printMaze(int W, int H, int row, int col, boolean[][] maze, int[][] distance,
                                  boolean[][] visited) {
        for (int i = 0; i < (2 * H + 1); i++) {
            for (int j = 0; j < (2 * W + 1); j++) {
                if (!maze[i][j]) {
                    System.out.print("*");
                } else {
                    if ((i == row) && (j == col)) {
                        System.out.print("H");
                    } else if (visited[i][j]) {
                        //System.out.print("x");
                        //System.out.print(distance[i][j]);
                        if (distance[i][j] == -1) {
                            System.out.print("N");
                        } else {
                            System.out.print("x");
                        }
                    } else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println("");
        }
    }
}