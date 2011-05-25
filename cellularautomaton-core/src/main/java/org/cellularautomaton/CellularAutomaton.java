package org.cellularautomaton;

import java.util.Collection;
import java.util.HashSet;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.ISpace;

/**
 * A cellular automaton is a space of evolving cells. Each cell evolves
 * considering its actual state and the states of other cells.
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
public class CellularAutomaton<StateType> {

	/**
	 * The space of cells this automaton work on.
	 */
	private final ISpace<StateType> cellSpace;

	/**
	 * The cells which need to see their new state calculated during the next
	 * step.
	 */
	private final Collection<ICell<StateType>> cellsToCalculate = new HashSet<ICell<StateType>>();

	/**
	 * Create an automaton on a specific space of cells.
	 * 
	 * @param cellSpace
	 *            the space of cells to work on
	 */
	public CellularAutomaton(ISpace<StateType> cellSpace) {
		this.cellSpace = cellSpace;
		cellsToCalculate.addAll(cellSpace.getAllCells());
	}

	/**
	 * Calculate the next state of each cell. After this step, the logical way
	 * is to apply it with {@link #applyNextStep()}.
	 */
	public void calculateNextStep() {
		for (ICell<StateType> cell : cellsToCalculate) {
			cell.calculateNextState();
		}
	}

	/**
	 * Apply the previously calculated states of each cell. If some cells have
	 * not calculated yet, their state does not change.
	 */
	public void applyNextStep() {
		Collection<ICell<StateType>> modifiedCells = new HashSet<ICell<StateType>>();
		for (ICell<StateType> cell : cellsToCalculate) {
			if (cell.isNextStateDifferent()) {
				cell.applyNextState();
				modifiedCells.add(cell);
			}
		}

		// check the next cells to calculate
		cellsToCalculate.clear();
		for (ICell<StateType> cell : modifiedCells) {
			cellsToCalculate.addAll(getCellsDependingTo(cell));
		}
	}

	/**
	 * This method allows to know the cells which need to be calculated at the
	 * next step. If a cell see its state modified during the current step, this
	 * method is called to know all the cells which depend on it. When this
	 * method is called, <b>all the cells</b> have their new state (the step is
	 * finished).<br/>
	 * <br/>
	 * The default implementation return all the cells of the space. This method
	 * may be overridden to optimize the process.
	 * 
	 * @param cell
	 *            the cell to consider the dependencies with
	 * @return the cells which depends of the cell given in argument
	 */
	protected Collection<ICell<StateType>> getCellsDependingTo(
			ICell<StateType> cell) {
		return getSpace().getAllCells();
	}

	/**
	 * Execute a complete step of the automaton. After that, each cell has its
	 * next state.
	 */
	public void doStep() {
		calculateNextStep();
		applyNextStep();
	}

	/**
	 * 
	 * @return the space of cells this automaton work on
	 */
	public ISpace<StateType> getSpace() {
		return cellSpace;
	}

	/**
	 * 
	 * @return the cells which need to see their new state calculated during the
	 *         next step
	 */
	public Collection<ICell<StateType>> getCellsToCalculate() {
		return cellsToCalculate;
	}

}
