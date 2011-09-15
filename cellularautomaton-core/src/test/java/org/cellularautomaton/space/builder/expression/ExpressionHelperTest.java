package org.cellularautomaton.space.builder.expression;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.cellularautomaton.cell.GenericCell;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.builder.expression.AndExpression;
import org.cellularautomaton.space.builder.expression.CellComparisonExpression;
import org.cellularautomaton.space.builder.expression.ComposedExpression;
import org.cellularautomaton.space.builder.expression.Expression;
import org.cellularautomaton.space.builder.expression.ExpressionHelper;
import org.cellularautomaton.space.builder.expression.OrExpression;
import org.cellularautomaton.util.Coords;
import org.junit.Test;

public class ExpressionHelperTest {

	private static final String COMPLEX_RULE = "(0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & ( (0,0)=A | (0,0)=B )";
	private static final Collection<Character> STATES = Collections
			.unmodifiableCollection(Arrays.asList(new Character[] { 'A', 'B',
					'C' }));
	private static final Comparator<ComposedExpression> COMPOSED_COMPARATOR = new Comparator<ComposedExpression>() {
		@Override
		public int compare(ComposedExpression e1, ComposedExpression e2) {
			Integer c1 = 0;
			for (Expression subexpression : e1.getExpressions()) {
				if (subexpression instanceof ComposedExpression) {
					c1++;
				}
			}
			Integer c2 = 0;
			for (Expression subexpression : e2.getExpressions()) {
				if (subexpression instanceof ComposedExpression) {
					c2++;
				}
			}
			return c1.compareTo(c2);
		}
	};

	@Test
	public void testParseRuleSimpleCell() {
		Expression expression = ExpressionHelper.parseRulePart("(+1,-1)=A",
				STATES);
		assertTrue(expression instanceof CellComparisonExpression);

		CellComparisonExpression e = (CellComparisonExpression) expression;
		assertEquals(1, e.getConstraints().keySet().size());
		assertTrue(e.getConstraints().keySet().contains('A'));
		assertEquals(1, e.getTargets().size());
		assertTrue(e.getTargets().contains(new Coords(1, -1)));
	}

	@Test
	public void testParseRuleOr() {
		Expression expression = ExpressionHelper.parseRulePart(
				"(-1,-1)=A | (+1,+1)=B", STATES);
		assertTrue(expression instanceof OrExpression);

		OrExpression e = (OrExpression) expression;
		assertEquals(2, e.getExpressions().size());

		Iterator<Expression> iterator = e.getExpressions().iterator();
		assertTrue(iterator.next() instanceof CellComparisonExpression);
		assertTrue(iterator.next() instanceof CellComparisonExpression);

		iterator = e.getExpressions().iterator();
		CellComparisonExpression a = (CellComparisonExpression) iterator.next();
		CellComparisonExpression b = (CellComparisonExpression) iterator.next();
		if (b.getConstraints().keySet().contains('A')) {
			CellComparisonExpression temp = a;
			a = b;
			b = temp;
		}
		assertEquals(1, a.getConstraints().keySet().size());
		assertTrue(a.getConstraints().keySet().contains('A'));
		assertEquals(1, a.getTargets().size());
		assertTrue(a.getTargets().contains(new Coords(-1, -1)));

		assertEquals(1, b.getConstraints().keySet().size());
		assertTrue(b.getConstraints().keySet().contains('B'));
		assertEquals(1, b.getTargets().size());
		assertTrue(b.getTargets().contains(new Coords(1, 1)));
	}

	@Test
	public void testParseRuleAnd() {
		Expression expression = ExpressionHelper.parseRulePart(
				"(-1,-1)=A & (+1,+1)=B", STATES);
		assertTrue(expression instanceof AndExpression);

		AndExpression e = (AndExpression) expression;
		assertEquals(2, e.getExpressions().size());

		Iterator<Expression> iterator = e.getExpressions().iterator();
		assertTrue(iterator.next() instanceof CellComparisonExpression);
		assertTrue(iterator.next() instanceof CellComparisonExpression);

		iterator = e.getExpressions().iterator();
		CellComparisonExpression a = (CellComparisonExpression) iterator.next();
		CellComparisonExpression b = (CellComparisonExpression) iterator.next();
		if (b.getConstraints().keySet().contains('A')) {
			CellComparisonExpression temp = a;
			a = b;
			b = temp;
		}
		assertEquals(1, a.getConstraints().keySet().size());
		assertTrue(a.getConstraints().keySet().contains('A'));
		assertEquals(1, a.getTargets().size());
		assertTrue(a.getTargets().contains(new Coords(-1, -1)));

		assertEquals(1, b.getConstraints().keySet().size());
		assertTrue(b.getConstraints().keySet().contains('B'));
		assertEquals(1, b.getTargets().size());
		assertTrue(b.getTargets().contains(new Coords(1, 1)));
	}

	@Test
	public void testParseRuleComplex() {
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & ( (0,0)=A | (0,0)=B )
		Expression expression = ExpressionHelper.parseRulePart(COMPLEX_RULE, STATES);
		assertTrue(expression instanceof OrExpression);

		// X | Y | Z
		// ? | ? | ?
		OrExpression root = (OrExpression) expression;
		assertEquals(3, root.getExpressions().size());

		// X = and, Y = and, Z = and
		Iterator<Expression> iteratorRoot = root.getExpressions().iterator();
		Expression e1 = iteratorRoot.next();
		Expression e2 = iteratorRoot.next();
		Expression e3 = iteratorRoot.next();
		assertTrue(e1 instanceof AndExpression);
		assertTrue(e2 instanceof AndExpression);
		assertTrue(e3 instanceof AndExpression);

		// X1 & X2 | Y1 & Y2 | Z1 & Z2
		// ? & ? | ? & ? | ? & ?
		AndExpression x = (AndExpression) e1;
		AndExpression y = (AndExpression) e2;
		AndExpression z = (AndExpression) e3;
		assertEquals(2, x.getExpressions().size());
		assertEquals(2, y.getExpressions().size());
		assertEquals(2, z.getExpressions().size());

		// preparation of next step
		List<AndExpression> level1 = Arrays.asList(new AndExpression[] { x, y,
				z });
		Collections.sort(level1, COMPOSED_COMPARATOR);

		// X1 = cell, X2 = cell
		// cell & cell | ? & ? | ? & ?
		Iterator<Expression> iteratorX = level1.get(0).getExpressions()
				.iterator();
		Expression e4 = iteratorX.next();
		Expression e5 = iteratorX.next();
		assertTrue(e4 instanceof CellComparisonExpression);
		assertTrue(e5 instanceof CellComparisonExpression);

		// Y1 = cell, Y2 = cell
		// cell & cell | cell & cell | ? & ?
		Iterator<Expression> iteratorY = level1.get(1).getExpressions()
				.iterator();
		Expression e6 = iteratorY.next();
		Expression e7 = iteratorY.next();
		assertTrue(e6 instanceof CellComparisonExpression);
		assertTrue(e7 instanceof CellComparisonExpression);

		// Z1 = cell, Z2 = or
		// cell & cell | cell & cell | cell & (|)
		Iterator<Expression> iteratorZ = level1.get(2).getExpressions()
				.iterator();
		Expression e8 = iteratorZ.next();
		Expression e9 = iteratorZ.next();
		if (e8 instanceof CellComparisonExpression) {
			assertTrue(e9 instanceof OrExpression);
		} else if (e8 instanceof OrExpression) {
			assertTrue(e9 instanceof CellComparisonExpression);
		} else {
			fail("expected " + CellComparisonExpression.class + " or "
					+ OrExpression.class + " but was " + e8.getClass());
		}

		// check X1 + X2
		// (0,0)=A & (0,1)=A | cell & cell | cell & (|)
		CellComparisonExpression x1 = (CellComparisonExpression) e4;
		CellComparisonExpression x2 = (CellComparisonExpression) e5;
		if (x1.getConstraints().keySet().contains('A')
				&& x1.getTargets().contains(new Coords(0, 0))) {
			assertEquals(1, x1.getConstraints().keySet().size());
			assertTrue(x1.getConstraints().keySet().contains('A'));
			assertEquals(1, x1.getTargets().size());
			assertTrue(x1.getTargets().contains(new Coords(0, 0)));

			assertEquals(1, x2.getConstraints().keySet().size());
			assertTrue(x2.getConstraints().keySet().contains('A'));
			assertEquals(1, x2.getTargets().size());
			assertTrue(x2.getTargets().contains(new Coords(0, 1)));
		} else if (x1.getConstraints().keySet().contains('A')
				&& x1.getTargets().contains(new Coords(0, 1))) {
			assertEquals(1, x1.getConstraints().keySet().size());
			assertTrue(x1.getConstraints().keySet().contains('A'));
			assertEquals(1, x1.getTargets().size());
			assertTrue(x1.getTargets().contains(new Coords(0, 1)));

			assertEquals(1, x2.getConstraints().keySet().size());
			assertTrue(x2.getConstraints().keySet().contains('A'));
			assertEquals(1, x2.getTargets().size());
			assertTrue(x2.getTargets().contains(new Coords(0, 0)));
		} else if (x1.getConstraints().keySet().contains('B')
				&& x1.getTargets().contains(new Coords(0, 0))) {
			assertEquals(1, x1.getConstraints().keySet().size());
			assertTrue(x1.getConstraints().keySet().contains('B'));
			assertEquals(1, x1.getTargets().size());
			assertTrue(x1.getTargets().contains(new Coords(0, 0)));

			assertEquals(1, x2.getConstraints().keySet().size());
			assertTrue(x2.getConstraints().keySet().contains('B'));
			assertEquals(1, x2.getTargets().size());
			assertTrue(x2.getTargets().contains(new Coords(0, -1)));
		} else if (x1.getConstraints().keySet().contains('B')
				&& x1.getTargets().contains(new Coords(0, -1))) {
			assertEquals(1, x2.getConstraints().keySet().size());
			assertTrue(x2.getConstraints().keySet().contains('B'));
			assertEquals(1, x2.getTargets().size());
			assertTrue(x2.getTargets().contains(new Coords(0, 0)));

			assertEquals(1, x1.getConstraints().keySet().size());
			assertTrue(x1.getConstraints().keySet().contains('B'));
			assertEquals(1, x1.getTargets().size());
			assertTrue(x1.getTargets().contains(new Coords(0, -1)));
		}

		// check Y1 + Y2
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | cell & (|)
		CellComparisonExpression y1 = (CellComparisonExpression) e6;
		CellComparisonExpression y2 = (CellComparisonExpression) e7;
		if (y1.getConstraints().keySet().contains('A')
				&& y1.getTargets().contains(new Coords(0, 0))) {
			assertEquals(1, y1.getConstraints().keySet().size());
			assertTrue(y1.getConstraints().keySet().contains('A'));
			assertEquals(1, y1.getTargets().size());
			assertTrue(y1.getTargets().contains(new Coords(0, 0)));

			assertEquals(1, y2.getConstraints().keySet().size());
			assertTrue(y2.getConstraints().keySet().contains('A'));
			assertEquals(1, y2.getTargets().size());
			assertTrue(y2.getTargets().contains(new Coords(0, 1)));
		} else if (y1.getConstraints().keySet().contains('A')
				&& y1.getTargets().contains(new Coords(0, 1))) {
			assertEquals(1, y1.getConstraints().keySet().size());
			assertTrue(y1.getConstraints().keySet().contains('A'));
			assertEquals(1, y1.getTargets().size());
			assertTrue(y1.getTargets().contains(new Coords(0, 1)));

			assertEquals(1, y2.getConstraints().keySet().size());
			assertTrue(y2.getConstraints().keySet().contains('A'));
			assertEquals(1, y2.getTargets().size());
			assertTrue(y2.getTargets().contains(new Coords(0, 0)));
		} else if (y1.getConstraints().keySet().contains('B')
				&& y1.getTargets().contains(new Coords(0, 0))) {
			assertEquals(1, y1.getConstraints().keySet().size());
			assertTrue(y1.getConstraints().keySet().contains('B'));
			assertEquals(1, y1.getTargets().size());
			assertTrue(y1.getTargets().contains(new Coords(0, 0)));

			assertEquals(1, y2.getConstraints().keySet().size());
			assertTrue(y2.getConstraints().keySet().contains('B'));
			assertEquals(1, y2.getTargets().size());
			assertTrue(y2.getTargets().contains(new Coords(0, -1)));
		} else if (y1.getConstraints().keySet().contains('B')
				&& y1.getTargets().contains(new Coords(0, -1))) {
			assertEquals(1, y1.getConstraints().keySet().size());
			assertTrue(y1.getConstraints().keySet().contains('B'));
			assertEquals(1, y1.getTargets().size());
			assertTrue(y1.getTargets().contains(new Coords(0, -1)));

			assertEquals(1, y2.getConstraints().keySet().size());
			assertTrue(y2.getConstraints().keySet().contains('B'));
			assertEquals(1, y2.getTargets().size());
			assertTrue(y2.getTargets().contains(new Coords(0, 0)));
		}

		// check Z1
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & (|)
		CellComparisonExpression z1 = (CellComparisonExpression) (e8 instanceof CellComparisonExpression ? e8
				: e9);
		assertEquals(1, z1.getConstraints().keySet().size());
		assertTrue(z1.getConstraints().keySet().contains('C'));
		assertEquals(1, z1.getTargets().size());
		assertTrue(z1.getTargets().contains(new Coords(-1, 1)));

		// Z2 = Z2A | Z2B
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & ( ? | ? )
		OrExpression z2 = (OrExpression) (e8 instanceof OrExpression ? e8 : e9);
		assertEquals(2, z2.getExpressions().size());

		// Z2A = cell, Z2B = cell
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & ( cell | cell )
		Iterator<Expression> iteratorZ2 = z2.getExpressions().iterator();
		Expression e10 = iteratorZ2.next();
		Expression e11 = iteratorZ2.next();
		assertTrue(e10 instanceof CellComparisonExpression);
		assertTrue(e11 instanceof CellComparisonExpression);

		// check Z2A + Z2B
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & ( (0,0)=A | (0,0)=B )
		CellComparisonExpression z2a = (CellComparisonExpression) e10;
		CellComparisonExpression z2b = (CellComparisonExpression) e11;
		assertEquals(1, z2a.getTargets().size());
		assertTrue(z2a.getTargets().contains(new Coords(0, 0)));
		assertEquals(1, z2b.getTargets().size());
		assertTrue(z2b.getTargets().contains(new Coords(0, 0)));
		if (z2a.getConstraints().keySet().contains('A')) {
			assertEquals(1, z2a.getConstraints().keySet().size());
			assertTrue(z2a.getConstraints().keySet().contains('A'));

			assertEquals(1, z2b.getConstraints().keySet().size());
			assertTrue(z2b.getConstraints().keySet().contains('B'));
		} else if (z2a.getConstraints().keySet().contains('B')) {
			assertEquals(1, z2a.getConstraints().keySet().size());
			assertTrue(z2a.getConstraints().keySet().contains('B'));

			assertEquals(1, z2b.getConstraints().keySet().size());
			assertTrue(z2b.getConstraints().keySet().contains('A'));
		} else {
			fail("expected one of A or B but was " + z2a.getConstraints());
		}
	}

	@Test
	public void testSetExpressionForOriginCell() {
		Expression expression = ExpressionHelper.parseRulePart(COMPLEX_RULE, STATES);
		ICell<Character> origin = new GenericCell<Character>();
		ExpressionHelper.setExpressionForOriginCell(expression, origin);

		Collection<CellComparisonExpression> cellExpressions = new HashSet<CellComparisonExpression>();
		Collection<ComposedExpression> composedExpressions = new HashSet<ComposedExpression>();
		composedExpressions.add((ComposedExpression) expression);
		while (!composedExpressions.isEmpty()) {
			ComposedExpression composed = composedExpressions.iterator().next();
			composedExpressions.remove(composed);
			for (Expression subexpression : composed.getExpressions()) {
				if (subexpression instanceof ComposedExpression) {
					composedExpressions.add((ComposedExpression) subexpression);
				} else {
					cellExpressions
							.add((CellComparisonExpression) subexpression);
				}
			}
		}

		for (CellComparisonExpression subexpression : cellExpressions) {
			assertEquals(origin, subexpression.getOrigin());
		}
	}

}
