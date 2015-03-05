public class Percolation {

	private static final int OPEN = 1; // the entry of open sites is 1
	private static final int CLOSE = 0; // the entry of blocked sites is 0

	private int[][] grid;
	private WeightedQuickUnionUF unionFind;
	private int size = 0;

	// create N-by-N grid, with all sites blocked
	public Percolation(int N) {
		if (N <= 0 ) {
			throw new IndexOutOfBoundsException("index out of bounds");
		}
		size = N;
		unionFind = new WeightedQuickUnionUF(size * size + 2);
		grid = new int[size][size];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = CLOSE;
			}
		}
	}

	// open site (row i, column j) if it is not open already
	public void open(int i, int j) {
		if (i <= 0 || i > size || j <= 0 || j > size) {
			throw new IndexOutOfBoundsException("index out of bounds");
		}
		grid[i - 1][j - 1] = OPEN;
		// connect with the open site above
		if (i - 1 > 0 && isOpen(i - 1, j)) {
			unionFind.union((i - 2) * size + j, (i - 1) * size + j);
		}
		// connect with the open site below
		if (i + 1 <= size && isOpen(i + 1, j)) {
			unionFind.union(i * size + j, (i - 1) * size + j);
		}
		// connect with the open site on the left
		if (j - 1 > 0 && isOpen(i, j - 1)) {
			unionFind.union((i - 1) * size + (j - 1), (i - 1) * size + j);
		}
		// connect with the open site on the right
		if (j + 1 <= size && isOpen(i, j + 1)) {
			unionFind.union((i - 1) * size + (j + 1), (i - 1) * size + j);
		}
		// indicate sites on the first row by union them with the first element
		// of unionFind object
		if (i == 1) {
			unionFind.union((i - 1) * size + j, 0);
		}
		// indicate sites on the last row by union them with the last element of
		// unionFind object when they are already full
		if (i == size) {
			unionFind.union((i - 1) * size + j, size * size + 1);
		}
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) {
		if (i <= 0 || i > size || j <= 0 || j > size) {
			throw new IndexOutOfBoundsException("index out of bounds");
		}
		return grid[i - 1][j - 1] == OPEN;
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j) {
		if (i <= 0 || i > size || j <= 0 || j > size) {
			throw new IndexOutOfBoundsException("index out of bounds");
		}
		return unionFind.connected(0, (i - 1) * size + j);
	}

	// does the system percolate?
	public boolean percolates() {
		return unionFind.connected(0, size * size + 1);
	}

	// test client
	public static void main(String[] args) {
		Percolation p = new Percolation(20);
		for (int i = 1; i < 21; i++) {
			p.open(i, 1);
		}
		StdOut.println(p.percolates());
	}
}
