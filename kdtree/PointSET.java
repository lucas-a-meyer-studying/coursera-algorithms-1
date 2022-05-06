import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> points = new TreeSet<>();

    public PointSET() {
        // construct an empty set of points
    }

    public boolean isEmpty() {
        // is the set empty?
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);

    }

    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException();

        List<Point2D> pointList = new ArrayList<>();

        for (Point2D p : points) {
            if (rect.contains(p))
                pointList.add(p);
        }

        return pointList;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new IllegalArgumentException();

        if (points.isEmpty())
            return null;

        Point2D minPoint = null;

        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D q : points) {
            double dist = p.distanceSquaredTo(q);
            if (p.distanceSquaredTo(q) < minDistance) {
                minPoint = q;
                minDistance = dist;
            }
        }
        return minPoint;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        StdOut.println("Tests");

        PointSET ps = new PointSET();

        StdOut.printf("Size of PointSET should be 0 and it's %d\n", ps.size());
        ps.insert(new Point2D(0.3, 0.4));
        StdOut.printf("Size of PointSET should be 1 and it's %d\n", ps.size());
        ps.insert(new Point2D(0.4, 0.3));
        ps.insert(new Point2D(0.9, 0.9));
        ps.insert(new Point2D(0.1, 0.8));
        RectHV r = new RectHV(0.1, 0.1, 0.8, 0.8);

        StdOut.println("Points in rectangle:");
        for (Point2D p : ps.range(r)) {
            StdOut.println(p.toString());
        }

        StdOut.println("Point nearest to (1.0, 1.0):");
        StdOut.println(ps.nearest(new Point2D(1, 1)));

        StdOut.println("Tests completed");
    }
}
