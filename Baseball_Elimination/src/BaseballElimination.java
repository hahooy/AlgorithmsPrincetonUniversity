import java.util.HashMap;

public class BaseballElimination {
	final private int N; // number of teams
	// key: team, value: team number
	private HashMap<String, Integer> teamMap = new HashMap<String, Integer>();
	private int[] w; // wins
	private int[] l; // losses
	private int[] r; // remaining games
	private int[][] g; // games left

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In input = new In(filename);
		N = Integer.parseInt(input.readLine());
		w = new int[N];
		l = new int[N];
		r = new int[N];
		g = new int[N][N];

		for (int i = 0; i < N; i++) {
			String[] tokens = input.readLine().trim().split("\\s+");
			teamMap.put(tokens[0], i);
			w[i] = Integer.parseInt(tokens[1]);
			l[i] = Integer.parseInt(tokens[2]);
			r[i] = Integer.parseInt(tokens[3]);
			for (int j = 0; j < N; j++) {
				g[i][j] = Integer.parseInt(tokens[4 + j]);
			}
		}
	}

	// number of teams
	public int numberOfTeams() {
		return N;
	}

	// all teams
	public Iterable<String> teams() {
		return teamMap.keySet();
	}

	// number of wins for given team
	public int wins(String team) {
		if (!teamMap.containsKey(team)) {
			throw new IllegalArgumentException();
		}
		return w[teamMap.get(team)];
	}

	// number of losses for given team
	public int losses(String team) {
		if (!teamMap.containsKey(team)) {
			throw new IllegalArgumentException();
		}
		return l[teamMap.get(team)];
	}

	// number of remaining games for given team
	public int remaining(String team) {
		if (!teamMap.containsKey(team)) {
			throw new IllegalArgumentException();
		}
		return r[teamMap.get(team)];
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		if (!teamMap.containsKey(team1) || !teamMap.containsKey(team2)) {
			throw new IllegalArgumentException();
		}
		return g[teamMap.get(team1)][teamMap.get(team2)];
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		if (!teamMap.containsKey(team)) {
			throw new IllegalArgumentException();
		}
		for (String s : teams()) {
			if (wins(team) + remaining(team) < wins(s)) {
				return true;
			}
		}
		FlowNetwork net = buildFlowNetwork(team);
		// System.out.println(net);

		// compute the maxflow and mincut of the game flow network
		FordFulkerson myFF = new FordFulkerson(net, 0, net.V() - 1);
		for (FlowEdge edge : net.adj(0)) {
			if (Double.compare(edge.flow(), edge.capacity()) < 0) {
				return true;
			}
		}
		return false;
	}

	private FlowNetwork buildFlowNetwork(String team) {
		int numOfVertices = 2 + N + ((int) Math.pow(N, 2) - N) / 2;
		int gameVertices = ((int) Math.pow(N, 2) - N) / 2;
		FlowNetwork net = new FlowNetwork(numOfVertices);

		int k = 1; // index of vertices, start from 1 as s is 0

		for (int i = 0; i < N; i++) {
			if (i == teamMap.get(team)) {
				continue;
			}
			for (int j = i + 1; j < N; j++) {
				if (j == teamMap.get(team)) {
					continue;
				}
				// connect s with game vertices
				FlowEdge sToGame = new FlowEdge(0, k, g[i][j]);
				// connect game vertices with team vertices
				FlowEdge gameToTeam1 = new FlowEdge(k, 1 + gameVertices + i,
						Double.POSITIVE_INFINITY);
				FlowEdge gameToTeam2 = new FlowEdge(k, 1 + gameVertices + j,
						Double.POSITIVE_INFINITY);

				net.addEdge(sToGame);
				net.addEdge(gameToTeam1);
				net.addEdge(gameToTeam2);

				k++;
			}

			// connect team vertices with t
			FlowEdge teamiToT = new FlowEdge(1 + gameVertices + i,
					numOfVertices - 1, wins(team) + remaining(team) - w[i]);
			net.addEdge(teamiToT);
		}
		return net;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		if (!teamMap.containsKey(team)) {
			throw new IllegalArgumentException();
		}
		if (!isEliminated(team)) {
			return null;
		}
		FlowNetwork net = buildFlowNetwork(team);
		FordFulkerson myFF = new FordFulkerson(net, 0, net.V() - 1);
		Queue<String> q = new Queue<String>();
		int startIndex = ((int) Math.pow(N, 2) - N) / 2 + 1;

		for (String s : teams()) {
			int i = teamMap.get(s);
			if (i == teamMap.get(team)) {
				continue;
			}
			if (myFF.inCut(i + startIndex)) {
				q.enqueue(s);
			}

		}
		return q;
	}

	public static void main(String[] args) {
		BaseballElimination be = new BaseballElimination("baseball/teams4.txt");
		/*
		 * for (String i : be.teams()) { System.out.printf("%s %d %d %d %d\n",
		 * i, be.wins(i), be.losses(i), be.remaining(i), be.against(i,
		 * "Detroit")); }
		 */
		boolean b = be.isEliminated("Philadelphia");
		System.out.println(b);
		for (String i : be.certificateOfElimination("Philadelphia")) {
			System.out.println(i);
		}
	}
}
