import java.util.Comparator;

public class Solver {

	private int moves = 0;
	private MinPQ<SearchNode> minPQ;
	private SearchNode lastNode;
	private final Comparator<SearchNode> PRIORITY_ORDER = new ByMht();

	private class SearchNode {
		private Board board;
		private int priority;
		private SearchNode previours;

		public SearchNode(Board b, int m, SearchNode n) {
			board = b;
			priority = m + board.manhattan();
			previours = n;
		}
	}

	private class ByMht implements Comparator<SearchNode> {
		public int compare(SearchNode sn1, SearchNode sn2) {
			return sn1.priority - sn2.priority;
		}
	}

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		SearchNode initialNode = new SearchNode(initial, moves, null);
		SearchNode initialNodeTwin = new SearchNode(initial.twin(), moves, null);
		MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(PRIORITY_ORDER);
		MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>(PRIORITY_ORDER);
		minPQ.insert(initialNode);
		minPQTwin.insert(initialNodeTwin);

		SearchNode pcesNode = minPQ.delMin();
		SearchNode pcesNodeTwin = minPQTwin.delMin();

		while (!pcesNode.board.isGoal() && !pcesNodeTwin.board.isGoal()) {

			moves++;

			Iterable<Board> nbBoard = pcesNode.board.neighbors();
			for (Board i : nbBoard) {
				if (i.equals(pcesNode)) continue;
				SearchNode nbNode = new SearchNode(i, moves, pcesNode);
				minPQ.insert(nbNode);
			}
			pcesNode = minPQ.delMin();

			Iterable<Board> nbBoardTwin = pcesNodeTwin.board.neighbors();
			for (Board i : nbBoardTwin) {
				if (i.equals(pcesNodeTwin)) continue;
				SearchNode nbNodeTwin = new SearchNode(i, moves, pcesNodeTwin);
				minPQTwin.insert(nbNodeTwin);
			}
			pcesNodeTwin = minPQTwin.delMin();

		}
		if (pcesNode.board.isGoal())
			lastNode = pcesNode;
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		return lastNode != null;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if (!isSolvable())
			return -1;
		return moves;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		if (!isSolvable())
			return null;
		Stack<Board> solution = new Stack<Board>();
		SearchNode n = lastNode;
		while (n != null) {
			solution.push(n.board);
			n = n.previours;
		}
		return solution;
	}

	// solve a slider puzzle (given below)
	public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
