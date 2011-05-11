package org.cellularautomaton.definition;

import junit.framework.TestCase;

import org.cellularautomaton.factory.CellFactory;
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
		/*
		 * this test is not exhaustive, there is just some evident cases but it
		 * must be extended when some rules are found to give null values. Be
		 * careful to check coherent ways : test the StaticRule on a cell having
		 * a null state give a null state as a result of the calculation, but it
		 * is not a coherent test as the problem here is not the result of the
		 * rule but the actual null state of the cell. This kind of test must be
		 * avoided.
		 */
		IRule<Integer> rule = createRule();

		// TODO replace cell factory by space builder
		CellFactory<Integer> factory = new CellFactory<Integer>();
		factory.setInitialState(0);
		assertNotNull(rule.calculateNextStateOf(factory.createCyclicCell()));
		assertNotNull(rule.calculateNextStateOf(factory.createIsolatedCell()));

		factory.setInitialState(42);
		assertNotNull(rule.calculateNextStateOf(factory.createCyclicCell()));
		assertNotNull(rule.calculateNextStateOf(factory.createIsolatedCell()));
	}
}
