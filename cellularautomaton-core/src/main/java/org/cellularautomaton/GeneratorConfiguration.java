package org.cellularautomaton;

import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.factory.RuleFactory;

/**
 * Configuration allowing to generate a space of cells for a cellular automaton,
 * via the constructor
 * {@link CellularAutomaton#CellularAutomaton(GeneratorConfiguration)}.
 * Basically, the configuration consider :<br/>
 * <ul>
 * <li>no initial state (given manually)</li>
 * <li>a memory size of 1</li>
 * <li>a cyclic space</li>
 * <li>2 dimensions 10 elements each</li>
 * <li>a static rule</li>
 * </ul>
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
// TODO replace progressively the use of configuration by the use of factories/builders
public class GeneratorConfiguration<StateType> {
	/**
	 * The initial state to apply to each cell.
	 */
	public StateType initialState = null;
	/**
	 * The size of the memory of each cell.
	 */
	public int memorySize = 1;
	/**
	 * Tell if the space of cells must be cyclic or not. For example, a 2D space
	 * is :<br/>
	 * <ul>
	 * <li>a square (not cyclic)</li>
	 * <li>a tore (cyclic)</li>
	 * </ul>
	 */
	public boolean isCyclic = true;
	/**
	 * The length (number of cells) of each dimension of the space of cells.
	 */
	public int[] dimensionSizes = new int[] { 10, 10 };

	/**
	 * The rule of each cell.
	 */
	public IRule<StateType> rule = new RuleFactory<StateType>()
			.getStaticRuleInstance();

	/**
	 * This method try to check all possible points in order to check this
	 * configuration can be used to generate a cellular automaton (with
	 * {@link CellularAutomaton#CellularAutomaton(GeneratorConfiguration)} ).
	 * 
	 * @return true if you can generate an automaton from this config, false
	 *         otherwise
	 */
	public boolean isValid() {
		return initialState != null && memorySize >= 0
				&& dimensionSizes != null && dimensionSizes.length > 0
				&& onlyStrictlyPositiveDimensionsIn(dimensionSizes);
	}

	/**
	 * 
	 * @return the number of dimensions this config considers
	 */
	public int getDimensions() {
		return dimensionSizes.length;
	}

	/**
	 * 
	 * @return the number of cells this config considers
	 */
	public long getCellsCount() {
		long cellsCount = 1;
		for (int size : dimensionSizes) {
			cellsCount *= size;
		}
		return cellsCount;
	}

	/**
	 * 
	 * @param dimensionSizes
	 *            the dimension sizes to check
	 * @return true if all the dimension sizes are 1 or more, false otherwise
	 */
	private boolean onlyStrictlyPositiveDimensionsIn(int... dimensionSizes) {
		for (int dimension : dimensionSizes) {
			if (dimension <= 0) {
				return false;
			}
		}
		return true;
	}
}
