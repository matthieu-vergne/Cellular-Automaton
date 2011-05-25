package org.cellularautomaton.cell;

public class StateNotCalculatedException extends IllegalStateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StateNotCalculatedException() {
		super("the next state is not calculated yet");
	}
}
