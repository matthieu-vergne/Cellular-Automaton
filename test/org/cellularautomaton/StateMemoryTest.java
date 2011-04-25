package org.cellularautomaton;

import junit.framework.TestCase;

public class StateMemoryTest extends TestCase {

	public void testMemoryInit() {
		StateMemory<String> memory = new StateMemory<String>(3, "");
		assertEquals("", memory.getState(0));
		assertEquals("", memory.getState(1));
		assertEquals("", memory.getState(2));
	}

	public void testMemoryPushGetForce() {
		StateMemory<String> memory = new StateMemory<String>(3, "");
		assertEquals("", memory.getState(0));
		assertEquals("", memory.getState(1));
		assertEquals("", memory.getState(2));

		memory.pushNewState("1");
		assertEquals("1", memory.getState(0));
		assertEquals("", memory.getState(1));
		assertEquals("", memory.getState(2));

		memory.pushNewState("2");
		assertEquals("2", memory.getState(0));
		assertEquals("1", memory.getState(1));
		assertEquals("", memory.getState(2));

		memory.pushNewState("3");
		assertEquals("3", memory.getState(0));
		assertEquals("2", memory.getState(1));
		assertEquals("1", memory.getState(2));

		memory.pushNewState("4");
		assertEquals("4", memory.getState(0));
		assertEquals("3", memory.getState(1));
		assertEquals("2", memory.getState(2));

		memory.forceCurrentState("15");
		assertEquals("15", memory.getState(0));
		assertEquals("3", memory.getState(1));
		assertEquals("2", memory.getState(2));

		memory.pushNewState("4");
		assertEquals("4", memory.getState(0));
		assertEquals("15", memory.getState(1));
		assertEquals("3", memory.getState(2));
	}
}
