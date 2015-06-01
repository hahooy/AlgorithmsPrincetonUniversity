import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BurrowsWheelerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncode() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecode() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNextArray() {
		char[] t = { 'A', 'R', 'D', '!', 'R', 'C', 'A', 'A', 'A', 'A', 'B', 'B' };
		int first = 3;
		int[] next = BurrowsWheeler.getNextArray(t);
		for (int i : next) {
			System.out.print(i + " ");
		}
	}
}
