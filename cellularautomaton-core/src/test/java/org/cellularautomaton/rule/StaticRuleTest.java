package org.cellularautomaton.rule;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.builder.SpaceBuilder;
import org.cellularautomaton.state.AbstractStateFactory;
import org.cellularautomaton.state.AbstractStateFactoryTest;
import org.cellularautomaton.state.IStateFactory;
import org.junit.Test;

public class StaticRuleTest extends IRuleTest<Integer> {

	@Override
	public IRule<Integer> createRule() {
		return new StaticRule<Integer>();
	}

	@Override
	public IStateFactory<Integer> getStateFactory() {
		return new IntegerStateFactory();
	}

	public static class IntegerStateFactory extends
			AbstractStateFactory<Integer> {
		public List<Integer> getPossibleStates() {
			return Arrays.asList(new Integer[] { -3, -1, 0, 1, 2, 3, 5 });
		}

		@Override
		public void customize(ICell<Integer> cell) {
			cell.setCurrentState(getRandomState());
		}
	}

	public static class IntegerStateFactoryTest extends
			AbstractStateFactoryTest<Integer> {
		@Override
		public IStateFactory<Integer> createFactory() {
			return new IntegerStateFactory();
		}
	}

	@Test
	public void testStaticValue() {
		IRule<Integer> rule = createRule();
		assertNotNull(rule);

		IStateFactory<Integer> stateFactory = new IntegerStateFactory();

		SpaceBuilder<Integer> builder = new SpaceBuilder<Integer>();
		builder.setStateFactory(stateFactory).setRule(rule);
		builder.createNewSpace().addDimension(5).addDimension(5)
				.addDimension(5);
		CellularAutomaton<Integer> automaton = new CellularAutomaton<Integer>(
				builder.getSpaceOfCell());

		for (ICell<Integer> cell : automaton.getSpace().getAllCells()) {
			assertEquals(cell.getCurrentState(),
					rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<Integer> cell : automaton.getSpace().getAllCells()) {
			assertEquals(cell.getCurrentState(),
					rule.calculateNextStateOf(cell));
		}
		automaton.doStep();
		for (ICell<Integer> cell : automaton.getSpace().getAllCells()) {
			assertEquals(cell.getCurrentState(),
					rule.calculateNextStateOf(cell));
		}
	}
}
