package org.cellularautomaton.rule;

import junit.framework.TestCase;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.space.SpaceBuilder;
import org.cellularautomaton.state.IStateFactory;
import org.junit.Test;

/**
 * This test case is a model for all the {@link IRule} implementations. All the
 * implementations of the {@link IRule} interface must have a test class
 * extending this test case.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
public abstract class IRuleTest<StateType> extends TestCase {

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

		// generate automaton
		// TODO replace fix value by not homogeneous values
		IStateFactory<StateType> stateFactory = getStateFactory();
		
		SpaceBuilder<StateType> builder = new SpaceBuilder<StateType>();
		builder.setStateFactory(stateFactory).setRule(rule);
		builder.createNewSpace(3).addDimension(5).addDimension(5)
				.addDimension(5);
		CellularAutomaton<StateType> automaton = new CellularAutomaton<StateType>(
				builder.getSpaceOfCell());

		/*
		 * check init of cells, this is just to ensure the benchmark is coherent
		 * to do tests on it.
		 */
		for (ICell<StateType> cell : automaton.getCellSpace().getAllCells()) {
			assertNotNull(cell.getCurrentState());
		}

		// check rule
		for (ICell<StateType> cell : automaton.getCellSpace().getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<StateType> cell : automaton.getCellSpace().getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<StateType> cell : automaton.getCellSpace().getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
	}
}
