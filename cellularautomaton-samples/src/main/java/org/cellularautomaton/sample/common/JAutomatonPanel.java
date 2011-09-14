package org.cellularautomaton.sample.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JPanel;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.util.Coords;

public class JAutomatonPanel<T> extends JPanel {
	public static final int CELL_DEFAULT_SIZE = 10;
	public final int WINDOW_MAX_SIZE = 500;
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
		g.clearRect(0, 0, getWidth(), getHeight());
		double xRate = (double) getWidth() / width;
		double yRate = (double) getHeight() / height;
		Iterator<?> iterator = automaton.getSpace().iterator();
		while (iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			ICell<? extends T> cell = (ICell<? extends T>) iterator.next();
			T state = cell.getCurrentState();
			Coords coords = cell.getCoords();
			g.setColor(renderer.getColor(state));
			g.fillRect((int) Math.ceil(coords.get(0) * xRate),
					(int) Math.ceil(coords.get(1) * yRate),
					(int) Math.ceil(xRate), (int) Math.ceil(yRate));
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Math.max(width,
				Math.min(width * CELL_DEFAULT_SIZE, WINDOW_MAX_SIZE)),
				Math.max(height,
						Math.min(height * CELL_DEFAULT_SIZE, WINDOW_MAX_SIZE)));
	}
}
