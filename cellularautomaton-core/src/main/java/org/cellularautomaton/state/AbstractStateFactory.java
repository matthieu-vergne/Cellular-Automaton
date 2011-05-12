package org.cellularautomaton.state;

import java.util.List;

import org.cellularautomaton.cell.ICell;

public abstract class AbstractStateFactory<StateType> implements
		IStateFactory<StateType> {
	/**
	 * The index of the last state used in the {@link #getRandomState()} method.
	 */
	private int lastStateIndex = 0;
	
	/**
	 * This implementation gives the first possible value;
	 */
	public StateType getDefaultState() {
		return getPossibleStates().get(0);
	}

	/**
	 * This implementation gives the default state.
	 */
	public StateType getStateFor(ICell<StateType> cell) {
		return getDefaultState();
	}

	/**
	 * This method implements a simple random generator.
	 */
	public StateType getRandomState() {
		List<StateType> states = getPossibleStates();
		int size = states.size();
		lastStateIndex = (size * lastStateIndex + (lastStateIndex + 1)) % size;
		return states.get(lastStateIndex);
	}
}
