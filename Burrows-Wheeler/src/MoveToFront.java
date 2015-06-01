public class MoveToFront {
	// apply move-to-front encoding, reading from standard input and writing to
	// standard output
	public static void encode() {
		// initialize the original sequence
		char[] sequence = initSequence();

		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar(8);
			int index = 0;

			for (int i = 0; i < sequence.length; i++) {
				if (sequence[i] == c) {
					index = i;
					break;
				}
			}
			
			BinaryStdOut.write(index, 8);
			move(index, sequence);
		}
		BinaryStdOut.close();
		BinaryStdIn.close();
	}

	// initialize an original sequence of character
	private static char[] initSequence() {
		int R = 256; // number of ASCII characters
		char[] sequence = new char[R];
		for (int i = 0; i < R; i++) {
			sequence[i] = (char) i;
		}
		return sequence;
	}

	// move the letter to the front
	private static void move(int index, char[] sequence) {
		char c = sequence[index];
		// move all entry before c one index further
		for (int i = index; i > 0; i--) {
			sequence[i] = sequence[i - 1];
		}
		// put c in the first entry
		sequence[0] = c;
	}

	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
	public static void decode() {
		char[] sequence = initSequence();

		while (!BinaryStdIn.isEmpty()) {
			int index = BinaryStdIn.readInt(8);
			BinaryStdOut.write(sequence[index], 8);
			move(index, sequence);
		}
		BinaryStdOut.close();
		BinaryStdIn.close();
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
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
