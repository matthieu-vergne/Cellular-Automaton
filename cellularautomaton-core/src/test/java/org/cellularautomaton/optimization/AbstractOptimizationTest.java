package org.cellularautomaton.optimization;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class AbstractOptimizationTest<OwnerType> extends OptimizationTest {
	
	public abstract AbstractOptimization<OwnerType> getAbstractOptimization();
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
