import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private final int size;
    private final int[][] board;
    private int digits = 0;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {

        // assumes that the board is square
        size = tiles.length;
        board = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tiles[i][j];
            }
        }

        int num = size * size - 1;
        while (num != 0) {
            num /= 10;
            digits++;
        }

    }

    // string representation of this board
    public String toString() {
        assert digits > 0;
        assert digits <= 5;

        StringBuilder output = new StringBuilder();
        output.append(String.format("%d\n", size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (digits == 1) output.append(String.format("%1d ", board[i][j]));
                if (digits == 2) output.append(String.format("%2d ", board[i][j]));
                if (digits == 3) output.append(String.format("%3d ", board[i][j]));
                if (digits == 4) output.append(String.format("%4d ", board[i][j]));
                if (digits == 5) output.append(String.format("%5d ", board[i][j]));
            }
            output.append("\n");
        }
        return output.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int out = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int expected = (i * size) + j + 1;
                if ((board[i][j] != expected) && (board[i][j] != 0)) out++;
                //                StdOut.printf("%d ?? %d\n", board[i][j], expected);
            }
        }
        return out;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int element = board[i][j];
                int expectedRow = (element - 1) / size;
                int expectedCol = (element - 1) % size;
                if (element == 0) {
                    expectedRow = i;
                    expectedCol = j;
                }
                // i is the current row and j is the current col
                dist += Math.abs(i - expectedRow);
                dist += Math.abs(j - expectedCol);
                //        StdOut.printf("%d (%d, %d) ?? %d (%d, %d) \n",
                //        board[i][j], i, j, dist, expectedRow, expectedCol);
            }
        }
        return dist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {

        // comparing to null --> F
        if (y == null) return false;

        // if comparing to itself --> T
        if (y == this) return true;

        // make sure the objects are the same class
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        return ((this.size == that.size) &&
                Arrays.deepEquals(this.board, that.board));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> boards = new Queue<Board>();

        int zeroRow = 0;
        int zeroCol = 0;

        // find where the zero is
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }

        // try to move the zero "north"
        if (zeroRow > 0) {
            int[][] newTiles = new int[size][size];
            int temp = board[zeroRow - 1][zeroCol];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    newTiles[i][j] = board[i][j];
                    if ((i == zeroRow - 1) && (j == zeroCol)) newTiles[i][j] = 0;
                    if ((i == zeroRow) && (j == zeroCol)) newTiles[i][j] = temp;
                }
            }
            Board bN = new Board(newTiles);
            boards.enqueue(bN);
        }

        // try to move the zero "south"
        if (zeroRow < size - 1) {
            int[][] newTiles = new int[size][size];
            int temp = board[zeroRow + 1][zeroCol];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    newTiles[i][j] = board[i][j];
                    if ((i == zeroRow + 1) && (j == zeroCol)) newTiles[i][j] = 0;
                    if ((i == zeroRow) && (j == zeroCol)) newTiles[i][j] = temp;
                }
            }
            Board bS = new Board(newTiles);
            boards.enqueue(bS);
        }

        // try to move the zero "west"
        if (zeroCol > 0) {
            int[][] newTiles = new int[size][size];
            int temp = board[zeroRow][zeroCol - 1];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    newTiles[i][j] = board[i][j];
                    if ((i == zeroRow) && (j == zeroCol - 1)) newTiles[i][j] = 0;
                    if ((i == zeroRow) && (j == zeroCol)) newTiles[i][j] = temp;
                }
            }
            Board bW = new Board(newTiles);
            boards.enqueue(bW);
        }

        // try to move the zero "east"
        if (zeroCol < size - 1) {
            int[][] newTiles = new int[size][size];
            int temp = board[zeroRow][zeroCol + 1];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    newTiles[i][j] = board[i][j];
                    if ((i == zeroRow) && (j == zeroCol + 1)) newTiles[i][j] = 0;
                    if ((i == zeroRow) && (j == zeroCol)) newTiles[i][j] = temp;
                }
            }
            Board bE = new Board(newTiles);
            boards.enqueue(bE);
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        assert size > 1;
        int[][] twinBoard = new int[size][size];

        // start with an exact copy
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                twinBoard[i][j] = board[i][j];
            }
        }

        // super lazy - first test if the top corner does not equal zero
        if (board[0][0] != 0) {
            if (board[0][1] != 0) { // try to swap to the right
                twinBoard[0][0] = board[0][1];
                twinBoard[0][1] = board[0][0];
            }
            else { // swap down
                twinBoard[0][0] = board[1][0];
                twinBoard[1][0] = board[0][0];
            }
        }
        else { // we know that the zero is on (0,0), swap (1,0) with (1,1)
            twinBoard[1][0] = board[1][1];
            twinBoard[1][1] = board[1][0];
        }

        return new Board(twinBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        StdOut.println("Board unit tests");

        int n = 3;
        int[][] tiles = new int[n][n];
        tiles[0][0] = 3;
        tiles[0][1] = 0;
        tiles[0][2] = 6;
        tiles[1][0] = 7;
        tiles[1][1] = 4;
        tiles[1][2] = 1;
        tiles[2][0] = 8;
        tiles[2][1] = 2;
        tiles[2][2] = 5;

        Board b = new Board(tiles);
        StdOut.println(b.toString());
        StdOut.printf("Hamming: %d \t Manhattan: %d\n",
                      b.hamming(), b.manhattan());


        int[][] tiles2 = new int[n][n];
        tiles2[0][0] = 8;
        tiles2[0][1] = 1;
        tiles2[0][2] = 3;
        tiles2[1][0] = 4;
        tiles2[1][1] = 0;
        tiles2[1][2] = 2;
        tiles2[2][0] = 7;
        tiles2[2][1] = 6;
        tiles2[2][2] = 5;

        Board b2 = new Board(tiles2);
        StdOut.println(b2.toString());
        StdOut.printf("Hamming: %d \t Manhattan: %d \n",
                      b2.hamming(), b2.manhattan());

        Board b1 = b;

        StdOut.printf("Board 1 equals board 1? %s\n", b.equals(b1) ? "true" : "false");
        StdOut.printf("Board 1 equals board 2? %s\n", b.equals(b2) ? "true" : "false");

        StdOut.println("\nNeighbors");
        StdOut.println(b);
        StdOut.println("Its twin");
        StdOut.println(b.twin());
        for (Board brd : b.neighbors()) {
            StdOut.println(brd);
            StdOut.println("Its twin");
            StdOut.println(brd.twin());
        }


    }

}
