package org.cellularautomaton.space;

import java.util.Collection;

import org.cellularautomaton.cell.ICell;

/**
 * A space of cell is a simple container of cells, with some useful methods.
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
public interface ISpace<StateType> extends Iterable<ICell<StateType>> {
	/**
	 * 
	 * @param origin
	 *            the cell this space work from, all the others cells the space
	 *            consider are available from this one (with relative
	 *            coordinates).
	 */
	public void setOrigin(ICell<StateType> origin);

	/**
	 * 
	 * @return the cell this space work from, all the others cells the space
	 *         consider are available from this one (with relative coordinates).
	 */
	public ICell<StateType> getOrigin();

	/**
	 * 
	 * @return true if there is no cell in the space
	 */
	public boolean isEmpty();

	/**
	 * 
	 * @return all the cells of the space (without specific ordering)
	 */
	public Collection<ICell<StateType>> getAllCells();
}
