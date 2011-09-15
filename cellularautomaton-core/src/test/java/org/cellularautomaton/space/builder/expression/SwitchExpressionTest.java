package org.cellularautomaton.space.builder.expression;

import static org.junit.Assert.*;

import org.cellularautomaton.space.builder.expression.SwitchExpression;
import org.junit.Test;

public class SwitchExpressionTest {

	@Test
	public void testSwitch() {
		SwitchExpression expression = new SwitchExpression();
		assertFalse(expression.evaluate());
		
		expression.switchEvaluation();
		assertTrue(expression.evaluate());
		
		expression.switchEvaluation();
		assertFalse(expression.evaluate());
		
		expression.switchEvaluation();
		assertTrue(expression.evaluate());
		
		expression.switchEvaluation();
		assertFalse(expression.evaluate());
	}

}
