public class Subset {
	public static void main(String[] args) {
		RandomizedQueue<String> q = new RandomizedQueue<String>();
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			q.enqueue(item);
		}
		for (int i = 0; i < Integer.parseInt(args[0]); i++) {
			StdOut.println(q.dequeue());
		}
	}
}