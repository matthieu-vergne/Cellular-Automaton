package org.cellularautomaton.optimization;

/**
 * <p>
 * This interface is the parent interface of all the steps an optimization can
 * be linked to. In other words, implementing a child of this interface make the
 * optimization running during the corresponding step in the cellular automaton.
 * For example, if the optimization implements
 * {@link PreCalculationOptimization} , the optimization will be executed just
 * before the calculation step. Normally, children of this interface should not
 * ask any specific implementation, as it only identifies when the optimization
 * should be executed.
 * </p>
 * <p>
 * Several steps can be implemented in the same optimization, meaning the
 * optimization will be used several times during the process. <b>Be careful</b>
 * : an optimization implementing several steps is in charge to check which step
 * is the current one (if different behaviors have to be implemented). In this
 * case, it can be a good idea to make several optimizations (one for each step)
 * with each one its own behavior. Choose carefully regarding the coupling
 * (shared data) of your optimizations.
 * </p>
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface OptimizationStep<StateType> extends Optimization<StateType> {

}
