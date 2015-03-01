import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RandomizedQueueTest {
	private RandomizedQueue<Integer> myRQ;
	private int[] myList = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	@Before
	public void setUp() throws Exception {
		myRQ = new RandomizedQueue<Integer>();
	}

	@After
	public void tearDown() throws Exception {
		myRQ = null;
	}

	@Test
	public void testIsEmpty() {
		myRQ.enqueue(1);
		myRQ.dequeue();
		assertTrue(myRQ.isEmpty());
	}

	@Test
	public void testSize() {
		for (int i : myList) {
			myRQ.enqueue(i);
		}
		assertEquals(10,myRQ.size());
	}

	@Test(expected = NullPointerException.class)
	public void testEnqueue() {
		myRQ.enqueue(null);
	}

	@Test(expected = NoSuchElementException.class)
	public void testDequeue() {
		myRQ.dequeue();
	}

	@Test
	public void testSample() {
		myRQ.enqueue(1);
		assertEquals(new Integer(1), myRQ.sample());
	}

	@Test
	public void testIterator() {
		for(int i : myList) {
			myRQ.enqueue(i);
		}
		for(Integer i : myRQ) {
			StdOut.println(i);
		}
	}

}
