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

public class AutomatonPostCalculationOptimizationTest extends
		OptimizationStepTest {

	@Test
	public void testPostCalculation() {
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
				AutomatonPostCalculationOptimization<Integer>,
				GenericOptimization<CellularAutomaton<Integer>> {
			@Override
			public void execute() {
				assertFalse(getOwner().isReadyForCalculation());
				executed[0] = true;
			}
		}
		automaton.add(new Optimization());

		// test
		assertFalse(executed[0]);

		automaton.calculateNextStep();
		assertTrue(executed[0]);

		executed[0] = false;
		automaton.applyNextStep();
		assertFalse(executed[0]);

		automaton.calculateNextStep();
		assertTrue(executed[0]);

		executed[0] = false;
		automaton.applyNextStep();
		assertFalse(executed[0]);
	}

}
