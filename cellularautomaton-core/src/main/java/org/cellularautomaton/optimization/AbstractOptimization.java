package org.cellularautomaton.optimization;

/**
 * This is the basic implementation of the {@link Optimization} interface. Any
 * optimization should extend this abstract class and add the needed interfaces
 * to indicate the steps and types of the optimization.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <OwnerType>
 *            the type of components the optimization should optimize.
 */
public abstract class AbstractOptimization<OwnerType> implements
		Optimization<OwnerType> {
	private OwnerType owner;

	public void setOwner(OwnerType owner) {
		this.owner = owner;
	}

	public OwnerType getOwner() {
		return owner;
	}
}
