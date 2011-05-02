package org.cellularautomaton.sample.gui;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.GeneratorConfiguration;

public class AutomatonFactory {

	public static CellularAutomaton<GameOfLifeState> createGameOfLifeAutomaton(
			int height, int width, boolean isCyclic) {
		final GeneratorConfiguration<GameOfLifeState> config = new GeneratorConfiguration<GameOfLifeState>() {

			@Override
			public GameOfLifeState calculateForCell(
					final Cell<GameOfLifeState> cell) {
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
						|| !isAlive && aliveNeighbors == 3 ? GameOfLifeState.ALIVE
						: GameOfLifeState.DEAD;
			}
		};
		config.dimensionSizes = new int[] { width, height };
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

	public static CellularAutomaton<AntState> createAntAutomaton(int height,
			int width, boolean cyclic) {
		final GeneratorConfiguration<AntState> config = new GeneratorConfiguration<AntState>() {

			@Override
			public AntState calculateForCell(final Cell<AntState> cell) {
				switch (cell.getCurrentState()) {
				case BLACK:
				case WHITE:
					// Ant coming from left
					AntState neighbor = cell.getRelativeNeighbor(-1,0).getCurrentState();
					if(neighbor == AntState.ANT_BLACK_RIGHT || neighbor == AntState.ANT_WHITE_RIGHT) {
						if(cell.getCurrentState() == AntState.BLACK){
							return AntState.ANT_WHITE_UP;
						}else{
							return AntState.ANT_BLACK_DOWN;
						}
					}
					// Ant coming from right
					neighbor = cell.getRelativeNeighbor(1,0).getCurrentState();
					if(neighbor == AntState.ANT_BLACK_LEFT || neighbor == AntState.ANT_WHITE_LEFT) {
						if(cell.getCurrentState() == AntState.BLACK){
							return AntState.ANT_WHITE_DOWN;
						}else{
							return AntState.ANT_BLACK_UP;
						}
					}
					
					// Ant coming from up
					neighbor = cell.getRelativeNeighbor(0,-1).getCurrentState();
					if(neighbor == AntState.ANT_BLACK_DOWN || neighbor == AntState.ANT_WHITE_DOWN) {
						if(cell.getCurrentState() == AntState.BLACK){
							return AntState.ANT_WHITE_RIGHT;
						}else{
							return AntState.ANT_BLACK_LEFT;
						}
					}
					// Ant coming from down
					neighbor = cell.getRelativeNeighbor(0,1).getCurrentState();
					if(neighbor == AntState.ANT_BLACK_UP || neighbor == AntState.ANT_WHITE_UP) {
						if(cell.getCurrentState() == AntState.BLACK){
							return AntState.ANT_WHITE_LEFT;
						}else{
							return AntState.ANT_BLACK_RIGHT;
						}
					}
					
					return cell.getCurrentState();
				case ANT_BLACK_DOWN:
				case ANT_BLACK_LEFT:
				case ANT_BLACK_RIGHT:
				case ANT_BLACK_UP:
					return AntState.BLACK;
				case ANT_WHITE_DOWN:
				case ANT_WHITE_LEFT:
				case ANT_WHITE_RIGHT:
				case ANT_WHITE_UP:
					return AntState.WHITE;
				default:
					throw new IllegalArgumentException("Unknown state : "
							+ cell.getCurrentState());
				}
			}
		};
		config.dimensionSizes = new int[] { width, height };
		config.initialState = AntState.BLACK;
		config.isCyclic = cyclic;
		config.memorySize = 1;
		return new CellularAutomaton<AntState>(config);
	}
}
