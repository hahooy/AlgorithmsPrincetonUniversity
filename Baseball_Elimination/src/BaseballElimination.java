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
		N = input.readInt();
		w = new int[N];
		l = new int[N];
		r = new int[N];
		g = new int[N][N];

		for (int i = 0; i < N; i++) {
			teamMap.put(input.readString(), i);
			w[i] = input.readInt();
			l[i] = input.readInt();
			r[i] = input.readInt();
			for (int j = 0; j < N; j++) {
				g[i][j] = input.readInt();
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
		
		// trivial elimination
		for (String s : teams()) {
			if (wins(team) + remaining(team) < wins(s)) {
				return true;
			}
		}
		
		// compute the maxflow of the game flow network
		FlowNetwork net = buildFlowNetwork(team);		
		FordFulkerson myFF = new FordFulkerson(net, 0, net.V() - 1);
		for (FlowEdge edge : net.adj(0)) {
			if (Double.compare(edge.flow(), edge.capacity()) < 0) {
				return true;
			}
		}
		return false;
	}
	
	// build game flow network
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
		
		Queue<String> R = new Queue<String>();
		// trivial elimination
		for (String s : teams()) {
			if (wins(team) + remaining(team) < wins(s)) {
				R.enqueue(s);
			}
		}
		if (!R.isEmpty()) {
			return R;
		}
		
		// use FordFulkerson's mincut method to compute the non-trivial case
		FlowNetwork net = buildFlowNetwork(team);
		FordFulkerson myFF = new FordFulkerson(net, 0, net.V() - 1);

		// the first index of team vertices
		int startIndex = ((int) Math.pow(N, 2) - N) / 2 + 1;

		for (String s : teams()) {
			int i = teamMap.get(s);
			if (i == teamMap.get(team)) {
				continue;
			}
			if (myFF.inCut(i + startIndex)) {
				R.enqueue(s);
			}

		}
		
		return R;
	}
	
	// unit testing
	public static void main(String[] args) {
		BaseballElimination be = new BaseballElimination("baseball/teams54.txt");
		boolean b = be.isEliminated("Team3");
		System.out.println(b);
		for (String i : be.certificateOfElimination("Team3")) {
			System.out.println(i);
		}
	}
}
