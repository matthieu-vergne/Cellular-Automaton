package org.cellularautomaton.space.expression;

import static org.junit.Assert.*;

import org.cellularautomaton.space.builder.expression.AndExpression;
import org.cellularautomaton.space.builder.expression.ComposedExpression;
import org.cellularautomaton.space.builder.expression.SwitchExpression;
import org.junit.Test;

public class AndExpressionTest extends ComposedExpressionTest {

	@Override
	protected ComposedExpression createComposedExpression() {
		return new AndExpression();
	}

	@Test
	public void testEvaluation() {
		SwitchExpression e1 = new SwitchExpression();
		SwitchExpression e2 = new SwitchExpression();
		SwitchExpression e3 = new SwitchExpression();
		AndExpression expression = new AndExpression();
		expression.addExpression(e1);
		expression.addExpression(e2);
		expression.addExpression(e3);

		//000
		assertFalse(expression.evaluate());

		//001
		e1.switchEvaluation();
		assertFalse(expression.evaluate());

		//010
		e1.switchEvaluation();
		e2.switchEvaluation();
		assertFalse(expression.evaluate());

		//011
		e1.switchEvaluation();
		assertFalse(expression.evaluate());

		//100
		e1.switchEvaluation();
		e2.switchEvaluation();
		e3.switchEvaluation();
		assertFalse(expression.evaluate());

		//101
		e1.switchEvaluation();
		assertFalse(expression.evaluate());

		//110
		e1.switchEvaluation();
		e2.switchEvaluation();
		assertFalse(expression.evaluate());

		//111
		e1.switchEvaluation();
		assertTrue(expression.evaluate());
	}

}
