package org.cellularautomaton.space.expression;

import java.util.Iterator;

public class AndExpression extends ComposedExpression {
	public boolean evaluate() {
		boolean isTrue = true;
		Iterator<Expression> iterator = getExpressions().iterator();
		while (isTrue && iterator.hasNext()) {
			isTrue = isTrue && iterator.next().evaluate();
		}
		return isTrue;
	};
}
