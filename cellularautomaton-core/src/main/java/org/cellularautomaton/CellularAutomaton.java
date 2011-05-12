package org.cellularautomaton;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.ICellSpace;

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
	private final ICellSpace<StateType> cellSpace;

	/**
	 * Create an automaton on a specific space of cells.
	 * 
	 * @param cellSpace
	 *            the space of cells to work on
	 */
	public CellularAutomaton(ICellSpace<StateType> cellSpace) {
		this.cellSpace = cellSpace;
	}

	/**
	 * Calculate the next state of each cell. After this step, the logical way
	 * is to apply it with {@link #applyNextStep()}.
	 */
	public void calculateNextStep() {
		for (ICell<StateType> cell : getCellSpace().getAllCells()) {
			cell.calculateNextState();
		}
	}

	/**
	 * Apply the previously calculated states of each cell. If some cells have
	 * not calculated yet, their state does not change.
	 */
	public void applyNextStep() {
		for (ICell<StateType> cell : getCellSpace().getAllCells()) {
			cell.applyNextState();
		}
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
	public ICellSpace<StateType> getCellSpace() {
		return cellSpace;
	}

}
