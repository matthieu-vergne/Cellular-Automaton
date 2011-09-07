package org.cellularautomaton.optimization;

import org.cellularautomaton.CellularAutomaton;

/**
 * This is the basic implementation of the {@link Optimization} interface. Any
 * optimization should extend this abstract class and add the needed interfaces
 * to indicate the steps and types of the optimization.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 */
public abstract class AbstractOptimization<StateType> implements
		Optimization<StateType> {
	private CellularAutomaton<StateType> automaton;

	public void setAutomaton(CellularAutomaton<StateType> automaton) {
		this.automaton = automaton;
	}

	public CellularAutomaton<StateType> getAutomaton() {
		return automaton;
	}
}
