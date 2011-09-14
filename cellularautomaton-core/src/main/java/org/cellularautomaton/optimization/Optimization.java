package org.cellularautomaton.optimization;

import org.cellularautomaton.CellularAutomaton;

/**
 * <p>
 * An optimization is a way to improve the execution of a cellular automaton.
 * Each optimization has to implement at least one {@link OptimizationStep} and
 * at least one {@link OptimizationType}. See these interfaces for more details.
 * In general, an optimization should extend {@link AbstractOptimization} (which
 * implements the basic methods) and implement the needed step/type interfaces.
 * </p>
 * <p>
 * Using one or several optimizations depends of the the problem to solve and
 * the way it is solved, meaning that no optimization is objectively good. You
 * have to be careful about which optimization you use and when.
 * </p>
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface Optimization<StateType> {
	public void setAutomaton(CellularAutomaton<StateType> automaton);

	public CellularAutomaton<StateType> getAutomaton();
}
