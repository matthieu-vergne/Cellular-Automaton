package org.cellularautomaton.state;

import java.util.ArrayList;
import java.util.List;

/**
 * A dynamic state factory is a state factory allowing to give possible states
 * progressively.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public class DynamicStateFactory<StateType> extends
		AbstractStateFactory<StateType> {

	/**
	 * The possible states.
	 */
	private List<StateType> states = new ArrayList<StateType>();

	/**
	 * Add a possible state to this factory. If the state is already known,
	 * nothing happen.
	 * 
	 * @param state
	 *            the state to add
	 */
	public void addPossibleState(StateType state) {
		if (!states.contains(state)) {
			states.add(state);
		}
	}

	/**
	 * Give the states added to this factory.
	 */
	@Override
	public List<StateType> getPossibleStates() {
		return states;
	}

}
