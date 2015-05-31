public class MyTST<Value> extends TST<Value> {
	private int N; // size
	private Node<Value> root; // root of TST

	private static class Node<Value> {
		private char c; // character
		private Node<Value> left, mid, right; // left, middle, and right
												// subtries
		private Value val; // value associated with string
	}

	// return subtrie corresponding to given key
	private Node<Value> get(Node<Value> x, String key, int d) {
		if (key == null)
			throw new NullPointerException();
		if (key.length() == 0)
			throw new IllegalArgumentException("key must have length >= 1");
		if (x == null)
			return null;
		char c = key.charAt(d);
		if (c < x.c)
			return get(x.left, key, d);
		else if (c > x.c)
			return get(x.right, key, d);
		else if (d < key.length() - 1)
			return get(x.mid, key, d + 1);
		else
			return x;
	}

}
