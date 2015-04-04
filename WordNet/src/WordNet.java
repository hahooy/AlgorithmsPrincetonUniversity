public class WordNet {
	private LinearProbingHashST<Integer, String> synsetsST;
	private LinearProbingHashST<String, SET<Integer>> nounsST;
	private SAP sap;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		synsetsST = buildSynsetsST(synsets);
		Digraph synsetsGraph = buildSynsetsGraph(hypernyms, synsetsST.size());
		nounsST = buildNounsST(synsets);
		sap = new SAP(synsetsGraph);
	}

	private Digraph buildSynsetsGraph(String hypernyms, int size) {
		Digraph graph = new Digraph(size);
		In in = new In(hypernyms);
		String hyperLine = null;
		while ((hyperLine = in.readLine()) != null) {
			String[] tokens = hyperLine.split("\\s*,\\s*");
			for (int i = 1; i < tokens.length; i++) {
				graph.addEdge(Integer.parseInt(tokens[0]),
						Integer.parseInt(tokens[i]));
			}
		}
		return graph;
	}

	private LinearProbingHashST<Integer, String> buildSynsetsST(
			String synsetsFileName) {
		LinearProbingHashST<Integer, String> ST = new LinearProbingHashST<Integer, String>();
		In in = new In(synsetsFileName);
		String synsetLine = null;
		while ((synsetLine = in.readLine()) != null) {
			String[] tokens = synsetLine.split("\\s*,\\s*");
			ST.put(Integer.parseInt(tokens[0]), tokens[1]);
		}
		return ST;
	}

	private LinearProbingHashST<String, SET<Integer>> buildNounsST(
			String sysnetsFileName) {
		LinearProbingHashST<String, SET<Integer>> ST = new LinearProbingHashST<String, SET<Integer>>();
		In in = new In(sysnetsFileName);
		String line = null;
		while ((line = in.readLine()) != null) {
			String[] tokens = line.split("\\s*,\\s*");
			String[] nounsTokens = tokens[1].split("\\s+");
			for (String i : nounsTokens) {
				SET<Integer> syn = new SET<Integer>();
				if (ST.contains(i)) {
					syn = ST.get(i);
				}
				syn.add(Integer.parseInt(tokens[0]));
				ST.put(i, syn);
			}
		}
		return ST;
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return nounsST.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return nounsST.contains(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		return sap.length(nounsST.get(nounA), nounsST.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		int anc = sap.ancestor(nounsST.get(nounA), nounsST.get(nounB));
		return synsetsST.get(anc);
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In("/Users/hahooy1/Downloads/wordnet/hypernyms.txt");
		String hyperLine = null;
		while ((hyperLine = in.readLine()) != null) {
			String[] tokens = hyperLine.split("\\s*,\\s*");
			for (String i : tokens) {
				StdOut.println(i);
			}
		}
	}
}
