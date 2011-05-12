package org.cellularautomaton.sample.ant;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.space.SpaceBuilder;
import org.cellularautomaton.state.EnumStateFactory;
import org.cellularautomaton.state.IStateFactory;
import org.cellularautomaton.util.Coords;

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

			@Override
			public AntState getStateFor(ICell<AntState> cell) {
				return cell.getCoords().equals(new Coords(30, 30)) ? AntState.ANT_WHITE_UP
						: cell.getCurrentState();
			}
		};

		SpaceBuilder<AntState> builder = new SpaceBuilder<AntState>();
		builder.setStateFactory(stateFactory).setRule(rule);
		builder.createNewSpace().addDimension(60).addDimension(60);

		CellularAutomaton<AntState> automaton = new CellularAutomaton<AntState>(
				builder.getSpaceOfCell());

		return automaton;
	}
}
