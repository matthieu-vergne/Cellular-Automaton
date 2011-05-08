package org.cellularautomaton.sample;

import java.util.Map;

import org.cellularautomaton.Cell;
import org.cellularautomaton.CellularAutomaton;

public abstract class SampleBase {

	protected abstract CellularAutomaton<Integer> generateAutomaton();

	protected abstract Map<Integer, Character> getRepresentation();

	protected abstract int getXSize();

	protected abstract int getYSize();

	protected int[] getDimensions() {
		return new int[] { getXSize(), getYSize() };
	}

	public void run(long timeBetweenSteps) {
		CellularAutomaton<Integer> automaton = generateAutomaton();

		System.out.println(toString(automaton));

		while (true) {
			try {
				Thread.sleep(timeBetweenSteps);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			automaton.doStep();
			System.out.println("=====");
			System.out.println(toString(automaton));
		}
	}

	private String toString(CellularAutomaton<Integer> automaton) {
		assert automaton != null;

		Cell<Integer> origin = automaton.getOriginCell();
		String result = "";
		for (int y = getYSize() - 1; y >= 0; y--) {
			for (int x = 0; x < getXSize(); x++) {
				Integer state = origin.getRelativeNeighbor(x, y)
						.getCurrentState();
				Character character = getRepresentation().get(state);
				if (character == null) {
					throw new IllegalStateException("unknown representation : "
							+ state);
				} else {
					result += character;
				}
			}
			result += "\n";
		}

		return result;
	}
}
