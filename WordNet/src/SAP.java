public class SAP {
	private Digraph G;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		this.G = new Digraph(G);
	}

	// find the length and ancestor of the shortest ancestral path for two
	// vertices
	private int[] findAncestor(int v, int w) {
		assert (v < G.V());
		assert (w < G.V());
		BreadthFirstDirectedPaths vBFDP = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths wBFDP = new BreadthFirstDirectedPaths(G, w);
		return computeAncestor(vBFDP, wBFDP);
	}

	// find the length and ancestor of the shortest ancestral path for two sets
	// of vertices
	private int[] findAncestor(Iterable<Integer> v, Iterable<Integer> w) {
		BreadthFirstDirectedPaths vBFDP = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths wBFDP = new BreadthFirstDirectedPaths(G, w);
		return computeAncestor(vBFDP, wBFDP);
	}

	// use breadth first search to compute the ancestor of the shortest
	// ancestral path
	private int[] computeAncestor(BreadthFirstDirectedPaths vBFDP,
			BreadthFirstDirectedPaths wBFDP) {
		int[] lengthAncestor = { Integer.MAX_VALUE, 0 };
		boolean hasAn = false;
		// interate through all vertices and compute the length of ancestral
		// path, store the shortest ancestral path in minLength

		for (int i = 0; i < G.V(); i++) {
			if (vBFDP.hasPathTo(i) && wBFDP.hasPathTo(i)) {
				hasAn = true;
				int temp = vBFDP.distTo(i) + wBFDP.distTo(i);
				if (temp < lengthAncestor[0]) {
					lengthAncestor[0] = temp;
					lengthAncestor[1] = i;
				}
			}
		}

		if (hasAn) {
			return lengthAncestor;
		} else {
			int[] notFound = { -1, -1 };
			return notFound;
		}
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		return findAncestor(v, w)[0];
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		return findAncestor(v, w)[1];
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		return findAncestor(v, w)[0];
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		return findAncestor(v, w)[1];
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}