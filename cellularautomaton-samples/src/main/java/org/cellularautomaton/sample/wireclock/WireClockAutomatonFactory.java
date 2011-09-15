package org.cellularautomaton.sample.wireclock;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.optimization.AbstractOptimization;
import org.cellularautomaton.optimization.implemented.CalculateOnlyEvolvingZonesOptimization;
import org.cellularautomaton.optimization.step.AutomatonPreCalculationOptimization;
import org.cellularautomaton.optimization.type.AutoRemoveOptimization;
import org.cellularautomaton.optimization.type.AutomatonCellsSelectionOptimization;
import org.cellularautomaton.space.builder.ScriptSpaceBuilder;

public class WireClockAutomatonFactory {

	public static CellularAutomaton<Character> createAutomaton() {
		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);
		out.println("[config]");
		out.println("states=X_o.");
		out.println("[rule]");
		out.println("(0,0)=o:.");
		out.println("(0,0)=.:_");
		out.println("(0,0)=_ & (-1,-1)+(-1,+0)+(-1,+1)+(+0,-1)+(+0,+1)+(+1,-1)+(+1,+0)+(+1,+1)=1o : o");
		out.println("(0,0)=_ & (-1,-1)+(-1,+0)+(-1,+1)+(+0,-1)+(+0,+1)+(+1,-1)+(+1,+0)+(+1,+1)=2o : o");
		out.println("[cells]");
		try {
			BufferedImage buffer = ImageIO.read(new File("wireclock.gif"));

			int width = buffer.getWidth();
			int height = buffer.getHeight();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Color color = new Color(buffer.getRGB(x, y));
					if (color.equals(new Color(0, 0, 0))) {
						out.print("X");
					} else if (color.equals(new Color(255, 128, 0))) {
						out.print("_");
					} else if (color.equals(new Color(0, 128, 255))) {
						out.print("o");
					} else if (color.equals(new Color(255, 255, 255))) {
						out.print(".");
					} else {
						throw new IllegalStateException("Not managed color : "
								+ color);
					}
				}
				out.println();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		out.close();

		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(sw.getBuffer().toString());

		Logger.getAnonymousLogger().info("Creating automaton...");
		CellularAutomaton<Character> automaton = new CellularAutomaton<Character>(
				builder.getSpaceOfCell());
		Logger.getAnonymousLogger().info("Automaton created.");
		// automaton.addOptimization(new BasicOptimization());
		automaton.add(new StartOptimization());

		return automaton;
	}

	static class BasicOptimization extends
			CalculateOnlyEvolvingZonesOptimization<Character> {
		@Override
		protected Collection<ICell<Character>> getCellsDependingTo(
				ICell<Character> cell) {
			Collection<ICell<Character>> neighbors = new ArrayList<ICell<Character>>();
			neighbors.add(cell.getRelativeCell(-1, -1));
			neighbors.add(cell.getRelativeCell(-1, +0));
			neighbors.add(cell.getRelativeCell(-1, +1));
			neighbors.add(cell.getRelativeCell(0, -1));
			neighbors.add(cell.getRelativeCell(0, +1));
			neighbors.add(cell.getRelativeCell(+1, -1));
			neighbors.add(cell.getRelativeCell(+1, +0));
			neighbors.add(cell.getRelativeCell(+1, +1));
			return neighbors;
		}
	}

	static class StartOptimization extends
			AbstractOptimization<CellularAutomaton<Character>> implements
			AutomatonPreCalculationOptimization<Character>,
			AutomatonCellsSelectionOptimization<Character>,
			AutoRemoveOptimization<CellularAutomaton<Character>> {
		private boolean mustBeRemoved = false;

		@Override
		public Collection<ICell<Character>> getCellsToManage() {
			if (!mustBeRemoved) {
				Collection<ICell<Character>> cellsToManage = new HashSet<ICell<Character>>();
				for (ICell<Character> cell : getOwner().getCellsToManage()) {
					if (!cell.getCurrentState().equals('X')) {
						cellsToManage.add(cell);
					}
				}
				mustBeRemoved = true;
				return cellsToManage;
			} else {
				return getOwner().getCellsToManage();
			}
		}

		@Override
		public boolean removeNow() {
			return mustBeRemoved;
		}
	}
}
