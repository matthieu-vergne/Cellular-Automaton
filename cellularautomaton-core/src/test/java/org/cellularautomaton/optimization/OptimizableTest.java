package org.cellularautomaton.optimization;

public abstract class OptimizableTest<OwnerType> {
	public abstract Optimizable<OwnerType> getOptimizable();

	public abstract Optimization<OwnerType> getRandomOptimization();

}
