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
	private final ICell<StateType>[][] neighbors;
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
		return neighbors.length;
	}

	/**
	 * Create a new cell. The cell is already valid, especially it its neighbors
	 * are defined as the cell itself (like a cyclic space of cells with only
	 * one cell). This way, the cell can be used immediately in a cellular
	 * automaton.
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
		this.neighbors = new ICell[dimensions][2];
		for (int dimension = 0; dimension < dimensions; dimension++) {
			neighbors[dimension] = new ICell[] { this, this };
		}
		coords = new int[dimensions];
		Arrays.fill(getCoords(), 0);
	}

	public StateType getCurrentState() {
		return getState(0);
	}

	public StateType getState(int age) {
		return previousStates.getState(age);
	}

	public void calculateNextState() {
		nextState = rule.calculateNextStateOf(this);
		if (nextState == null) {
			throw new NullPointerException(
					"the calculation has returned a null value");
		}
	}

	public void applyNextState() {
		if (!isNextStateCalculated()) {
			throw new IllegalStateException(
					"the next state is not calculated yet");
		}

		previousStates.pushNewState(nextState);
		nextState = null;
	}

	public ICell<StateType> getNextCellOnDimension(int dimension) {
		return neighbors[dimension][1];
	}

	public void setNextCellOnDimension(int dimension, ICell<StateType> neighbor) {
		neighbors[dimension][1] = neighbor;
	}

	public void setPreviousCellOnDimension(int dimension,
			ICell<StateType> neighbor) {
		neighbors[dimension][0] = neighbor;
	}

	public ICell<StateType> getPreviousCellOnDimension(int dimension) {
		return neighbors[dimension][0];
	}

	public boolean isNextStateCalculated() {
		return nextState != null;
	}

	public Set<ICell<StateType>> getAllCellsAround() {
		Set<ICell<StateType>> neighbors = new HashSet<ICell<StateType>>();
		for (int dimension = 0; dimension < getDimensions(); dimension++) {
			neighbors.add(getPreviousCellOnDimension(dimension));
			neighbors.add(getNextCellOnDimension(dimension));
		}
		return neighbors;
	}

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

	public int[] getCoords() {
		return coords;
	}

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
