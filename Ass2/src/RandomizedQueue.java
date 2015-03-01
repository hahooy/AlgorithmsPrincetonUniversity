import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] q;
	private int N = 0;

	// construct an empty randomized queue
	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		q = (Item[]) new Object[2];
	}

	// is the queue empty?
	public boolean isEmpty() {
		return N == 0;
	}

	// return the number of items on the queue
	public int size() {
		return N;
	}

	// resize array
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		Item[] tempArray = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			tempArray[i] = q[i];
		}
		q = tempArray;
	}

	// add the item
	public void enqueue(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		// increase array length if necessary
		if (N == q.length) {
			resize(2 * q.length);
		}
		q[N++] = item;
	}

	// remove and return a random item
	public Item dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		int removeIndex = StdRandom.uniform(N);
		Item item = q[removeIndex];
		q[removeIndex] = q[--N];
		q[N] = null;
		// shrink array if necessary
		if (N > 0 && N == q.length / 4) {
			resize(q.length / 2);
		}
		return item;
	}

	// return (but do not remove) a random item
	public Item sample() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		int returnIndex = StdRandom.uniform(N);
		return q[returnIndex];
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	// an iterator
	private class RandomizedQueueIterator implements Iterator<Item> {
		private int i;
		private Item[] tempArray;

		@SuppressWarnings("unchecked")
		public RandomizedQueueIterator() {
			i = 0;
			tempArray = (Item[]) new Object[N];
			for (int i = 0; i < N; i++) {
				tempArray[i] = q[i];
			}
			StdRandom.shuffle(tempArray);
		}

		public boolean hasNext() {
			return i < N;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Item item = tempArray[i++];
			return item;
		}
	}

	// unit testing
	public static void main(String[] args) {
		RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
		int[] myList = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		for (int i : myList) {
			q.enqueue(i);
		}
		for (Integer k : q) {
			StdOut.println(k);
		}
	}
}
