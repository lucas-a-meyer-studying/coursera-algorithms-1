import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;


public class FastCollinearPoints {


    private final ArrayList<LineSegment> segs = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (points == null)
            throw new IllegalArgumentException();

        checkNulls(points);

        Point[] slopeOrderedPoints = points.clone();
        checkDuplicates(slopeOrderedPoints);

        int nPoints = slopeOrderedPoints.length;

        for (int pointerCurrentPoint = 0; pointerCurrentPoint < nPoints - 3; pointerCurrentPoint++) {

            Arrays.sort(slopeOrderedPoints);

            // sort by slopes with respect to the point being examined
            // remember that it's guaranteed that the point being examined
            // will be the first point (position 0) of the sorted array
            Arrays.sort(slopeOrderedPoints, slopeOrderedPoints[pointerCurrentPoint].slopeOrder());

            // try to find all sequences of at least 3 similar slopes
            // once the sequence is found, we'll cut it out and
            // add it to a segment
            int first = 1;
            int last = 2;

            // will cut segments until can't cut three elements
            for (int p = 0; last < slopeOrderedPoints.length; last++) {

                // find last point that has the same slope as p
                while (last < slopeOrderedPoints.length
                        && Double.compare(slopeOrderedPoints[p].slopeTo(slopeOrderedPoints[first]),
                        slopeOrderedPoints[p].slopeTo(slopeOrderedPoints[last])) == 0) {
                    last++;
                }

                // because the points are in order, once a point has been entered it can't enter again
                // all new points entered need to be bigger than the current point
                // also, we'll only create a segment if it has 3 or more elements (+p will make it a 4-point segment)
                if (last - first >= 3 && slopeOrderedPoints[p].compareTo(slopeOrderedPoints[first]) < 0) {
                    segs.add(new LineSegment(slopeOrderedPoints[p], slopeOrderedPoints[last - 1]));
                }

                // skip to the last examined point and move on
                first = last;
            }
        }

    }

    private void checkNulls(Point[] pts) {
        for (Point p : pts)
            if (p == null)
                throw new IllegalArgumentException();
    }

    private void checkDuplicates(Point[] pts) {
        // check for same point appearing more than once or null points
        if (pts[0] == null)
            throw new IllegalArgumentException();

        Arrays.sort(pts);
        for (int i = 1; i < pts.length; i++) {
            if (pts[i] == null)
                throw new IllegalArgumentException();
            if (pts[i].compareTo(pts[i - 1]) == 0)
                throw new IllegalArgumentException();
        }

    }

    public int numberOfSegments() {
        // the number of line segments
        return segs.size();
    }

    public LineSegment[] segments() {
        return segs.toArray(new LineSegment[segs.size()]);
    }

    public static void main(String[] args) {
        StdOut.println("Tests");
    }
}
