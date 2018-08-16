import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] points;
    private LineSegment[] lineSegments;
    private int numOfLineSegment;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) { throw new IllegalArgumentException(); }

        /* takes 2 * n * log(n) time complixety here */
        for (int i = 0; i < points.length; i++) {                // check whether exist null pointer
            if (points[i] == null) { throw new IllegalArgumentException(); }
        }

        Arrays.sort(points);                                    // sort by compareTo() of Point first
        for (int i = 0; i < points.length; i++) {
            if ((i + 1) != points.length) {                     // check whether exist repeated points
                if (points[i].compareTo(points[i + 1]) == 0) { throw new IllegalArgumentException(); }
            }
        }

        this.points = points;
        this.numOfLineSegment = 0;
        this.lineSegments = new LineSegment[10];
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    if (points[j].slopeTo(points[i]) != points[k].slopeTo(points[j])) {   // if the first three points are not collinear then switch to another point
                        continue;
                    } else {
                        for (int s = k + 1; s < points.length; s++) {
                            if (points[k].slopeTo(points[j]) == points[s].slopeTo(points[k])) {
                                if (numOfLineSegment + 3 >= lineSegments.length) {
                                    lineSegments = Arrays.copyOf(lineSegments, lineSegments.length * 2);
                                }
                                lineSegments[numOfLineSegment++] = new LineSegment(points[i], points[s]);
                            }
                        }
                    }
                }
            }
        }
    }

    public LineSegment[] segments() {
        int index = 0;
        LineSegment[] segments = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; i++) {
            if (lineSegments[i] != null) {
                segments[index++] = lineSegments[i];
            }
        }
        return Arrays.copyOf(segments, index);
    }

    public int numberOfSegments() {
        return numOfLineSegment;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
