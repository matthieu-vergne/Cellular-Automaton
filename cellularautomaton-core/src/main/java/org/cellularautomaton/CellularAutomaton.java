package org.cellularautomaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.factory.CellFactory;

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
	private final ICell<StateType> originCell;
	/**
	 * The factory used to create the space of cells.
	 */
	private final CellFactory<StateType> cellFactory;
	/**
	 * The length of each dimension of the space of cells.
	 */
	private int[] dimensionSizes;
	/**
	 * The cyclic property of the space of cells.
	 */
	private boolean isCyclicSpace;

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
	public CellularAutomaton(ICell<StateType> originCell) {
		this.originCell = originCell;
		this.cellFactory = null;
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

		this.cellFactory = new CellFactory<StateType>();
		cellFactory.setInitialState(config.initialState);
		cellFactory.setDimensions(config.dimensionSizes.length);
		cellFactory.setMemorySize(config.memorySize);
		cellFactory.setRule(config.rule);
		this.dimensionSizes = config.dimensionSizes;
		this.isCyclicSpace = config.isCyclic;

		/* linking cells */
		originCell = generateCells(config.getDimensions() - 1);
	}

	/**
	 * 
	 * @param dimension
	 *            the zero-based dimension to consider, basically the dimension
	 *            in the configuration - 1
	 * @return a cell which can be used to get all the others (looking the cells
	 *         around)
	 */
	private ICell<StateType> generateCells(int dimension) {
		if (dimension < 0) {
			// TODO consider the isCyclic field
			ICell<StateType> cell = cellFactory.createCyclicCell();
			return cell;
		} else {
			ICell<StateType> start = null;
			ICell<StateType> ref = null;
			for (int coord = 0; coord < dimensionSizes[dimension]; coord++) {
				ICell<StateType> cell = generateCells(dimension - 1);
				if (start != null) {
					checkLevel(ref, cell, dimension - 1, dimension, coord);
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
	private void checkLevel(ICell<StateType> cellRef,
			ICell<StateType> cellToInsert, int dimensionToCheck,
			int initialDimension, int coord) {
		if (dimensionToCheck < 0) {
			cellToInsert.setPreviousCellOnDimension(initialDimension, cellRef);
			cellToInsert.setNextCellOnDimension(initialDimension,
					cellRef.getNextCellOnDimension(initialDimension));
			cellToInsert.getCoords()[initialDimension] = coord;
			cellRef.getNextCellOnDimension(initialDimension)
					.setPreviousCellOnDimension(initialDimension, cellToInsert);
			cellRef.setNextCellOnDimension(initialDimension, cellToInsert);
		} else {
			for (int i = 0; i < dimensionSizes[dimensionToCheck]; i++) {
				checkLevel(cellRef, cellToInsert, dimensionToCheck - 1,
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
		for (ICell<StateType> cell : getAllCells()) {
			cell.calculateNextState();
		}
	}

	/**
	 * Apply the previously calculated states of each cell. If some cells have
	 * not calculated yet, their state does not change.
	 */
	public void applyNextStep() {
		for (ICell<StateType> cell : getAllCells()) {
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
	 *         automaton can work on are available from this one (with relative
	 *         coordinates).
	 */
	public ICell<StateType> getOriginCell() {
		synchronized (originCell) {
			return originCell;
		}
	}

	/**
	 * 
	 * @return all the cells of the automaton (without specific ordering)
	 */
	public Collection<ICell<StateType>> getAllCells() {
		Collection<ICell<StateType>> result = new ArrayList<ICell<StateType>>();
		for (Iterator<ICell<StateType>> iterator = iterator(); iterator
				.hasNext();) {
			ICell<StateType> cell = iterator.next();
			result.add(cell);
		}
		return result;
	}

	/**
	 * Give an iterator over the cells. It is strongly recommended to <b>not
	 * modify the space of cells during iteration</b>, as the modifications can
	 * generate unexpected behaviors.
	 * 
	 * @return an iterator over the cells
	 */
	public Iterator<ICell<StateType>> iterator() {
		return new CellIterator();
	}

	/**
	 * Specific iterator for the space of cells. If the space is modified during
	 * the iteration, it is possible that the modifications are not considered :<br/>
	 * <ul>
	 * <li>if a modification is done "far" (not in the cells around) of the
	 * cells already iterated, it should be considered,</li>
	 * <li>if a modification is done between cells already iterated, it should
	 * not be considered</li>
	 * <li>if a modification is done at the limit between iterated and not
	 * iterated cells, all is possible.</li>
	 * </ul>
	 * 
	 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
	 * 
	 */
	private class CellIterator implements Iterator<ICell<StateType>> {

		/**
		 * The cells already returned.
		 */
		Collection<ICell<StateType>> cellsUsed = new HashSet<ICell<StateType>>();
		/**
		 * The cells to look for next iterations.
		 */
		Collection<ICell<StateType>> cellsToCheck = new HashSet<ICell<StateType>>();

		/**
		 * Create an iterator over the current space of cells.
		 */
		public CellIterator() {
			cellsToCheck.add(getOriginCell());
		}

		public boolean hasNext() {
			return !cellsToCheck.isEmpty();
		}

		public ICell<StateType> next() {

			if (hasNext()) {
				ICell<StateType> cell = cellsToCheck.iterator().next();
				cellsToCheck.remove(cell);
				cellsUsed.add(cell);
				for (ICell<StateType> cellAround : cell.getAllCellsAround()) {
					if (!cellsUsed.contains(cellAround)) {
						cellsToCheck.add(cellAround);
					}
				}
				return cell;
			} else {
				throw new NoSuchElementException();
			}
		}

		/**
		 * This method do nothing.
		 */
		public void remove() {
			// do nothing
		}

	}
}
