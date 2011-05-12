package org.cellularautomaton.sample.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;

public class JAutomatonPanel<T> extends JPanel {
	private static final int CELL_SIZE = 10;
	private int height;
	private int width;
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
		this.width = 0;
		this.height = 0;
		for (ICell<?> cell : automaton.getSpace().getAllCells()) {
			this.width = Math.max(this.width, cell.getCoords()[0]);
			this.height = Math.max(this.height, cell.getCoords()[1]);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		ICell<? extends T> origin = automaton.getSpace().getOrigin();
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				T state = origin.getRelativeCell(x, y).getCurrentState();
				g.setColor(renderer.getColor(state));
				g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width * CELL_SIZE, height * CELL_SIZE);
	}
}
