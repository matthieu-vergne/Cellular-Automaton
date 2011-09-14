package org.cellularautomaton.sample.wireclock;

import java.awt.Color;

import org.cellularautomaton.sample.common.JAutomatonFrame;
import org.cellularautomaton.sample.common.JAutomatonPanel;

public class WireClock extends JAutomatonFrame<Character> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1506799381792842883L;

	private static final class WireWorldStateRenderer implements
			JAutomatonPanel.CellularRenderer<Character> {
		public Color getColor(Character state) {
			switch (state) {
				case 'X':
					return Color.BLACK;
				case 'o':
					return Color.BLUE;
				case '.':
					return Color.RED;
				case '_':
					return Color.ORANGE;
				default:
					return Color.GRAY;
			}
		}
	}

	public WireClock() {
		super("Wireworld Clock", WireClockAutomatonFactory.createAutomaton(),
				new WireWorldStateRenderer(), 0);
	}

	public static void main(String[] args) {
		new WireClock();
	}

}
