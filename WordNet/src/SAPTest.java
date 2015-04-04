import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SAPTest {
	private SAP sap;

	@Before
	public void setUp() throws Exception {
		In in = new In("/Users/hahooy1/Downloads/wordnet/Digraph1.txt");
		Digraph G = new Digraph(in);
		sap = new SAP(G);
	}

	@After
	public void tearDown() throws Exception {
		sap = null;
	}

	@Test
	public void testLengthIntInt() {
		int length = sap.length(9, 12);
		assertEquals(3, length);
	}

	@Test
	public void testLengthIntInt1() {
		int length = sap.length(3, 11);
		assertEquals(4, length);
	}

	@Test
	public void testLengthIntInt2() {
		int length = sap.length(7, 2);
		assertEquals(4, length);
	}

	@Test
	public void testLengthIntInt3() {
		int length = sap.length(1, 6);
		assertEquals(-1, length);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testLengthIntInt4() {
		int length = sap.length(1, 600);
	}

	@Test
	public void testAncestorIntInt() {
		int ancestor = sap.ancestor(3, 11);
		assertEquals(1, ancestor);
	}

	@Test
	public void testAncestorIntInt1() {
		int ancestor = sap.ancestor(1, 6);
		assertEquals(-1, ancestor);
	}

	@Test
	public void testAncestorIntInt2() {
		int ancestor = sap.ancestor(7, 2);
		assertEquals(0, ancestor);
	}

	@Test
	public void testAncestorIntInt3() {
		int ancestor = sap.ancestor(9, 12);
		assertEquals(5, ancestor);
	}

	@Test
	public void testLengthIterableOfIntegerIterableOfInteger() {
		Queue<Integer> v = new Queue<Integer>();
		Queue<Integer> w = new Queue<Integer>();
		v.enqueue(3);
		v.enqueue(1);
		w.enqueue(11);
		w.enqueue(6);
		int length = sap.length(v, w);
		assertEquals(3, length);
	}

	@Ignore
	public void testAncestorIterableOfIntegerIterableOfInteger() {
		fail("Not yet implemented");
	}

}
