package org.cellularautomaton.space;

import java.util.ArrayList;
import java.util.Iterator;

import org.cellularautomaton.cell.CellFactory;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.state.IStateFactory;

public class SpaceBuilder<StateType> {
	/**
	 * The cell to consider as the start of the space (all the other cells are
	 * accessible from this one).
	 */
	private ICell<StateType> originCell;
	/**
	 * The factory used to create the space of cells.
	 */
	private final CellFactory<StateType> cellFactory;
	/**
	 * The lengths of each dimension.
	 */
	private final ArrayList<Integer> dimensionLengths;
	/**
	 * The state factory used to initialize the cells.
	 */
	private IStateFactory<StateType> stateFactory;
	
	/**
	 * Create a new space builder with :<br/>
	 * <ul>
	 * <li>no state factory (given manually)</li>
	 * <li>a memory size of 1</li>
	 * <li>a static rule</li>
	 * </ul>
	 */
	public SpaceBuilder() {
		cellFactory = new CellFactory<StateType>();
		dimensionLengths = new ArrayList<Integer>();
	}

	// TODO adapt to remove the constraint of giving dimensions from the
	// beginning
	public SpaceBuilder<StateType> createNewSpace(int dimensions) {
		cellFactory.setDimensions(dimensions);
		dimensionLengths.clear();
		originCell = null;
		return this;
	}

	public SpaceBuilder<StateType> addDimension(int length) {
		dimensionLengths.add(length);
		if (dimensionLengths.size() == cellFactory.getDimensions()) {
			originCell = generateCells(dimensionLengths.size() - 1);
		}
		return this;
	}

	/**
	 * 
	 * @return the space of cells
	 */
	public ISpace<StateType> getSpaceOfCell() {
		GenericSpace<StateType> space = new GenericSpace<StateType>(originCell);
		for (Iterator<ICell<StateType>> iterator = space.iterator(); iterator.hasNext();) {
			ICell<StateType> cell = iterator.next();
			cell.setCurrentState(stateFactory.getStateFor(cell));
		}
		return space;
	}

	/**
	 * 
	 * @param dimension
	 *            the zero-based dimension to consider, basically the dimension
	 *            in the configuration - 1
	 * @return a cell which can be used to get all the others (looking the cells
	 *         around)
	 */
	// FIXME this method is for complete generation, not incremental
	private ICell<StateType> generateCells(int dimension) {
		if (dimension < 0) {
			// TODO consider the isCyclic field after creating test
			ICell<StateType> cell = cellFactory.createCyclicCell();
			return cell;
		} else {
			ICell<StateType> start = null;
			ICell<StateType> ref = null;
			for (int coord = 0; coord < dimensionLengths.get(dimension); coord++) {
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
	 * @param cellBefore
	 *            the cell in the previous place
	 * @param cellAfter
	 *            the cell in the next place
	 * @param dimensionToCheck
	 *            the zero-based dimension to consider
	 * @param initialDimension
	 *            the initial zero-based dimension of the check
	 * @param coord
	 *            the coordinate of the cell in the considered dimension
	 */
	// FIXME this method is for complete generation, not incremental
	private void checkLevel(ICell<StateType> cellBefore,
			ICell<StateType> cellAfter, int dimensionToCheck,
			int initialDimension, int coord) {
		if (dimensionToCheck < 0) {
			// TODO consider the isCyclic field after creating test
			cellAfter.setPreviousCellOnDimension(initialDimension, cellBefore);
			cellAfter.setNextCellOnDimension(initialDimension,
					cellBefore.getNextCellOnDimension(initialDimension));
			cellAfter.getCoords().set(initialDimension, coord);
			ICell<StateType> tempCell = cellBefore
					.getNextCellOnDimension(initialDimension);
			if (tempCell != null) {
				tempCell.setPreviousCellOnDimension(initialDimension, cellAfter);
			}
			cellBefore.setNextCellOnDimension(initialDimension, cellAfter);
		} else {
			for (int i = 0; i < dimensionLengths.get(dimensionToCheck); i++) {
				checkLevel(cellBefore, cellAfter, dimensionToCheck - 1,
						initialDimension, coord);
				cellBefore = cellBefore
						.getNextCellOnDimension(dimensionToCheck);
				cellAfter = cellAfter.getNextCellOnDimension(dimensionToCheck);
			}
		}
	}

	public SpaceBuilder<StateType> setMemorySize(int memorySize) {
		cellFactory.setMemorySize(memorySize);
		return this;
	}

	public int getMemorySize() {
		return cellFactory.getMemorySize();
	}

	public SpaceBuilder<StateType> setRule(IRule<StateType> rule) {
		cellFactory.setRule(rule);
		return this;
	}

	public IRule<StateType> getRule() {
		return cellFactory.getRule();
	}

	/**
	 * 
	 * @param stateFactory the state factory used to initialize the cells
	 */
	public SpaceBuilder<StateType> setStateFactory(IStateFactory<StateType> stateFactory) {
		this.stateFactory = stateFactory;
		cellFactory.setInitialState(stateFactory.getDefaultState());
		return this;
	}

	/**
	 * 
	 * @return the state factory used to initialize the cells
	 */
	public IStateFactory<StateType> getStateFactory() {
		return stateFactory;
	}
	
	
}
