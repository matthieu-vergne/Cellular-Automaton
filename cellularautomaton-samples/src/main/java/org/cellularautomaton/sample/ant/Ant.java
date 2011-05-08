package org.cellularautomaton.sample.ant;

import java.awt.Color;

import org.cellularautomaton.sample.common.JAutomatonPanel;
import org.cellularautomaton.sample.common.JAutomatonFrame;

public class Ant extends JAutomatonFrame<AntState> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6600231663158995866L;

	private static final class AntRenderer implements
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

	public Ant() {
		super("Langston Ant", AntAutomatonFactory.createAutomaton(),
				new AntRenderer(), 0);
	}

	public static void main(String[] args) {
		new Ant();
	}

}
