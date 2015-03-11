import java.util.Comparator;

public class Solver {
	
	//private MinPQ<SearchNode> minPQ;
	private SearchNode lastNode;	

	private class SearchNode {
		private Board board;
		private int moves;
		private SearchNode previours;

		public SearchNode(Board b, int m, SearchNode n) {
			board = b;
			moves = m;
			previours = n;
		}
	}

	private class ByMht implements Comparator<SearchNode> {

		public int compare(SearchNode s1, SearchNode s2) {
			int priority1 = s1.board.manhattan() + s1.moves;
			int priority2 = s2.board.manhattan() + s2.moves;

			if (priority1 > priority2)
				return 1;
			else if (priority1 < priority2)
				return -1;
			else
				return 0;

		}
	}

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		int moves = 0;
		int movesTwin = 0;
		Comparator<SearchNode> PriorityOrder = new ByMht();
		Comparator<SearchNode> PriorityOrderTwin = new ByMht();
		
		SearchNode initialNode = new SearchNode(initial, moves, null);
		SearchNode initialNodeTwin = new SearchNode(initial.twin(), movesTwin, null);
		MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(PriorityOrder);
		MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>(PriorityOrderTwin);
		minPQ.insert(initialNode);
		minPQTwin.insert(initialNodeTwin);

		SearchNode pcesNode = minPQ.delMin();
		SearchNode pcesNodeTwin = minPQTwin.delMin();

		while (!pcesNode.board.isGoal() && !pcesNodeTwin.board.isGoal()) {

			moves++;
			Iterable<Board> nbBoard = pcesNode.board.neighbors();
			for (Board i : nbBoard) {
				if (i.equals(pcesNode))
					continue;
				SearchNode nbNode = new SearchNode(i, moves, pcesNode);
				minPQ.insert(nbNode);
			}
			pcesNode = minPQ.delMin();

			movesTwin++;
			Iterable<Board> nbBoardTwin = pcesNodeTwin.board.neighbors();
			for (Board i : nbBoardTwin) {
				if (i.equals(pcesNodeTwin))
					continue;
				SearchNode nbNodeTwin = new SearchNode(i, movesTwin, pcesNodeTwin);
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
		return lastNode.moves;
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
