package org.cellularautomaton.optimization;

/**
 * <p>
 * An optimization type defines the action to execute in order to optimize the
 * cellular automaton. Each possible optimization process corresponds to a
 * specific type, which is a child of this interface. All the type interfaces
 * should give a specific method to implement, depending of the action to
 * execute. For example, {@link CellsSelectionOptimization} ask to implement a
 * method giving the cells to select.
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
 */
public interface OptimizationType<StateType> extends Optimization<StateType> {

}
