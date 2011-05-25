package org.cellularautomaton.cell;

import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.rule.RuleFactory;

/**
 * A cell factory allows to create different type of cells easily. Several
 * parameters are available in order to customize the cells to produce.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public class CellFactory<StateType> {

	/**
	 * The initial state to apply to new cells.
	 */
	private StateType initialState;
	/**
	 * The number of dimensions to consider for each new cell.
	 */
	private int dimensions;
	/**
	 * The memory size of each cell (the number of states it can remember, 1 is
	 * the basic value).
	 */
	private int memorySize;
	/**
	 * The rule to apply to each new cell.
	 */
	private IRule<StateType> rule;

	/**
	 * Create a new factory with the corresponding configuration :<br/>
	 * <ul>
	 * <li>no initial state (null)</li>
	 * <li>0 dimensions (no cells around)</li>
	 * <li>a memory size of 1</li>
	 * <li>a static rule (keep current state)</li>
	 * </ul>
	 */
	public CellFactory() {
		initialState = null;
		dimensions = 0;
		memorySize = 1;
		rule = new RuleFactory<StateType>().getStaticRuleInstance();
	}

	/**
	 * This method simply instantiates a new cell. The basic implementation take
	 * a {@link GenericCell}, but it can be overridden to change the type of
	 * cell. The objective is, for example, to use optimized implementations.
	 * 
	 * @return the cell instantiated
	 */
	public ICell<StateType> instantiateCell() {
		return new GenericCell<StateType>();
	}

	/**
	 * 
	 * @return a new cell with the rule and dimensions set in the factory
	 */
	public ICell<StateType> createCell() {
		ICell<StateType> cell = instantiateCell();
		cell.setMemory(getMemorySize(), getInitialState());
		cell.setRule(getRule());
		cell.setDimensions(getDimensions());
		return cell;
	}

	/**
	 * 
	 * @return a cell linked to itself on each dimension.
	 */
	public ICell<StateType> createCyclicCell() {
		ICell<StateType> cell = createCell();
		for (int dim = 0; dim < cell.getDimensions(); dim++) {
			cell.setPreviousCellOnDimension(dim, cell);
			cell.setNextCellOnDimension(dim, cell);
		}
		return cell;
	}

	/**
	 * 
	 * @param initialState
	 *            the initial state to apply to new cells.
	 * @return the factory
	 */
	public CellFactory<StateType> setInitialState(StateType initialState) {
		this.initialState = initialState;
		return this;
	}

	/**
	 * 
	 * @return the initial state to apply to new cells.
	 */
	public StateType getInitialState() {
		return initialState;
	}

	/**
	 * 
	 * @param dimensions
	 *            the number of dimensions to consider for each new cell.
	 * @return the factory
	 */
	public CellFactory<StateType> setDimensions(int dimensions) {
		this.dimensions = dimensions;
		return this;
	}

	/**
	 * 
	 * @return the number of dimensions to consider for each new cell.
	 */
	public int getDimensions() {
		return dimensions;
	}

	/**
	 * Set the amount of states the cell has to remember.
	 * @param memorySize
	 *            the memory size of each cell. Default value: 1.
	 * @return the factory
	 */
	public CellFactory<StateType> setMemorySize(int memorySize) {
		this.memorySize = memorySize;
		return this;
	}

	/**
	 * 
	 * @return the memory size of each cell (the number of states it can
	 *         remember, 1 is the basic value).
	 */
	public int getMemorySize() {
		return memorySize;
	}

	/**
	 * 
	 * @param rule
	 *            the rule to apply to each new cell.
	 */
	public void setRule(IRule<StateType> rule) {
		this.rule = rule;
	}

	/**
	 * 
	 * @return the rule to apply to each new cell.
	 */
	public IRule<StateType> getRule() {
		return rule;
	}
}
