package org.cellularautomaton.optimization;

/**
 * This interface allows to execute the optimization in the calculation process,
 * just after the calculation.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface PostCalculationOptimization<StateType> extends
		OptimizationStep<StateType> {

}
