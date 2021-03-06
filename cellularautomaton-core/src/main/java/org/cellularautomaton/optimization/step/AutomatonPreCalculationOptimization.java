package org.cellularautomaton.optimization.step;

import org.cellularautomaton.CellularAutomaton;

/**
 * This interface allows to execute the optimization in the calculation process
 * of the cellular automaton, just before the calculation.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface AutomatonPreCalculationOptimization<StateType> extends
		OptimizationStep<CellularAutomaton<StateType>> {

}
