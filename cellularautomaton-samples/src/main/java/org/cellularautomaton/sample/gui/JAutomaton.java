package org.cellularautomaton.sample.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;

public class JAutomaton<T> extends JPanel {
	private int height = 10;
	private int width = 20;
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
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				T state = origin.getRelativeNeighbor(x, y)
						.getCurrentState();
				g.setColor(renderer.getColor(state));
				g.fillRect(x*10, y*10, 10, 10);
			}
		}
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width*10, height*10);
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
}
