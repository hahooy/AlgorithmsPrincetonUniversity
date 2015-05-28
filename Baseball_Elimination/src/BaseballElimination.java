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
			String[] tokens = input.readLine().split("\\s+");
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
		return w[teamMap.get(team)];
	}

	// number of losses for given team
	public int losses(String team) {
		return l[teamMap.get(team)];
	}

	// number of remaining games for given team
	public int remaining(String team) {
		return r[teamMap.get(team)];
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		return g[teamMap.get(team1)][teamMap.get(team2)];
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		for (String s : teams()) {
			if (wins(team) + remaining(team) < wins(s)) {
				return true;
			}
		}
		int numOfVertices = 2 + (N - 1) + ((int) Math.pow(N - 1, 2) - (N - 1))
				/ 2;
		int gameVertices = ((int) Math.pow(N - 1, 2) - (N - 1)) / 2;
		int teamVertices = N - 1;
		FlowNetwork net = new FlowNetwork(numOfVertices);

		
		int k = 1; // index of vertices, start from 1 as s is 0				 
		int l = gameVertices + 1; // index of team vertices
		
		
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
				FlowEdge gameToTeam1 = new FlowEdge(k, l,
						Double.POSITIVE_INFINITY);
				FlowEdge gameToTeam2 = new FlowEdge(k, l,
						Double.POSITIVE_INFINITY);

				net.addEdge(sToGame);
				net.addEdge(gameToTeam1);
				net.addEdge(gameToTeam2);

				if (i == teamVertices - 2) {
					FlowEdge lastTeamToT = new FlowEdge(gameVertices + 1 + j,
							numOfVertices - 1, wins(team) + remaining(team)
									- w[j]);
					net.addEdge(lastTeamToT);
				}

				k++;
			}
			
			// connect team vertices with t
			FlowEdge teamiToT = new FlowEdge(l,
					numOfVertices - 1, wins(team) + remaining(team) - w[i]);
			net.addEdge(teamiToT);
			l++;
		}
		System.out.println(net);
		return false;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		return teamMap.keySet();
	}

	public static void main(String[] args) {
		BaseballElimination be = new BaseballElimination("baseball/teams4.txt");
		int j = 0;
		for (String i : be.teams()) {
			System.out.printf("%s %d %d %d %d\n", i, be.wins(i), be.losses(i),
					be.remaining(i), be.against(i, "Philadelphia"));
		}
		boolean b = be.isEliminated("Philadelphia");
		System.out.println(b);
	}
}
