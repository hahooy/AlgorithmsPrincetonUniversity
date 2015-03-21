/*************************************************************************
 * Compilation: javac KdTree
 * java Execution: java KdTree 
 * Dependencies: Point.java, RectHV.java, In.java, StdDraw.java
 *
 * This class represents a set of points in the unit square implemented by a
 * 2dTree
 *************************************************************************/

public class KdTree {

	private Node root; // root of kdtree
	private int size;

	private static class Node {
		private Point2D p; // the point
		private RectHV rect; // the axis-aligned rectangle corresponding to this
								// node
		private Node lb; // the left/bottom subtree
		private Node rt; // the right/top subtree

		public Node(Point2D p, RectHV rect) {
			this.p = p;
			this.rect = rect;
		}
	}

	// construct an empty set of points
	public KdTree() {
		root = null;
		size = 0;
	}

	// is the set empty?
	public boolean isEmpty() {
		return size() == 0;
	}

	// number of points in the set
	public int size() {
		return size;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		// the axis-align rectangle for the first point is (0,0,1,1)
		RectHV r = new RectHV(0, 0, 1, 1);
		root = insert(root, p, 'v', r);
	}

	// insert key into kdtree rooted at node x and return the root, pass in
	// orientation and rectangle as arguments
	private Node insert(Node x, Point2D p, char o, RectHV r) {
		if (x == null) {
			size++;
			return new Node(p, r);
		}
		if (x.p.equals(p)) {
			return x;
		}
		// use the x-coordinate when the orientation of the root is vertical
		// else use the y-coordinate
		if (o == 'v') {
			if (p.x() < x.p.x()) {
				if (x.lb == null) {
					x.lb = insert(x.lb, p, 'h', new RectHV(r.xmin(), r.ymin(),
							x.p.x(), r.ymax()));
				} else {
					x.lb = insert(x.lb, p, 'h', x.lb.rect);
				}
			} else {
				if (x.rt == null) {
					x.rt = insert(x.rt, p, 'h',
							new RectHV(x.p.x(), r.ymin(), r.xmax(), r.ymax()));
				} else {
					x.rt = insert(x.rt, p, 'h', x.rt.rect);
				}
			}
		} else {
			if (p.y() < x.p.y()) {
				if (x.lb == null) {
					x.lb = insert(x.lb, p, 'v', new RectHV(r.xmin(), r.ymin(),
							r.xmax(), x.p.y()));
				} else {
					x.lb = insert(x.lb, p, 'v', x.lb.rect);
				}
			} else {
				if (x.rt == null) {
					x.rt = insert(x.rt, p, 'v',
							new RectHV(r.xmin(), x.p.y(), r.xmax(), r.ymax()));
				} else {
					x.rt = insert(x.rt, p, 'v', x.rt.rect);
				}
			}
		}
		return x;
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		return isFound(root, p, 'v');
	}

	// return true if the point is in the kdtree rooted at node x
	private boolean isFound(Node x, Point2D p, char o) {
		if (x == null) {
			return false;
		}
		if (p.equals(x.p)) {
			return true;
		}
		if (o == 'v') {
			if (p.x() < x.p.x()) {
				return isFound(x.lb, p, 'h');
			} else {
				return isFound(x.rt, p, 'h');
			}
		} else {
			if (p.y() < x.p.y()) {
				return isFound(x.lb, p, 'v');
			} else {
				return isFound(x.rt, p, 'v');
			}
		}
	}

	// draw all points to standard draw
	public void draw() {
		drawPt(root, 'v');
	}

	// draw points and splitting lines
	private void drawPt(Node x, char o) {
		if (x == null) {
			return;
		}
		StdDraw.setPenColor();
		StdDraw.setPenRadius(0.01);
		x.p.draw();
		if (o == 'v') {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius();
			StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
			drawPt(x.lb, 'h');
			drawPt(x.rt, 'h');
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius();
			StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
			drawPt(x.lb, 'v');
			drawPt(x.rt, 'v');
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new java.lang.NullPointerException();
		}
		Queue<Point2D> q = new Queue<Point2D>();
		getRange(root, rect, q);
		return q;
	}

	// enqueue all points that are inside the rectangle to the queue for the
	// kdtree rooted at node x
	private void getRange(Node x, RectHV rect, Queue<Point2D> q) {
		if (x == null) {
			return;
		}
		// enable pruning of the subtree rooted at x if the query rectangle does
		// not intersect with the axis-align rectangle of the point in x node
		if (!rect.intersects(x.rect)) {
			return;
		}
		if (rect.contains(x.p)) {
			q.enqueue(x.p);
		}
		getRange(x.lb, rect, q);
		getRange(x.rt, rect, q);
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		if (isEmpty()) {
			return null;
		}
		return nearest(root, p, root.p, 'v');
	}

	// return the nearest neighbor for the kdtree rooted at node x
	private Point2D nearest(Node x, Point2D queryPt, Point2D nearestPt, char o) {
		if (x == null) {
			return nearestPt;
		}
		double qpToNp = nearestPt.distanceTo(queryPt);
		// enable pruning of the subtree rooted at x
		if (x.rect.distanceTo(queryPt) >= qpToNp) {
			return nearestPt;
		}
		if (queryPt.distanceTo(x.p) < qpToNp) {
			nearestPt = x.p;
		}
		Point2D ln = nearestPt;
		Point2D rn = nearestPt;
		// choose the subtree that is on the same side of the splitting line as
		// the query point as the first subtree to explore
		if (o == 'v') {
			if (queryPt.x() < x.p.x()) {
				ln = nearest(x.lb, queryPt, nearestPt, 'h');
				rn = nearest(x.rt, queryPt, nearestPt, 'h');
			} else {
				rn = nearest(x.rt, queryPt, nearestPt, 'h');
				ln = nearest(x.lb, queryPt, nearestPt, 'h');
			}
		} else {
			if (queryPt.y() < x.p.y()) {
				ln = nearest(x.lb, queryPt, nearestPt, 'v');
				rn = nearest(x.rt, queryPt, nearestPt, 'v');
			} else {
				rn = nearest(x.rt, queryPt, nearestPt, 'v');
				ln = nearest(x.lb, queryPt, nearestPt, 'v');
			}
		}
		assert ln != null;
		assert rn != null;
		if (queryPt.distanceTo(ln) < queryPt.distanceTo(rn)) {
			return ln;
		} else {
			return rn;
		}
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		StdDraw.setXscale(0, 1);// rescale coordinates
		StdDraw.setYscale(0, 1);

		KdTree ptSet = new KdTree();
		RectHV rect = new RectHV(0.2, 0.2, 0.4, 0.4);
		Point2D pt1 = new Point2D(0.7, 0.2);
		Point2D pt2 = new Point2D(0.5, 0.4);
		Point2D pt3 = new Point2D(0.2, 0.3);
		Point2D pt4 = new Point2D(0.4, 0.7);
		Point2D pt5 = new Point2D(0.9, 0.6);
		ptSet.insert(pt1);
		ptSet.insert(pt2);
		ptSet.insert(pt3);
		ptSet.insert(pt4);
		ptSet.insert(pt5);
		ptSet.draw();

		StdDraw.setPenRadius(); // make the line narrower
		rect.draw();
		Iterable<Point2D> ptIN = ptSet.range(rect);

		for (Point2D pt : ptIN) {
			pt.draw();
		}

		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLUE);
		ptSet.nearest(new Point2D(0.8, 0.8)).draw();
	}
}
