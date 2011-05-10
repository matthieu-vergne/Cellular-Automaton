package org.cellularautomaton.definition;

import java.util.Set;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.GeneratorConfiguration;

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
	 * @return the number of dimensions this cell work on
	 */
	public int getDimensions();

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
	 * 
	 * @param age
	 *            the age of the asked state, 0 is the actual state
	 * @return the state the cell had
	 */
	public StateType getState(int age);

	/**
	 * 
	 * @param rule the rule to apply in order to calculate the next state of the cell
	 */
	public void setRule(IRule<StateType> rule);

	/**
	 * Calculate the next state. To apply it as the actual state of the cell,
	 * use {@link #applyNextState()}.
	 * 
	 * @throws NullPointerException
	 *             the calculation has returned a <code>null</code> value
	 */
	public void calculateNextState();

	/**
	 * 
	 * @return true if the next state has been given, false otherwise
	 */
	public boolean isNextStateCalculated();

	/**
	 * Consider the next state of the cell as the new current state.
	 * 
	 * @throws IllegalStateException
	 *             the next state is not calculated yet
	 */
	public void applyNextState();

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
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
	public void setPreviousCellOnDimension(int dimension,
			ICell<StateType> cell);

	/**
	 * 
	 * @param dimension
	 *            the dimension to consider (0 for the first dimension)
	 * @return the previous cell on this dimension
	 */
	public ICell<StateType> getPreviousCellOnDimension(int dimension);

	/**
	 * This method allows to get all the cells linked to this one. The linked
	 * cells are the ones accessible from
	 * {@link #getPreviousCellOnDimension(int)} and
	 * {@link #getNextCellOnDimension(int)} for each dimension.
	 * 
	 * @return all the cells near of the current cell.
	 */
	public Set<ICell<StateType>> getAllCellsAround();

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
	public ICell<StateType> getRelativeCell(int... coords);

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
	public void setCoords(int... coords);

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
	public int[] getCoords();
}
