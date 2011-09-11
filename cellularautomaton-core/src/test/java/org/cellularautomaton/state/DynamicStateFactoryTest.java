package org.cellularautomaton.state;

import org.junit.Test;
import static org.junit.Assert.*;


public class DynamicStateFactoryTest extends AbstractStateFactoryTest<String> {

	@Override
	public IStateFactory<String> createFactory() {
		DynamicStateFactory<String> factory = new DynamicStateFactory<String>();
		factory.addPossibleState("a");
		factory.addPossibleState("b");
		factory.addPossibleState("c");
		return factory;
	}
	
	@Test
	public void testAdding() {
		DynamicStateFactory<String> factory = new DynamicStateFactory<String>();
		assertEquals(0, factory.getPossibleStates().size());
		
		String state1 = "a";
		factory.addPossibleState(state1);
		assertEquals(1, factory.getPossibleStates().size());
		assertTrue(factory.getPossibleStates().contains(state1));
		
		String state2 = "b";
		factory.addPossibleState(state2);
		assertEquals(2, factory.getPossibleStates().size());
		assertTrue(factory.getPossibleStates().contains(state1));
		assertTrue(factory.getPossibleStates().contains(state2));
		
		factory.addPossibleState(state2);
		assertEquals(2, factory.getPossibleStates().size());
		assertTrue(factory.getPossibleStates().contains(state1));
		assertTrue(factory.getPossibleStates().contains(state2));
		
		String state3 = "c";
		factory.addPossibleState(state3);
		assertEquals(3, factory.getPossibleStates().size());
		assertTrue(factory.getPossibleStates().contains(state1));
		assertTrue(factory.getPossibleStates().contains(state2));
		assertTrue(factory.getPossibleStates().contains(state3));
	}
}
