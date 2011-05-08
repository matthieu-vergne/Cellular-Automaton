package org.cellularautomaton;

import java.util.Collection;
import java.util.HashSet;

/**
 * A cellular automaton is a space of evolving cells. Each cell evolves
 * considering its actual state and the states of other cells.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by each cell, it can be {@link Boolean} for
 *            a simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses (just consider all the cells use the same
 *            type).
 */
public class CellularAutomaton<StateType> {
	/**
	 * The cell to consider as the start of the space (all the other cells are
	 * accessible from this one).
	 */
	private final Cell<StateType> originCell;

	/**
	 * <p>
	 * Create an automaton on a specific space of cells.
	 * </p>
	 * <p>
	 * <b>Be careful :</b> the cell given to the automaton is the only access
	 * point, so all the cells must be reachable from this one. If a cell cannot
	 * be reached its state will never evolute.
	 * </p>
	 * 
	 * @param originCell
	 *            a cell of the space of cells.
	 */
	public CellularAutomaton(Cell<StateType> originCell) {
		this.originCell = originCell;
	}

	/**
	 * Generate an orthogonal space of cells. In other words a line in 1D, a
	 * square-based space in 2D, a cubic-based space in 3D, ...
	 * 
	 * @param initialState
	 *            the initial state of each cell
	 * @param config
	 *            the calculation method to apply for each cell
	 * @param memorySize
	 *            the memory size of each cell (number of states they can
	 *            remember)
	 * @param isCyclic
	 *            tell if the cell space generated must be cyclic (for each
	 *            dimension, the next cell after the last one is the first one)
	 *            ore not (a frontier cell is given after the last one and
	 *            before the first one)
	 * @param dimensionSizes
	 *            the number of cells to put in each dimension, the number of
	 *            sizes put in arguments give the dimension of the automaton
	 */
	public CellularAutomaton(final GeneratorConfiguration<StateType> config) {
		/* check */
		assert config != null;
		assert config.isValid();

		/* linking cells */
		// TODO consider the isCyclic parameter
		originCell = generateCells(config, config.getDimensions() - 1);
	}

	/**
	 * 
	 * @param config
	 *            the configuration to consider
	 * @param dimension
	 *            the zero-based dimension to consider, basically the dimension
	 *            in the configuration - 1
	 * @return a cell which can be used to get all the others (looking
	 *         neighbors)
	 */
	private Cell<StateType> generateCells(
			final GeneratorConfiguration<StateType> config, int dimension) {
		if (dimension < 0) {
			return new Cell<StateType>(config.initialState,
					config.getDimensions(), config.memorySize) {
				@Override
				protected StateType calculateState() {
					return config.calculateForCell(this);
				}
			};
		} else {
			Cell<StateType> start = null;
			Cell<StateType> ref = null;
			for (int coord = 0; coord < config.dimensionSizes[dimension]; coord++) {
				Cell<StateType> cell = generateCells(config, dimension - 1);
				if (start != null) {
					checkLevel(config, ref, cell, dimension - 1, dimension,
							coord);
				} else {
					start = cell;
				}
				ref = cell;
			}
			return start;
		}
	}

	/**
	 * 
	 * @param config
	 *            the configuration to consider
	 * @param cellRef
	 *            the cell already in the space
	 * @param cellToInsert
	 *            the cell to insert in the space
	 * @param dimensionToCheck
	 *            the zero-based dimension to consider
	 * @param initialDimension
	 *            the initial zero-based dimension of the check
	 * @param coord
	 *            the coordinate of the cell in the considered dimension
	 */
	private void checkLevel(final GeneratorConfiguration<StateType> config,
			Cell<StateType> cellRef, Cell<StateType> cellToInsert,
			int dimensionToCheck, int initialDimension, int coord) {
		if (dimensionToCheck < 0) {
			cellToInsert.setPreviousCellOnDimension(initialDimension, cellRef);
			cellToInsert.setNextCellOnDimension(initialDimension,
					cellRef.getNextCellOnDimension(initialDimension));
			cellToInsert.getCoords()[initialDimension] = coord;
			cellRef.getNextCellOnDimension(initialDimension)
					.setPreviousCellOnDimension(initialDimension, cellToInsert);
			cellRef.setNextCellOnDimension(initialDimension, cellToInsert);
		} else {
			for (int i = 0; i < config.dimensionSizes[dimensionToCheck]; i++) {
				checkLevel(config, cellRef, cellToInsert, dimensionToCheck - 1,
						initialDimension, coord);
				cellRef = cellRef.getNextCellOnDimension(dimensionToCheck);
				cellToInsert = cellToInsert
						.getNextCellOnDimension(dimensionToCheck);
			}
		}
	}

	/**
	 * Calculate the next state of each cell. After this step, the logical way
	 * is to apply it with {@link #applyNextStep()}.
	 */
	public void calculateNextStep() {
		for (Cell<StateType> cell : getAllCells()) {
			cell.calculateNextState();
		}
	}

	/**
	 * Apply the previously calculated states of each cell. If some cells have
	 * not calculated yet, their state does not change.
	 */
	public void applyNextStep() {
		for (Cell<StateType> cell : getAllCells()) {
			cell.applyNextState();
		}
	}

	/**
	 * Execute a complete step of the automaton. After that, each cell has its
	 * next state.
	 */
	public void doStep() {
		calculateNextStep();
		applyNextStep();
	}

	/**
	 * 
	 * @return the cell this automaton work from, all the others cells the
	 *         automaton can work on are available from this one (with neighbor
	 *         links).
	 */
	public Cell<StateType> getOriginCell() {
		synchronized (originCell) {
			return originCell;
		}
	}

	/**
	 * 
	 * @return all the cells of the automaton (without specific ordering)
	 */
	public Collection<Cell<StateType>> getAllCells() {
		Collection<Cell<StateType>> result = new HashSet<Cell<StateType>>();
		Collection<Cell<StateType>> cellsToCheck = new HashSet<Cell<StateType>>();
		cellsToCheck.add(getOriginCell());

		while (!cellsToCheck.isEmpty()) {
			Cell<StateType> cell = cellsToCheck.iterator().next();
			cellsToCheck.remove(cell);
			result.add(cell);
			for (Cell<StateType> neighbor : cell.getAllNeighbors()) {
				if (!result.contains(neighbor)) {
					cellsToCheck.add(neighbor);
				}
			}
		}

		return result;
	}
}
