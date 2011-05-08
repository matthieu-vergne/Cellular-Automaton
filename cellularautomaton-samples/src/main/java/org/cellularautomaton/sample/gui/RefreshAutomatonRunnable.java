package org.cellularautomaton.sample.gui;

import org.cellularautomaton.CellularAutomaton;

public class RefreshAutomatonRunnable implements Runnable {
	private JAutomaton<?> jautomaton;
	private int millis;

	public RefreshAutomatonRunnable(JAutomaton<?> jautomaton,
			int millisBetweenSteps) {
		this.jautomaton = jautomaton;
		millis = millisBetweenSteps;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CellularAutomaton<?> automaton = jautomaton.getAutomaton();
			automaton.doStep();

			jautomaton.repaint();
		}
	}

}
