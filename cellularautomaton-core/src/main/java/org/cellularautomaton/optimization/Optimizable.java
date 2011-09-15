package org.cellularautomaton.optimization;

/**
 * This interface has to be implemented by the elements using optimizations. A
 * good way to do is to implement this interface and to use a
 * {@link OptimizationManager} to manage it.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <OwnerType>
 *            the type of components the optimization should optimize.
 */
public interface Optimizable<OwnerType> {
	/**
	 * 
	 * @param optimization
	 *            the optimization to add
	 */
	public void add(Optimization<OwnerType> optimization);

	/**
	 * @param optimization
	 *            the optimization to remove
	 */
	public void remove(Optimization<OwnerType> optimization);

	/**
	 * @param optimization
	 *            the optimization to look for
	 * @return true if the owner know the given optimization, false otherwise
	 */
	public boolean contains(Optimization<OwnerType> optimization);
}
