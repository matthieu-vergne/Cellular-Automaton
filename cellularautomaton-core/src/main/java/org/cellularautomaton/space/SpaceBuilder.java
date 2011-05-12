package org.cellularautomaton.space;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.cellularautomaton.cell.CellFactory;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.state.IStateFactory;

/**
 * A space builder allows to create a complete space of cells. The number of
 * dimensions and the number of cells on each dimension is configurable. The
 * coordinates are automatically written.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
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

	/**
	 * Create an empty space with 0 dimensions. You must add dimensions in order
	 * to build the space with cells.
	 * 
	 * @return this builder
	 * @see #addDimension(int, boolean)
	 */
	public SpaceBuilder<StateType> createNewSpace() {
		dimensionLengths.clear();
		originCell = null;
		return this;
	}

	/**
	 * Same as {@link #addDimension(int, boolean)} with the cyclic parameter at
	 * <code>true</code>.
	 */
	public SpaceBuilder<StateType> addDimension(int length) {
		return addDimension(length, true);
	}

	/**
	 * This method allows to add a dimension and to fill it with cells. The
	 * length of the dimension indicate the number of cells on this dimension.
	 * The coordinates of these cells go from 0 to <code>length - 1</code> on
	 * the new dimension. When no more dimensions (and cells) are needed, you
	 * can get the space.
	 * 
	 * @param length
	 *            the number of cells to put in this dimension
	 * @param cyclic
	 *            tells if the last cells of the dimension must loop to the
	 *            firsts
	 * @return this builder
	 * @see #getSpaceOfCell()
	 */
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

	/**
	 * This method make a loop with the given dimension, linking the last cells
	 * of the dimension to the first ones.
	 * 
	 * @param startCells
	 *            the cells which are at the beginning of the dimension
	 * @param dimension
	 *            the dimension to consider
	 */
	private void cycle(Collection<ICell<StateType>> startCells, int dimension) {
		for (ICell<StateType> start : startCells) {
			ICell<StateType> stop = start;
			while (stop.getNextCellOnDimension(dimension) != null) {
				stop = stop.getNextCellOnDimension(dimension);
			}
			stop.setNextCellOnDimension(dimension, start);
			start.setPreviousCellOnDimension(dimension, stop);
		}
	}

	/**
	 * This method create cells on a new dimension, considering the given cells
	 * as the first cells of the dimension. <code>length - 1</code> new cells
	 * are added (and linked) to each of the first cells. The generated
	 * dimension is not cyclic.
	 * 
	 * @param startCells
	 * @param length
	 */
	private void generateDimensionFrom(Collection<ICell<StateType>> startCells,
			int length) {
		// dimension characteristics
		int actualDimension = dimensionLengths.size() - 1;

		// expansion
		for (int rank = 1; rank < length; rank++) {
			// creation of the next layer of cells
			Collection<ICell<StateType>> added = new HashSet<ICell<StateType>>();
			for (ICell<StateType> cellBefore : startCells) {
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
			startCells = added;
		}
	}

	/**
	 * Links the first cell to the second.
	 * 
	 * @param cellBefore
	 *            the previous cell
	 * @param cellAfter
	 *            the next cell
	 * @param dimension
	 *            the dimension to consider
	 */
	private void linkCells(ICell<StateType> cellBefore,
			ICell<StateType> cellAfter, int dimension) {
		cellBefore.setNextCellOnDimension(dimension, cellAfter);
		cellAfter.setPreviousCellOnDimension(dimension, cellBefore);
	}

	/**
	 * <b>BE CAREFUL :</b> when you get the space of cells through this method,
	 * it is not deleted from the builder. If you add another dimension, it is
	 * probable that the already gotten space will be affected. Be sure to
	 * create a new space before to modify it if this is not what you want.
	 * 
	 * @return the space of cells
	 * @see #createNewSpace()
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

	/**
	 * 
	 * @param memorySize
	 *            the memory size of the created cells
	 * @return this builder
	 */
	public SpaceBuilder<StateType> setMemorySize(int memorySize) {
		cellFactory.setMemorySize(memorySize);
		return this;
	}

	/**
	 * 
	 * @return the memory size of the created cells
	 */
	public int getMemorySize() {
		return cellFactory.getMemorySize();
	}

	/**
	 * 
	 * @param rule
	 *            the rule given to the created cells
	 * @return this builder
	 */
	public SpaceBuilder<StateType> setRule(IRule<StateType> rule) {
		cellFactory.setRule(rule);
		return this;
	}

	/**
	 * 
	 * @return the rule given to the created cells
	 */
	public IRule<StateType> getRule() {
		return cellFactory.getRule();
	}

	/**
	 * The state factory is used in this way :<br/>
	 * <ol>
	 * <li>{@link IStateFactory#getDefaultState()} is used to create the cells,
	 * this value fill the memory of the cells newly created</li>
	 * <li>{@link IStateFactory#getStateFor(ICell)} is used to initialize the
	 * current state of each cell after creation (and insertion in the space),
	 * it allows to give specific values to particular cells.</li>
	 * </ol>
	 * Notice that if the memory size is set to 1, the second point just
	 * overwrite the first, as the only memory slot available corresponds to the
	 * current state.
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
