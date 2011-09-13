package org.cellularautomaton.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SwitcherTest {

	@Test
	public void testAllComponents() {
		String c1 = "a";
		String c2 = "b";
		String c3 = "c";
		Switcher<String> switcher = new Switcher<String>();
		switcher.add(c1);
		assertEquals(1, switcher.getAll().size());
		assertTrue(switcher.getAll().contains(c1));
		
		switcher.add(c2);
		assertEquals(2, switcher.getAll().size());
		assertTrue(switcher.getAll().contains(c1));
		assertTrue(switcher.getAll().contains(c2));
		
		switcher.add(c3);
		assertEquals(3, switcher.getAll().size());
		assertTrue(switcher.getAll().contains(c1));
		assertTrue(switcher.getAll().contains(c2));
		assertTrue(switcher.getAll().contains(c3));
	}

	@Test
	public void testSwitching() {
		String c1 = "a";
		String c2 = "b";
		String c3 = "c";
		Switcher<String> switcher = new Switcher<String>();
		switcher.add(c1);
		switcher.add(c2);
		switcher.add(c3);
		
		assertEquals(c1, switcher.get());
		
		switcher.switchComponent();
		assertEquals(c2, switcher.get());
		
		switcher.switchComponent();
		assertEquals(c3, switcher.get());
		
		switcher.switchComponent();
		assertEquals(c1, switcher.get());
		
		switcher.switchComponent();
		assertEquals(c2, switcher.get());
		
		switcher.switchComponent();
		assertEquals(c3, switcher.get());
		
		switcher.remove(c2);
		assertEquals(c3, switcher.get());
		
		switcher.remove(c2);
		assertEquals(c3, switcher.get());
		
		switcher.switchComponent();
		assertEquals(c1, switcher.get());
		
		switcher.remove(c1);
		assertEquals(c3, switcher.get());
	}

	@Test
	public void testNext() {
		String c1 = "a";
		String c2 = "b";
		String c3 = "c";
		Switcher<String> switcher = new Switcher<String>();
		switcher.add(c1);
		switcher.add(c2);
		switcher.add(c3);
		
		assertEquals(c1, switcher.get());
		assertEquals(c2, switcher.next());
		assertEquals(c3, switcher.next());
		assertEquals(c1, switcher.next());
		assertEquals(c2, switcher.next());
		assertEquals(c3, switcher.next());
	}

	@Test
	public void testEmpty() {
		Switcher<String> switcher = new Switcher<String>();
		assertTrue(switcher.isEmpty());
		
		String c1 = "a";
		switcher.add(c1);
		assertFalse(switcher.isEmpty());
		
		String c2 = "b";
		switcher.add(c2);
		assertFalse(switcher.isEmpty());
		
		String c3 = "c";
		switcher.add(c3);
		assertFalse(switcher.isEmpty());
		
		switcher.remove(c2);
		assertFalse(switcher.isEmpty());
		
		switcher.remove(c1);
		assertFalse(switcher.isEmpty());
		
		switcher.remove(c3);
		assertTrue(switcher.isEmpty());
	}

}
