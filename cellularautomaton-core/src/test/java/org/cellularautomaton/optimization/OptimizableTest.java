package org.cellularautomaton.optimization;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class OptimizableTest<OwnerType> {
	public abstract Optimizable<OwnerType> getOptimizable();

	public abstract Optimization<OwnerType> getRandomOptimization();

	@Test
	public void testOptimizable() {
		Optimizable<OwnerType> optimizable = getOptimizable();
		assertNotNull(optimizable);

		Optimization<OwnerType> optimization = getRandomOptimization();
		assertNotNull(optimization);

		assertFalse(optimizable.contains(optimization));

		optimizable.add(optimization);
		assertTrue(optimizable.contains(optimization));

		optimizable.remove(optimization);
		assertFalse(optimizable.contains(optimization));
	}
}
