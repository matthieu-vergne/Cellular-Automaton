package org.cellularautomaton.space.builder.expression;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.builder.ScriptSpaceBuilder;
import org.cellularautomaton.space.builder.expression.CellComparisonExpression;
import org.cellularautomaton.util.Coords;
import org.junit.Test;

public class CellComparisonExpressionTest {

	@Test
	public void testSimpleComparison() {
		String description = "[config]\n" + "states=X-\n" + "[cells]\n"
				+ "X-\n" + "-X\n";

		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);
		ICell<Character> cell00 = builder.getSpaceOfCell().getOrigin();

		CellComparisonExpression expression = new CellComparisonExpression();
		expression.addConstraint('X', 1);
		expression.addTarget(new Coords(0, 0));
		expression.setOrigin(cell00);
		assertTrue(expression.evaluate());

		expression.addConstraint('-', 1);
		assertFalse(expression.evaluate());

		expression.addTarget(new Coords(0, 1));
		assertTrue(expression.evaluate());
	}

	@Test
	public void testComplexComparison() {
		StringWriter out = new StringWriter();
		PrintWriter writer = new PrintWriter(out);
		writer.println("[config]");
		writer.println("states=X-");
		writer.println("[cells]");
		writer.println("X-");
		writer.println("-X");
		writer.close();
		String description = out.toString();

		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);
		ICell<Character> cell00 = builder.getSpaceOfCell().getOrigin();
		
		CellComparisonExpression expression = new CellComparisonExpression();
		expression.setOrigin(cell00);
		expression.addTarget(new Coords(0, 0));
		expression.addTarget(new Coords(0, 1));
		expression.addTarget(new Coords(1, 0));
		assertTrue(expression.evaluate());

		expression.addConstraint('X', 1);
		assertTrue(expression.evaluate());

		expression.addConstraint('-', 1);
		assertFalse(expression.evaluate());

		expression.addConstraint('-', 1);
		assertTrue(expression.evaluate());

		expression.addConstraint('X', 1);
		assertFalse(expression.evaluate());
	}

}
