package org.cellularautomaton.cell;

public class StateNotCalculatedException extends IllegalStateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StateNotCalculatedException() {
		super("The next state is not calculated yet");
	}
}
