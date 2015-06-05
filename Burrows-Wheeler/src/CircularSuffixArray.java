public class CircularSuffixArray {
	private String s;
	private int length;
	private Suffix[] indexArray;

	// circular suffix array of s
	public CircularSuffixArray(String s) {
		this.s = s;
		length = s.length();
		indexArray = new Suffix[length];
		for (int i = 0; i < length; i++) {
			indexArray[i] = new Suffix(i);
		}
		MergeX.sort(indexArray);
	}

	private class Suffix implements Comparable<Suffix> {
		private final int index;

		private Suffix(int index) {
			this.index = index;
		}

		private char charAt(int i) {
			int newIndex = index + i;
			if (newIndex >= length) {
				return s.charAt(newIndex - length);
			} else {
				return s.charAt(index + i);
			}
		}

		public int compareTo(Suffix that) {
			if (this == that)
				return 0; // optimization
			for (int i = 0; i < length; i++) {
				if (this.charAt(i) < that.charAt(i))
					return -1;
				if (this.charAt(i) > that.charAt(i))
					return +1;
			}
			return 0;
		}
	}

	// length of s
	public int length() {
		return length;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		return indexArray[i].index;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		String s = "ABBABABABB";
		CircularSuffixArray sa = new CircularSuffixArray(s);

		for (int i = 0; i < sa.length(); i++) {
			System.out.println(sa.index(i));
		}
	}
}
