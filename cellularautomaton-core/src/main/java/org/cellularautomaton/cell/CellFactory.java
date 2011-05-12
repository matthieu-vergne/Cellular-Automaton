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
	 * <li>1 dimension</li>
	 * <li>a memory size of 1</li>
	 * <li>a static rule (keep current state)</li>
	 * </ul>
	 */
	public CellFactory() {
		initialState = null;
		dimensions = 1;
		memorySize = 1;
		rule = new RuleFactory<StateType>().getStaticRuleInstance();
	}

	/**
	 * 
	 * @return a cell not linked to any other cell.
	 */
	public ICell<StateType> createIsolatedCell() {
		ICell<StateType> cell = new GenericCell<StateType>(getInitialState(), getMemorySize());
		cell.setRule(getRule());
		cell.setDimensions(getDimensions());
		return cell;
	}

	/**
	 * 
	 * @return a cell linked to itself on each dimension.
	 */
	public ICell<StateType> createCyclicCell() {
		ICell<StateType> cell = new GenericCell<StateType>(getInitialState(), getMemorySize());
		cell.setRule(getRule());
		cell.setDimensions(getDimensions());
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
	 * 
	 * @param memorySize
	 *            the memory size of each cell (the number of states it can
	 *            remember, 1 is the basic value).
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
