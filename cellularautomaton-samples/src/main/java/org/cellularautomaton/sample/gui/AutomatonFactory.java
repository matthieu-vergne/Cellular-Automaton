package org.cellularautomaton.sample.gui;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.GeneratorConfiguration;

public class AutomatonFactory {
	
	public static CellularAutomaton<GameOfLifeState> createGameOfLifeAutomaton(int height, int width, boolean isCyclic){
		final GeneratorConfiguration<GameOfLifeState> config = new GeneratorConfiguration<GameOfLifeState>() {

			@Override
			public GameOfLifeState calculateForCell(final Cell<GameOfLifeState> cell) {
				final boolean isAlive = cell.getCurrentState() == GameOfLifeState.ALIVE;
				int aliveNeighbors = 0;
				for (final int[] coords : new int[][] { { -1, -1 }, { -1, 0 },
						{ -1, 1 }, { 0, -1 }, { 0, 1 }, { +1, -1 }, { +1, 0 },
						{ +1, 1 } }) {
					if (cell.getRelativeNeighbor(coords).getCurrentState() == GameOfLifeState.ALIVE) {
						aliveNeighbors++;
					}
				}
				return isAlive && (aliveNeighbors == 2 || aliveNeighbors == 3)
						|| !isAlive && aliveNeighbors == 3 ? GameOfLifeState.ALIVE : GameOfLifeState.DEAD;
			}
		};
		config.dimensionSizes = new int[]{width,height};
		config.initialState = GameOfLifeState.DEAD;
		config.isCyclic = isCyclic;
		config.memorySize = 1;	
		return new CellularAutomaton<GameOfLifeState>(config);
	}
	
	public static CellularAutomaton<WireWorldState> createWireWorldAutomaton(
			int height, int width, boolean isCyclic) {
		final GeneratorConfiguration<WireWorldState> config = new GeneratorConfiguration<WireWorldState>() {

			@Override
			public WireWorldState calculateForCell(
					final Cell<WireWorldState> cell) {
				switch (cell.getCurrentState()) {
				case EMPTY:
					return WireWorldState.EMPTY;
				case METAL:
					int heads = 0;
					for (final int[] coords : new int[][] { { -1, -1 },
							{ -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 },
							{ +1, -1 }, { +1, 0 }, { +1, 1 } }) {
						if (cell.getRelativeNeighbor(coords).getCurrentState() == WireWorldState.HEAD) {
							heads++;
						}
					}
					return heads == 1 || heads == 2 ? WireWorldState.HEAD
							: WireWorldState.METAL;
				case HEAD:
					return WireWorldState.QUEUE;
				case QUEUE:
					return WireWorldState.METAL;
				default:
					throw new IllegalStateException("undefined case : "
							+ cell.getCurrentState());
				}
			}
		};
		config.dimensionSizes = new int[] { width, height };
		config.initialState = WireWorldState.EMPTY;
		config.isCyclic = isCyclic;
		config.memorySize = 1;
		return new CellularAutomaton<WireWorldState>(config);
	}
}