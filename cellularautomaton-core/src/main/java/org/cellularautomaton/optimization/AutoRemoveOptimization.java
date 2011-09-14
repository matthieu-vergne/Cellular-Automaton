package org.cellularautomaton.optimization;

/**
 * This kind of optimization allows to know when the optimization has to be
 * automatically removed.
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
public interface AutoRemoveOptimization<StateType> extends
		OptimizationType<StateType> {

	/**
	 * This method must tell when the optimization should be removed from the
	 * automaton.
	 * 
	 * @return true if the optimization should be removed
	 */
	public boolean removeNow();
}
