public class PercolationStats {

	private int trials;
	private double[] threshold;
	private double mean;
	private double stddev;

	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0)
			throw new java.lang.IllegalArgumentException();
		threshold = new double[T];
		trials = T;

		for (int i = 0; i < T; i++) {
			int numOfOpen = 0;
			Percolation p = new Percolation(N);

			while (!p.percolates()) {
				int j = StdRandom.uniform(1, N + 1);
				int k = StdRandom.uniform(1, N + 1);
				if (!p.isOpen(j, k)) {
					p.open(j, k);
					numOfOpen++;
				} else {
					continue;
				}
			}
			threshold[i] = (double) numOfOpen / (N * N);
		}
	}

	// sample mean of percolation threshold
	public double mean() {
		mean = StdStats.mean(threshold);
		return mean;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		stddev = StdStats.stddev(threshold);
		return stddev;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mean - 1.96 * stddev / Math.sqrt(trials);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mean + 1.96 * stddev / Math.sqrt(trials);
	}

	// test client (described below)
	public static void main(String[] args) {
		PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		StdOut.println("mean =" + ps.mean());
		StdOut.println("stddev =" + ps.stddev());
		StdOut.println("95% confidence interval =" + ps.confidenceLo()
				+ ps.confidenceHi());
	}
}
