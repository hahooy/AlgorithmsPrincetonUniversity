/*************************************************************************
 * Name: JJ Huang Email: iamhuangyz@gmail.com
 *
 * Compilation: javac Brute.java Execution: Dependencies: StdDraw.java
 *
 * Description: use brute force algorithm to check 4 points are collinear
 *
 *************************************************************************/
import java.util.Arrays;

public class Brute {
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
		// compare the points by slope
		for (int i = 0; i < N; i++)
			for (int j = i + 1; j < N; j++)
				for (int k = j + 1; k < N; k++)
					for (int l = k + 1; l < N; l++)
						if (Double.compare(pList[i].slopeTo(pList[j]),
								pList[k].slopeTo(pList[j])) == 0
								&& Double.compare(pList[i].slopeTo(pList[j]),
										pList[j].slopeTo(pList[l])) == 0) {

							StdOut.println(pList[i] + " -> " + pList[j]
									+ " -> " + pList[k] + " -> " + pList[l]);
							pList[i].drawTo(pList[l]);
						}

		// display to screen all at once
		StdDraw.show(0);
	}
}
