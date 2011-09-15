package org.cellularautomaton.sample.gameoflife;

import java.util.Arrays;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.space.builder.SpaceBuilder;
import org.cellularautomaton.state.EnumStateFactory;
import org.cellularautomaton.state.IStateFactory;
import org.cellularautomaton.util.Coords;

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

			private final List<Coords> aliveCoords = Arrays
					.asList(new Coords[] { new Coords(2, 0), new Coords(2, 1),
							new Coords(1, 2), new Coords(3, 1),
							new Coords(3, 2), });

			@Override
			public void customize(ICell<GameOfLifeState> cell) {
				if (aliveCoords.contains(cell.getCoords())) {
					cell.setCurrentState(GameOfLifeState.ALIVE);
				}
			}
		};

		SpaceBuilder<GameOfLifeState> builder = new SpaceBuilder<GameOfLifeState>();
		builder.setStateFactory(stateFactory).setMemorySize(1).setRule(rule);
		builder.createNewSpace().addDimension(40).addDimension(50);

		CellularAutomaton<GameOfLifeState> automaton = new CellularAutomaton<GameOfLifeState>(
				builder.getSpaceOfCell());

		return automaton;
	}
}
