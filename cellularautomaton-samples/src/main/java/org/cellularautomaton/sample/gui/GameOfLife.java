package org.cellularautomaton.sample.gui;

import java.awt.Color;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;

public class GameOfLife {

	private static final class GameOfLifeRenderer implements
			JAutomaton.CellularRenderer<GameOfLifeState> {
		public Color getColor(GameOfLifeState state) {
			if (state == GameOfLifeState.ALIVE) {
				return Color.BLACK;
			} else {
				return Color.WHITE;
			}
		}
	}

	public static void main(String[] args) {
		JFrame jf = new JFrame("Game of life");

		CellularAutomaton<GameOfLifeState> automaton = AutomatonFactory
				.createGameOfLifeAutomaton(10, 50, true);
		
		final Cell<GameOfLifeState> origin = automaton.getOriginCell();
		for (final int[] coords : new int[][] { { 2, 0 }, { 2, 1 }, { 1, 2 },
				{ 3, 1 }, { 3, 2 }, }) {
			origin.getRelativeNeighbor(coords).setCurrentState(
					GameOfLifeState.ALIVE);
		}
		
		JAutomaton<GameOfLifeState> jautomaton = new JAutomaton<GameOfLifeState>(
				automaton, new GameOfLifeRenderer());
		jf.getContentPane().add(jautomaton);

		Executors.newSingleThreadExecutor().execute(
				new RefreshAutomatonRunnable(jautomaton, 300));

		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

}
