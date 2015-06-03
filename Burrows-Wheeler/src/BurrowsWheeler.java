import java.util.Arrays;

public class BurrowsWheeler {
	// apply Burrows-Wheeler encoding, reading from standard input and writing
	// to standard output
	public static void encode() {
		while (!BinaryStdIn.isEmpty()) {
			String s = BinaryStdIn.readString();
			CircularSuffixArray csa = new CircularSuffixArray(s);
			// locate the index of the original string
			for (int i = 0; i < csa.length(); i++) {
				if (csa.index(i) == 0) {
					BinaryStdOut.write(i);
					break;
				}
			}
			// write the last column in the sorted suffixes array
			for (int i = 0; i < csa.length(); i++) {
				int last = csa.index(i) + csa.length() - 1;
				if (last >= csa.length()) {
					last -= csa.length();
				}
				char c = s.charAt(last);
				BinaryStdOut.write(c);
			}
		}
		BinaryStdOut.close();
	}

	// apply Burrows-Wheeler decoding, reading from standard input and writing
	// to standard output
	public static void decode() {
		while (!BinaryStdIn.isEmpty()) {
			int first = BinaryStdIn.readInt();
			String s = BinaryStdIn.readString();
			IndexedChar[] t = new IndexedChar[s.length()];
			for (int i = 0; i < s.length(); i++) {
				IndexedChar c = new IndexedChar(s.charAt(i), i);
				t[i] = c;
			}
			// the index of sorted t[] i is the index of sorted suffixes, the
			// value t[i].index is next[i], so next[] can be represented by
			// t[].index
			Arrays.sort(t);

			for (int i = 0; i < t.length; i++) {
				char letter = t[first].c;
				first = t[first].index;
				BinaryStdOut.write(letter);
			}

		}
		BinaryStdOut.close();
	}

	// use an inner class to store t[] and characters
	private static class IndexedChar implements Comparable<IndexedChar> {
		private char c;
		private int index;

		public IndexedChar(char c, int index) {
			this.c = c;
			this.index = index;
		}

		public int compareTo(IndexedChar that) {
			if (this.c < that.c) {
				return -1;
			}
			if (this.c > that.c) {
				return +1;
			}
			return 0;
		}
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("Please enter '-' or '+'");
		}
		if (args[0].equals(String.valueOf('-'))) {
			encode();
		} else if (args[0].equals(String.valueOf('+'))) {
			decode();
		} else {
			throw new IllegalArgumentException("Please enter '-' or '+'");
		}
	}
}
