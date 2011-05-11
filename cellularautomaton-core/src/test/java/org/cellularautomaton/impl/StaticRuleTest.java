package org.cellularautomaton.impl;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.builder.CellSpaceBuilder;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.definition.IRuleTest;
import org.junit.Test;

/**
 * This test case uses the generic test defined for the {@link IRule} interface.
 * It can be expanded with more tests but the basic tests must be checked too,
 * that is why it extends the abstract test case {@link IRuleTest}.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
public class StaticRuleTest extends IRuleTest {

	@Override
	public <StateType> IRule<StateType> createRule() {
		return new StaticRule<StateType>();
	}

	@Test
	public void testStaticValue() {
		IRule<Integer> rule = createRule();

		// TODO replace fix value by not homogeneous values
		CellSpaceBuilder<Integer> builder = new CellSpaceBuilder<Integer>();
		builder.setInitialState(0).setRule(rule);
		builder.createNewSpace(3).addDimension(5).addDimension(5)
				.addDimension(5);
		CellularAutomaton<Integer> automaton = new CellularAutomaton<Integer>(
				builder.getSpaceOfCellOrigin());

		for (ICell<Integer> cell : automaton.getAllCells()) {
			assertEquals(cell.getCurrentState(), rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<Integer> cell : automaton.getAllCells()) {
			assertEquals(cell.getCurrentState(), rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<Integer> cell : automaton.getAllCells()) {
			assertEquals(cell.getCurrentState(), rule.calculateNextStateOf(cell));
		}
	}
}
