package org.cellularautomaton.impl;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.definition.IRuleTest;
import org.cellularautomaton.factory.CellFactory;
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
		/*
		 * this test is not exhaustive, there is just some evident cases but it
		 * must be extended when some cells are found to give not static values.
		 */
		IRule<String> rule = createRule();

		CellFactory<String> factory = new CellFactory<String>();
		factory.setInitialState("0");
		ICell<String> cell = factory.createCyclicCell();
		assertEquals("0", rule.calculateNextStateOf(cell));
		cell = factory.createIsolatedCell();
		assertEquals("0", rule.calculateNextStateOf(cell));

		factory.setInitialState("aze");
		cell = factory.createCyclicCell();
		assertEquals("aze", rule.calculateNextStateOf(cell));
		cell = factory.createIsolatedCell();
		assertEquals("aze", rule.calculateNextStateOf(cell));
	}
}
