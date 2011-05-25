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
		assertEquals("[]", new Coords().toString());
		assertEquals("[1, 2, 3]", new Coords(1, 2, 3).toString());
	}

	public void testEqualsReflexive() {
		Coords c = new Coords(2);
		assertTrue("CoordsTest.equals() must be reflexive", c.equals(c));
	}
	
	public void testEqualsSymmetric(){
		Coords c1 = new Coords(1,1);
		Coords c2 = new Coords(1,1);
		Coords c3 = new Coords(2,2);
		
		assertTrue("CoordsTest.equals() must be symmetric", c1.equals(c2) == c2.equals(c1));
		assertTrue("CoordsTest.equals() must be symmetric", c1.equals(c3) == c3.equals(c1));
	}
	
	public void testEqualsTransitive(){
		Coords c1 = new Coords(1,1);
		Coords c2 = new Coords(1,1);
		Coords c3 = new Coords(1,1);
		
		if(c1.equals(c2) && c2.equals(c3)) {
			assertTrue("CoordsTest.equals() must be transitive",  c1.equals(c3));
		}
	}
	
	public void testEqualsConsistent(){
		Coords c1 = new Coords(1,1);
		Coords c2 = new Coords(1,1);
		assertTrue("CoordsTest.equals() must be consistent", c1.equals(c1) == c2.equals(c2));
	}
	
	public void testEqualsNull(){
		Coords c1 = new Coords(1,1);
		assertFalse(c1.equals(null));
	}
}
