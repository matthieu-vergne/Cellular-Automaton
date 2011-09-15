package org.cellularautomaton.optimization;

import static org.junit.Assert.*;

import org.cellularautomaton.optimization.OptimizationManager.OptimizationExecutor;
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

	interface Type extends OptimizationType<Object> {
		public int execute(int input);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExecutor() {
		// create optimization
		final boolean[] executed = new boolean[1];
		executed[0] = false;
		class Optimization extends AbstractOptimization<Object> implements
				OptimizationStep<Object>, Type {
			@Override
			public int execute(int input) {
				executed[0] = true;
				return input;
			}

		}
		Optimization optimization = new Optimization();

		// test
		OptimizationManager<Object> manager = new OptimizationManager<Object>();
		manager.setOwner(new Object());
		manager.add(optimization);

		manager.setExecutor(Type.class, new OptimizationExecutor<Object>() {

			@Override
			public void execute(
					org.cellularautomaton.optimization.Optimization<Object> optimization) {
				assertEquals(1, ((Optimization) optimization).execute(1));
			}
		});

		assertFalse(executed[0]);
		manager.execute((Class<? extends OptimizationStep<Object>>) OptimizationStep.class);
		assertTrue(executed[0]);
	}
}
