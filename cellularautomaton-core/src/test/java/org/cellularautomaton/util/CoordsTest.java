package org.cellularautomaton.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Test;

public class CoordsTest {

	@Test
	public void testCreation() {
		Coords coords = new Coords();
		assertEquals(0, coords.getDimensions());

		coords = new Coords(1, 2, 3);
		assertEquals(3, coords.getDimensions());
		assertEquals(1, coords.get(0));
		assertEquals(2, coords.get(1));
		assertEquals(3, coords.get(2));

		coords = new Coords("(1, 2, 3)");
		assertEquals(3, coords.getDimensions());
		assertEquals(1, coords.get(0));
		assertEquals(2, coords.get(1));
		assertEquals(3, coords.get(2));
	}

	@Test
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

	@Test
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

	@Test
	public void testToString() {
		assertEquals("()", new Coords().toString());
		assertEquals("(0)", new Coords(0).toString());
		assertEquals("(1, 2, 3)", new Coords(1, 2, 3).toString());
	}

	@Test
	public void testEqualsReflexive() {
		for (Coords c : new Coords[] { new Coords(), new Coords(2),
				new Coords(3, 5, 4) }) {
			assertTrue("CoordsTest.equals() must be reflexive", c.equals(c));
		}
	}

	@Test
	public void testEqualsSymmetric() {
		Coords c1 = new Coords(1, 1);
		Coords c2 = new Coords(1, 1);
		Coords c3 = new Coords(2, 2);

		assertTrue("CoordsTest.equals() must be symmetric",
				c1.equals(c2) == c2.equals(c1));
		assertTrue("CoordsTest.equals() must be symmetric",
				c1.equals(c3) == c3.equals(c1));
	}

	@Test
	public void testEqualsTransitive() {
		Coords c1 = new Coords(1, 1);
		Coords c2 = new Coords(1, 1);
		Coords c3 = new Coords(1, 1);

		if (c1.equals(c2) && c2.equals(c3)) {
			assertTrue("CoordsTest.equals() must be transitive", c1.equals(c3));
		}
	}

	@Test
	public void testEqualsConsistent() {
		Coords c1 = new Coords(1, 1);
		Coords c2 = new Coords(1, 1);
		assertTrue("CoordsTest.equals() must be consistent",
				c1.equals(c1) == c2.equals(c2));
	}

	@Test
	public void testEqualsNull() {
		Coords c1 = new Coords(1, 1);
		assertFalse(c1.equals(null));
	}

	@Test
	public void testEquals() {
		Coords c1a = new Coords(1);
		Coords c2a = new Coords(2);
		Coords c3a = new Coords(1, 1);
		Coords c4a = new Coords(1, 2);
		Coords c5a = new Coords(2, 1);

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
