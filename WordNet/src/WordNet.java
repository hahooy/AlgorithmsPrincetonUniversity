public class WordNet {
	private LinearProbingHashST<Integer, String> synsetsST = new LinearProbingHashST<Integer, String>();
	private LinearProbingHashST<String, SET<Integer>> nounsST = new LinearProbingHashST<String, SET<Integer>>();
	private SAP sap;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new NullPointerException();
		}
		buildSynSTs(synsets, hypernyms);
		buildSap(hypernyms, synsetsST.size());
	}

	// build synsets and nouns symbol tables
	private void buildSynSTs(String sysnetsFileName, String hypernyms) {
		In in = new In(sysnetsFileName);
		String line = null;
		while ((line = in.readLine()) != null) {
			String[] tokens = line.split("\\s*,\\s*");
			String[] nounsTokens = tokens[1].split("\\s+");
			for (String i : nounsTokens) {
				SET<Integer> syn = new SET<Integer>();
				if (nounsST.contains(i)) {
					syn = nounsST.get(i);
				}
				syn.add(Integer.parseInt(tokens[0]));
				nounsST.put(i, syn);
			}
			synsetsST.put(Integer.parseInt(tokens[0]), tokens[1]);
		}
	}

	// construct a digraph and build a SAP
	private void buildSap(String hypernyms, int size) {
		Digraph graph = new Digraph(size);
		In in = new In(hypernyms);
		String line = null;
		while ((line = in.readLine()) != null) {
			String[] tokens = line.split("\\s*,\\s*");
			for (int i = 1; i < tokens.length; i++) {
				graph.addEdge(Integer.parseInt(tokens[0]),
						Integer.parseInt(tokens[i]));
			}
		}
		if (!isDAG(graph)) {
			throw new IllegalArgumentException();
		}
		sap = new SAP(graph);
	}

	private boolean isDAG(Digraph dg) {
		Digraph g = new Digraph(dg);
		int numOfRoot = 0;
		for (int i = 0; i < g.V(); i++) {
			if (g.outdegree(i) == 0) {
				numOfRoot++;
			}
			if (numOfRoot > 1) {
				return false;
			}
		}
		/*
		 * Digraph rg = g.reverse(); for (int i = 0; i < rg.V(); i++) { if
		 * (rg.outdegree(i) == 0) {
		 * 
		 * } }
		 */
		return true;
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return nounsST.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null) {
			throw new NullPointerException();
		}
		return nounsST.contains(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new NullPointerException();
		}
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new IllegalArgumentException();
		}
		return sap.length(nounsST.get(nounA), nounsST.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new NullPointerException();
		}
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new IllegalArgumentException();
		}
		int anc = sap.ancestor(nounsST.get(nounA), nounsST.get(nounB));
		return synsetsST.get(anc);
	}

	// do unit testing of this class
	public static void main(String[] args) {
		/*
		 * WordNet wn = new WordNet(
		 * "/Users/hahooy1/Downloads/wordnet/synsets.txt",
		 * "/Users/hahooy1/Downloads/wordnet/hypernyms.txt");
		 * System.out.println(wn.isNoun("ACE_inhibitor"));
		 * System.out.println(wn.distance("worm", "bird"));
		 * System.out.println(wn.sap("worm", "bird"));
		 */
	}
}
