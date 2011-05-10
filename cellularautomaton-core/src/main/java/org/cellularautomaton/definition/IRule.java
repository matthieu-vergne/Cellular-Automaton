package org.cellularautomaton.definition;

/**
 * A rule describe how to calculate a state for a specific cell. This state is
 * calculated depending on this cell (and possibly others through the link of
 * the cells).
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public interface IRule<StateType> {
	/**
	 * 
	 * @param cell the cell to calculate the state for
	 * @return the next state to apply to this cell
	 */
	public StateType calculateNextStateOf(ICell<StateType> cell);
}
