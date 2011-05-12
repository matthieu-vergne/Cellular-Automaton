package org.cellularautomaton.impl;

import org.cellularautomaton.definition.IStateFactoryTest;


public abstract class AbstractStateFactoryTest<StateType> extends IStateFactoryTest<StateType> {

	public void testCreation() {
		createFactory();
	}
}
