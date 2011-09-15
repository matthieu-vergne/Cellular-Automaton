package org.cellularautomaton.optimization;

/**
 * This interface has to be implemented by the elements using optimizations.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <StateType>
 *            the type of data used by each cell, it can be {@link Boolean} for
 *            a simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses (just consider all the cells use the same
 *            type).
 */
public interface Optimizable<StateType> {
	/**
	 * Add an optimization to the automaton.
	 * 
	 * @param optimization
	 *            the optimization to add
	 */
	public void addOptimization(Optimization<StateType> optimization);

	/**
	 * Remove an optimization. Nothing happen if the automaton does not know the
	 * optimization.
	 * 
	 * @param optimization
	 *            the optimization to remove
	 */
	public void removeOptimization(Optimization<StateType> optimization);
}
