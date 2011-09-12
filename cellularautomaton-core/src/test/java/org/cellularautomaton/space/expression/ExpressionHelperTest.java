package org.cellularautomaton.space.expression;

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
import org.cellularautomaton.util.Coords;
import org.junit.Test;

public class ExpressionHelperTest {

	private static final String RULE = "(0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & ( =A | =B )";
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
	public void testParseRulePart() {
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & ( =A | =B )
		Expression expression = ExpressionHelper.parseRulePart(RULE, STATES);
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
		if (x1.getReference().equals('A')
				&& x1.getTarget().equals(new Coords(0, 0))) {
			assertEquals('A', (char) x2.getReference());
			assertEquals(new Coords(0, 1), x2.getTarget());
		} else if (x1.getReference().equals('A')
				&& x1.getTarget().equals(new Coords(0, 1))) {
			assertEquals('A', (char) x2.getReference());
			assertEquals(new Coords(0, 0), x2.getTarget());
		} else if (x1.getReference().equals('B')
				&& x1.getTarget().equals(new Coords(0, 0))) {
			assertEquals('B', (char) x2.getReference());
			assertEquals(new Coords(0, -1), x2.getTarget());
		} else if (x1.getReference().equals('B')
				&& x1.getTarget().equals(new Coords(0, -1))) {
			assertEquals('B', (char) x2.getReference());
			assertEquals(new Coords(0, 0), x2.getTarget());
		}

		// check Y1 + Y2
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | cell & (|)
		CellComparisonExpression y1 = (CellComparisonExpression) e6;
		CellComparisonExpression y2 = (CellComparisonExpression) e7;
		if (y1.getReference().equals('A')
				&& y1.getTarget().equals(new Coords(0, 0))) {
			assertEquals('A', (char) y2.getReference());
			assertEquals(new Coords(0, 1), y2.getTarget());
		} else if (y1.getReference().equals('A')
				&& y1.getTarget().equals(new Coords(0, 1))) {
			assertEquals('A', (char) y2.getReference());
			assertEquals(new Coords(0, 0), y2.getTarget());
		} else if (y1.getReference().equals('B')
				&& y1.getTarget().equals(new Coords(0, 0))) {
			assertEquals('B', (char) y2.getReference());
			assertEquals(new Coords(0, -1), y2.getTarget());
		} else if (y1.getReference().equals('B')
				&& y1.getTarget().equals(new Coords(0, -1))) {
			assertEquals('B', (char) y2.getReference());
			assertEquals(new Coords(0, 0), y2.getTarget());
		}

		// check Z1
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & (|)
		CellComparisonExpression z1 = (CellComparisonExpression) (e8 instanceof CellComparisonExpression ? e8
				: e9);
		assertEquals('C', (char) z1.getReference());
		assertEquals(new Coords(-1, 1), z1.getTarget());

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
		// (0,0)=A & (0,1)=A | (0,0)=B & (0,-1)=B | (-1,+1)=C & ( =A | =B )
		CellComparisonExpression z2a = (CellComparisonExpression) e10;
		CellComparisonExpression z2b = (CellComparisonExpression) e11;
		assertEquals(CellComparisonExpression.ORIGIN, z2a.getTarget());
		assertEquals(CellComparisonExpression.ORIGIN, z2b.getTarget());
		if (z2a.getReference().equals('A')) {
			assertEquals('B', (char) z2b.getReference());
		} else if (z2a.getReference().equals('B')) {
			assertEquals('A', (char) z2b.getReference());
		} else {
			fail("expected A or B but was " + z2a.getReference());
		}
	}

	@Test
	public void testSetExpressionForOriginCell() {
		Expression expression = ExpressionHelper.parseRulePart(RULE, STATES);
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
