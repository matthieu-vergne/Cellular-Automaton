package org.cellularautomaton.sample.langtonant;

import java.awt.Color;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.sample.common.JAutomatonFrame;
import org.cellularautomaton.sample.common.JAutomatonPanel;

public class AntFrame extends JAutomatonFrame<AntState> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6600231663158995866L;

	protected static final class AntRenderer implements
			JAutomatonPanel.CellularRenderer<AntState> {
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

	public AntFrame(CellularAutomaton<AntState> automaton) {
		super("Langston Ant", automaton, new AntRenderer(), 0);
	}
}
