package org.cellularautomaton;

import java.util.ArrayList;

/**
 * A state memory is the memory of a cell. It keeps the previous states of the
 * cell.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 */
public class StateMemory<StateType> {
	/**
	 * The states memorized.
	 */
	private final ArrayList<StateType> states = new ArrayList<StateType>();

	/**
	 * 
	 * @param size
	 *            the size of the memory
	 * @param initialState
	 *            the state to fill the memory with
	 */
	public StateMemory(int size, StateType initialState) {
		assert initialState != null;

		for (int i = 0; i < size; i++) {
			states.add(initialState);
		}
	}

	/**
	 * @param state
	 *            the new state to push in the memory, which forgets the oldest
	 *            state
	 */
	public void pushNewState(StateType state) {
		assert state != null;

		states.remove(0);
		states.add(state);
	}

	/**
	 * @param the
	 *            age of the state asked, 0 is the most recent
	 * @return the asked memorized state
	 */
	public StateType getState(int age) {
		int size = getMemorySize();
		assert age >= 0 && age < size : "the age must be between 0 and " + size;

		return states.get(size - age - 1);
	}

	/**
	 * 
	 * @return the size of the memory
	 */
	public int getMemorySize() {
		return states.size();
	}

	/**
	 * 
	 * @param state
	 *            the state to apply instead of the current state, the state is
	 *            not pushed so the previous state is not kept in memory
	 */
	public void forceCurrentState(StateType state) {
		states.set(states.size() - 1, state);
	}
}
