package org.cellularautomaton.state;

import java.util.List;

import org.cellularautomaton.cell.ICell;

/**
 * This class implements the bases of the {@link IStateFactory} interface.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public abstract class AbstractStateFactory<StateType> implements
		IStateFactory<StateType> {
	/**
	 * The index of the last state used in the {@link #getRandomState()} method.
	 */
	private int lastStateIndex = 0;
	private int lastStateDelta = 1;

	/**
	 * This implementation gives the first possible state.
	 * 
	 * @see #getPossibleStates()
	 */
	public StateType getDefaultState() {
		return getPossibleStates().get(0);
	}

	/**
	 * This implementation gives the default state.
	 * 
	 * @see #getDefaultState()
	 */
	public StateType getStateFor(ICell<StateType> cell) {
		return getDefaultState();
	}

	/**
	 * This method implements a simple random generator (which basically passes
	 * the tests). It must be modified or overridden if the tests fails for some
	 * test cases or if it does not give good results for particular cases.
	 */
	public StateType getRandomState() {
		List<StateType> states = getPossibleStates();
		int size = states.size();
		lastStateIndex = (lastStateIndex + lastStateDelta) % size;
		lastStateDelta = (lastStateDelta + 1) % (13 * size / 17);
		return states.get(lastStateIndex);
	}
}
