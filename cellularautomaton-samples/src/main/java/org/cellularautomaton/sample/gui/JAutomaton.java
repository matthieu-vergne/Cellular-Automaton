package org.cellularautomaton.sample.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;

public class JAutomaton<T> extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4839521220659722820L;
	private CellularAutomaton<? extends T> automaton;
	
	public CellularAutomaton<? extends T> getAutomaton() {
		return automaton;
	}

	public static interface CellularRenderer<T> {
		Color getColor(T state);
	}
	
	private CellularRenderer<? super T> renderer;

	public JAutomaton(CellularAutomaton<? extends T> automaton, CellularRenderer<? super T> renderer) {
		this.automaton = automaton;
		this.renderer = renderer;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Cell<? extends T> origin = automaton.getOriginCell();
		for (int y = 10 - 1; y >= 0; y--) {
			for (int x = 0; x < 50; x++) {
				T state = origin.getRelativeNeighbor(x, y)
						.getCurrentState();
				g.setColor(renderer.getColor(state));
				g.fillRect(x*10, y*10, 10, 10);
			}
		}
	}
}
