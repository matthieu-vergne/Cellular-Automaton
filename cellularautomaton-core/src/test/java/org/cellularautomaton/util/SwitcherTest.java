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
		switcher.addComponent(c1);
		assertEquals(1, switcher.getAllComponents().size());
		assertTrue(switcher.getAllComponents().contains(c1));
		
		switcher.addComponent(c2);
		assertEquals(2, switcher.getAllComponents().size());
		assertTrue(switcher.getAllComponents().contains(c1));
		assertTrue(switcher.getAllComponents().contains(c2));
		
		switcher.addComponent(c3);
		assertEquals(3, switcher.getAllComponents().size());
		assertTrue(switcher.getAllComponents().contains(c1));
		assertTrue(switcher.getAllComponents().contains(c2));
		assertTrue(switcher.getAllComponents().contains(c3));
	}

	@Test
	public void testSwitching() {
		String c1 = "a";
		String c2 = "b";
		String c3 = "c";
		Switcher<String> switcher = new Switcher<String>();
		switcher.addComponent(c1);
		switcher.addComponent(c2);
		switcher.addComponent(c3);
		
		assertEquals(c1, switcher.getComponent());
		
		switcher.switchComponent();
		assertEquals(c2, switcher.getComponent());
		
		switcher.switchComponent();
		assertEquals(c3, switcher.getComponent());
		
		switcher.switchComponent();
		assertEquals(c1, switcher.getComponent());
	}

	@Test
	public void testNext() {
		String c1 = "a";
		String c2 = "b";
		String c3 = "c";
		Switcher<String> switcher = new Switcher<String>();
		switcher.addComponent(c1);
		switcher.addComponent(c2);
		switcher.addComponent(c3);
		
		assertEquals(c1, switcher.getComponent());
		assertEquals(c2, switcher.next());
		assertEquals(c3, switcher.next());
		assertEquals(c1, switcher.next());
		assertEquals(c2, switcher.next());
		assertEquals(c3, switcher.next());
	}

}
