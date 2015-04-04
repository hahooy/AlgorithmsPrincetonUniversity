import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WordNetTest {
	private WordNet wn;

	@Before
	public void setUp() throws Exception {
		wn = new WordNet("/Users/hahooy1/Downloads/wordnet/synsets.txt",
				"/Users/hahooy1/Downloads/wordnet/hypernyms.txt");
	}

	@After
	public void tearDown() throws Exception {
		wn = null;
	}

	@Ignore
	public void testNouns() {

	}

	@Test
	public void testIsNoun() {
		assertTrue(wn.isNoun("ACE_inhibitor"));
	}

	@Test
	public void testDistance() {
		int dist = wn.distance("worm", "bird");
		assertEquals(5, dist);
	}

	@Test
	public void testSap() {
		String anc = wn.sap("worm", "bird");
		assertEquals("animal animate_being beast brute creature fauna", anc);
	}

}
