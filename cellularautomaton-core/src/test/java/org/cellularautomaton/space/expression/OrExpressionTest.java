package org.cellularautomaton.space.expression;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrExpressionTest extends ComposedExpressionTest {

	@Override
	protected ComposedExpression createComposedExpression() {
		return new OrExpression();
	}

	@Test
	public void testEvaluation() {
		SwitchExpression e1 = new SwitchExpression();
		SwitchExpression e2 = new SwitchExpression();
		SwitchExpression e3 = new SwitchExpression();
		OrExpression expression = new OrExpression();
		expression.addExpression(e1);
		expression.addExpression(e2);
		expression.addExpression(e3);

		//000
		assertFalse(expression.evaluate());

		//001
		e1.switchEvaluation();
		assertTrue(expression.evaluate());

		//010
		e1.switchEvaluation();
		e2.switchEvaluation();
		assertTrue(expression.evaluate());

		//011
		e1.switchEvaluation();
		assertTrue(expression.evaluate());

		//100
		e1.switchEvaluation();
		e2.switchEvaluation();
		e3.switchEvaluation();
		assertTrue(expression.evaluate());

		//101
		e1.switchEvaluation();
		assertTrue(expression.evaluate());

		//110
		e1.switchEvaluation();
		e2.switchEvaluation();
		assertTrue(expression.evaluate());

		//111
		e1.switchEvaluation();
		assertTrue(expression.evaluate());
	}

}
