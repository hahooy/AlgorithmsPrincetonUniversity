import java.util.Arrays;

public class Board {
	private int[] blocks;
	private int dimension;

	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		dimension = blocks[0].length;
		this.blocks = new int[dimension * dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				this.blocks[i * dimension + j] = blocks[i][j];
			}
		}
	}

	// construct a board from an N*N array of blocks
	private Board(int[] blocks) {
		dimension = (int) Math.sqrt(blocks.length);
		this.blocks = new int[blocks.length];
		for (int i = 0; i < blocks.length; i++) {
			this.blocks[i] = blocks[i];
		}
	}

	// board dimension N
	public int dimension() {
		return dimension;
	}

	// number of blocks out of place
	public int hamming() {
		int ham = 0;
		for (int i = 0; i < dimension * dimension - 1; i++) {
			if (blocks[i] != i + 1)
				ham++;
		}
		return ham;
	}

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		int man = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++)
				if (blocks[i * dimension + j] != 0) {
					man += Math.abs((blocks[i * dimension + j] - 1) / dimension
							- i)
							+ Math.abs(((blocks[i * dimension + j] - 1) % dimension)
									- j);
				}
		}
		return man;
	}

	// is this board the goal board?
	public boolean isGoal() {
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] != 0 && blocks[i] != i + 1)
				return false;
		}
		return true;
	}

	// a little helper function for exchanging blocks
	private void exch(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	// a board that is obtained by exchanging two adjacent blocks in the same
	// row
	public Board twin() {
		if (dimension <= 1)
			return null;
		Board twin = new Board(blocks);
		if (blocks[0] != 0 && blocks[1] != 0) {
			exch(twin.blocks, 0, 1);
		} else {
			exch(twin.blocks, dimension, dimension + 1);
		}
		return twin;
	}

	// does this board equal y?
	public boolean equals(Object y) {
		if (y == this)
			return true;
		if (y == null)
			return false;
		if (y.getClass() != this.getClass())
			return false;
		Board that = (Board) y;
		return Arrays.equals(that.blocks, blocks);
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		Queue<Board> neighbors = new Queue<Board>();
		int i;
		Board neighborBoard;

		for (i = 0; i < blocks.length; i++) {
			if (blocks[i] == 0)
				break;
		}

		if (i >= blocks.length)
			return null;

		if (i >= dimension) {
			neighborBoard = new Board(blocks);
			exch(neighborBoard.blocks, i, i - dimension);
			neighbors.enqueue(neighborBoard);
		}
		if (i < blocks.length - dimension) {
			neighborBoard = new Board(blocks);
			exch(neighborBoard.blocks, i, i + dimension);
			neighbors.enqueue(neighborBoard);
		}
		if (i % dimension != 0) {
			neighborBoard = new Board(blocks);
			exch(neighborBoard.blocks, i, i - 1);
			neighbors.enqueue(neighborBoard);
		}
		if ((i + 1) % dimension != 0) {
			neighborBoard = new Board(blocks);
			exch(neighborBoard.blocks, i, i + 1);
			neighbors.enqueue(neighborBoard);
		}
		return neighbors;
	}

	// string representation of this board (in the output format specified
	// below)
	public String toString() {
		String s = dimension + "\n";
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				s += String.format("%2d ", blocks[i * dimension + j]);
			}
			s += "\n";
		}
		return s;
	}

	// unit tests (not graded)
	public static void main(String[] args) {
		int N = StdIn.readInt();
		int[][] blocks = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				blocks[i][j] = StdIn.readInt();
			}
		}

		Board b = new Board(blocks);
		StdOut.println(b);
		StdOut.println(b.hamming());
		StdOut.println(b.manhattan());
		StdOut.println(b.isGoal());
	}
}
