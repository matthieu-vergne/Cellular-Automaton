package org.cellularautomaton.util;

import java.util.Iterator;
import java.util.TreeSet;

import junit.framework.TestCase;

public class CoordsTest extends TestCase {

	public void testCreation() {
		Coords coords = new Coords();
		assertEquals(0, coords.getDimensions());

		coords = new Coords(1, 2, 3);
		assertEquals(3, coords.getDimensions());
		assertEquals(1, coords.get(0));
		assertEquals(2, coords.get(1));
		assertEquals(3, coords.get(2));
	}

	public void testComparison() {
		Coords c1 = new Coords(1);
		Coords c2 = new Coords(2);
		Coords c3 = new Coords(1, 1);
		Coords c4 = new Coords(1, 2);
		Coords c5 = new Coords(2, 1);

		TreeSet<Coords> set = new TreeSet<Coords>();
		set.add(c2);
		set.add(c4);
		set.add(c1);
		set.add(c5);
		set.add(c3);

		Iterator<Coords> iterator = set.iterator();
		assertEquals(c1, iterator.next());
		assertEquals(c2, iterator.next());
		assertEquals(c3, iterator.next());
		assertEquals(c4, iterator.next());
		assertEquals(c5, iterator.next());
	}

	public void testValues() {
		Coords c = new Coords(1, 2, 3);
		assertEquals(3, c.getDimensions());
		assertEquals(1, c.get(0));
		assertEquals(2, c.get(1));
		assertEquals(3, c.get(2));

		c.set(0, 5);
		assertEquals(3, c.getDimensions());
		assertEquals(5, c.get(0));
		assertEquals(2, c.get(1));
		assertEquals(3, c.get(2));

		c.set(1, 4);
		assertEquals(3, c.getDimensions());
		assertEquals(5, c.get(0));
		assertEquals(4, c.get(1));
		assertEquals(3, c.get(2));

		c.set(2, 12);
		assertEquals(3, c.getDimensions());
		assertEquals(5, c.get(0));
		assertEquals(4, c.get(1));
		assertEquals(12, c.get(2));
	}

	public void testToString() {
		assertEquals("()", new Coords().toString());
		assertEquals("(1,2,3)", new Coords(1, 2, 3).toString());
	}

	public void testEquals() {
		Coords c1a = new Coords(1);
		Coords c2a = new Coords(2);
		Coords c3a = new Coords(1, 1);
		Coords c4a = new Coords(1, 2);
		Coords c5a = new Coords(2, 1);

		assertTrue(c1a.equals(new int[] {1}));
		assertFalse(c1a.equals(new int[] {2}));
		assertFalse(c1a.equals(new int[] {1, 1}));
		assertFalse(c1a.equals(new int[] {1, 2}));
		assertFalse(c1a.equals(new int[] {2, 1}));

		assertFalse(c2a.equals(new int[] {1}));
		assertTrue(c2a.equals(new int[] {2}));
		assertFalse(c2a.equals(new int[] {1, 1}));
		assertFalse(c2a.equals(new int[] {1, 2}));
		assertFalse(c2a.equals(new int[] {2, 1}));

		assertFalse(c3a.equals(new int[] {1}));
		assertFalse(c3a.equals(new int[] {2}));
		assertTrue(c3a.equals(new int[] {1, 1}));
		assertFalse(c3a.equals(new int[] {1, 2}));
		assertFalse(c3a.equals(new int[] {2, 1}));

		assertFalse(c4a.equals(new int[] {1}));
		assertFalse(c4a.equals(new int[] {2}));
		assertFalse(c4a.equals(new int[] {1, 1}));
		assertTrue(c4a.equals(new int[] {1, 2}));
		assertFalse(c4a.equals(new int[] {2, 1}));

		assertFalse(c5a.equals(new int[] {1}));
		assertFalse(c5a.equals(new int[] {2}));
		assertFalse(c5a.equals(new int[] {1, 1}));
		assertFalse(c5a.equals(new int[] {1, 2}));
		assertTrue(c5a.equals(new int[] {2, 1}));


		Coords c1b = new Coords(1);
		Coords c2b = new Coords(2);
		Coords c3b = new Coords(1, 1);
		Coords c4b = new Coords(1, 2);
		Coords c5b = new Coords(2, 1);

		assertTrue(c1a.equals(c1b));
		assertFalse(c1a.equals(c2b));
		assertFalse(c1a.equals(c3b));
		assertFalse(c1a.equals(c4b));
		assertFalse(c1a.equals(c5b));

		assertFalse(c2a.equals(c1b));
		assertTrue(c2a.equals(c2b));
		assertFalse(c2a.equals(c3b));
		assertFalse(c2a.equals(c4b));
		assertFalse(c2a.equals(c5b));

		assertFalse(c3a.equals(c1b));
		assertFalse(c3a.equals(c2b));
		assertTrue(c3a.equals(c3b));
		assertFalse(c3a.equals(c4b));
		assertFalse(c3a.equals(c5b));

		assertFalse(c4a.equals(c1b));
		assertFalse(c4a.equals(c2b));
		assertFalse(c4a.equals(c3b));
		assertTrue(c4a.equals(c4b));
		assertFalse(c4a.equals(c5b));

		assertFalse(c5a.equals(c1b));
		assertFalse(c5a.equals(c2b));
		assertFalse(c5a.equals(c3b));
		assertFalse(c5a.equals(c4b));
		assertTrue(c5a.equals(c5b));
	}
}
