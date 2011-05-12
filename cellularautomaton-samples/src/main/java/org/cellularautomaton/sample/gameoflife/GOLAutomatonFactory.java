package org.cellularautomaton.sample.gameoflife;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.builder.CellSpaceBuilder;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.definition.IStateFactory;
import org.cellularautomaton.impl.EnumStateFactory;

public class GOLAutomatonFactory {

	public static CellularAutomaton<GameOfLifeState> createAutomaton() {
		IRule<GameOfLifeState> rule = new IRule<GameOfLifeState>() {

			public GameOfLifeState calculateNextStateOf(
					ICell<GameOfLifeState> cell) {
				final boolean isAlive = cell.getCurrentState() == GameOfLifeState.ALIVE;
				int aliveNeighbors = 0;
				for (final int[] coords : new int[][] { { -1, -1 }, { -1, 0 },
						{ -1, 1 }, { 0, -1 }, { 0, 1 }, { +1, -1 }, { +1, 0 },
						{ +1, 1 } }) {
					if (cell.getRelativeCell(coords).getCurrentState() == GameOfLifeState.ALIVE) {
						aliveNeighbors++;
					}
				}
				return isAlive && (aliveNeighbors == 2 || aliveNeighbors == 3)
						|| !isAlive && aliveNeighbors == 3 ? GameOfLifeState.ALIVE
						: GameOfLifeState.DEAD;
			}
		};
		
		// TODO use this factory to set the initial states of the cells
		IStateFactory<GameOfLifeState> stateFactory = new EnumStateFactory<GameOfLifeState>() {
			@Override
			public Class<GameOfLifeState> getEnumType() {
				return GameOfLifeState.class;
			}
			
			/**
			 * The initial state of all the cells is dead.
			 */
			@Override
			public GameOfLifeState getDefaultState() {
				return GameOfLifeState.DEAD;
			}
		};
		
		CellSpaceBuilder<GameOfLifeState> builder = new CellSpaceBuilder<GameOfLifeState>();
		builder.setStateFactory(stateFactory).setMemorySize(1).setRule(rule);
		builder.createNewSpace(2).addDimension(40).addDimension(50);

		CellularAutomaton<GameOfLifeState> automaton = new CellularAutomaton<GameOfLifeState>(
				builder.getSpaceOfCell());

		final ICell<GameOfLifeState> origin = automaton.getCellSpace().getOrigin();
		for (final int[] coords : new int[][] { { 2, 0 }, { 2, 1 }, { 1, 2 },
				{ 3, 1 }, { 3, 2 }, }) {
			origin.getRelativeCell(coords).setCurrentState(
					GameOfLifeState.ALIVE);
		}

		return automaton;
	}

}
