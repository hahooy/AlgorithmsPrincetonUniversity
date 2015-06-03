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
		final int R = 256;
		while (!BinaryStdIn.isEmpty()) {
			int first = BinaryStdIn.readInt();
			String s = BinaryStdIn.readString();
			int N = s.length();

			// sort the characters in s using key index counting
			int count[] = new int[R + 1], next[] = new int[N];
			char[] firstCol = new char[N];
			for (int i = 0; i < N; i++) {
				count[s.charAt(i) + 1]++;
			}

			for (int i = 0; i < R; i++) {
				count[i + 1] += count[i];
			}

			for (int i = 0; i < N; i++) {
				next[count[s.charAt(i)]] = i;
				firstCol[count[s.charAt(i)]++] = s.charAt(i);
			}

			for (int i = 0; i < N; i++) {
				char letter = firstCol[first];
				first = next[first];
				BinaryStdOut.write(letter);
			}

		}
		BinaryStdOut.close();
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
