import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private int numOfEndPoints;
    private Point[] endPoints;
    private double[] slopes;
    private Point[] points;
    private LineSegment[] lineSegments;
    private int numOfLineSegment;
    private int numOfSlopes;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        /* takes 2 * n * log(n) time complixety here */
        for (int i = 0; i < points.length; i++) {              // check whether exist null pointer
            if (points[i] == null) { throw new IllegalArgumentException(); }
        }

        Arrays.sort(points);                                  // sort by compareTo() of Point first
        for (int i = 0; i < points.length; i++) {
            if ((i + 1) != points.length) {               // check whether exist repeated points
                if (points[i].compareTo(points[i + 1]) == 0) { throw new IllegalArgumentException(); }
            }
        }

        Point[] subarr = new Point[0];
        this.points = points;
        this.numOfLineSegment = 0;
        this.lineSegments = new LineSegment[10];
        this.endPoints = new Point[1];
        this.slopes = new double[1];
        this.numOfEndPoints = 0;
        this.numOfSlopes = 0;

        for (int i = 0; i < this.points.length; i++) {
            Arrays.sort(this.points);
            Arrays.sort(this.points, this.points[i].slopeOrder());

            int first = 0;
            int endIndex = 0;
            while (endIndex < this.points.length && first < this.points.length - 2) {
                endIndex = findCollinear(first);

                if (endIndex != -1) {                                                                 // found collinear points
                    subarr = Arrays.copyOfRange(this.points, first, endIndex + 1);                        // 多抓一個非collinear point用來被points[0]取代
                    subarr = Arrays.copyOf(subarr, subarr.length + 1);
                    subarr[subarr.length - 1] = points[0];

                    Arrays.sort(subarr);                                                                // worst case is (n / n) * n * log(n) time
                    if (!checkFofDuplicateSegments(subarr[subarr.length - 1], subarr[0].slopeTo(subarr[1]))) {
                        if (numOfLineSegment >= lineSegments.length - 1) {
                            lineSegments = Arrays.copyOf(lineSegments, lineSegments.length * 2);
                        }
                        lineSegments[++numOfLineSegment] = new LineSegment(subarr[0], subarr[subarr.length - 1]);

                        while (numOfEndPoints == endPoints.length - 1) {                                // resize the array
                            endPoints = Arrays.copyOf(endPoints, endPoints.length * 2);

                        }

                        while (numOfSlopes == slopes.length - 1) {                                     // resize the array
                            slopes = Arrays.copyOf(slopes, slopes.length * 2);

                        }
                        endPoints[numOfEndPoints] = subarr[subarr.length - 1];                         // add the new endpoint to the array
                        slopes[numOfEndPoints] = subarr[0].slopeTo(subarr[1]);
                        numOfEndPoints++;
                        numOfSlopes++;
                        continue;
                    }
                    first = endIndex + 1;
                }
                first++;                                                              // not found collinear points, move first index left with increment 1
            }
            //this.points = Arrays.copyOfRange(this.points, 1, this.points.length - 1);
        }
    }

    public int numberOfSegments() {
        return numOfLineSegment;
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

    private int findCollinear(int first) {
        int last = 0;
        if ((first + 2) > (points.length - 1)) {            // over the length of array, excluding the index = 0
            return -1;
        }
        if (points[first].slopeTo(points[0]) != points[first + 2].slopeTo(points[0])) {            // not collinear with at least 4 points starting from points[first]
            return -1;
        } else {                                             // collinear with at least 4 points starting from points[first]
            last = first + 2;

            if (last < points.length - 1) {
                while (points[last].slopeTo(points[0]) == points[last + 1].slopeTo(points[0])) {   // collinear with at least 4 or more points starting from points[first]
                    last++;
                    if (last == points.length - 1) {
                        break;
                    }
                }
            }
            return last;
        }
    }

    private boolean checkFofDuplicateSegments(Point endpoint, double slope) {
        if (endPoints.length == 1 && endPoints[0] == null) {
            return false;
        }

        for (int i = 0; i < numOfEndPoints; i++) {
            if (endPoints[i].compareTo(endpoint) == 0) {
                for (int j = 0; j < numOfSlopes; j++) {
                    if (slope == slopes[j]) {
                        return true;             // duplicate segment
                    }
                }
            }
        }
        return false;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
