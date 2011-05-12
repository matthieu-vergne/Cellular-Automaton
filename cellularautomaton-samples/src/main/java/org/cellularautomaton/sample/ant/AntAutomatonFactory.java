package org.cellularautomaton.sample.ant;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.builder.CellSpaceBuilder;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.definition.IStateFactory;
import org.cellularautomaton.impl.EnumStateFactory;

public class AntAutomatonFactory {

	public static CellularAutomaton<AntState> createAutomaton() {
		IRule<AntState> rule = new IRule<AntState>() {

			public AntState calculateNextStateOf(ICell<AntState> cell) {
				switch (cell.getCurrentState()) {
				case BLACK:
				case WHITE:
					// Ant coming from left
					AntState neighbor = cell.getRelativeCell(-1, 0)
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
					neighbor = cell.getRelativeCell(1, 0).getCurrentState();
					if (neighbor == AntState.ANT_BLACK_LEFT
							|| neighbor == AntState.ANT_WHITE_LEFT) {
						if (cell.getCurrentState() == AntState.BLACK) {
							return AntState.ANT_WHITE_DOWN;
						} else {
							return AntState.ANT_BLACK_UP;
						}
					}

					// Ant coming from up
					neighbor = cell.getRelativeCell(0, -1).getCurrentState();
					if (neighbor == AntState.ANT_BLACK_DOWN
							|| neighbor == AntState.ANT_WHITE_DOWN) {
						if (cell.getCurrentState() == AntState.BLACK) {
							return AntState.ANT_WHITE_RIGHT;
						} else {
							return AntState.ANT_BLACK_LEFT;
						}
					}
					// Ant coming from down
					neighbor = cell.getRelativeCell(0, 1).getCurrentState();
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

		// TODO use this factory to set the initial states of the cells
		IStateFactory<AntState> stateFactory = new EnumStateFactory<AntState>() {
			@Override
			public Class<AntState> getEnumType() {
				return AntState.class;
			}

			/**
			 * The most used state of the ant automaton is
			 * {@link AntState#BLACK }.
			 */
			@Override
			public AntState getDefaultState() {
				return AntState.BLACK;
			}
		};

		CellSpaceBuilder<AntState> builder = new CellSpaceBuilder<AntState>();
		builder.setStateFactory(stateFactory).setRule(rule);
		builder.createNewSpace(2).addDimension(60).addDimension(60);

		CellularAutomaton<AntState> automaton = new CellularAutomaton<AntState>(
				builder.getSpaceOfCell());

		final ICell<AntState> origin = automaton.getCellSpace().getOrigin();
		origin.getRelativeCell(30, 30).setCurrentState(AntState.ANT_WHITE_UP);

		return automaton;
	}
}
