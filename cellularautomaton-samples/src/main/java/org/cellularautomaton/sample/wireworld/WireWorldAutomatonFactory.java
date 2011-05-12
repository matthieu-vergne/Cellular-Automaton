package org.cellularautomaton.sample.wireworld;

import java.util.Arrays;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.space.SpaceBuilder;
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
					.asList(new Coords[] { new Coords(0, 2), new Coords(1, 2),
							new Coords(2, 2), new Coords(3, 2),
							new Coords(4, 2), new Coords(5, 1),
							new Coords(5, 3), new Coords(6, 1),
							new Coords(6, 2), new Coords(6, 3),
							new Coords(7, 2), new Coords(8, 2),
							new Coords(9, 2),

							new Coords(0, 7), new Coords(1, 7),
							new Coords(2, 7), new Coords(3, 7),
							new Coords(4, 7), new Coords(5, 6),
							new Coords(5, 7), new Coords(5, 8),
							new Coords(6, 6), new Coords(6, 8),
							new Coords(7, 7), new Coords(8, 7),
							new Coords(9, 7),

							new Coords(10, 1), new Coords(11, 1),
							new Coords(12, 1), new Coords(13, 1),
							new Coords(10, 3), new Coords(11, 3),
							new Coords(12, 3), new Coords(13, 3),
							new Coords(14, 2),

							new Coords(10, 6), new Coords(11, 6),
							new Coords(12, 6), new Coords(13, 6),
							new Coords(10, 8), new Coords(11, 8),
							new Coords(12, 8), new Coords(13, 8),
							new Coords(14, 7), });

			private final List<Coords> headCoords = Arrays.asList(new Coords[] {
					new Coords(14, 2), new Coords(14, 7) });

			private final List<Coords> queueCoords = Arrays
					.asList(new Coords[] { new Coords(13, 3), new Coords(13, 8) });

			@Override
			public WireWorldState getStateFor(ICell<WireWorldState> cell) {
				if (headCoords.contains(cell.getCoords())) {
					return WireWorldState.HEAD;
				} else if (queueCoords.contains(cell.getCoords())) {
					return WireWorldState.QUEUE;
				} else if (metalCoords.contains(cell.getCoords())) {
					return WireWorldState.METAL;
				} else {
					return cell.getCurrentState();
				}
			}
		};

		SpaceBuilder<WireWorldState> builder = new SpaceBuilder<WireWorldState>();
		builder.setStateFactory(stateFactory).setRule(rule);
		builder.createNewSpace(2).addDimension(20).addDimension(10);

		CellularAutomaton<WireWorldState> automaton = new CellularAutomaton<WireWorldState>(
				builder.getSpaceOfCell());

		return automaton;
	}
}
