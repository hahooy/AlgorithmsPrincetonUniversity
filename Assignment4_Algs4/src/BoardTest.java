import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

public class BoardTest {
	Board board;

	private static int[][] buildBlocks() {
		int[][] blocks = new int[3][3];
		int[] input = { 4, 1, 3, 0, 2, 5, 7, 8, 6 };
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				blocks[i][j] = input[i * blocks.length + j];
			}
		}
		return blocks;
	}

	@Before
	public void setUp() throws Exception {
		board = new Board(buildBlocks());
	}

	@After
	public void tearDown() throws Exception {
		board = null;
	}

	@Test
	public void testDimension() {
		int N = board.dimension();
		assertEquals(3, N);
	}

	@Test
	public void testHamming() {
		int ham = board.hamming();
		assertEquals(5, ham);
	}

	@Test
	public void testManhattan() {
		int man = board.manhattan();
		assertEquals(5, man);
	}

	@Test
	public void testIsGoal() {
		assertTrue(!board.isGoal());
	}

	@Ignore
	public void testTwin() {
		fail("Not yet implemented");
	}

	@Test
	public void testEquals() {
		Board board1 = new Board(buildBlocks());
		assertTrue(board.equals(board1));
	}

	@Test
	public void testEquals1() {
		int[][] blocks = new int[3][3];
		int k = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				blocks[i][j] = k++;
			}
		}
		Board board1 = new Board(blocks);
		assertTrue(!board.equals(board1));
	}

	@Ignore
	public void testNeighbors() {
		Iterable<Board> nb = board.neighbors();
		int j = 0;
		for (Board i : nb) {
			if (board.equals(i))
				break;
			j++;
		}
		assertEquals(3,j);
	}

}
