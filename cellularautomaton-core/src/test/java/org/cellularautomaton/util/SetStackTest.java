package org.cellularautomaton.util;

import junit.framework.TestCase;

public class SetStackTest extends TestCase {

	public void testPushPop() {
		SetStack<String> stack = new SetStack<String>();
		stack.push("1");
		stack.push("2");
		stack.push("3");
		stack.push("4");
		stack.push("5");
		stack.push("6");
		assertEquals("1", stack.pop());
		assertEquals("2", stack.pop());
		assertEquals("3", stack.pop());
		assertEquals("4", stack.pop());
		assertEquals("5", stack.pop());
		assertEquals("6", stack.pop());
	}
	
	public void testGet() {
		SetStack<String> stack = new SetStack<String>();
		stack.push("1");
		stack.push("2");
		stack.push("3");
		stack.push("4");
		stack.push("5");
		stack.push("6");
		assertEquals("4", stack.get(3));
		assertEquals("6", stack.get(5));
		assertEquals("1", stack.get(0));
		assertEquals("3", stack.get(2));
		assertEquals("5", stack.get(4));
		assertEquals("2", stack.get(1));
	}
	
	public void testSize(){
		SetStack<String> stack = new SetStack<String>();
		assertEquals(0, stack.getSize());
		stack.push("1");
		assertEquals(1, stack.getSize());
		stack.push("2");
		assertEquals(2, stack.getSize());
		stack.push("3");
		assertEquals(3, stack.getSize());
		stack.push("4");
		assertEquals(4, stack.getSize());
		stack.push("5");
		assertEquals(5, stack.getSize());
		stack.push("6");
		assertEquals(6, stack.getSize());
		stack.pop();
		assertEquals(5, stack.getSize());
		stack.pop();
		assertEquals(4, stack.getSize());
		stack.pop();
		assertEquals(3, stack.getSize());
		stack.pop();
		assertEquals(2, stack.getSize());
		stack.pop();
		assertEquals(1, stack.getSize());
		stack.pop();
		assertEquals(0, stack.getSize());
	}
}
