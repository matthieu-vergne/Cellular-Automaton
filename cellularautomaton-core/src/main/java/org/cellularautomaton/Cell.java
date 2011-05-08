package org.cellularautomaton;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A cell is an element storing a state. This cell evolves in a space of cells
 * considering other cells (the neighbors), changing its state consequently.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public abstract class Cell<StateType> {

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
	private final Cell<StateType>[][] neighbors;
	/**
	 * The coordinates of the cell in the space of cells.
	 */
	private final int[] coords;

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
	public Cell(StateType initialState, int dimensions, int memorySize) {
		assert initialState != null;
		assert dimensions > 0;
		assert memorySize >= 0;

		previousStates = new StateMemory<StateType>(memorySize, initialState);
		this.neighbors = new Cell[dimensions][2];
		for (int dimension = 0; dimension < dimensions; dimension++) {
			neighbors[dimension] = new Cell[] { this, this };
		}
		coords = new int[dimensions];
		Arrays.fill(getCoords(), 0);
	}

	/**
	 * Create a cell with a one slot memory. It means it can remember only its
	 * actual state.
	 * 
	 * @see #Cell(Object, int, int)
	 */
	public Cell(StateType initialState, int dimensions) {
		this(initialState, dimensions, 1);
	}

	/**
	 * 
	 * @return the current state of the cell
	 */
	public StateType getCurrentState() {
		return getState(0);
	}

	/**
	 * 
	 * @param age
	 *            the age of the asked state, 0 is the actual state
	 * @return the state the cell had
	 */
	public StateType getState(int age) {
		return previousStates.getState(age);
	}

	/**
	 * 
	 * @return the next state of this cell
	 */
	protected abstract StateType calculateState();

	/**
	 * Calculate the next state. To apply it as the actual state of the cell,
	 * use {@link #applyNextState()}.
	 * 
	 * @throws NullPointerException
	 *             the calculation has returned a <code>null</code> value
	 */
	public void calculateNextState() {
		nextState = calculateState();
		if (nextState == null) {
			throw new NullPointerException(
					"the calculation has returned a null value");
		}
	}

	/**
	 * Consider the next state of the cell as the new current state.
	 * 
	 * @throws IllegalStateException
	 *             the next state is not calculated yet
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
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
	 * @return the next cell on this dimension
	 */
	public Cell<StateType> getNextCellOnDimension(int dimension) {
		return neighbors[dimension][1];
	}

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
	 * @param neighbor
	 *            the cell to consider as the next cell on this dimension
	 */
	public void setNextCellOnDimension(int dimension, Cell<StateType> neighbor) {
		neighbors[dimension][1] = neighbor;
	}

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
	 * @param neighbor
	 *            the cell to consider as the previous cell on this dimension
	 */
	public void setPreviousCellOnDimension(int dimension,
			Cell<StateType> neighbor) {
		neighbors[dimension][0] = neighbor;
	}

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
	 * @return the previous cell on this dimension
	 */
	public Cell<StateType> getPreviousCellOnDimension(int dimension) {
		return neighbors[dimension][0];
	}

	/**
	 * 
	 * @return true if the next state has been given, false otherwise
	 */
	public boolean isNextStateCalculated() {
		return nextState != null;
	}

	/**
	 * This method allows to get all the cells linked to this one. The linked
	 * cells are the ones accessible from
	 * {@link #getPreviousCellOnDimension(int)} and
	 * {@link #getNextCellOnDimension(int)} for each dimension.
	 * 
	 * @return all the cells near of the current cell.
	 */
	public Set<Cell<StateType>> getAllCellsAround() {
		Set<Cell<StateType>> neighbors = new HashSet<Cell<StateType>>();
		for (int dimension = 0; dimension < getDimensions(); dimension++) {
			neighbors.add(getPreviousCellOnDimension(dimension));
			neighbors.add(getNextCellOnDimension(dimension));
		}
		return neighbors;
	}

	/**
	 * <p>
	 * Give the cell which is at the given coordinates, starting from the
	 * current cell.
	 * </p>
	 * <p>
	 * <b>Be careful :</b> the behavior of this method is ensured for regular
	 * spaces only, with constant size on each dimension (see
	 * {@link CellularAutomaton#CellularAutomaton(Object, org.cellularautomaton.CellularAutomaton.CalculationWrapper, int, boolean, int...)}
	 * for more details about these spaces). It means that any customized space
	 * of cells has strong chances to not work well with this method. In this
	 * case, check it works well before to use it. Otherwise prefer to use
	 * {@link #getPreviousCellOnDimension(int)} and
	 * {@link #getNextCellOnDimension(int)} directly.
	 * </p>
	 * 
	 * @param coords
	 *            the relative coordinates of the cell, where (0, 0, ..., 0)
	 *            corresponds to the current cell.
	 * @return the cell found at the given coordinates
	 */
	public Cell<StateType> getRelativeCell(int... coords) {
		Cell<StateType> cell = this;
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
	 * Gives the coords of the current cell. These coords are given manually via
	 * the {@link #setCoords(int...)}, in particular by the automaton when it
	 * generates the space of cells. Anyway, these coords are usable only if you
	 * are sure they corresponds to the reality and, in general, only in the
	 * case of an orthogonal space of cells with constant sizes for each
	 * dimension (see
	 * {@link CellularAutomaton#CellularAutomaton(GeneratorConfiguration)} for
	 * more details). In any other cases, mainly if you do not know if the
	 * coords was updated correctly, using them is not recommended.
	 * 
	 * @return the coords of the cell in the space
	 */
	public int[] getCoords() {
		return coords;
	}

	/**
	 * This method allows to give coords to the current cell. This is only a
	 * parameter of the cell, it does not "move" the cell in the space in order
	 * to place it at the given coords. Moreover, no check is done on the
	 * values, so you must be sure of what you give if you want to use these
	 * coords later correctly.
	 * 
	 * @param coords
	 *            the coords of the cell
	 */
	public void setCoords(int... coords) {
		assert coords != null;
		assert coords.length == this.coords.length;

		for (int i = 0; i < coords.length; i++) {
			this.coords[i] = coords[i];
		}
	}

	/**
	 * 
	 * @param state
	 *            the state to apply to the cell
	 */
	public void setCurrentState(StateType state) {
		previousStates.forceCurrentState(state);
	}
}
