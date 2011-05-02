package org.cellularautomaton.sample.gui;

import java.awt.Color;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;

public class WireWorld {


	private static final class WireWorldStateRenderer implements
			JAutomaton.CellularRenderer<WireWorldState> {
		public Color getColor(WireWorldState state) {
			final Color color;
			switch (state) {
			case EMPTY:
				color = Color.BLACK;
				break;
			case HEAD:
				color=Color.RED;
				break;
			case METAL:
				color=Color.WHITE;
				break;
			case QUEUE:
				color = Color.BLUE;
				break;
				default:
					color=Color.GRAY;
			}
			return color;
		}
	}

	public static void main(String[] args) {
		JFrame jf = new JFrame("Game of life");

		CellularAutomaton<WireWorldState> automaton = AutomatonFactory
				.createWireWorldAutomaton(10, 20, true);
		

		final Cell<WireWorldState> origin = automaton.getOriginCell();
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
			origin.getRelativeNeighbor(coords).setCurrentState(WireWorldState.METAL);
		}
		origin.getRelativeNeighbor(14, 2).setCurrentState(WireWorldState.HEAD);
		origin.getRelativeNeighbor(14, 7).setCurrentState(WireWorldState.HEAD);
		origin.getRelativeNeighbor(13, 3).setCurrentState(WireWorldState.QUEUE);
		origin.getRelativeNeighbor(13, 8).setCurrentState(WireWorldState.QUEUE);
		
		JAutomaton<WireWorldState> jautomaton = new JAutomaton<WireWorldState>(
				automaton, new WireWorldStateRenderer());
		jf.getContentPane().add(jautomaton);

		Executors.newSingleThreadExecutor().execute(
				new RefreshAutomatonRunnable(jautomaton, 300));

		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

}
