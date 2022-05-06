/* *****************************************************************************
 *  Name: Lucas A. Meyer
 *  Date: 2019-12-28
 *  Description: Assignment for Coursera Algs 1
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final double[] trialsUntilPercolation;
    private final int trials;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Invalid parameters");

        this.trials = trials;
        trialsUntilPercolation = new double[trials];

        for (int trial = 0; trial < trials; trial++) {
            Percolation p = new Percolation(n);
            while (!p.percolates())
                p.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);

            trialsUntilPercolation[trial] = (double) p.numberOfOpenSites() / ((double) (n * n));
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean <= 0)
            mean = StdStats.mean(trialsUntilPercolation);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddev <= 0)
            stddev = StdStats.stddev(trialsUntilPercolation);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n;
        int trials;

        if (args.length < 2) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        else {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);

            if (n <= 0 || trials <= 0)
                throw new IllegalArgumentException("N and trials need to be > 0");
        }

        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.printf("mean                    = %f\n", ps.mean());
        StdOut.printf("stddev                  = %f\n", ps.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n",
                      ps.confidenceLo(), ps.confidenceHi());


    }
}
