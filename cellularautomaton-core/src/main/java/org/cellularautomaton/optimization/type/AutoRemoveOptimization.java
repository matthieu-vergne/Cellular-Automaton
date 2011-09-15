package org.cellularautomaton.optimization.type;

import org.cellularautomaton.optimization.OptimizationManager;

/**
 * This kind of optimization allows to know when the optimization has to be
 * automatically removed. It does not mean this optimization <u>will</u> be
 * removed, whatever you do with it : the owner of such optimization is in
 * charge to take care of the properties of such optimization, in order to know
 * when it has to remove it. The {@link OptimizationManager} in particular is
 * able to manage such optimizations.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <OwnerType>
 *            the type of components the optimization should optimize.
 */
public interface AutoRemoveOptimization<OwnerType> extends
		OptimizationType<OwnerType> {

	/**
	 * This method must tell when the optimization should be removed from its
	 * owner.
	 * 
	 * @return true if the optimization should be removed
	 */
	public boolean removeNow();
}
