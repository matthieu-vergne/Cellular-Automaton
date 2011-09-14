package org.cellularautomaton.sample.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
	private boolean fullRendering = true;

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
			width = Math.max(width, cell.getCoords().get(1));
			height = Math.max(height, cell.getCoords().get(0));
		}
		this.width = width + 1;
		this.height = height + 1;

		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				askFullRendering();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				askFullRendering();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// do nothing
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// do nothing
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		double xRate = (double) getWidth() / width;
		double yRate = (double) getHeight() / height;
		Iterator<?> iterator;
		// TODO allow to activate this optimization
		if (fullRendering) {
			g.clearRect(0, 0, getWidth(), getHeight());
			iterator = automaton.getSpace().iterator();
		} else {
			iterator = automaton.getCellsToManage().iterator();
		}
		int xLength = (int) Math.ceil(xRate);
		int yLength = (int) Math.ceil(yRate);
		while (iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			ICell<? extends T> cell = (ICell<? extends T>) iterator.next();
			T state = cell.getCurrentState();
			Coords coords = cell.getCoords();
			g.setColor(renderer.getColor(state));
			int x = coords.get(1);
			int y = coords.get(0);
			if (xRate != 1) {
				x = (int) Math.ceil(x * xRate);
			}
			if (yRate != 1) {
				y = (int) Math.ceil(y * yRate);
			}
			g.fillRect(x, y, xLength, yLength);
		}
		fullRendering = false;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Math.max(width,
				Math.min(width * CELL_DEFAULT_SIZE, WINDOW_MAX_SIZE)),
				Math.max(height,
						Math.min(height * CELL_DEFAULT_SIZE, WINDOW_MAX_SIZE)));
	}

	public void askFullRendering() {
		fullRendering = true;
	}

}
