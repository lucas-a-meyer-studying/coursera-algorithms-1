import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

class TestPoints {
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        StdOut.println("Fast");
        FastCollinearPoints fcollinear = new FastCollinearPoints(points);
        StdOut.println(fcollinear.numberOfSegments());
        for (LineSegment segment : fcollinear.segments()) {
            StdOut.println(segment);
            // segment.draw();
        }
        // StdDraw.show();
    }
}
