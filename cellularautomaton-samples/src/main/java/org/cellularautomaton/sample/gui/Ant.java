package org.cellularautomaton.sample.gui;

import java.awt.Color;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;

public class Ant {

	private static final class AntRenderer implements
			JAutomaton.CellularRenderer<AntState> {
		public Color getColor(AntState state) {
			switch (state) {
			case BLACK:
				return Color.BLACK;
			case WHITE:
				return Color.WHITE;
			case ANT_BLACK_DOWN:
			case ANT_BLACK_LEFT:
			case ANT_BLACK_RIGHT:
			case ANT_BLACK_UP:
			case ANT_WHITE_DOWN:
			case ANT_WHITE_LEFT:
			case ANT_WHITE_RIGHT:
			case ANT_WHITE_UP:
				return Color.BLUE;
			default:
				return Color.GRAY;
			}
		}
	}

	public static void main(String[] args) {
		JFrame jf = new JFrame("Game of life");

		CellularAutomaton<AntState> automaton = AutomatonFactory
				.createAntAutomaton(60, 60, true);

		final Cell<AntState> origin = automaton.getOriginCell();
		origin.getRelativeNeighbor(30, 30).setCurrentState(
				AntState.ANT_WHITE_UP);

		JAutomaton<AntState> jautomaton = new JAutomaton<AntState>(automaton,
				new AntRenderer());
		jautomaton.setHeight(60);
		jautomaton.setWidth(60);
		jf.getContentPane().add(jautomaton);

		Executors.newSingleThreadExecutor().execute(
				new RefreshAutomatonRunnable(jautomaton, 5));

		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

}
