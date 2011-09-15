package org.cellularautomaton.optimization;

import static org.junit.Assert.*;

import org.cellularautomaton.optimization.step.OptimizationStep;
import org.cellularautomaton.optimization.type.OptimizationType;
import org.junit.Test;

public class OptimizationManagerTest extends OptimizableTest<Object> {

	@Override
	public Optimizable<Object> getOptimizable() {
		OptimizationManager<Object> manager = new OptimizationManager<Object>();
		manager.setOwner(new Object());
		return manager;
	}

	@Override
	public Optimization<Object> getRandomOptimization() {
		class Optimization extends AbstractOptimization<Object> implements
				OptimizationStep<Object>, OptimizationType<Object> {
		}
		return new Optimization();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
