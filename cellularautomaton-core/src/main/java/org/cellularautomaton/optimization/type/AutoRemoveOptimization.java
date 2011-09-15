package org.cellularautomaton.optimization.type;

/**
 * This kind of optimization allows to know when the optimization has to be
 * automatically removed.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <OwnerType>
 *            the type of components the optimization should optimize.
 */
public interface AutoRemoveOptimization<OwnerType> extends
		OptimizationType<OwnerType> {

	/**
	 * This method must tell when the optimization should be removed from the
	 * automaton.
	 * 
	 * @return true if the optimization should be removed
	 */
	public boolean removeNow();
}
