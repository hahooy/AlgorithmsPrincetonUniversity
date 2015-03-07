/*************************************************************************
 * Name: Dan Huang Email: iamhuangyz@gmail.com
 *
 * Compilation: javac Fast.java Execution: java Fast
 * 
 * Dependencies: StdDraw.java
 *
 * Description: use  algorithm to check 4 points are collinear
 *
 *************************************************************************/

import java.util.Arrays;

public class Fast {
	public static void main(String[] args) {

		// rescale coordinates and turn on animation mode
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		StdDraw.show(0);
		StdDraw.setPenRadius(0.005); // make the points a bit larger

		// read in the input
		String filename = args[0];
		In in = new In(filename);
		int N = in.readInt();
		Point[] pList = new Point[N];
		for (int i = 0; i < N; i++) {
			int x = in.readInt();
			int y = in.readInt();
			Point p = new Point(x, y);
			p.draw();
			pList[i] = p;
		}

		Arrays.sort(pList);
		// create an auxiliary array for sorting points by slope
		Point[] aux = new Point[N];
		for (int i = 0; i < N - 3; i++) {

			for (int j = i; j < N; j++) {
				aux[j] = pList[j];
			}

			// sort points other than i by slope
			Arrays.sort(aux, i + 1, N, aux[i].SLOPE_ORDER);
			int countSameSlope = 1;
			String result = aux[i] + " -> ";
			for (int k = i + 1; k < N - 1; k++) {

				if (Math.abs(aux[i].slopeTo(aux[k])
						- aux[i].slopeTo(aux[k + 1])) < 0.00001) {
					result += aux[k] + " -> ";
					countSameSlope++;
				} else if (countSameSlope >= 3) {
					result += aux[k];
					StdOut.println(result);
					aux[i].drawTo(pList[k]);
					result = aux[i] + " -> ";
					countSameSlope = 1;
				} else {
					result = aux[i] + " -> ";
					countSameSlope = 1;
				}
			}
			// handle the case in which the last 3 or more points are collinear
			// with point i
			if (countSameSlope >= 3) {
				result += aux[N - 1];
				StdOut.println(result);
				aux[i].drawTo(pList[N - 1]);
			}

		}

		// display to screen all at once
		StdDraw.show(0);
	}
}
