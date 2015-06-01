import java.util.Arrays;

public class BurrowsWheeler {
	// apply Burrows-Wheeler encoding, reading from standard input and writing
	// to standard output
	public static void encode() {
		while (!StdIn.isEmpty()) {
			String s = StdIn.readString();
			CircularSuffixArray csa = new CircularSuffixArray(s);
			for (int i = 0; i < csa.length(); i++) {
				if (csa.index(i) == 0) {
					BinaryStdOut.write(i);
					break;
				}
			}
			for (int i = 0; i < csa.length(); i++) {
				int last = (csa.index(i) + csa.length() - 1) % csa.length();
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
			char[] t = new char[s.length()];
			for (int i = 0; i < s.length(); i++) {
				t[i] = s.charAt(i);
			}
			char[] firstColumn = getSortedT(t);
			int[] next = getNextArray(t, firstColumn);

			for (int i = 0; i < firstColumn.length; i++) {
				char letter = firstColumn[first];
				first = next[first];
				BinaryStdOut.write(letter);
			}

		}
		BinaryStdOut.close();
		BinaryStdIn.close();
	}

	private static char[] getSortedT(char[] t) {
		char[] firstColumn = new char[t.length];
		for (int i = 0; i < t.length; i++) {
			firstColumn[i] = t[i];
		}
		Arrays.sort(firstColumn);
		return firstColumn;
	}

	private static int[] getNextArray(char[] t, char[] firstColumn) {
		int[] next = new int[t.length];
		boolean[] marked = new boolean[t.length];

		for (int i = 0; i < t.length; i++) {
			for (int j = 0; j < t.length; j++) {
				if (t[i] == firstColumn[j] && !marked[j]) {
					marked[j] = true;
					next[j] = i;
					break;
				}
			}
		}
		return next;
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
