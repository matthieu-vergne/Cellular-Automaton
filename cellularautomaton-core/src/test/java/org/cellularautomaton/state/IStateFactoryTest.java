package org.cellularautomaton.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.cellularautomaton.state.IStateFactory;

import junit.framework.TestCase;

public abstract class IStateFactoryTest<StateType> extends TestCase {
	/**
	 * 
	 * @return a new rule to test
	 */
	public abstract IStateFactory<StateType> createFactory();

	public void testDefaultIsInPossibleStates() {
		IStateFactory<StateType> factory = createFactory();
		assertTrue(factory.getPossibleStates().contains(
				factory.getDefaultState()));
	}

	public void testAtLeastTwoPossibleStates() {
		IStateFactory<StateType> factory = createFactory();
		HashSet<StateType> states = new HashSet<StateType>(
				factory.getPossibleStates());
		assertTrue(states.size() >= 2);
	}

	public void testGoodRandomGenerator() {
		IStateFactory<StateType> factory = createFactory();

		Map<StateType, Integer> counters = new HashMap<StateType, Integer>();
		List<StateType> states = factory.getPossibleStates();
		for (StateType state : states) {
			counters.put(state, 0);
		}

		// check after a reduced number of iterations, all the values are used
		int minOccurences = 5;
		for (int i = 0; i < minOccurences * states.size(); i++) {
			StateType state = factory.getRandomState();
			counters.put(state, counters.get(state) + 1);
		}
		assertFalse("some values not used", counters.values().contains(0));

		// check all the values are enough used
		int averageOccurences = 100;
		assertTrue(averageOccurences >= minOccurences);
		for (int i = minOccurences * states.size(); i < averageOccurences
				* states.size(); i++) {
			StateType state = factory.getRandomState();
			counters.put(state, counters.get(state) + 1);
		}
		for (StateType state : states) {
			assertTrue(state + " state not enough used",
					counters.get(state) > averageOccurences / 2);
		}
	}

}
