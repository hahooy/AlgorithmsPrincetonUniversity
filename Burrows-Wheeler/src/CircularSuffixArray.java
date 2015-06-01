public class CircularSuffixArray {
	private int length;
	private int[] index;
	
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		length = s.length();
		index = new int[length];
		SuffixArrayX suffixArray = new SuffixArrayX(s);
		for (int i = 0; i < length; i++) {
			index[i] = suffixArray.index(i);
		}		
	}

	// length of s
	public int length() {
		return length;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		return index[i];
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		CircularSuffixArray sa = new CircularSuffixArray("ABRACADABRA!");
		// System.out.println(sa.length());
		for(int i = 0; i < sa.length(); i++) {
			System.out.println(sa.index(i));
		}
	}
}
