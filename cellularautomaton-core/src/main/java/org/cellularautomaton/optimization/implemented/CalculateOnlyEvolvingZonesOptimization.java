package org.cellularautomaton.optimization.implemented;

import java.util.Collection;
import java.util.HashSet;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.optimization.AbstractOptimization;
import org.cellularautomaton.optimization.step.AutomatonPostApplyingOptimization;
import org.cellularautomaton.optimization.step.AutomatonPostCalculationOptimization;
import org.cellularautomaton.optimization.type.AutomatonCellsSelectionOptimization;

/**
 * <p>
 * This optimization allows to limit the calculation process to the cells which
 * have an evolving neighborhood. In other words, each cell being linked to a
 * set of neighbors, if one or several neighbors have a new state, the cells
 * depending of them will be calculated during the next step.
 * </p>
 * <p>
 * It is a big optimization for the spaces where only a little part of the space
 * evolve at a time. One of the inconvenient is that the memory of the cells is
 * not consistent (if the cell keeps several previous states in memory), because
 * the cells which are not managed stop to evolve (their memory is frozen too).
 * </p>
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public abstract class CalculateOnlyEvolvingZonesOptimization<StateType> extends
		AbstractOptimization<CellularAutomaton<StateType>> implements
		AutomatonPostCalculationOptimization<StateType>,
		AutomatonPostApplyingOptimization<StateType>,
		AutomatonCellsSelectionOptimization<StateType> {

	private Collection<ICell<StateType>> cellsToCalculate = new HashSet<ICell<StateType>>();
	private Collection<ICell<StateType>> cellsToApply = new HashSet<ICell<StateType>>();
	private boolean isFirstOccurence = true;

	public Collection<ICell<StateType>> getCellsToManage() {
		if (isFirstOccurence) {
			cellsToCalculate.addAll(getOwner().getSpace().getAllCells());
			isFirstOccurence = false;
		}

		if (getOwner().isReadyForCalculation()) {
			return cellsToCalculate;
		} else if (getOwner().isReadyForApplying()) {
			Collection<ICell<StateType>> temp = cellsToCalculate;
			cellsToCalculate = cellsToApply;
			cellsToApply = temp;

			cellsToCalculate.clear();
			for (ICell<StateType> cell : cellsToApply) {
				if (cell.isNextStateDifferent()) {
					cellsToCalculate.addAll(getCellsDependingTo(cell));
				}
			}
			return cellsToApply;
		} else {
			throw new RuntimeException("This case should not occur.");
		}
	}

	/**
	 * <p>
	 * If a cell see its state modified during the current step, this method
	 * should give all the cells which depend on it. A basic implementation can
	 * be to return the direct neighbors :
	 * 
	 * <pre>
	 * protected Collection&lt;...&gt; getCellsDependingTo(ICell&lt;...&gt; cell) {
	 * 	return cell.getAllCellsAround();
	 * }
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param cell
	 *            the cell to consider the dependencies with
	 * @return the cells which depends of the cell given in argument
	 */
	protected abstract Collection<ICell<StateType>> getCellsDependingTo(
			ICell<StateType> cell);

}
