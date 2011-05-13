package org.cellularautomaton.cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.state.StateMemory;
import org.cellularautomaton.util.Coords;

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
	private StateMemory<StateType> previousStates;
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
	private final List<LinkedCellsCouple> surroundings;
	/**
	 * The coordinates of the cell in the space of cells.
	 */
	private Coords coords;
	/**
	 * The rule to use in order to calculate the newt state of the cell.
	 */
	private IRule<StateType> rule;

	/**
	 * 
	 * @return the dimensions this cell work on
	 */
	public int getDimensions() {
		return surroundings.size();
	}

	/**
	 * If the dimension is decreased, the cells around for the kept dimensions
	 * are preserved. If the dimensions are increased, no cells are linked for
	 * the new dimensions, you need to give it manually.
	 * 
	 * @see #setPreviousCellOnDimension(int, ICell)
	 * @see #setNextCellOnDimension(int, ICell)
	 */
	public void setDimensions(int dimensions) {
		assert dimensions > 0;

		if (dimensions == getDimensions()) {
			// nothing to do
		} else {
			if (dimensions > getDimensions()) {
				for (int dimension = getDimensions(); dimension < dimensions; dimension++) {
					LinkedCellsCouple couple = new LinkedCellsCouple();
					surroundings.add(couple);
				}
			} else {
				for (int dimension = getDimensions() - 1; dimension >= dimensions; dimension--) {
					surroundings.remove(dimension);
				}
			}
			coords.setDimensions(dimensions);
		}
	}

	/**
	 * Create a new cell. You must set its memory and its rule before to use it.
	 * 
	 * @see #setMemory(int, Object)
	 * @see #setRule(IRule)
	 */
	public GenericCell() {
		this.surroundings = new ArrayList<LinkedCellsCouple>();
		coords = new Coords();
	}

	/**
	 * Give a new memory to the cell. Basically, this method is used just after
	 * the instantiation of the cell.
	 */
	public void setMemory(int memorySize, StateType initialState) {
		previousStates = new StateMemory<StateType>(memorySize, initialState);
	}

	/**
	 * This method is similar to {@link #getState(int)} with the argument 0.
	 */
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

	/**
	 * Change the link between this cell and the given one as : current -> next.
	 * <b>It does not make the reverse link.</b>
	 */
	public void setNextCellOnDimension(int dimension, ICell<StateType> next) {
		surroundings.get(dimension).next = next;
	}

	/**
	 * Gives the cell which follows this one on the given dimension.
	 */
	public ICell<StateType> getNextCellOnDimension(int dimension) {
		return surroundings.get(dimension).next;
	}

	/**
	 * Change the link between this cell and the given one as : previous <-
	 * current. <b>It does not make the reverse link.</b>
	 */
	public void setPreviousCellOnDimension(int dimension,
			ICell<StateType> previous) {
		surroundings.get(dimension).previous = previous;
	}

	/**
	 * Gives the cell which precedes this one on the given dimension.
	 */
	public ICell<StateType> getPreviousCellOnDimension(int dimension) {
		return surroundings.get(dimension).previous;
	}

	/**
	 * This method indicate <code>true</code> after the calculating is done. It
	 * returns to <code>false</code> when the next state is applied or removed
	 * (set to <code>null</code>.
	 * 
	 * @see #calculateNextState()
	 * @see #applyNextState()
	 */
	public boolean isNextStateCalculated() {
		return nextState != null;
	}

	/**
	 * This method allows to get all the cells linked to this one : the cells
	 * before and after this one on each dimension.
	 * 
	 * @see #getPreviousCellOnDimension(int)
	 * @see #getNextCellOnDimension(int)
	 */
	public Set<ICell<StateType>> getAllCellsAround() {
		Set<ICell<StateType>> neighbors = new HashSet<ICell<StateType>>();
		for (int dimension = 0; dimension < getDimensions(); dimension++) {
			neighbors.add(getPreviousCellOnDimension(dimension));
			neighbors.add(getNextCellOnDimension(dimension));
		}
		neighbors.remove(null);
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

	/**
	 * Gives a simple display of the cell.
	 */
	@Override
	public String toString() {
		return "cell(" + getCurrentState().toString() + ") "
				+ Arrays.toString(coords.getAll());
	}

	/**
	 * Gives the coordinates of the current cell. These coordinates are given
	 * manually via the {@link #setCoords(int...)}. These coordinates are usable
	 * only if you are sure they corresponds to the reality. Otherwise using
	 * them is not recommended.
	 */
	public Coords getCoords() {
		return coords;
	}

	/**
	 * Force the current state to a particular value.
	 */
	public void setCurrentState(StateType state) {
		previousStates.forceCurrentState(state);
	}

	/**
	 * Replace the rule to apply to this cell.
	 */
	public void setRule(IRule<StateType> rule) {
		this.rule = rule;
	}

	/**
	 * This class is a simple container for the cells before/after the current
	 * one on a specific dimensions. Basically, there is no link (
	 * <code>null</code>).
	 * 
	 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
	 * 
	 */
	private class LinkedCellsCouple {
		public ICell<StateType> previous = null;
		public ICell<StateType> next = null;
	}
}
