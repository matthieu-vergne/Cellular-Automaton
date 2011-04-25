package org.cellularautomaton.sample;

import java.util.HashMap;
import java.util.Map;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.GeneratorConfiguration;

public class Wireworld extends SampleBase {
	protected static final int EMPTY = 0;
	protected static final int METAL = 1;
	protected static final int HEAD = 2;
	protected static final int QUEUE = 3;

	public static void main(String[] args) {
		new Wireworld().run(300);
	}

	@Override
	protected CellularAutomaton<Integer> generateAutomaton() {
		final GeneratorConfiguration<Integer> config = new GeneratorConfiguration<Integer>() {

			@Override
			public Integer calculateForCell(final Cell<Integer> cell) {
				int state = cell.getCurrentState().intValue();
				switch (state) {
				case EMPTY:
					return EMPTY;
				case METAL:
					int heads = 0;
					for (final int[] coords : new int[][] { { -1, -1 },
							{ -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 },
							{ +1, -1 }, { +1, 0 }, { +1, 1 } }) {
						if (cell.getRelativeNeighbor(coords).getCurrentState() == HEAD) {
							heads++;
						}
					}
					return heads == 1 || heads == 2 ? HEAD : METAL;
				case HEAD:
					return QUEUE;
				case QUEUE:
					return METAL;
				default:
					throw new IllegalStateException("undefined case : " + state);
				}
			}
		};
		config.dimensionSizes = getDimensions();
		config.initialState = EMPTY;
		config.isCyclic = true;
		config.memorySize = 1;

		final CellularAutomaton<Integer> automaton = new CellularAutomaton<Integer>(
				config);

		final Cell<Integer> origin = automaton.getOriginCell();
		for (final int[] coords : new int[][] { { 0, 2 }, { 1, 2 }, { 2, 2 },
				{ 3, 2 }, { 4, 2 }, { 5, 1 }, { 5, 3 }, { 6, 1 }, { 6, 2 },
				{ 6, 3 }, { 7, 2 }, { 8, 2 }, { 9, 2 },

				{ 0, 7 }, { 1, 7 }, { 2, 7 }, { 3, 7 }, { 4, 7 }, { 5, 6 },
				{ 5, 7 }, { 5, 8 }, { 6, 6 }, { 6, 8 }, { 7, 7 }, { 8, 7 },
				{ 9, 7 },

				{ 10, 1 }, { 11, 1 }, { 12, 1 }, { 13, 1 }, { 10, 3 },
				{ 11, 3 }, { 12, 3 }, { 13, 3 }, { 14, 2 },

				{ 10, 6 }, { 11, 6 }, { 12, 6 }, { 13, 6 }, { 10, 8 },
				{ 11, 8 }, { 12, 8 }, { 13, 8 }, { 14, 7 }, }) {
			origin.getRelativeNeighbor(coords).setCurrentState(METAL);
		}
		origin.getRelativeNeighbor(14, 2).setCurrentState(HEAD);
		origin.getRelativeNeighbor(14, 7).setCurrentState(HEAD);
		origin.getRelativeNeighbor(13, 3).setCurrentState(QUEUE);
		origin.getRelativeNeighbor(13, 8).setCurrentState(QUEUE);

		return automaton;
	}

	@Override
	protected Map<Integer, Character> getRepresentation() {
		Map<Integer, Character> map = new HashMap<Integer, Character>();
		map.put(EMPTY, '#');
		map.put(METAL, ' ');
		map.put(HEAD, '×');
		map.put(QUEUE, '·');
		return map;
	}
	
	@Override
	protected int getXSize() {
		return 20;
	}
	
	@Override
	protected int getYSize() {
		return 10;
	}
}
