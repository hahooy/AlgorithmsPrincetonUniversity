/*************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    java PointSET
 *  Dependencies: Point.java, RectHV.java, In.java, StdDraw.java
 *
 *  This class represents a set of points in the unit square
 *************************************************************************/

public class PointSET {
	private SET<Point2D> points;

	// construct an empty set of points
	public PointSET() {
		points = new SET<Point2D>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return points.isEmpty();
	}

	// number of points in the set
	public int size() {
		return points.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.NullPointerException();
		points.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.NullPointerException();
		return points.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D p : points) {
			p.draw();
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new java.lang.NullPointerException();
		Queue<Point2D> pointQ = new Queue<Point2D>();
		for (Point2D p : points) {
			if (rect.contains(p))
				pointQ.enqueue(p);
		}
		return pointQ;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		if (isEmpty()) {
			return null;
		}
		Point2D nearestPt = points.min();
		for (Point2D pts : points) {
			if (pts.distanceTo(p) < nearestPt.distanceTo(p))
				nearestPt = pts;
		}
		return nearestPt;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		// rescale coordinates and turn on animation mode
		StdDraw.setXscale(0, 1);
		StdDraw.setYscale(0, 1);

		PointSET ptSet = new PointSET();
		RectHV rect = new RectHV(0.2, 0.2, 0.4, 0.4);
		Point2D pt1 = new Point2D(0.1, 0.1);
		Point2D pt2 = new Point2D(0.3, 0.3);
		Point2D pt3 = new Point2D(0.9, 0.9);
		ptSet.insert(pt1);
		ptSet.insert(pt2);
		ptSet.insert(pt3);
		StdDraw.setPenRadius(0.01); // make the points a bit larger
		ptSet.draw();
		StdDraw.setPenColor(StdDraw.RED); // set the color to red
		StdDraw.setPenRadius(); // make the line narrower
		rect.draw();
		Iterable<Point2D> ptIN = ptSet.range(rect);
		StdDraw.setPenRadius(0.01); // make the points a bit larger
		for (Point2D pt : ptIN) {
			pt.draw();
		}
		StdDraw.setPenColor(StdDraw.BLUE); // set the color to red
		ptSet.nearest(new Point2D(0.8, 0.8)).draw();
	}
}
