import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KdTreeTest {
	private KdTree kdtree;
	private RectHV rect = new RectHV(0.2, 0.2, 0.4, 0.4);
	private Point2D pt1 = new Point2D(0.1, 0.1);
	private Point2D pt2 = new Point2D(0.3, 0.3);
	private Point2D pt3 = new Point2D(0.9, 0.9);
	private Point2D pt4 = new Point2D(0.25, 0.25);

	@Before
	public void setUp() throws Exception {
		kdtree = new KdTree();
		kdtree.insert(pt1);
		kdtree.insert(pt2);
		kdtree.insert(pt3);
		kdtree.insert(pt4);
	}

	@After
	public void tearDown() throws Exception {
		kdtree = null;
	}

	@Test
	public void testIsEmpty() {
		assertTrue(!kdtree.isEmpty());
	}

	@Test
	public void testIsEmpty1() {
		kdtree = new KdTree();
		assertTrue(kdtree.isEmpty());
	}

	@Test
	public void testSize() {
		assertEquals(4, kdtree.size());
	}

	@Test
	public void testInsert() {
		Point2D pt = new Point2D(0.5, 0.3);
		kdtree.insert(pt);
		assertTrue(kdtree.contains(pt));
	}

	@Test
	public void testContains() {
		assertTrue(kdtree.contains(new Point2D(0.1, 0.1)));
	}

	@Test
	public void testContains1() {
		assertTrue(kdtree.contains(new Point2D(0.3, 0.3)));
	}

	@Test
	public void testContains2() {
		assertTrue(kdtree.contains(new Point2D(0.9, 0.9)));
	}

	@Test
	public void testNearest() {
		kdtree = new KdTree();		
		assertEquals(null, kdtree.nearest(pt1));
	}	
}
