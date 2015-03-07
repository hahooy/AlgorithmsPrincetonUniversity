import java.util.Arrays;

/*************************************************************************
 * Name: JJ Huang Email: iamhuangyz@gmail.com
 *
 * Compilation: javac Brute.java Execution: Dependencies: StdDraw.java
 *
 * Description: use brute force algorithm to check 4 points are collinear
 *
 *************************************************************************/

public class Brute {
	public static void main(String[] args) {

		// rescale coordinates and turn on animation mode
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		StdDraw.show(0);
		StdDraw.setPenRadius(0.01); // make the points a bit larger

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
						if (pList[i].slopeTo(pList[j]) == pList[i]
								.slopeTo(pList[k])
								&& pList[i].slopeTo(pList[j]) == pList[i]
										.slopeTo(pList[l])) {
							/*							 
							 * line[0].drawTo(line[3]);
							 */
							StdOut.println(pList[i] + " -> " + pList[j]
									+ " -> " + pList[k] + " -> " + pList[l]);
						}

		// display to screen all at once
		StdDraw.show(0);

		// reset the pen radius
		StdDraw.setPenRadius();
	}
}
