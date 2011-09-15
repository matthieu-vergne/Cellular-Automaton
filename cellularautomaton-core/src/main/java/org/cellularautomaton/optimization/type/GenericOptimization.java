package org.cellularautomaton.optimization.type;

/**
 * This optimization is used for whatever you need which does not interact with
 * the internal components of the owner (as nothing is given in argument and no
 * output is used by the owner).
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <OwnerType>
 *            the type of components the optimization should optimize.
 */
public interface GenericOptimization<OwnerType> extends
		OptimizationType<OwnerType> {

	public void execute();
}
