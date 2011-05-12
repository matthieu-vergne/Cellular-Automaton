package org.cellularautomaton.state;



public abstract class AbstractStateFactoryTest<StateType> extends IStateFactoryTest<StateType> {

	public void testCreation() {
		createFactory();
	}
}
