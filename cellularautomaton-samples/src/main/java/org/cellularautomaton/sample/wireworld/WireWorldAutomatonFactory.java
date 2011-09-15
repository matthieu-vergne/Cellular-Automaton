package org.cellularautomaton.sample.wireworld;

import java.util.Arrays;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.space.builder.SpaceBuilder;
import org.cellularautomaton.state.EnumStateFactory;
import org.cellularautomaton.state.IStateFactory;
import org.cellularautomaton.util.Coords;

public class WireWorldAutomatonFactory {

	public static CellularAutomaton<WireWorldState> createAutomaton() {
		IRule<WireWorldState> rule = new IRule<WireWorldState>() {
			public WireWorldState calculateNextStateOf(
					ICell<WireWorldState> cell) {
				switch (cell.getCurrentState()) {
				case EMPTY:
					return WireWorldState.EMPTY;
				case METAL:
					int heads = 0;
					for (final int[] coords : new int[][] { { -1, -1 },
							{ -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 },
							{ +1, -1 }, { +1, 0 }, { +1, 1 } }) {
						if (cell.getRelativeCell(coords).getCurrentState() == WireWorldState.HEAD) {
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

		IStateFactory<WireWorldState> stateFactory = new EnumStateFactory<WireWorldState>() {
			@Override
			public Class<WireWorldState> getEnumType() {
				return WireWorldState.class;
			}

			/**
			 * The most used state of the wireworld automaton is empty.
			 */
			@Override
			public WireWorldState getDefaultState() {
				return WireWorldState.EMPTY;
			}

			private final List<Coords> metalCoords = Arrays
					.asList(new Coords[] { new Coords(2, 0), new Coords(2, 1),
							new Coords(2, 2), new Coords(2, 3),
							new Coords(2, 4), new Coords(1, 5),
							new Coords(3, 5), new Coords(1, 6),
							new Coords(2, 6), new Coords(3, 6),
							new Coords(2, 7), new Coords(2, 8),
							new Coords(2, 9),

							new Coords(7, 0), new Coords(7, 1),
							new Coords(7, 2), new Coords(7, 3),
							new Coords(7, 4), new Coords(6, 5),
							new Coords(7, 5), new Coords(8, 5),
							new Coords(6, 6), new Coords(8, 6),
							new Coords(7, 7), new Coords(7, 8),
							new Coords(7, 9),

							new Coords(1, 10), new Coords(1, 11),
							new Coords(1, 12), new Coords(1, 13),
							new Coords(3, 10), new Coords(3, 11),
							new Coords(3, 12), new Coords(3, 13),
							new Coords(2, 14),

							new Coords(6, 10), new Coords(6, 11),
							new Coords(6, 12), new Coords(6, 13),
							new Coords(8, 10), new Coords(8, 11),
							new Coords(8, 12), new Coords(8, 13),
							new Coords(7, 14), });

			private final List<Coords> headCoords = Arrays.asList(new Coords[] {
					new Coords(2, 14), new Coords(7, 14) });

			private final List<Coords> queueCoords = Arrays
					.asList(new Coords[] { new Coords(3, 13), new Coords(8, 13) });

			@Override
			public void customize(ICell<WireWorldState> cell) {
				if (headCoords.contains(cell.getCoords())) {
					cell.setCurrentState(WireWorldState.HEAD);
				} else if (queueCoords.contains(cell.getCoords())) {
					cell.setCurrentState(WireWorldState.QUEUE);
				} else if (metalCoords.contains(cell.getCoords())) {
					cell.setCurrentState(WireWorldState.METAL);
				}
			}
		};

		SpaceBuilder<WireWorldState> builder = new SpaceBuilder<WireWorldState>();
		builder.setStateFactory(stateFactory).setRule(rule);
		builder.createNewSpace().addDimension(10).addDimension(20);

		CellularAutomaton<WireWorldState> automaton = new CellularAutomaton<WireWorldState>(
				builder.getSpaceOfCell());

		return automaton;
	}
}
