package org.cellularautomaton.sample.ant;

public class OptimizedAnt extends AntFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OptimizedAnt() {
		super(AntAutomatonFactory.createOptimizedAutomaton());
	}

	public static void main(String[] args) {
		new OptimizedAnt();
	}
}
