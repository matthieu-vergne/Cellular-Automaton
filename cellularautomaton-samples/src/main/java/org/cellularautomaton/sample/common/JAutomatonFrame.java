package org.cellularautomaton.sample.common;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.sample.common.JAutomatonPanel.CellularRenderer;

public class JAutomatonFrame<T> extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8072377921681422273L;

	public JAutomatonFrame(String title, CellularAutomaton<T> automaton,
			CellularRenderer<? super T> renderer, int stepTime) {
		super(title);

		final JAutomatonPanel<T> jautomaton = new JAutomatonPanel<T>(automaton,
				renderer);
		getContentPane().add(jautomaton);

		Executors.newSingleThreadExecutor().execute(
				new RefreshAutomatonRunnable(jautomaton, stepTime));
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// do nothing
			}

			@Override
			public void focusGained(FocusEvent e) {
				jautomaton.askFullRendering();
			}
		});
	}

	private class RefreshAutomatonRunnable implements Runnable {
		private JAutomatonPanel<?> jautomaton;
		private int millis;

		public RefreshAutomatonRunnable(JAutomatonPanel<?> jautomaton,
				int millisBetweenSteps) {
			this.jautomaton = jautomaton;
			millis = millisBetweenSteps;
		}

		public void run() {
			while (true) {
				if (millis != 0) {
					try {
						Thread.sleep(millis);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				jautomaton.getAutomaton().doStep();
				jautomaton.repaint();
			}
		}

	}
}
