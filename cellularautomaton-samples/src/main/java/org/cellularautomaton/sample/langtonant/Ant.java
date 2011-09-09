package org.cellularautomaton.sample.langtonant;

public class Ant extends AntFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ant() {
		super(AntAutomatonFactory.createAutomaton());
	}

	public static void main(String[] args) {
		new Ant();
	}
}
