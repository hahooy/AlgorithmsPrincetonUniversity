/*************************************************************************
 * Compilation: javac KdTree.java Execution: java KdTree Dependencies:
 * Point.java, RectHV.java, In.java, StdDraw.java
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
		RectHV r = new RectHV(0, 0, 1, 1);
		root = insert(root, p, 'v', r);
		size++;
	}

	// insert key into kdtree and return the root, pass in orientation and
	// rectangle as arguments
	private Node insert(Node x, Point2D p, char o, RectHV r) {
		if (x == null) {
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
		if (p == null)
			throw new java.lang.NullPointerException();
		return isFound(root, p, 'v');
	}

	// return true if the point is in the points set
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

	// draw lines of points
	private void drawPt(Node x, char o) {
		if (x == null) {
			return;
		}
		StdDraw.setPenColor();
		// StdDraw.setPenRadius(0.01);
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
		if (rect == null)
			throw new java.lang.NullPointerException();
		Queue<Point2D> q = new Queue<Point2D>();
		getRange(root, rect, q);
		return q;
	}

	private void getRange(Node x, RectHV rect, Queue<Point2D> q) {
		if (x == null) {
			return;
		}
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
		return nearest(root, p, root.p);
	}

	// return the nearest neighbor
	private Point2D nearest(Node x, Point2D p, Point2D nearestPt) {
		if (x == null) {
			return nearestPt;
		}
		assert x != null;
		if (x.rect.distanceTo(p) >= nearestPt.distanceTo(p)) {
			return nearestPt;
		}
		if (p.distanceTo(x.p) < p.distanceTo(nearestPt)) {
			nearestPt = x.p;
		}
		Point2D ln = nearest(x.lb, p, nearestPt);
		Point2D rn = nearest(x.rt, p, nearestPt);
		assert ln != null;
		assert rn != null;
		if (p.distanceTo(ln) < p.distanceTo(rn)) {
			return ln;
		} else {
			return rn;
		}
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) { // rescale coordinates and turn on
												// animation mode
		StdDraw.setXscale(0, 1);
		StdDraw.setYscale(0, 1);

		KdTree ptSet = new KdTree();
		// RectHV rect = new RectHV(0.2, 0.2, 0.4, 0.4);
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
		StdDraw.setPenRadius(0.01); // make the points a bit larger
		ptSet.draw();
		/*
		 * StdDraw.setPenColor(StdDraw.RED); // set the color to red
		 * StdDraw.setPenRadius(); // make the line narrower //rect.draw();
		 * Iterable<Point2D> ptIN = ptSet.range(rect);
		 * StdDraw.setPenRadius(0.01); // make the points a bit larger for
		 * (Point2D pt : ptIN) { pt.draw(); } StdDraw.setPenColor(StdDraw.BLUE);
		 * // set the color to red ptSet.nearest(new Point2D(0.8, 0.8)).draw();
		 */
	}
}
