package org.cellularautomaton.definition;

/**
 * A rule describe how to calculate a state for a specific cell. This state is
 * calculated depending on this cell (and possibly others through the link of
 * the cells).
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 */
public interface IRule<StateType> {
	public StateType calculateNextStateOf(ICell<StateType> cell);
}
