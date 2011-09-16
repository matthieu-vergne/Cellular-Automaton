package org.cellularautomaton.optimization.step;

import org.cellularautomaton.optimization.Optimization;

/**
 * <p>
 * This interface is the parent interface of all the steps an optimization can
 * be linked to. In other words, implementing a child of this interface make the
 * optimization running during the corresponding step in the owner process. For
 * example, if the optimization implements {@link AutomatonPreCalculationOptimization} ,
 * the optimization will be executed just before the calculation step in the
 * cellular automaton. Normally, children of this interface should not ask any
 * specific implementation, as it only identifies when the optimization should
 * be executed.
 * </p>
 * <p>
 * Several steps can be implemented in the same optimization, meaning the
 * optimization will be executed at several moments. <b>Be careful</b> : an
 * optimization implementing several steps is in charge to check which step is
 * the current one (if different behaviors have to be implemented). In this
 * case, it can be a good idea to make several optimizations (one for each step)
 * with each one its own behavior. Choose carefully regarding the coupling
 * (shared data) of your optimizations.
 * </p>
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <OwnerType>
 *            the type of components the optimization should optimize.
 */
public interface OptimizationStep<OwnerType> extends Optimization<OwnerType> {

}
