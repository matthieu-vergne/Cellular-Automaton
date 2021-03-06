package org.cellularautomaton.rule;

import static org.junit.Assert.assertNotNull;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.builder.SpaceBuilder;
import org.cellularautomaton.state.IStateFactory;
import org.junit.Test;

public abstract class IRuleTest<StateType> {

	/**
	 * 
	 * @return a new rule to test
	 */
	public abstract IRule<StateType> createRule();

	/**
	 * 
	 * @return a collection of values to use in tests
	 */
	public abstract IStateFactory<StateType> getStateFactory();

	@Test
	public void testNotNull() {
		IRule<StateType> rule = createRule();
		assertNotNull(rule);

		// generate automaton
		IStateFactory<StateType> stateFactory = getStateFactory();
		assertNotNull(stateFactory);
		
		SpaceBuilder<StateType> builder = new SpaceBuilder<StateType>();
		builder.setStateFactory(stateFactory).setRule(rule);
		builder.createNewSpace().addDimension(5).addDimension(5)
				.addDimension(5);
		CellularAutomaton<StateType> automaton = new CellularAutomaton<StateType>(
				builder.getSpaceOfCell());

		/*
		 * check init of cells, this is just to ensure the benchmark is coherent
		 * to do tests on it.
		 */
		for (ICell<StateType> cell : automaton.getSpace().getAllCells()) {
			assertNotNull(cell.getCurrentState());
		}

		// check rule
		for (ICell<StateType> cell : automaton.getSpace().getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<StateType> cell : automaton.getSpace().getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<StateType> cell : automaton.getSpace().getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
	}
}
