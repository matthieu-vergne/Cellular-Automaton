package org.cellularautomaton.rule;

import java.util.Arrays;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.rule.StaticRule;
import org.cellularautomaton.space.SpaceBuilder;
import org.cellularautomaton.state.AbstractStateFactory;
import org.cellularautomaton.state.AbstractStateFactoryTest;
import org.cellularautomaton.state.IStateFactory;
import org.junit.Test;

/**
 * This test case uses the generic test defined for the {@link IRule} interface.
 * It can be expanded with more tests but the basic tests must be checked too,
 * that is why it extends the abstract test case {@link IRuleTest}.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
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
		public Integer getStateFor(ICell<Integer> cell) {
			return getRandomState();
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

		IStateFactory<Integer> stateFactory = new IntegerStateFactory();

		SpaceBuilder<Integer> builder = new SpaceBuilder<Integer>();
		builder.setStateFactory(stateFactory).setRule(rule);
		builder.createNewSpace(3).addDimension(5).addDimension(5)
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
