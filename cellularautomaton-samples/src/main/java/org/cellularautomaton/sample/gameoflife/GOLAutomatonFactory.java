package org.cellularautomaton.sample.gameoflife;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.GeneratorConfiguration;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;

public class GOLAutomatonFactory {

	public static CellularAutomaton<GameOfLifeState> createAutomaton() {
		final GeneratorConfiguration<GameOfLifeState> config = new GeneratorConfiguration<GameOfLifeState>();
		config.rule = new IRule<GameOfLifeState>() {

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
		config.dimensionSizes = new int[] { 40, 50 };
		config.initialState = GameOfLifeState.DEAD;
		config.isCyclic = true;
		config.memorySize = 1;

		CellularAutomaton<GameOfLifeState> automaton = new CellularAutomaton<GameOfLifeState>(
				config);

		final ICell<GameOfLifeState> origin = automaton.getOriginCell();
		for (final int[] coords : new int[][] { { 2, 0 }, { 2, 1 }, { 1, 2 },
				{ 3, 1 }, { 3, 2 }, }) {
			origin.getRelativeCell(coords).setCurrentState(
					GameOfLifeState.ALIVE);
		}

		return automaton;
	}

}
