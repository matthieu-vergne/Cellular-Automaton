package org.cellularautomaton.space.expression;

import static org.junit.Assert.*;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.FileSpaceBuilder;
import org.cellularautomaton.util.Coords;
import org.junit.Test;

public class CellComparisonExpressionTest {

	@Test
	public void testComparison() {
		String description = "[config]\n" + "states=X-\n" + "[cells]\n"
				+ "X-\n" + "-X\n";

		FileSpaceBuilder builder = new FileSpaceBuilder();
		builder.createSpaceFromString(description);
		ICell<Character> cell00 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell01 = cell00.getNextCellOnDimension(1);

		CellComparisonExpression expression = new CellComparisonExpression();
		expression.setReference('X');
		expression.setTarget(new Coords(0, 0));
		expression.setOrigin(cell00);
		assertTrue(expression.evaluate());

		expression.setReference('-');
		assertFalse(expression.evaluate());

		expression.setTarget(new Coords(0, 1));
		assertTrue(expression.evaluate());

		expression.setOrigin(cell01);
		assertFalse(expression.evaluate());
	}

}
