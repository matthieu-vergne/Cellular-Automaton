package org.cellularautomaton.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.cellularautomaton.StateMemory;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;

/**
 * A generic cell implements all the interface {@link ICell} in a generic way.
 * It implies that all can be done to this kind of cell, but no optimization is
 * done.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public class GenericCell<StateType> implements ICell<StateType> {

	/**
	 * The previous states of the cell. This memory is used in the case several
	 * generations are needed to calculate the new states.
	 */
	private final StateMemory<StateType> previousStates;
	/**
	 * The next state to apply to this cell.
	 * 
	 * @see #calculateNextState()
	 */
	private StateType nextState;
	/**
	 * The cells around this one. <b>They are not the (logical) neighbors
	 * considered by the rule</b> which change the state of the cell, they are
	 * the (physical) neighbors in the space of cells.
	 */
	private final ICell<StateType>[][] surroundings;
	/**
	 * The coordinates of the cell in the space of cells.
	 */
	private final int[] coords;
	private IRule<StateType> rule;

	/**
	 * 
	 * @return the dimensions this cell work on
	 */
	public int getDimensions() {
		return surroundings.length;
	}

	/**
	 * Create a new cell.
	 * 
	 * @param initialState
	 *            the initial state of the cell
	 * @param dimensions
	 *            the number of dimensions to consider (at least 1)
	 * @param memorySize
	 *            the number of states the cell can remember (at least 0)
	 */
	@SuppressWarnings("unchecked")
	public GenericCell(StateType initialState, int dimensions, int memorySize) {
		assert initialState != null;
		assert dimensions > 0;
		assert memorySize >= 0;

		previousStates = new StateMemory<StateType>(memorySize, initialState);
		this.surroundings = new ICell[dimensions][2];
		for (int dimension = 0; dimension < dimensions; dimension++) {
			surroundings[dimension] = new ICell[] { this, this };
		}
		coords = new int[dimensions];
		Arrays.fill(getCoords(), 0);
	}

	public StateType getCurrentState() {
		return getState(0);
	}

	/**
	 * This method allows to get the memorized states of the cell. The number of
	 * states memorized (including current state) corresponds to the memory size
	 * given to the cell.
	 */
	public StateType getState(int age) {
		return previousStates.getState(age);
	}

	/**
	 * This method calculate the next state with the rule assigned to the cell.
	 * It does not change the current state of the cell.
	 */
	public void calculateNextState() {
		nextState = rule.calculateNextStateOf(this);
		if (nextState == null) {
			throw new NullPointerException(
					"the calculation has returned a null value");
		}
	}

	/**
	 * The calculated next state is applied to the cell with this method.
	 */
	public void applyNextState() {
		if (!isNextStateCalculated()) {
			throw new IllegalStateException(
					"the next state is not calculated yet");
		}

		previousStates.pushNewState(nextState);
		nextState = null;
	}

	public ICell<StateType> getNextCellOnDimension(int dimension) {
		return surroundings[dimension][1];
	}

	public void setNextCellOnDimension(int dimension, ICell<StateType> cell) {
		surroundings[dimension][1] = cell;
	}

	public void setPreviousCellOnDimension(int dimension, ICell<StateType> cell) {
		surroundings[dimension][0] = cell;
	}

	public ICell<StateType> getPreviousCellOnDimension(int dimension) {
		return surroundings[dimension][0];
	}

	public boolean isNextStateCalculated() {
		return nextState != null;
	}

	/**
	 * This method allows to get all the cells linked to this one : the cells
	 * before and after this one on each dimension.
	 */
	public Set<ICell<StateType>> getAllCellsAround() {
		Set<ICell<StateType>> neighbors = new HashSet<ICell<StateType>>();
		for (int dimension = 0; dimension < getDimensions(); dimension++) {
			neighbors.add(getPreviousCellOnDimension(dimension));
			neighbors.add(getNextCellOnDimension(dimension));
		}
		return neighbors;
	}

	/**
	 * <b>Be careful :</b> this method browse the space with the links between
	 * each cell. It means that if you are not sure of the values given by
	 * {@link #getPreviousCellOnDimension(int)} and
	 * {@link #getNextCellOnDimension(int)}, it is strongly not recommended to
	 * use this one.
	 */
	public ICell<StateType> getRelativeCell(int... coords) {
		ICell<StateType> cell = this;
		for (int dimension = 0; dimension < coords.length; dimension++) {
			boolean goNext = true;
			int coord = coords[dimension];
			if (coord < 0) {
				goNext = false;
				coord = -coord;
			}

			while (coord > 0) {
				cell = goNext ? cell.getNextCellOnDimension(dimension) : cell
						.getPreviousCellOnDimension(dimension);
				coord--;
			}
		}
		return cell;
	}

	@Override
	public String toString() {
		return "cell(" + getCurrentState().toString() + ") "
				+ Arrays.toString(coords);
	}

	/**
	 * Gives the coordinates of the current cell. These coordinates are given
	 * manually via the {@link #setCoords(int...)}. These coordinates are usable
	 * only if you are sure they corresponds to the reality. Otherwise using
	 * them is not recommended.
	 */
	public int[] getCoords() {
		return coords;
	}

	/**
	 * This method allows to give specific coordinates to the current cell. This
	 * is only a parameter of the cell, it does not "move" the cell in the space
	 * in order to place it at the given coordinates. Moreover, it does not
	 * update automatically if the cell is moved. No check is done on the
	 * values, so you must be sure of what you give if you want to use these
	 * coordinates later correctly.
	 */
	public void setCoords(int... coords) {
		assert coords != null;
		assert coords.length == this.coords.length;

		for (int i = 0; i < coords.length; i++) {
			this.coords[i] = coords[i];
		}
	}

	public void setCurrentState(StateType state) {
		previousStates.forceCurrentState(state);
	}

	public void setRule(IRule<StateType> rule) {
		this.rule = rule;
	}
}
