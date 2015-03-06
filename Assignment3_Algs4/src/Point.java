/*************************************************************************
 * Name: JJ Huang
 * Email: iamhuangyz@gmail.com
 *
 * Compilation:  javac Point.java
 * Execution: java Point.java
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

	// compare points by slope
	public final Comparator<Point> SLOPE_ORDER = new BySlope();

	private final int x; // x coordinate
	private final int y; // y coordinate

	private class BySlope implements Comparator<Point> {
		public int compare(Point p1, Point p2) {
			if (slopeTo(p1) < slopeTo(p2)) {
				return -1;
			} else if (slopeTo(p1) > slopeTo(p2)) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	// create the point (x, y)
	public Point(int x, int y) {
		/* DO NOT MODIFY */
		this.x = x;
		this.y = y;
	}

	// plot this point to standard drawing
	public void draw() {
		/* DO NOT MODIFY */
		StdDraw.point(x, y);
	}

	// draw line between this point and that point to standard drawing
	public void drawTo(Point that) {
		/* DO NOT MODIFY */
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	// slope between this point and that point
	public double slopeTo(Point that) {
		int deltaY = that.y - this.y;
		int deltaX = that.x - this.x;
		if (deltaY == 0 && deltaX == 0) {
			return Double.NEGATIVE_INFINITY;
		}
		if (deltaY == 0) {
			return 0.0;
		}
		if (deltaX == 0) {
			return Double.POSITIVE_INFINITY;
		}
		return deltaY / deltaX;
	}

	// is this point lexicographically smaller than that one?
	// comparing y-coordinates and breaking ties by x-coordinates
	public int compareTo(Point that) {
		if (this.y < that.y || (this.y == that.y && this.x < that.x)) {
			return -1;
		} else if (this.x == that.x && this.y == that.y) {
			return 0;
		} else {
			return 1;
		}
	}

	// return string representation of this point
	public String toString() {
		/* DO NOT MODIFY */
		return "(" + x + ", " + y + ")";
	}

	// unit test
	public static void main(String[] args) {
		Point p1 = new Point(1, 1);
		Point p2 = new Point(2, 2);
		StdOut.println("p1 = " + p1 + " p2 = " + p2);
	}
}
