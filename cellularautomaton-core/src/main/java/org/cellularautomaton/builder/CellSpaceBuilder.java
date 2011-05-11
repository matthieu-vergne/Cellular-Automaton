package org.cellularautomaton.builder;

import java.util.ArrayList;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.factory.CellFactory;

public class CellSpaceBuilder<StateType> {
	/**
	 * The cell to consider as the start of the space (all the other cells are
	 * accessible from this one).
	 */
	private ICell<StateType> originCell;
	/**
	 * The factory used to create the space of cells.
	 */
	private final CellFactory<StateType> cellFactory = new CellFactory<StateType>();
	private final ArrayList<Integer> dimensionLengths = new ArrayList<Integer>();

	// TODO adapt to remove the constraint of giving dimensions from the beginning
	public CellSpaceBuilder<StateType> createNewSpace(int dimensions) {
		cellFactory.setDimensions(dimensions);
		dimensionLengths.clear();
		originCell = null;
		return this;
	}

	public CellSpaceBuilder<StateType> addDimension(int length) {
		dimensionLengths.add(length);
		if (dimensionLengths.size() == cellFactory.getDimensions()) {
			originCell = generateCells(dimensionLengths.size() - 1);
		}
		return this;
	}

	public ICell<StateType> getSpaceOfCellOrigin() {
		return originCell;
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
			cellAfter.getCoords()[initialDimension] = coord;
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

	public CellSpaceBuilder<StateType> setMemorySize(int memorySize) {
		cellFactory.setMemorySize(memorySize);
		return this;
	}

	public int getMemorySize() {
		return cellFactory.getMemorySize();
	}

	public CellSpaceBuilder<StateType> setRule(IRule<StateType> rule) {
		cellFactory.setRule(rule);
		return this;
	}

	public IRule<StateType> getRule() {
		return cellFactory.getRule();
	}

	public CellSpaceBuilder<StateType> setInitialState(StateType initialState) {
		cellFactory.setInitialState(initialState);
		return this;
	}

	public StateType getInitialState() {
		return cellFactory.getInitialState();
	}
}
