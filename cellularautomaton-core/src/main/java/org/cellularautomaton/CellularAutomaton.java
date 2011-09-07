package org.cellularautomaton;

import java.util.Collection;
import java.util.HashSet;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.optimization.CellsSelectionOptimization;
import org.cellularautomaton.optimization.GenericOptimization;
import org.cellularautomaton.optimization.Optimization;
import org.cellularautomaton.optimization.OptimizationStep;
import org.cellularautomaton.optimization.OptimizationType;
import org.cellularautomaton.optimization.PostApplyingOptimization;
import org.cellularautomaton.optimization.PostCalculationOptimization;
import org.cellularautomaton.optimization.PreApplyingOptimization;
import org.cellularautomaton.optimization.PreCalculationOptimization;
import org.cellularautomaton.space.ISpace;

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
	 * The space of cells this automaton work on.
	 */
	private final ISpace<StateType> cellSpace;

	/**
	 * The cells which have to be managed.
	 */
	private Collection<ICell<StateType>> cellsToManage = new HashSet<ICell<StateType>>();

	/**
	 * The optimizations to apply.
	 */
	private final Collection<Optimization<StateType>> optimizations = new HashSet<Optimization<StateType>>();

	/**
	 * Tell if the calculation has been done and if we are waiting for applying.
	 */
	private boolean isCalculationDone;

	/**
	 * Create an automaton on a specific space of cells.
	 * 
	 * @param cellSpace
	 *            the space of cells to work on
	 */
	public CellularAutomaton(ISpace<StateType> cellSpace) {
		this.cellSpace = cellSpace;
		cellsToManage.addAll(cellSpace.getAllCells());
	}

	/**
	 * Calculate the next state of each cell. After this step, the logical way
	 * is to apply it with {@link #applyNextStep()}.
	 */
	@SuppressWarnings("unchecked")
	public void calculateNextStep() {
		applyOptimizations((Class<? extends OptimizationStep<StateType>>) PreCalculationOptimization.class);

		for (ICell<StateType> cell : cellsToManage) {
			cell.calculateNextState();
		}
		isCalculationDone = true;

		applyOptimizations((Class<? extends OptimizationStep<StateType>>) PostCalculationOptimization.class);
	}

	/**
	 * Apply the previously calculated states of each cell. If some cells have
	 * not calculated yet, their state does not change.
	 */
	@SuppressWarnings("unchecked")
	public void applyNextStep() {
		applyOptimizations((Class<? extends OptimizationStep<StateType>>) PreApplyingOptimization.class);

		for (ICell<StateType> cell : cellsToManage) {
			cell.applyNextState();
		}
		isCalculationDone = false;

		applyOptimizations((Class<? extends OptimizationStep<StateType>>) PostApplyingOptimization.class);
	}

	/**
	 * Apply the the optimizations of a specific step.
	 * 
	 * @param step
	 */
	private void applyOptimizations(
			Class<? extends OptimizationStep<StateType>> step) {
		for (Optimization<StateType> optimization : getOptimizationsOf(step)) {
			if (optimization instanceof CellsSelectionOptimization) {
				cellsToManage = ((CellsSelectionOptimization<StateType>) optimization)
						.getCellsToManage();
			}
			if (optimization instanceof GenericOptimization) {
				((GenericOptimization<StateType>) optimization).execute();
			}
		}
	}

	/**
	 * This method is a simple getter for the optimizations, except that you can
	 * give a filter to get only specific optimizations. You can also give null
	 * to get all the optimizations.
	 * 
	 * @param filter
	 *            the class of the wanted optimizations (generally a child of
	 *            {@link OptimizationStep} or {@link OptimizationType})
	 * @return the optimizations corresponding to the given class
	 */
	public Collection<Optimization<StateType>> getOptimizationsOf(
			Class<? extends Optimization<StateType>> filter) {
		Collection<Optimization<StateType>> optimizations = new HashSet<Optimization<StateType>>();
		for (Optimization<StateType> optimization : this.optimizations) {
			if (filter == null || filter.isInstance(optimization)) {
				optimizations.add(optimization);
			}
		}
		return optimizations;
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
	 * @return the space of cells this automaton work on
	 */
	public ISpace<StateType> getSpace() {
		return cellSpace;
	}

	/**
	 * The cells which are managed by the automaton are basically all the cells
	 * of the space. It is also possible to have another set of cells, for
	 * example if some optimizations are used to restrain the cells to
	 * calculate.
	 * 
	 * @return the cells which have to be managed
	 */
	public Collection<ICell<StateType>> getCellsToManage() {
		return cellsToManage;
	}

	/**
	 * Add an optimization to the automaton.
	 * 
	 * @param optimization
	 *            the optimization to add
	 */
	public void addOptimization(Optimization<StateType> optimization) {
		if (!(optimization instanceof OptimizationStep)) {
			throw new IllegalArgumentException(
					"No step has been given for the optimization "
							+ optimization);
		} else if (!(optimization instanceof OptimizationType)) {
			throw new IllegalArgumentException(
					"No type has been given for the optimization "
							+ optimization);
		} else if (optimization.getAutomaton() != null) {
			throw new IllegalArgumentException(
					"Another automaton use the optimization " + optimization);
		} else {
			optimization.setAutomaton(this);
			optimizations.add(optimization);
		}
	}

	/**
	 * 
	 * @return True if the calculation has been done and we are waiting for
	 *         applying, false otherwise
	 */
	public boolean isReadyForApplying() {
		return isCalculationDone;
	}

	/**
	 * 
	 * @return True if we are at the beginning of a new step, false otherwise
	 */
	public boolean isReadyForCalculation() {
		return !isCalculationDone;
	}

}
