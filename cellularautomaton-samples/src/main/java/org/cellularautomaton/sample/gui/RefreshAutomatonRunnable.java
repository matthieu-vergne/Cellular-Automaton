package org.cellularautomaton.sample.gui;

import org.cellularautomaton.CellularAutomaton;

public class RefreshAutomatonRunnable implements Runnable {
	private JAutomaton<?> jautomaton;

	public RefreshAutomatonRunnable(JAutomaton<?> jautomaton) {
		this.jautomaton = jautomaton;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CellularAutomaton<?> automaton = jautomaton.getAutomaton();
			automaton.calculateNextStep();
			automaton.applyNextStep();
			
			jautomaton.repaint();
		}
	}

}
