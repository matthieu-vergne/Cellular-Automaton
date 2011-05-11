package org.cellularautomaton.sample.wireworld;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.builder.CellSpaceBuilder;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;

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

		CellSpaceBuilder<WireWorldState> builder = new CellSpaceBuilder<WireWorldState>();
		builder.setInitialState(WireWorldState.EMPTY).setRule(rule);
		builder.createNewSpace(2).addDimension(20).addDimension(10);

		CellularAutomaton<WireWorldState> automaton = new CellularAutomaton<WireWorldState>(
				builder.getSpaceOfCellOrigin());

		final ICell<WireWorldState> origin = automaton.getOriginCell();
		for (final int[] coords : new int[][] { { 0, 2 }, { 1, 2 }, { 2, 2 },
				{ 3, 2 }, { 4, 2 }, { 5, 1 }, { 5, 3 }, { 6, 1 }, { 6, 2 },
				{ 6, 3 }, { 7, 2 }, { 8, 2 }, { 9, 2 },

				{ 0, 7 }, { 1, 7 }, { 2, 7 }, { 3, 7 }, { 4, 7 }, { 5, 6 },
				{ 5, 7 }, { 5, 8 }, { 6, 6 }, { 6, 8 }, { 7, 7 }, { 8, 7 },
				{ 9, 7 },

				{ 10, 1 }, { 11, 1 }, { 12, 1 }, { 13, 1 }, { 10, 3 },
				{ 11, 3 }, { 12, 3 }, { 13, 3 }, { 14, 2 },

				{ 10, 6 }, { 11, 6 }, { 12, 6 }, { 13, 6 }, { 10, 8 },
				{ 11, 8 }, { 12, 8 }, { 13, 8 }, { 14, 7 }, }) {
			origin.getRelativeCell(coords)
					.setCurrentState(WireWorldState.METAL);
		}
		origin.getRelativeCell(14, 2).setCurrentState(WireWorldState.HEAD);
		origin.getRelativeCell(14, 7).setCurrentState(WireWorldState.HEAD);
		origin.getRelativeCell(13, 3).setCurrentState(WireWorldState.QUEUE);
		origin.getRelativeCell(13, 8).setCurrentState(WireWorldState.QUEUE);

		return automaton;
	}

}
