package org.cellularautomaton.sample.wireworld;

import java.awt.Color;

import org.cellularautomaton.sample.common.JAutomatonPanel;
import org.cellularautomaton.sample.common.JAutomatonFrame;

public class WireWorld extends JAutomatonFrame<WireWorldState> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1506799381792842883L;

	private static final class WireWorldStateRenderer implements
			JAutomatonPanel.CellularRenderer<WireWorldState> {
		public Color getColor(WireWorldState state) {
			switch (state) {
				case EMPTY:
					return Color.BLACK;
				case HEAD:
					return Color.BLUE;
				case METAL:
					return Color.ORANGE;
				case QUEUE:
					return Color.RED;
				default:
					return Color.GRAY;
			}
		}
	}

	public WireWorld() {
		super("Wireworld", WireWorldAutomatonFactory.createAutomaton(),
				new WireWorldStateRenderer(), 100);
	}

	public static void main(String[] args) {
		new WireWorld();
	}

}
