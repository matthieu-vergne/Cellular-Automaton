package org.cellularautomaton.sample.gameoflife;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.builder.CellSpaceBuilder;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;

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
		
		CellSpaceBuilder<GameOfLifeState> builder = new CellSpaceBuilder<GameOfLifeState>();
		builder.setInitialState(GameOfLifeState.DEAD).setMemorySize(1).setRule(rule);
		builder.createNewSpace(2).addDimension(40).addDimension(50);

		CellularAutomaton<GameOfLifeState> automaton = new CellularAutomaton<GameOfLifeState>(
				builder.getSpaceOfCellOrigin());

		final ICell<GameOfLifeState> origin = automaton.getOriginCell();
		for (final int[] coords : new int[][] { { 2, 0 }, { 2, 1 }, { 1, 2 },
				{ 3, 1 }, { 3, 2 }, }) {
			origin.getRelativeCell(coords).setCurrentState(
					GameOfLifeState.ALIVE);
		}

		return automaton;
	}

}
