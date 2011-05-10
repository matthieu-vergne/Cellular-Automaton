package org.cellularautomaton.impl;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;

/**
 * A static rule allows to keep the state unchanged. The calculation give the
 * same state than the current state of the cell.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by each cell, it can be {@link Boolean} for
 *            a simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses (just consider all the cells use the same
 *            type).
 */
public class StaticRule<StateType> implements IRule<StateType> {

	/**
	 * This method is designed to return the current state of the cell, so it
	 * will not change.
	 */
	public StateType calculateNextStateOf(ICell<StateType> cell) {
		return cell.getCurrentState();
	}
}
