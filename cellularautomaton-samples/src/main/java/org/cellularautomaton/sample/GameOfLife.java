package org.cellularautomaton.sample;

import java.util.HashMap;
import java.util.Map;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.GeneratorConfiguration;

public class GameOfLife extends SampleBase {
	protected static final int DEAD = 0;
	protected static final int ALIVE = 1;

	public static void main(String[] args) {
		new GameOfLife().run(300);
	}

	@Override
	protected CellularAutomaton<Integer> generateAutomaton() {
		final GeneratorConfiguration<Integer> config = new GeneratorConfiguration<Integer>() {

			@Override
			public Integer calculateForCell(final Cell<Integer> cell) {
				final boolean isAlive = cell.getCurrentState() == ALIVE;
				int aliveNeighbors = 0;
				for (final int[] coords : new int[][] { { -1, -1 }, { -1, 0 },
						{ -1, 1 }, { 0, -1 }, { 0, 1 }, { +1, -1 }, { +1, 0 },
						{ +1, 1 } }) {
					if (cell.getRelativeNeighbor(coords).getCurrentState() == ALIVE) {
						aliveNeighbors++;
					}
				}
				return isAlive && (aliveNeighbors == 2 || aliveNeighbors == 3)
						|| !isAlive && aliveNeighbors == 3 ? ALIVE : DEAD;
			}
		};
		config.dimensionSizes = getDimensions();
		config.initialState = DEAD;
		config.isCyclic = true;
		config.memorySize = 1;

		final CellularAutomaton<Integer> automaton = new CellularAutomaton<Integer>(
				config);

		final Cell<Integer> origin = automaton.getOriginCell();
		for (final int[] coords : new int[][] { { 2, 0 }, { 2, 1 }, { 1, 2 },
				{ 3, 1 }, { 3, 2 }, }) {
			origin.getRelativeNeighbor(coords).setCurrentState(ALIVE);
		}

		return automaton;
	}

	@Override
	protected Map<Integer, Character> getRepresentation() {
		Map<Integer, Character> map = new HashMap<Integer, Character>();
		map.put(DEAD, ' ');
		map.put(ALIVE, 'X');
		return map;
	}

	@Override
	protected int getXSize() {
		return 50;
	}

	@Override
	protected int getYSize() {
		return 10;
	}
}
