package org.cellularautomaton.factory;

import junit.framework.TestCase;

import org.cellularautomaton.builder.CellSpaceBuilder;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.junit.Test;

public class RuleFactoryTest extends TestCase {

	@Test
	public void testStaticRuleInstance() {
		/* create 1D space */
		CellSpaceBuilder<String> builder = new CellSpaceBuilder<String>();
		builder.setInitialState("0").createNewSpace(1).addDimension(4);
		ICell<String> cell0 = builder.getSpaceOfCellOrigin();
		ICell<String> cell1 = cell0.getNextCellOnDimension(0);
		ICell<String> cell2 = cell1.getNextCellOnDimension(0);
		ICell<String> cell3 = cell2.getNextCellOnDimension(0);

		/* get rule */
		RuleFactory<String> factory = new RuleFactory<String>();
		IRule<String> rule = factory.getStaticRuleInstance();

		/* test rule */
		assertEquals(rule.calculateNextStateOf(cell0), cell0.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell1), cell1.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell2), cell2.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell3), cell3.getCurrentState());

		cell0.setCurrentState("0");
		cell1.setCurrentState("1");
		cell2.setCurrentState("2");
		cell3.setCurrentState("3");

		assertEquals(rule.calculateNextStateOf(cell0), cell0.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell1), cell1.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell2), cell2.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell3), cell3.getCurrentState());

		cell0.setCurrentState("aze");
		cell1.setCurrentState("sdf");
		cell2.setCurrentState("654");
		cell3.setCurrentState("321");

		assertEquals(rule.calculateNextStateOf(cell0), cell0.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell1), cell1.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell2), cell2.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell3), cell3.getCurrentState());

		cell0.setCurrentState("");
		cell1.setCurrentState("sdfvjhgsdcv");
		cell2.setCurrentState("65435");
		cell3.setCurrentState("xs85é6#7se54s@c");

		assertEquals(rule.calculateNextStateOf(cell0), cell0.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell1), cell1.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell2), cell2.getCurrentState());
		assertEquals(rule.calculateNextStateOf(cell3), cell3.getCurrentState());

		/* test invariability (one instance) */
		IRule<String> rule2 = factory.getStaticRuleInstance();
		assertEquals(rule, rule2);
	}

	public static class StaticRuleTest extends org.cellularautomaton.impl.StaticRuleTest {
		@Override
		public <StateType> IRule<StateType> createRule() {
			return new RuleFactory<StateType>().getStaticRuleInstance();
		}
	}
}
