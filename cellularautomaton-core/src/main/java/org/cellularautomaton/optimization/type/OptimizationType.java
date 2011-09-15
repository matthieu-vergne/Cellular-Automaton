package org.cellularautomaton.optimization.type;

import org.cellularautomaton.optimization.Optimization;

/**
 * <p>
 * An optimization type defines the action to execute in order to optimize its
 * owner. Each possible optimization process corresponds to a specific type,
 * which is a child of this interface. All the type interfaces should give a
 * specific method to implement, depending of the action to execute. For
 * example, {@link AutomatonCellsSelectionOptimization} ask to implement a method giving
 * the cells to manage in the automaton.
 * </p>
 * <p>
 * It is possible to implement several types in the same optimization, but it is
 * not recommended. In particular, the order of each process is not guaranteed
 * (and can change during the execution of the automaton), so each process has
 * to be relatively independent. If you need to implement several types in the
 * same optimization, be sure to keep this point in mind.
 * </p>
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <OwnerType>
 *            the type of components the optimization should optimize.
 */
public interface OptimizationType<OwnerType> extends Optimization<OwnerType> {

}
