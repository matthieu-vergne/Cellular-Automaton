package org.cellularautomaton.optimization.implemented;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.optimization.type.AutomatonCellsSelectionOptimizationTest;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.space.builder.SpaceBuilder;
import org.cellularautomaton.state.AbstractStateFactory;
import org.junit.Test;

public class CalculateOnlyEvolvingZonesOptimizationTest extends
		AutomatonCellsSelectionOptimizationTest {

	@Test
	public void testCalculateOnlyEvolvingZones() {
		// create automaton
		SpaceBuilder<Integer> builder = new SpaceBuilder<Integer>();
		builder.setStateFactory(new AbstractStateFactory<Integer>() {
			@Override
			public List<Integer> getPossibleStates() {
				return Arrays.asList(0);
			}
		}).setRule(new IRule<Integer>() {
			
			@Override
			public Integer calculateNextStateOf(ICell<Integer> cell) {
				return 0;
			}
		}).createNewSpace().addDimension(5).addDimension(5).finalizeSpace();
		CellularAutomaton<Integer> automaton = new CellularAutomaton<Integer>(builder.getSpaceOfCell());
		
		// create optimization
		automaton.add(new CalculateOnlyEvolvingZonesOptimization<Integer>() {
			@Override
			protected Collection<ICell<Integer>> getCellsDependingTo(
					ICell<Integer> cell) {
				return cell.getAllCellsAround();
			}
		});
		
		// test
		ICell<Integer> origin = automaton.getSpace().getOrigin();
		Set<ICell<Integer>> around = origin.getAllCellsAround();
		Collection<ICell<Integer>> allCells = automaton.getSpace().getAllCells();
		
		assertTrue(automaton.getCellsToManage().containsAll(allCells));
		assertTrue(allCells.containsAll(automaton.getCellsToManage()));
		
		origin.setCurrentState(1);
		automaton.calculateNextStep();
		automaton.applyNextStep();
		assertTrue(automaton.getCellsToManage().containsAll(around));
		assertTrue(around.containsAll(automaton.getCellsToManage()));
		
		automaton.calculateNextStep();
		automaton.applyNextStep();
		assertTrue(automaton.getCellsToManage().isEmpty());
	}

}
