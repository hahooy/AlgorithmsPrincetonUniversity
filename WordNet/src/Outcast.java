public class Outcast {
	private WordNet wn;

	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		wn = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		String oc = null;
		int max = 0;
		for (String i : nouns) {
			int d = 0;
			for (String j : nouns) {
				d += wn.distance(i, j);
			}
			if (d > max) {
				max = d;
				oc = i;
			}
		}
		return oc;
	}

	// see test client below
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}
