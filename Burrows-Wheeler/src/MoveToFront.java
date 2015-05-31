import java.util.HashMap;

public class MoveToFront {		
	// apply move-to-front encoding, reading from standard input and writing to
	// standard output
	public static void encode() {
		HashMap<Character, Integer> sequence = new HashMap<Character,Integer>(256);
		for (int i = 0; i < 256; i++) {
			sequence.put((char) i, i); 
			System.out.printf("%c -- %d\n", (char) i, sequence.get((char) i));
		}
		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar(8);
			int index = sequence.get(c);
			BinaryStdOut.write(index);
		}
	}

	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
	public static void decode() {

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
