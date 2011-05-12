package org.cellularautomaton.definition;

import java.util.List;

/**
 * A state factory is a simple way to give a state in a specific context.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public interface IStateFactory<StateType> {

	/**
	 * @return all or a list of possible values to use
	 */
	public List<StateType> getPossibleStates();

	/**
	 * @return a default state to consider
	 */
	public StateType getDefaultState();

	/**
	 * This method must give a state for a specific cell.
	 * 
	 * @param cell
	 *            the cell to consider
	 * @return the state to give to the cell
	 */
	public StateType getStateFor(ICell<StateType> cell);

	/**
	 * This method must give a randomized state.
	 * 
	 * @return a randomized state
	 */
	public StateType getRandomState();
}
