package org.cellularautomaton.space;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

	public SpaceBuilder<StateType> createNewSpace() {
		dimensionLengths.clear();
		originCell = null;
		return this;
	}

	public SpaceBuilder<StateType> addDimension(int length) {
		return addDimension(length, true);
	}

	public SpaceBuilder<StateType> addDimension(int length, boolean cyclic) {
		if (originCell == null) {
			originCell = cellFactory.createCell();
		}

		dimensionLengths.add(length);
		final Collection<ICell<StateType>> starts = new GenericSpace<StateType>(
				originCell).getAllCells();
		generateDimensionFrom(starts, length);

		if (cyclic) {
			cycle(starts, dimensionLengths.size() - 1);
		}

		return this;
	}

	private void cycle(Collection<ICell<StateType>> startCells, int i) {
		for (ICell<StateType> start : startCells) {
			ICell<StateType> stop = start;
			while (stop.getNextCellOnDimension(i) != null) {
				stop = stop.getNextCellOnDimension(i);
			}
			stop.setNextCellOnDimension(i, start);
			start.setPreviousCellOnDimension(i, stop);
		}
	}

	private void generateDimensionFrom(Collection<ICell<StateType>> refs,
			int length) {
		// dimension caracteristics
		int actualDimension = dimensionLengths.size() - 1;

		// expansion
		for (int rank = 1; rank < length; rank++) {
			// creation of the next layer of cells
			Collection<ICell<StateType>> added = new HashSet<ICell<StateType>>();
			for (ICell<StateType> cellBefore : refs) {
				ICell<StateType> cellAfter = cellFactory.createCell();

				if (rank == 1) {
					cellBefore.setDimensions(actualDimension + 1);
				}
				cellAfter.setDimensions(cellBefore.getDimensions());
				cellAfter.getCoords().setAll(cellBefore.getCoords().getAll());
				cellAfter.getCoords().set(actualDimension, rank);

				linkCells(cellBefore, cellAfter, actualDimension);

				added.add(cellAfter);
			}

			// linking of the new layer
			for (ICell<StateType> cellBefore : added) {
				ICell<StateType> ref = cellBefore
						.getPreviousCellOnDimension(actualDimension);
				for (int dim = 0; dim < actualDimension; dim++) {
					if (ref.getNextCellOnDimension(dim) != null
							&& ref.getNextCellOnDimension(dim) != ref) {
						ICell<StateType> cellAfter = ref
								.getNextCellOnDimension(dim)
								.getNextCellOnDimension(actualDimension);

						linkCells(cellBefore, cellAfter, dim);
					}
				}
			}

			// preparation of the next layer
			refs = added;
		}
	}

	private void linkCells(ICell<StateType> cellBefore,
			ICell<StateType> cellAfter, int dimension) {
		cellBefore.setNextCellOnDimension(dimension, cellAfter);

		cellAfter.setPreviousCellOnDimension(dimension, cellBefore);
	}

	/**
	 * 
	 * @return the space of cells
	 */
	public ISpace<StateType> getSpaceOfCell() {
		GenericSpace<StateType> space = new GenericSpace<StateType>(originCell);
		for (Iterator<ICell<StateType>> iterator = space.iterator(); iterator
				.hasNext();) {
			ICell<StateType> cell = iterator.next();
			cell.setCurrentState(stateFactory.getStateFor(cell));
		}
		return space;
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
	 * @param stateFactory
	 *            the state factory used to initialize the cells
	 */
	public SpaceBuilder<StateType> setStateFactory(
			IStateFactory<StateType> stateFactory) {
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
