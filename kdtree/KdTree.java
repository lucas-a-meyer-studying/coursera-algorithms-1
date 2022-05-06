import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private Node treeRoot;
    private int nItems;
    private int nVisits = 0;

    public KdTree() {
        treeRoot = null;
        nItems = 0;
    }

    public boolean isEmpty() {
        // is the set empty?
        return treeRoot == null;
    }

    public int size() {
        return nItems;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException();

        if (contains(p))
            return;

        double xMin = 0.0;
        double yMin = 0.0;
        double xMax = 1.0;
        double yMax = 1.0;

        if (treeRoot == null) {
            treeRoot = new Node(p, 0, xMin, yMin, xMax, yMax);
            nItems = nItems + 1;
        }
        else {
            Node pointer = treeRoot;
            Node insertPointer = null;
            int insertLevel = 0;
            boolean left = false;
            boolean horizontal = false;
            double pX = p.x();
            double pY = p.y();

            while (pointer != null) {
                insertLevel = insertLevel + 1;
                double pointScalar;
                double compScalar;
                if (insertLevel % 2 != 0) {
                    pointScalar = pX;
                    compScalar = pointer.getPoint().x();
                    horizontal = true;
                }
                else {
                    pointScalar = pY;
                    compScalar = pointer.getPoint().y();
                    horizontal = false;
                }
                if (pointScalar < compScalar) {
                    left = true;
                    if (horizontal)
                        xMax = compScalar;
                    else
                        yMax = compScalar;

                    insertPointer = pointer;
                    pointer = pointer.getLeft();

                }
                else {
                    left = false;
                    if (horizontal)
                        xMin = compScalar;
                    else
                        yMin = compScalar;
                    insertPointer = pointer;
                    pointer = pointer.getRight();
                }
            }
            // found where to insert
            if (left) {
                insertPointer.setLeft(
                        new Node(p, insertLevel, xMin, yMin, xMax, yMax));
            }
            else {
                insertPointer.setRight(
                        new Node(p, insertLevel, xMin, yMin, xMax, yMax));
            }
            nItems = nItems + 1;
        }
    }


    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null)
            throw new IllegalArgumentException();
        //        return points.contains(p);
        if (isEmpty())
            return false;

        Node pointer = treeRoot;
        double pY = p.y();
        double pX = p.x();

        int searchLevel = 0;
        while (pointer != null) {
            searchLevel = searchLevel + 1;
            double pointScalar;
            double compScalar;
            if (searchLevel % 2 != 0) {
                pointScalar = pX;
                compScalar = pointer.getPoint().x();
            }
            else {
                pointScalar = pY;
                compScalar = pointer.getPoint().y();
            }
            // check if we found the point
            if (p.equals(pointer.getPoint()))
                return true;

            if (pointScalar < compScalar) {
                pointer = pointer.getLeft();
            }
            else {
                pointer = pointer.getRight();
            }
        }
        // got to a null point
        return false;
    }


    public void draw() {

        // enqueue all points left to right into q
        Queue<Node> q = new Queue<Node>();
        inorder(treeRoot, q);

        // clear and draw
        StdDraw.clear();
        for (Node n : q) {
            // StdOut.println(n.toString());

            // draw the points
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            n.getPoint().draw();

            // draw the rectangle, dividing it
            // using the appropriate point coordinate
            StdDraw.setPenRadius();
            if (n.getLevel() % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                RectHV bounds = n.getRect();
                Point2D line = n.getPoint();
                RectHV r = new RectHV(line.x(), bounds.ymin(), line.x(), bounds.ymax());
                r.draw();
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                RectHV bounds = n.getRect();
                Point2D line = n.getPoint();
                RectHV r = new RectHV(bounds.xmin(), line.y(), bounds.xmax(), line.y());
                r.draw();
            }

            StdDraw.setPenColor(StdDraw.BLACK);

        }
    }

    private void inorder(Node x, Queue<Node> q) {
        if (x == null) return;
        inorder(x.getLeft(), q);
        q.enqueue(x);
        inorder(x.getRight(), q);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        // all points that are inside the rectangle (or on the boundary)
        List<Point2D> inPoints = new LinkedList<>();
        Stack<Node> nodesToSearch = new Stack<Node>();

        nodesToSearch.push(treeRoot);

        while (!nodesToSearch.isEmpty()) {
            Node n = nodesToSearch.pop();

            if (n != null) {
                Point2D currentPoint = n.getPoint();
                int level = n.getLevel();

                if (level % 2 == 0) {
                    // horizontal
                    double x = currentPoint.x();
                    if (rect.xmax() <= x)
                        nodesToSearch.push(n.getLeft());
                    if (rect.xmin() >= x)
                        nodesToSearch.push(n.getRight());
                    if (rect.xmin() <= x && rect.xmax() >= x) {
                        if (rect.contains(currentPoint)) inPoints.add(currentPoint);
                        nodesToSearch.push(n.getLeft());
                        nodesToSearch.push(n.getRight());
                    }
                }
                else {
                    // vertical
                    double y = currentPoint.y();
                    if (rect.ymax() <= y)
                        nodesToSearch.push(n.getLeft());
                    if (rect.ymin() >= y)
                        nodesToSearch.push(n.getRight());
                    if (rect.ymin() <= y && rect.ymax() >= y) {
                        if (rect.contains(currentPoint)) inPoints.add(currentPoint);
                        nodesToSearch.push(n.getLeft());
                        nodesToSearch.push(n.getRight());
                    }
                }
            }
        }

        List<Point2D> newList = new LinkedList<>();
        Collections.sort(inPoints);
        Point2D lastPointSeen = null;
        for (Point2D point : inPoints) {
            if (lastPointSeen == point) continue;
            lastPointSeen = point;
            newList.add(point);
        }
        return newList;
    }

    private Point2D searchNearest(Point2D p, Node n, double minDist, Point2D minPoint) {

        if (n != null) {
            if ((n.getRect().distanceSquaredTo(p)) < minDist) {

                nVisits = nVisits + 1;

                // determine whether Node has horizontal or vertical line
                boolean horizontal = (n.getLevel() % 2 != 0);

                // determine direction to go:
                boolean left;

                if (horizontal) left = p.y() < n.getPoint().y();
                else left = p.x() < n.getPoint().x();

                double dist = p.distanceSquaredTo(n.getPoint());
                if (dist < minDist) {
                    // found a closer point
                    minPoint = n.getPoint();
                    minDist = dist;
                }

                if (left) {
                    minPoint = searchNearest(p, n.getLeft(), minDist, minPoint);
                    minPoint = searchNearest(p, n.getRight(), minPoint.distanceSquaredTo(p),
                                             minPoint);
                }
                else {
                    minPoint = searchNearest(p, n.getRight(), minDist, minPoint);
                    minPoint = searchNearest(p, n.getLeft(), minPoint.distanceSquaredTo(p),
                                             minPoint);
                }

            }
        }

        return minPoint;

    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty

        nVisits = 0;

        if (p == null)
            throw new IllegalArgumentException();

        return searchNearest(p, treeRoot, Double.POSITIVE_INFINITY, null);
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect = null;    // the axis-aligned rectangle corresponding to this node
        private Node lb = null;        // the left/bottom subtree
        private Node rt = null;        // the right/top subtree
        private int level = 0;

        public Node(Point2D p, int level,
                    double xMin, double yMin, double xMax, double yMax) {

            this.p = p;
            this.level = level;
            this.rect = new RectHV(xMin, yMin, xMax, yMax);

        }

        public Point2D getPoint() {
            return p;
        }

        public Node getLeft() {
            return lb;
        }

        public Node getRight() {
            return rt;
        }

        public void setLeft(Node n) {
            this.lb = n;
        }

        public void setRight(Node n) {
            this.rt = n;
        }

        public void setRect(RectHV r) {
            rect = r;
        }

        public RectHV getRect() {
            return rect;
        }

        public int getLevel() {
            return level;
        }

        public String toString() {
            return String.format("Level: %4d \t\t (%07.6f, %07.6f)\n", level, p.x(), p.y());
        }

    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        StdOut.println("Tests");

        KdTree ps = new KdTree();


        ps.insert(new Point2D(0.206107, 0.095492));
        ps.insert(new Point2D(0.975528, 0.654508));
        ps.insert(new Point2D(0.024472, 0.345492));
        ps.insert(new Point2D(0.793893, 0.095492));
        ps.insert(new Point2D(0.793893, 0.904508));
        ps.insert(new Point2D(0.975528, 0.345492));
        ps.insert(new Point2D(0.206107, 0.904508));
        ps.insert(new Point2D(0.500000, 0.000000));
        ps.insert(new Point2D(0.024472, 0.654508));
        ps.insert(new Point2D(0.500000, 1.000000));
        StdOut.printf("Size: %d\n\n", ps.size());

        ps.draw();

        StdOut.println(ps.nearest(new Point2D(0.5, 0.01)));
        StdOut.println("Tests completed");
    }

}
