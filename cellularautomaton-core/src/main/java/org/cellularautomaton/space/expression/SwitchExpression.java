package org.cellularautomaton.space.expression;

public class SwitchExpression implements Expression {
	private boolean state = false;

	public void switchEvaluation() {
		state = !state;
	}

	@Override
	public boolean evaluate() {
		return state;
	}
}
