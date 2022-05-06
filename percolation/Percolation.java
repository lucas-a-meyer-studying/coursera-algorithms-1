/* *****************************************************************************
 *  Name: Lucas A. Meyer
 *  Date: 2019-12-28
 *  Description: Assignment for Coursera Algs 1
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf2;
    private boolean[] open;
    private int size;
    private int nOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n has to be greater than 0");
        else {
            // n^2 + 2 because 0 and n^2 + 1 are phantom nodes
            uf = new WeightedQuickUnionUF((n * n) + 2);
            uf2 = new WeightedQuickUnionUF((n * n) + 1);
            open = new boolean[n * n + 2];
            nOpen = 0;
            size = n;
        }
    }

    // Translate row and col to position in the array
    private int translate(int row, int col) {
        return ((row - 1) * size) + col;
    }

    private void connect(int src, int dest) {
        uf.union(src, dest);
        if (dest != size * size + 1)
            uf2.union(src, dest);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > size || col > size)
            throw new IllegalArgumentException("row and col need to be > 0");

        if (!isOpen(row, col)) {
            open[translate(row, col)] = true;
            nOpen++;

            // north
            if (row != 1) {
                if (isOpen(row - 1, col))
                    connect(translate(row, col), translate(row - 1, col));
            }
            else // if row == 1, connect to phantom node
                connect(translate(row, col), 0);

            // south
            if (row != size) {
                if (isOpen(row + 1, col))
                    connect(translate(row + 1, col), translate(row, col));
            }
            else
                connect(translate(row, col), size * size + 1);

            // east - do nothing if col == size
            if (col != size) {
                if (isOpen(row, col + 1))
                    connect(translate(row, col), translate(row, col + 1));
            }
            // west - do nothing if col == 1
            if (col != 1) {
                if (isOpen(row, col - 1))
                    connect(translate(row, col), translate(row, col - 1));
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > size || col > size)
            throw new IllegalArgumentException("row and col need to be > 0");
        return open[translate(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > size || col > size)
            throw new IllegalArgumentException("row and col need to be > 0");
        // A cell will be full if it connects to the top phantom node
        return uf2.connected(0, translate(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(0, size * size + 1);
    }

    /* private void print() {
        StdOut.printf("Canonical for root: %d, Canonical for sink %d\n",
                      uf.find(0), uf.find(size * size + 1));

        for (int i = 1; i <= size; i++)
            for (int j = 1; j <= size; j++) {
                StdOut.printf("Cell (%d, %d) is %s and its canonical is %d.\n",
                              i, j, isOpen(i, j) ? "open" : "closed",
                              uf.find(translate(i, j)));
            }

    } */

    // test client (optional)
    public static void main(String[] args) {
        // empty
    }
}
