package org.cellularautomaton.optimization.type;

import static org.junit.Assert.*;

import org.cellularautomaton.optimization.AbstractOptimization;
import org.cellularautomaton.optimization.OptimizationManager;
import org.cellularautomaton.optimization.step.OptimizationStep;
import org.junit.Test;

public class AutoRemoveOptimizationTest extends OptimizationTypeTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testAutoRemoving() {
		final boolean[] remove = new boolean[]{false};
		final boolean[] executed = new boolean[]{false};
		class Optimization extends AbstractOptimization<Object> implements OptimizationStep<Object>, AutoRemoveOptimization<Object>, GenericOptimization<Object> {
			@Override
			public void execute() {
				executed[0] = true;
				if (remove[0]) {
					fail("The optimization has not been removed");
				}
			}
			
			@Override
			public boolean removeNow() {
				return remove[0];
			}
		}
		
		OptimizationManager<Object> manager = new OptimizationManager<Object>();
		manager.setOwner(new Object());
		Optimization optimization = new Optimization();
		manager.add(optimization);
		
		assertTrue(manager.contains(optimization));
		manager.execute((Class<? extends OptimizationStep<Object>>) OptimizationStep.class);
		assertTrue(executed[0]);
		
		remove[0] = true;
		executed[0] = false;
		assertFalse(manager.contains(optimization));
		manager.execute((Class<? extends OptimizationStep<Object>>) OptimizationStep.class);
		assertFalse(executed[0]);
		
		remove[0] = false;
		executed[0] = false;
		manager.add(optimization);
		manager.execute((Class<? extends OptimizationStep<Object>>) OptimizationStep.class);
		assertTrue(executed[0]);
		assertTrue(manager.contains(optimization));
		
		remove[0] = true;
		executed[0] = false;
		manager.execute((Class<? extends OptimizationStep<Object>>) OptimizationStep.class);
		assertFalse(executed[0]);
		assertFalse(manager.contains(optimization));
	}

}
