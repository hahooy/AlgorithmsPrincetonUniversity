public class BoggleSolver {
	private TST<Boolean> dic;
	private SET<String> validWords;

	// Initializes the data structure using the given array of strings as the
	// dictionary.
	// (You can assume each word in the dictionary contains only the uppercase
	// letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		dic = new TST<Boolean>();
		for (String i : dictionary) {
			dic.put(i, true);
		}
	}

	// Returns the set of all valid words in the given Boggle board, as an
	// Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		validWords = new SET<String>();
		int rows = board.rows(), cols = board.cols();
		Bag<int[]>[][] graph = buildGraph(rows, cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				DFS d = new DFS(graph, board, i, j);
			}
		}
		return validWords;
	}

	// search the graph to enumerate all strings that can be composed by
	// following sequences of adjacent dice
	private class DFS {
		private boolean[][] marked;
		private BoggleBoard board;
		private String s;
		private StringBuilder sb;
		private Stack<String> wordStack;

		public DFS(Bag<int[]>[][] graph, BoggleBoard board, int si, int sj) {
			marked = new boolean[graph.length][graph[0].length];
			wordStack = new Stack<String>();
			sb = new StringBuilder();
			this.board = board;
			char letter = board.getLetter(si, sj);
			if (letter != 'Q') {
				sb.append(letter);
				// this.s = String.valueOf(letter);
			} else {
				sb.append("QU");
				//this.s = "QU";
			}
			dfs(graph, si, sj);
		}

		private void dfs(Bag<int[]>[][] graph, int si, int sj) {
			// the string is not the prefix of any words in the dictionary, so
			// there is no need to expand this path
			if (!dic.keysWithPrefix(s).iterator().hasNext()) {
				return;
			}
			// mark the visited letter
			marked[si][sj] = true;

			// visit all adjacent letters
			for (int[] a : graph[si][sj]) {
				if (!marked[a[0]][a[1]]) {
					char letter = board.getLetter(a[0], a[1]);
					if (letter != 'Q') {
						//s += letter;
					} else {
						s += "QU";
					}

					if (s.length() > 2 && dic.contains(s)) {
						validWords.add(s);
					}
					dfs(graph, a[0], a[1]);
					// decrease the string when the function returns
					if (letter != 'Q') {
						s = s.substring(0, s.length() - 1);
					} else {
						s = s.substring(0, s.length() - 2);
					}
				}
			}
			// unmark the letter when the function returns
			marked[si][sj] = false;
		}
	}

	// build an implicit graph for the board
	private Bag<int[]>[][] buildGraph(int rows, int cols) {
		Bag<int[]>[][] graph = (Bag<int[]>[][]) new Bag[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Bag<int[]> bag = new Bag<int[]>();

				for (int r = i - 1; r < i + 2; r++) {
					for (int c = j - 1; c < j + 2; c++) {
						if (r == i && c == j) {
							continue;
						}
						if (r >= 0 && r < rows && c >= 0 && c < cols) {
							int[] adjen = { r, c };
							bag.add(adjen);
						}
					}
				}
				graph[i][j] = bag;
			}
		}

		return graph;
	}

	// Returns the score of the given word if it is in the dictionary, zero
	// otherwise.
	// (You can assume the word contains only the uppercase letters A through
	// Z.)
	public int scoreOf(String word) {
		int score = 0;
		int length = word.length();
		if (dic.contains(word)) {
			if (length >= 3 && length <= 4) {
				score = 1;
			} else if (length == 5) {
				score = 2;
			} else if (length == 6) {
				score = 3;
			} else if (length == 7) {
				score = 5;
			} else if (length > 7) {
				score = 11;
			}
		}
		return score;
	}

	// string representation of the dictionary
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (String i : dic.keys()) {
			s.append(i).append("\n");
		}
		return s.toString();
	}

	public static void main(String[] args) {
		Stopwatch watch = new Stopwatch();
		In in = new In(args[0]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(args[1]);
		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
		System.out.println(watch.elapsedTime());
	}
}