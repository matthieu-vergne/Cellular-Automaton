package org.cellularautomaton.cell;

import java.util.Set;

import org.cellularautomaton.rule.IRule;

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
public interface ICell<StateType> {

	/**
	 * 
	 * @param state
	 *            the state to apply to the cell
	 */
	public void setCurrentState(StateType state);

	/**
	 * 
	 * @return the current state of the cell
	 */
	public StateType getCurrentState();

	/**
	 * This method must give the state of the cell in a specific generation from
	 * the current one. The age 0 corresponds to the last (current) state, the
	 * age 1 the state before, etc. This method allows to have dependencies
	 * through multiple generations.
	 * 
	 * @param age
	 *            the age of the asked state, 0 is the current state
	 * @return the state the cell had
	 */
	public StateType getState(int age);

	/**
	 * 
	 * @param rule
	 *            the rule to apply in order to calculate the next state of the
	 *            cell
	 */
	public void setRule(IRule<StateType> rule);

	/**
	 * This method must calculate the next state of the cell. The current state
	 * is <b>not changed</b>.
	 * 
	 * @throws NullPointerException
	 *             the calculation has returned a <code>null</code> value
	 * @see #applyNextState()
	 */
	public void calculateNextState() throws NullPointerException;

	/**
	 * 
	 * @return true if the next state has been given, false otherwise
	 */
	public boolean isNextStateCalculated();

	/**
	 * This method must change the current state in order to pass to the next
	 * generation.
	 * 
	 * @throws IllegalStateException
	 *             the next state is not calculated yet
	 */
	public void applyNextState() throws IllegalStateException;

	/**
	 * 
	 * @return the number of dimensions this cell work on
	 */
	public int getDimensions();

	/**
	 * This method may preserve all coherent data : if the dimensions are
	 * decreased from 3 to 2 for example, the linked cells around must be
	 * preserved for the dimensions 0 and 1 (as it is zero-based).
	 * 
	 * @param dimensions
	 *            the number of dimensions this cell work on
	 */
	public void setDimensions(int dimensions);

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (zero-based)
	 * @param cell
	 *            the cell to consider as the next cell on this dimension
	 */
	public void setNextCellOnDimension(int dimension, ICell<StateType> cell);

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
	 * @return the next cell on this dimension
	 */
	public ICell<StateType> getNextCellOnDimension(int dimension);

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
	 * @param cell
	 *            the cell to consider as the previous cell on this dimension
	 */
	public void setPreviousCellOnDimension(int dimension, ICell<StateType> cell);

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
	 * @return the previous cell on this dimension
	 */
	public ICell<StateType> getPreviousCellOnDimension(int dimension);

	/**
	 * This method must give all the cells near to this one. This is the
	 * <i>physical</i> neighborhood (nearest cells in the space), not the
	 * <i>logical</i> neighborhood (the cells used to calculate the next state).
	 * 
	 * @return all the cells near of the current cell.
	 * @see #getPreviousCellOnDimension(int)
	 * @see #getNextCellOnDimension(int)
	 */
	public Set<ICell<StateType>> getAllCellsAround();

	/**
	 * This method must give the cell which is at the given coordinates,
	 * starting from the current cell.
	 * 
	 * @param coords
	 *            the relative coordinates of the cell to find, where (0, 0,
	 *            ..., 0) corresponds to the current cell.
	 * @return the cell found at the given relative coordinates
	 */
	public ICell<StateType> getRelativeCell(int... coords);

	/**
	 * 
	 * @param coords
	 *            the coordinates of the cell
	 */
	public void setCoords(int... coords);

	/**
	 * 
	 * @return the coordinates of the cell in the space
	 */
	public int[] getCoords();
}
