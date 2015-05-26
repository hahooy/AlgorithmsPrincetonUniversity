import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SeamCarverTest {
	Picture image;
	SeamCarver sc;

	@Before
	public void setUp() throws Exception {
		In in = new In(
				"/Users/hahooy1/Documents/workspace/Seam_Carving/seamCarvingTest/3x4.txt");
		int w = Integer.parseInt(in.readLine());
		int h = Integer.parseInt(in.readLine());
		image = new Picture(w, h);

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				String[] components = in.readLine().substring(1).split("[,)]");
				int red = Integer.parseInt(components[0]);
				int green = Integer.parseInt(components[1]);
				int blue = Integer.parseInt(components[2]);
				image.set(j, i, new Color(red, green, blue));
			}
		}

		sc = new SeamCarver(image);
	}

	@After
	public void tearDown() throws Exception {
		image = null;
		sc = null;
	}

	@Ignore
	public void testPicture() {
		for (int i = 0; i < sc.height(); i++) {
			for (int j = 0; j < sc.width(); j++) {
				System.out.printf("%s  ", sc.picture().get(j, i));
			}
			System.out.println();
		}
	}

	@Test
	public void testWidth() {
		assertEquals(sc.width(), 3);
	}

	@Test
	public void testHeight() {
		assertEquals(sc.height(), 4);
	}

	@Ignore
	public void testEnergy() {
		for (int i = 0; i < sc.height(); i++) {
			for (int j = 0; j < sc.width(); j++) {
				System.out.printf("%10.1f", sc.energy(j, i));
			}
			System.out.println();
		}
	}

	@Test
	public void testFindHorizontalSeam() {
		sc.findHorizontalSeam();
	}

	@Ignore
	public void testFindVerticalSeam() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testRemoveHorizontalSeam() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testRemoveVerticalSeam() {
		fail("Not yet implemented");
	}

}
