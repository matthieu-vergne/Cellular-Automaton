package org.cellularautomaton.space.expression;

import java.util.Collection;
import java.util.HashSet;

public abstract class ComposedExpression implements Expression {
	private final Collection<Expression> expressions = new HashSet<Expression>(2);

	public Collection<Expression> getExpressions() {
		return expressions;
	}

	public void addExpression(Expression expression) {
		expressions.add(expression);
	}

}
