package org.cellularautomaton.space.builder.expression;

import static org.junit.Assert.*;

import org.cellularautomaton.space.builder.expression.ComposedExpression;
import org.cellularautomaton.space.builder.expression.Expression;
import org.junit.Test;

public abstract class ComposedExpressionTest {
	
	protected abstract ComposedExpression createComposedExpression();
	
	private Expression createFakeExpression() {
		return new Expression() {
			
			@Override
			public boolean evaluate() {
				return false;
			}
		};
	}

	@Test
	public void testComposition() {
		ComposedExpression expression = createComposedExpression();
		assertNotNull(expression);
		assertEquals(0, expression.getExpressions().size());
		
		Expression e1 = createFakeExpression();
		expression.addExpression(e1);
		assertEquals(1, expression.getExpressions().size());
		assertTrue(expression.getExpressions().contains(e1));
		
		Expression e2 = createFakeExpression();
		expression.addExpression(e2);
		assertEquals(2, expression.getExpressions().size());
		assertTrue(expression.getExpressions().contains(e1));
		assertTrue(expression.getExpressions().contains(e2));
		
		Expression e3 = createFakeExpression();
		expression.addExpression(e3);
		assertEquals(3, expression.getExpressions().size());
		assertTrue(expression.getExpressions().contains(e1));
		assertTrue(expression.getExpressions().contains(e2));
		assertTrue(expression.getExpressions().contains(e3));
	}

}
