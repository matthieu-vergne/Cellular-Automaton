package org.cellularautomaton.optimization.type;

import java.util.Collection;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;

/**
 * This optimization is used to know which cells have to be managed by the
 * cellular automaton. Basically, the automaton manage all the cells of its
 * space, but you can, for example, select a reduced set of cells to manage in
 * order to decrease the execution time.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface AutomatonCellsSelectionOptimization<StateType> extends
		OptimizationType<CellularAutomaton<StateType>> {

	public Collection<ICell<StateType>> getCellsToManage();
}
