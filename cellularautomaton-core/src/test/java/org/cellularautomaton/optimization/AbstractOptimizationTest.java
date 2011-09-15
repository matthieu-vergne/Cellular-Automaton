package org.cellularautomaton.optimization;

public abstract class AbstractOptimizationTest<OwnerType> extends
		OptimizationTest {

	public abstract AbstractOptimization<OwnerType> getAbstractOptimization();

	// nothing to test
}
