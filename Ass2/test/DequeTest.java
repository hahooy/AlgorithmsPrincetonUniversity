import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;
import org.junit.Test;

public class DequeTest {

	private Deque<Integer> myDeque;
	private int[] myList = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	@Before
	public void setUp() throws Exception {
		myDeque = new Deque<Integer>();
	}

	@After
	public void tearDown() throws Exception {
		myDeque = null;
	}

	@Test
	public void testIsEmpty() {
		myDeque.addFirst(1);
		myDeque.removeFirst();
		assertTrue(myDeque.isEmpty());
	}

	@Test
	public void testSize() {
		for (int i : myList) {
			myDeque.addFirst(i);
		}
		int size = myDeque.size();
		assertEquals(10, size);
	}

	@Test
	public void testAddFirst() {
		myDeque.addFirst(1);
		myDeque.addFirst(2);
		assertEquals(2, myDeque.removeFirst().intValue());
	}

	@Test(expected = NullPointerException.class)
	public void testAddFirst1() {
		myDeque.addFirst(null);
	}

	@Test
	public void testAddLast() {
		myDeque.addLast(1);
		myDeque.addLast(2);
		assertEquals(1, myDeque.removeFirst().intValue());
	}

	@Test(expected = NullPointerException.class)
	public void testAddLast1() {
		myDeque.addLast(null);
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveFirst() {
		myDeque.removeFirst();
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveFirst1() {
		myDeque.removeFirst();
	}

	@Test
	public void testRemoveLast() {
		myDeque.addLast(1);
		myDeque.addLast(2);
		assertEquals(2, myDeque.removeLast().intValue());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveLast1() {
		myDeque.removeLast();
	}

	@Test
	public void testIterator() {
		int[] k = new int[10];
		for (int i : myList) {
			myDeque.addFirst(i);
		}
		for (int i : myDeque) {
			k[i] = i;
		}
		assertArrayEquals(myList, k);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testIterator1() {
		myDeque.iterator().remove();
	}

	@Test(expected = NoSuchElementException.class)
	public void testIterator2() {
		myDeque.iterator().next();
	}

}
