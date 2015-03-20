import java.util.Comparator;

public class Solver {

	// final board
	private SearchNode lastNode;

	// search node class
	private class SearchNode {
		private Board board;
		private int moves;
		private int priority;
		private SearchNode preNode;

		public SearchNode(Board b, int m, SearchNode n) {
			board = b;
			priority = m + board.manhattan(); // based on Manhattan function
			preNode = n;
			moves = m;
		}
	}

	// priority comparator
	private class PriorityByMht implements Comparator<SearchNode> {
		public int compare(SearchNode sn1, SearchNode sn2) {
			return sn1.priority - sn2.priority;
		}
	}

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		// initiate a original search node and a twin search node
		SearchNode initialNode = new SearchNode(initial, 0, null);
		SearchNode initialNodeTwin = new SearchNode(initial.twin(), 0, null);
		// priority queue
		MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(new PriorityByMht());
		MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>(new PriorityByMht());
		minPQ.insert(initialNode);
		minPQTwin.insert(initialNodeTwin);

		// dequeue the minimum priority search node from both priority queue
		// until one of them becomes the goal board
		SearchNode pcesNode = minPQ.delMin();
		SearchNode pcesNodeTwin = minPQTwin.delMin();
		while (!pcesNode.board.isGoal() && !pcesNodeTwin.board.isGoal()) {
			System.out.println(pcesNodeTwin.moves);
			Iterable<Board> nbBoard = pcesNode.board.neighbors();
			for (Board i : nbBoard) {
				assert pcesNode.preNode != null;

				if (pcesNode.preNode != null
						&& i.equals(pcesNode.preNode.board))
					continue;
				SearchNode nbNode = new SearchNode(i, pcesNode.moves + 1,
						pcesNode);
				minPQ.insert(nbNode);
			}
			pcesNode = minPQ.delMin();

			Iterable<Board> nbBoardTwin = pcesNodeTwin.board.neighbors();
			for (Board i : nbBoardTwin) {
				if (pcesNodeTwin.preNode != null
						&& i.equals(pcesNodeTwin.preNode.board))
					continue;
				SearchNode nbNodeTwin = new SearchNode(i,
						pcesNodeTwin.moves + 1, pcesNodeTwin);
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
			n = n.preNode;
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
