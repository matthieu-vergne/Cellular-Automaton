package org.cellularautomaton.sample.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;

public class JAutomatonPanel<T> extends JPanel {
	public static final int CELL_DEFAULT_SIZE = 10;
	private final int height;
	private final int width;
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

	public JAutomatonPanel(CellularAutomaton<? extends T> automaton,
			CellularRenderer<? super T> renderer) {
		this.automaton = automaton;
		this.renderer = renderer;
		int width = 0;
		int height = 0;
		for (ICell<?> cell : automaton.getSpace().getAllCells()) {
			width = Math.max(width, cell.getCoords().get(0));
			height = Math.max(height, cell.getCoords().get(1));
		}
		this.width = width;
		this.height = height;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		ICell<? extends T> origin = automaton.getSpace().getOrigin();
		double xRate = (double) getWidth() / width;
		double yRate = (double) getHeight() / height;
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				T state = origin.getRelativeCell(x, y).getCurrentState();
				g.setColor(renderer.getColor(state));
				g.fillRect((int) Math.ceil(x * xRate),
						(int) Math.ceil(y * yRate), (int) Math.ceil(xRate),
						(int) Math.ceil(yRate));
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Math.min(width * CELL_DEFAULT_SIZE, 500),
				Math.min(height * CELL_DEFAULT_SIZE, 500));
	}
}
