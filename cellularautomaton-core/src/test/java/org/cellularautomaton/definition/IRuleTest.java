package org.cellularautomaton.definition;

import junit.framework.TestCase;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.builder.CellSpaceBuilder;
import org.junit.Test;

/**
 * This test case is a model for all the {@link IRule} implementations. All the
 * implementations of the {@link IRule} interface must have a test class
 * extending this test case.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
public abstract class IRuleTest extends TestCase {

	public abstract <StateType> IRule<StateType> createRule();

	@Test
	public void testNotNull() {
		IRule<Integer> rule = createRule();

		// generate automaton
		// TODO replace fix value by not homogeneous values
		CellSpaceBuilder<Integer> builder = new CellSpaceBuilder<Integer>();
		builder.setInitialState(0).setRule(rule);
		builder.createNewSpace(3).addDimension(5).addDimension(5)
				.addDimension(5);
		CellularAutomaton<Integer> automaton = new CellularAutomaton<Integer>(
				builder.getSpaceOfCellOrigin());

		/*
		 * check init of cells, this is just to ensure the benchmark is coherent
		 * to do tests on it.
		 */
		for (ICell<Integer> cell : automaton.getAllCells()) {
			assertNotNull(cell.getCurrentState());
		}

		// check rule
		for (ICell<Integer> cell : automaton.getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<Integer> cell : automaton.getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<Integer> cell : automaton.getAllCells()) {
			assertNotNull(rule.calculateNextStateOf(cell));
		}
	}
}
