import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
	private int N; // size of the deque
	private Node first; // first node of the deque
	private Node last; // last node of the Deque

	// construct an empty deque
	public Deque() {
		first = null;
		last = null;
		N = 0;
	}

	// double linked list inner class
	private class Node {
		private Item item;
		private Node back;
		private Node next;
	}

	// is the deque empty?
	public boolean isEmpty() {
		return N == 0;
	}

	// return the number of items on the deque
	public int size() {
		return N;
	}

	// add the item to the front
	public void addFirst(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		if (first != null) {
			Node oldFirst = first;
			first = new Node();
			first.item = item;
			first.next = oldFirst;
			oldFirst.back = first;
		} else {
			first = new Node();
			first.item = item;
			last = first;
		}
		N++;
	}

	// add the item to the end
	public void addLast(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		if (last != null) {
			Node oldLast = last;
			last = new Node();
			last.item = item;
			last.back = oldLast;
			oldLast.next = last;
		} else {
			last = new Node();
			last.item = item;
			first = last;
		}
		N++;
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		Item item = first.item;

		if (first.next != null && first.next != last) {
			first = first.next;
			first.back = null;
		} else if (first.next != null && first.next == last) {
			first = last;
			first.next = null;
			first.back = null;
		} else {
			first = null;
			last = null;
		}
		N--;
		return item;
	}

	// remove and return the item from the end
	public Item removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		Item item = last.item;

		if (last.back != null && last.back != first) {
			last = last.back;
			last.next = null;
		} else if (last.back != null && last.back == first) {
			last = first;
			last.next = null;
			last.back = null;
		} else {
			last = null;
			first = null;
		}
		N--;
		return item;
	}

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	// an iterator
	private class DequeIterator implements Iterator<Item> {
		private Node current = first;

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Item item = current.item;
			current = current.next;
			return item;
		}
	}

	// unit testing
	public static void main(String[] args) {
		Deque<Integer> aDeque = new Deque<Integer>();
		int[] aList = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		for (int i : aList) {
			aDeque.addFirst(i);
		}
		for (int i : aDeque) {
			System.out.print(i + " ");
		}
	}
}
