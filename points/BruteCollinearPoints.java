import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segs;
    private int numSegments;

    public BruteCollinearPoints(Point[] points) {

        // finds all line segments containing 4 points
        if (points == null)
            throw new IllegalArgumentException();

        Point[] pts = points.clone();
        checkNulls(pts);
        checkDuplicates(pts);

        int k = 4;
        int n = pts.length;
        numSegments = 0;
        segs = new LineSegment[0];

        combinations(pts, n, k);

    }

    private void checkNulls(Point[] pts) {
        for (Point p : pts)
            if (p == null)
                throw new IllegalArgumentException();
    }

    private void checkDuplicates(Point[] pts) {
        Arrays.sort(pts);
        for (int i = 1; i < pts.length; i++) {
            if (pts[i].compareTo(pts[i - 1]) == 0)
                throw new IllegalArgumentException();
        }

    }

    private void findSegments(Point[] points, int k) {

        Arrays.sort(points);
        double slope = points[0].slopeTo(points[1]);
        boolean sameSlope = true;
        for (int i = 2; i < k; i++) {
            double currSlope = points[0].slopeTo(points[i]);
            if ((slope < currSlope) || (slope > currSlope))
                sameSlope = false;
        }
        if (sameSlope) {
            numSegments++;
            int oldLength = segs.length;
            if (oldLength < numSegments) { // need to expand the array size
                LineSegment[] newSegs = new LineSegment[numSegments * 2];
                for (int s = 0; s < oldLength; s++) {
                    newSegs[s] = segs[s];
                }
                segs = newSegs.clone();

            }
            LineSegment segmentToAdd = new LineSegment(points[0], points[k - 1]);
            segs[numSegments - 1] = segmentToAdd;

        }

    }

    private void combinations(Point[] points, int n, int k) {

        int[] pointers = new int[k];
        int i = 0;
        int r = 0;

        // Forward-backard
        while (r >= 0) {
            if (i <= n + r - k) {
                pointers[r] = i;

                if (r == k - 1) {
                    Point[] sample = new Point[k];
                    for (int p = 0; p < k; p++) {
                        sample[p] = points[pointers[p]];
                    }
                    findSegments(sample, k);
                    i++;
                } else {
                    i = pointers[r] + 1;
                    r++;
                }
            } else {
                // backward step
                r--;
                if (r >= 0)
                    i = pointers[r] + 1;

            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return numSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] s = new LineSegment[numSegments];

        for (int i = 0; i < numSegments; i++) {
            s[i] = segs[i];
        }


        return s;
    }

    public static void main(String[] args) {
        // unit tests
        StdOut.println("Tests");

    }

}

