/*
ID: eddycyu
LANG: JAVA
TASK: castle
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class castle {

    static final int WEST = 0x1;
    static final int NORTH = 0x2;
    static final int EAST = 0x4;
    static final int SOUTH = 0x8;

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("castle.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("castle.out")))) {

            // read castle dimensions
            final String[] dimensions = f.readLine().split(" ");
            final int maxColumns = Integer.parseInt(dimensions[0]);
            final int maxRows = Integer.parseInt(dimensions[1]);

            // read castle squares into array
            final int[][] squares = new int[maxRows][maxColumns];
            for (int row = 0; row < maxRows; row++) {
                final String[] tokens = f.readLine().split(" ");
                for (int col = 0; col < maxColumns; col++) {
                    squares[row][col] = Integer.parseInt(tokens[col]);
                }
            }

            // assign room numbers to each square
            final int[][] roomNumbers = new int[maxRows][maxColumns];
            final int numberOfRooms = assignAllRoomNumbers(roomNumbers, maxRows, maxColumns, squares);

            // output the number of rooms the castle has
            out.println(numberOfRooms);

            // compute room sizes and find the largest room size
            final int[] roomSizes = computeRoomSizes(roomNumbers, numberOfRooms, maxRows, maxColumns);
            final int maxRoomSize = getLargestRoomSize(roomSizes);

            // output the size of the largest room
            out.println(maxRoomSize);

            // remove walls (from westernmost and southernmost squares)
            int afterRemoveMaxRoomSize = maxRoomSize;
            int afterRemoveRow = maxRows;
            int afterRemoveCol = maxColumns;
            int afterRemoveWall = 0;
            for (int col = 0; col < maxColumns; col++) {
                for (int row = 0; row < maxRows; row++) {
                    final int[] result = removeWall(row, col, maxColumns, squares, roomNumbers, roomSizes);
                    final int lastSize = result[0];
                    final int lastRow = result[1] + 1;
                    final int lastCol = result[2] + 1;
                    if (lastSize > afterRemoveMaxRoomSize) {
                        afterRemoveMaxRoomSize = lastSize;
                        afterRemoveRow = lastRow;
                        afterRemoveCol = lastCol;
                        afterRemoveWall = result[3];
                    } else if ((lastSize == afterRemoveMaxRoomSize) && (lastCol <= afterRemoveCol)) {
                        afterRemoveRow = lastRow;
                        afterRemoveCol = lastCol;
                        afterRemoveWall = result[3];
                    }
                }
            }

            // output size of the largest room created by removing one wall
            out.println(afterRemoveMaxRoomSize);

            // output the single wall to remove to make the largest room possible
            out.println(afterRemoveRow + " " + afterRemoveCol + " " + (afterRemoveWall == NORTH ? "N" : "E"));
        }
    }

    private static boolean hasWestWall(int number) {
        return (number & WEST) == WEST;
    }

    private static boolean hasNorthWall(int number) {
        return (number & NORTH) == NORTH;
    }

    private static boolean hasEastWall(int number) {
        return (number & EAST) == EAST;
    }

    private static boolean hasSouthWall(int number) {
        return (number & SOUTH) == SOUTH;
    }

    private static int assignAllRoomNumbers(int[][] roomNumbers, int maxRows, int maxColumns, int[][] squares) {
        // first mark all rooms with room number = -1
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxColumns; col++) {
                roomNumbers[row][col] = -1;
            }
        }

        // then assign room numbers to each square
        int nextRoomNumber = 1;
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxColumns; col++) {
                nextRoomNumber = assignRoomNumber(roomNumbers, row, col, nextRoomNumber, maxRows, maxColumns, squares);
            }
        }

        // return the number of rooms
        return nextRoomNumber - 1;
    }

    private static int assignRoomNumber(int[][] roomNumbers, int row, int col, int nextRoomNumber,
                                        int maxRows, int maxColumns, int[][] squares) {
        // assign room number and increment counter for next room number
        if (roomNumbers[row][col] == -1) {
            roomNumbers[row][col] = nextRoomNumber++;
        }

        // check west wall
        if ((col != 0) && !hasWestWall(squares[row][col]) && (roomNumbers[row][col - 1] == -1)) {
            roomNumbers[row][col - 1] = roomNumbers[row][col];
            assignRoomNumber(roomNumbers, row, col - 1, nextRoomNumber, maxRows, maxColumns, squares);
        }

        // check south wall
        if ((row != maxRows - 1) && !hasSouthWall(squares[row][col]) && (roomNumbers[row + 1][col] == -1)) {
            roomNumbers[row + 1][col] = roomNumbers[row][col];
            assignRoomNumber(roomNumbers, row + 1, col, nextRoomNumber, maxRows, maxColumns, squares);
        }

        // check north wall
        if ((row != 0) && !hasNorthWall(squares[row][col]) && (roomNumbers[row - 1][col] == -1)) {
            roomNumbers[row - 1][col] = roomNumbers[row][col];
            assignRoomNumber(roomNumbers, row - 1, col, nextRoomNumber, maxRows, maxColumns, squares);
        }

        // check east wall
        if ((col != maxColumns - 1) && !hasEastWall(squares[row][col]) && (roomNumbers[row][col + 1] == -1)) {
            roomNumbers[row][col + 1] = roomNumbers[row][col];
            assignRoomNumber(roomNumbers, row, col + 1, nextRoomNumber, maxRows, maxColumns, squares);
        }

        return nextRoomNumber;
    }

    private static int[] computeRoomSizes(int[][] roomNumbers, int numberOfRooms, int maxRows, int maxColumns) {
        final int[] roomSizes = new int[numberOfRooms];
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxColumns; col++) {
                final int roomNumber = roomNumbers[row][col];
                roomSizes[roomNumber - 1] += 1;
            }
        }
        return roomSizes;
    }

    private static int getLargestRoomSize(int[] roomSizes) {
        int maxRoomSize = 0;
        for (int roomSize : roomSizes) {
            maxRoomSize = Math.max(maxRoomSize, roomSize);
        }
        return maxRoomSize;
    }

    public static int[] removeWall(int row, int col, int maxColumns, int[][] squares,
                                   int[][] roomNumbers, int[] roomSizes) {
        final int originalSquare = squares[row][col];
        final int currentRoomNumber = roomNumbers[row][col];

        int maxRoomSizeFromNorthWall = roomSizes[currentRoomNumber - 1];
        if ((row != 0) && hasNorthWall(squares[row][col])) {
            // remove north wall from current square
            final int modifiedSquare = originalSquare ^ NORTH;
            squares[row][col] = modifiedSquare;

            // remove south wall from adjoining square
            final int originalAdjoiningSquare = squares[row - 1][col];
            final int modifiedAdjoiningSquare = originalAdjoiningSquare ^ SOUTH;
            squares[row - 1][col] = modifiedAdjoiningSquare;

            // compute newly combined room size (if combined distinct rooms)
            final int adjoiningRoomNumber = roomNumbers[row - 1][col];
            if (currentRoomNumber != adjoiningRoomNumber) {
                maxRoomSizeFromNorthWall += roomSizes[adjoiningRoomNumber - 1];
            }

            // restore original walls
            squares[row][col] = originalSquare;
            squares[row - 1][col] = originalAdjoiningSquare;
        }

        int maxRoomSizeFromEastWall = roomSizes[currentRoomNumber - 1];
        if ((col != maxColumns - 1) && hasEastWall(squares[row][col])) {
            // remove east wall from current square
            final int modifiedSquare = originalSquare ^ EAST;
            squares[row][col] = modifiedSquare;

            // remove west wall for adjoining square
            final int originalAdjoiningSquare = squares[row][col + 1];
            final int modifiedAdjoiningSquare = originalAdjoiningSquare ^ WEST;
            squares[row][col + 1] = modifiedAdjoiningSquare;

            // compute newly combined room size (if combined distinct rooms)
            final int adjoiningRoomNumber = roomNumbers[row][col + 1];
            if (currentRoomNumber != adjoiningRoomNumber) {
                maxRoomSizeFromEastWall += roomSizes[adjoiningRoomNumber - 1];
            }

            // restore original walls
            squares[row][col] = originalSquare;
            squares[row][col + 1] = originalAdjoiningSquare;
        }

        // return the removal that resulted in the largest combined rooms
        if (maxRoomSizeFromNorthWall >= maxRoomSizeFromEastWall) {
            return new int[]{maxRoomSizeFromNorthWall, row, col, NORTH};
        } else {
            return new int[]{maxRoomSizeFromEastWall, row, col, EAST};
        }
    }
}
