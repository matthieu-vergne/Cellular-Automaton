package org.cellularautomaton.sample.ant;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.GeneratorConfiguration;

public class AntAutomatonFactory {

	public static CellularAutomaton<AntState> createAutomaton() {
		final GeneratorConfiguration<AntState> config = new GeneratorConfiguration<AntState>() {

			@Override
			public AntState calculateForCell(final Cell<AntState> cell) {
				switch (cell.getCurrentState()) {
				case BLACK:
				case WHITE:
					// Ant coming from left
					AntState neighbor = cell.getRelativeNeighbor(-1, 0)
							.getCurrentState();
					if (neighbor == AntState.ANT_BLACK_RIGHT
							|| neighbor == AntState.ANT_WHITE_RIGHT) {
						if (cell.getCurrentState() == AntState.BLACK) {
							return AntState.ANT_WHITE_UP;
						} else {
							return AntState.ANT_BLACK_DOWN;
						}
					}
					// Ant coming from right
					neighbor = cell.getRelativeNeighbor(1, 0).getCurrentState();
					if (neighbor == AntState.ANT_BLACK_LEFT
							|| neighbor == AntState.ANT_WHITE_LEFT) {
						if (cell.getCurrentState() == AntState.BLACK) {
							return AntState.ANT_WHITE_DOWN;
						} else {
							return AntState.ANT_BLACK_UP;
						}
					}

					// Ant coming from up
					neighbor = cell.getRelativeNeighbor(0, -1)
							.getCurrentState();
					if (neighbor == AntState.ANT_BLACK_DOWN
							|| neighbor == AntState.ANT_WHITE_DOWN) {
						if (cell.getCurrentState() == AntState.BLACK) {
							return AntState.ANT_WHITE_RIGHT;
						} else {
							return AntState.ANT_BLACK_LEFT;
						}
					}
					// Ant coming from down
					neighbor = cell.getRelativeNeighbor(0, 1).getCurrentState();
					if (neighbor == AntState.ANT_BLACK_UP
							|| neighbor == AntState.ANT_WHITE_UP) {
						if (cell.getCurrentState() == AntState.BLACK) {
							return AntState.ANT_WHITE_LEFT;
						} else {
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
		config.dimensionSizes = new int[] { 60, 60 };
		config.initialState = AntState.BLACK;
		config.isCyclic = true;
		config.memorySize = 1;

		CellularAutomaton<AntState> automaton = new CellularAutomaton<AntState>(
				config);

		final Cell<AntState> origin = automaton.getOriginCell();
		origin.getRelativeNeighbor(30, 30).setCurrentState(
				AntState.ANT_WHITE_UP);

		return automaton;
	}
}
