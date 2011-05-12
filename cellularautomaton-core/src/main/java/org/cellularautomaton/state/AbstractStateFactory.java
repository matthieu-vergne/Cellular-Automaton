package org.cellularautomaton.state;

import java.util.List;

import org.cellularautomaton.cell.ICell;

public abstract class AbstractStateFactory<StateType> implements
		IStateFactory<StateType> {
	/**
	 * The index of the last state used in the {@link #getRandomState()} method.
	 */
	private int lastStateIndex = 0;
	private int lastStateDelta = 1;

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
		lastStateIndex = (lastStateIndex + lastStateDelta) % size;
		lastStateDelta = (lastStateDelta + 1) % (13 * size / 17);
		return states.get(lastStateIndex);
	}
}
