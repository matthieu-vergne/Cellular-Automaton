package org.cellularautomaton.optimization.step;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.optimization.AbstractOptimization;
import org.cellularautomaton.optimization.type.GenericOptimization;
import org.cellularautomaton.space.builder.SpaceBuilder;
import org.cellularautomaton.state.AbstractStateFactory;
import org.junit.Test;

public class AutomatonPostApplyingOptimizationTest extends OptimizationStepTest {

	@Test
	public void testPostApplying() {
		// create automaton
		SpaceBuilder<Integer> builder = new SpaceBuilder<Integer>();
		builder.setStateFactory(new AbstractStateFactory<Integer>() {
			@Override
			public List<Integer> getPossibleStates() {
				return Arrays.asList(0);
			}
		}).createNewSpace().addDimension(2).addDimension(2).finalizeSpace();
		CellularAutomaton<Integer> automaton = new CellularAutomaton<Integer>(
				builder.getSpaceOfCell());

		// create optimization
		final boolean[] executed = new boolean[] { false };
		class Optimization extends
				AbstractOptimization<CellularAutomaton<Integer>> implements
				AutomatonPostApplyingOptimization<Integer>,
				GenericOptimization<CellularAutomaton<Integer>> {
			@Override
			public void execute() {
				assertFalse(getOwner().isReadyForApplying());
				executed[0] = true;
			}
		}
		automaton.add(new Optimization());
		
		// test
		assertFalse(executed[0]);
		
		automaton.calculateNextStep();
		assertFalse(executed[0]);
		
		automaton.applyNextStep();
		assertTrue(executed[0]);
		
		executed[0] = false;
		automaton.calculateNextStep();
		assertFalse(executed[0]);
		
		automaton.applyNextStep();
		assertTrue(executed[0]);
	}

}
