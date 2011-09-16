package org.cellularautomaton.optimization.step;

import org.cellularautomaton.CellularAutomaton;

/**
 * This interface allows to execute the optimization in the applying process of
 * the cellular automaton, just before the applying.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface AutomatonPreApplyingOptimization<StateType> extends
		OptimizationStep<CellularAutomaton<StateType>> {

}
