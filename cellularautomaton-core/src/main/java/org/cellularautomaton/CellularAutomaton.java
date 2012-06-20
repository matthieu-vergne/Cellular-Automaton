package org.cellularautomaton;

import java.util.Collection;
import java.util.HashSet;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.optimization.Optimizable;
import org.cellularautomaton.optimization.Optimization;
import org.cellularautomaton.optimization.OptimizationManager;
import org.cellularautomaton.optimization.OptimizationManager.OptimizationExecutor;
import org.cellularautomaton.optimization.step.AutomatonPostApplyingOptimization;
import org.cellularautomaton.optimization.step.AutomatonPostCalculationOptimization;
import org.cellularautomaton.optimization.step.AutomatonPreApplyingOptimization;
import org.cellularautomaton.optimization.step.AutomatonPreCalculationOptimization;
import org.cellularautomaton.optimization.step.OptimizationStep;
import org.cellularautomaton.optimization.type.AutomatonCellsSelectionOptimization;
import org.cellularautomaton.optimization.type.OptimizationType;
import org.cellularautomaton.space.ISpace;

import com.amd.aparapi.Kernel;
import com.amd.aparapi.Range;

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
public class CellularAutomaton<StateType> implements
		Optimizable<CellularAutomaton<StateType>> {

	/**
	 * The space of cells this automaton work on.
	 */
	private final ISpace<StateType> cellSpace;

	/**
	 * The cells which have to be managed.
	 */
	private Collection<ICell<StateType>> cellsToManage = new HashSet<ICell<StateType>>();

	/**
	 * The optimizations to use.
	 */
	private final OptimizationManager<CellularAutomaton<StateType>> optimizations = new OptimizationManager<CellularAutomaton<StateType>>();

	/**
	 * Tell if the calculation has been done and if we are waiting for applying.
	 */
	private boolean isCalculationDone;
	/**
	 * Tell if the APARAPI library has to be used.
	 */
	private boolean useAparapi = true;

	/**
	 * Create an automaton on a specific space of cells.
	 * 
	 * @param cellSpace
	 *            the space of cells to work on
	 */
	@SuppressWarnings("unchecked")
	public CellularAutomaton(ISpace<StateType> cellSpace) {
		this.cellSpace = cellSpace;
		cellsToManage.addAll(cellSpace.getAllCells());
		optimizations.setOwner(this);
		optimizations
				.setExecutor(
						(Class<? extends OptimizationType<CellularAutomaton<StateType>>>) AutomatonCellsSelectionOptimization.class,
						new OptimizationExecutor<CellularAutomaton<StateType>>() {

							@Override
							public void execute(
									Optimization<CellularAutomaton<StateType>> optimization) {
								cellsToManage = ((AutomatonCellsSelectionOptimization<StateType>) optimization)
										.getCellsToManage();
							}

						});
	}

	/**
	 * Calculate the next state of each cell. After this step, the logical way
	 * is to apply it with {@link #applyNextStep()}.
	 */
	@SuppressWarnings("unchecked")
	public void calculateNextStep() {
		optimizations
				.execute((Class<? extends OptimizationStep<CellularAutomaton<StateType>>>) AutomatonPreCalculationOptimization.class);

		if (isAparapiUsed()) {
			final Object[] cells = cellsToManage.toArray();
			Kernel kernel = new Kernel() {
				@Override
				public void run() {
					int i = getGlobalId();
					((ICell<StateType>) cells[i]).calculateNextState();
				}
			};
			Range range = Range.create(cells.length);
			kernel.execute(range);
		} else {
			for (ICell<StateType> cell : cellsToManage) {
				cell.calculateNextState();
			}
		}
		isCalculationDone = true;

		optimizations
				.execute((Class<? extends OptimizationStep<CellularAutomaton<StateType>>>) AutomatonPostCalculationOptimization.class);
	}

	/**
	 * Apply the previously calculated states of each cell. If some cells have
	 * not calculated yet, their state does not change.
	 */
	@SuppressWarnings("unchecked")
	public void applyNextStep() {
		optimizations
				.execute((Class<? extends OptimizationStep<CellularAutomaton<StateType>>>) AutomatonPreApplyingOptimization.class);

		for (ICell<StateType> cell : cellsToManage) {
			cell.applyNextState();
		}
		isCalculationDone = false;

		optimizations
				.execute((Class<? extends OptimizationStep<CellularAutomaton<StateType>>>) AutomatonPostApplyingOptimization.class);
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
	public void add(Optimization<CellularAutomaton<StateType>> optimization) {
		optimizations.add(optimization);
	}

	/**
	 * Remove an optimization. Nothing happen if the automaton does not know the
	 * optimization.
	 * 
	 * @param optimization
	 *            the optimization to remove
	 */
	public void remove(Optimization<CellularAutomaton<StateType>> optimization) {
		optimizations.remove(optimization);
	}

	/**
	 * Check the automaton knows about a given optimization.
	 */
	@Override
	public boolean contains(
			Optimization<CellularAutomaton<StateType>> optimization) {
		return optimizations.contains(optimization);
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

	public boolean isAparapiUsed() {
		return useAparapi;
	}

	public void setAparapiUsed(boolean useAparapi) {
		this.useAparapi = useAparapi;
	}

}
