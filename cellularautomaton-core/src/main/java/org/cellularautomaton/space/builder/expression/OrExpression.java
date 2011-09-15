package org.cellularautomaton.space.builder.expression;

import java.util.Iterator;

public class OrExpression extends ComposedExpression {
	public boolean evaluate() {
		boolean isTrue = false;
		Iterator<Expression> iterator = getExpressions().iterator();
		while (!isTrue && iterator.hasNext()) {
			isTrue = isTrue || iterator.next().evaluate();
		}
		return isTrue;
	};
}
