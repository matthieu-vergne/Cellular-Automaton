package org.cellularautomaton;

/**
 * Configuration allowing to generate a space of cells for a cellular automaton,
 * via the constructor
 * {@link CellularAutomaton#CellularAutomaton(GeneratorConfiguration)}.
 * Basically, the configuration consider :<br/>
 * <ul>
 * <li>no initial state (given manually)</li>
 * <li>a memory size of 1</li>
 * <li>a cyclic space</li>
 * <li>no dimensions (given manually)</li>
 * <li>no calculation method (given by
 * {@link GeneratorConfiguration#calculateForCell(Cell)} when generating the
 * config instance)</li>
 * </ul>
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 */
public abstract class GeneratorConfiguration<StateType> {
	// TODO replace members by methods where useful
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
	public int[] dimensionSizes = null;

	/**
	 * This method must implement the calculation rule to give to each cell of
	 * the space of cells.
	 * 
	 * @param cell
	 *            the cell to consider
	 * @return the calculated state to apply to the cell
	 */
	public abstract StateType calculateForCell(Cell<StateType> cell);

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
