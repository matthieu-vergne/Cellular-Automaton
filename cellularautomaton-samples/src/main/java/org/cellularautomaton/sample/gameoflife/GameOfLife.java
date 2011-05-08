package org.cellularautomaton.sample.gameoflife;

import java.awt.Color;

import org.cellularautomaton.sample.common.JAutomatonPanel;
import org.cellularautomaton.sample.common.JAutomatonFrame;

public class GameOfLife extends JAutomatonFrame<GameOfLifeState> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2609208480681275662L;

	private static final class GameOfLifeRenderer implements
			JAutomatonPanel.CellularRenderer<GameOfLifeState> {
		public Color getColor(GameOfLifeState state) {
			if (state == GameOfLifeState.ALIVE) {
				return Color.BLACK;
			} else {
				return Color.WHITE;
			}
		}
	}

	public GameOfLife() {
		super("Game of life", GOLAutomatonFactory.createAutomaton(),
				new GameOfLifeRenderer(), 100);
	}

	public static void main(String[] args) {
		new GameOfLife();
	}

}
