package org.cellularautomaton.space;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.cellularautomaton.cell.ICell;

/**
 * This generic space of cell offers a simple way to contains and access the
 * cells. Other spaces can be implemented to have specific optimizations.
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
public class GenericSpace<StateType> implements ISpace<StateType> {
	/**
	 * The cell to consider as the start of the space (all the other cells are
	 * accessible from this one).
	 */
	private ICell<StateType> originCell;

	/**
	 * Create a space of cells with the given origin and all the other cells
	 * accessible from this one.
	 * 
	 * @param originCell
	 *            the cell to consider as the origin of the space
	 */
	public GenericSpace() {
		this.originCell = null;
	}

	/**
	 * All the cells accessible from the origin cell are returned (without any
	 * ordering).
	 */
	public Collection<ICell<StateType>> getAllCells() {
		Collection<ICell<StateType>> result = new ArrayList<ICell<StateType>>();
		for (Iterator<ICell<StateType>> iterator = iterator(); iterator
				.hasNext();) {
			ICell<StateType> cell = iterator.next();
			result.add(cell);
		}
		return result;
	}

	/**
	 * Change the origin of the space. <b>BE CAREFUL :</b> the complete space
	 * can be changed with this way, especially if you give a cell which is not
	 * accessible in the current space. All the space works on this origin.
	 */
	public void setOrigin(ICell<StateType> origin) {
		originCell = origin;
	}

	/**
	 * Give the origin of the space of cells.
	 */
	public ICell<StateType> getOrigin() {
		return originCell;
	}

	/**
	 * Give an iterator over the cells. It is strongly recommended to <b>not
	 * modify the space of cells during iteration</b>, as the modifications can
	 * generate unexpected behaviors.
	 * 
	 * @return an iterator over the cells
	 */
	public Iterator<ICell<StateType>> iterator() {
		return new CellIterator();
	}

	/**
	 * An empty space is a space with no origin cell.
	 * @see #setOrigin(ICell)
	 */
	public boolean isEmpty() {
		return originCell == null;
	}
	
	/**
	 * Specific iterator for the space of cells. If the space is modified during
	 * the iteration, it is possible that the modifications are not considered :<br/>
	 * <ul>
	 * <li>if a modification is done "far" (not in the cells around) of the
	 * cells already iterated, it should be considered,</li>
	 * <li>if a modification is done between cells already iterated, it should
	 * not be considered</li>
	 * <li>if a modification is done at the limit between iterated and not
	 * iterated cells, all is possible.</li>
	 * </ul>
	 * 
	 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
	 * 
	 */
	private class CellIterator implements Iterator<ICell<StateType>> {

		/**
		 * The cells already returned.
		 */
		Collection<ICell<StateType>> cellsUsed = new HashSet<ICell<StateType>>();
		/**
		 * The cells to look for next iterations.
		 */
		Collection<ICell<StateType>> cellsToCheck = new HashSet<ICell<StateType>>();

		/**
		 * Create an iterator over the current space of cells.
		 */
		public CellIterator() {
			if (getOrigin() != null) {
				cellsToCheck.add(getOrigin());
			}
		}

		public boolean hasNext() {
			return !cellsToCheck.isEmpty();
		}

		public ICell<StateType> next() {

			if (hasNext()) {
				ICell<StateType> cell = cellsToCheck.iterator().next();
				cellsToCheck.remove(cell);
				cellsUsed.add(cell);
				for (ICell<StateType> cellAround : cell.getAllCellsAround()) {
					if (!cellsUsed.contains(cellAround)) {
						cellsToCheck.add(cellAround);
					}
				}
				return cell;
			} else {
				throw new NoSuchElementException();
			}
		}

		/**
		 * This method do nothing.
		 */
		public void remove() {
			// do nothing
		}

	}
}
