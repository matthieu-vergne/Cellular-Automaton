package org.cellularautomaton;

import junit.framework.TestCase;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.factory.RuleFactory;
import org.cellularautomaton.impl.GenericCell;
import org.junit.Assert;

public class CellTest extends TestCase {

	private RuleFactory<String> ruleFactory = new RuleFactory<String>();
	
	public void testCellState() {
		int dimension = 2;
		IRule<Integer> rule = new IRule<Integer>() {
			public Integer calculateNextStateOf(ICell<Integer> cell) {
				return cell.getCurrentState() + 1;
			}
		};
		ICell<Integer> cell = new GenericCell<Integer>(0, dimension, 3);
		cell.setRule(rule);
		assertEquals(Integer.valueOf(0), cell.getCurrentState());
		assertEquals(Integer.valueOf(0), cell.getState(0));
		assertEquals(Integer.valueOf(0), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());

		cell.calculateNextState();
		assertEquals(Integer.valueOf(0), cell.getCurrentState());
		assertEquals(Integer.valueOf(0), cell.getState(0));
		assertEquals(Integer.valueOf(0), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));
		assertTrue(cell.isNextStateCalculated());

		cell.applyNextState();
		assertEquals(Integer.valueOf(1), cell.getCurrentState());
		assertEquals(Integer.valueOf(1), cell.getState(0));
		assertEquals(Integer.valueOf(0), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());

		cell.calculateNextState();
		assertEquals(Integer.valueOf(1), cell.getCurrentState());
		assertEquals(Integer.valueOf(1), cell.getState(0));
		assertEquals(Integer.valueOf(0), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));
		assertTrue(cell.isNextStateCalculated());

		cell.applyNextState();
		assertEquals(Integer.valueOf(2), cell.getCurrentState());
		assertEquals(Integer.valueOf(2), cell.getState(0));
		assertEquals(Integer.valueOf(1), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());

		cell.calculateNextState();
		assertEquals(Integer.valueOf(2), cell.getCurrentState());
		assertEquals(Integer.valueOf(2), cell.getState(0));
		assertEquals(Integer.valueOf(1), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));
		assertTrue(cell.isNextStateCalculated());

		cell.applyNextState();
		assertEquals(Integer.valueOf(3), cell.getCurrentState());
		assertEquals(Integer.valueOf(3), cell.getState(0));
		assertEquals(Integer.valueOf(2), cell.getState(1));
		assertEquals(Integer.valueOf(1), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());

		cell.calculateNextState();
		assertEquals(Integer.valueOf(3), cell.getCurrentState());
		assertEquals(Integer.valueOf(3), cell.getState(0));
		assertEquals(Integer.valueOf(2), cell.getState(1));
		assertEquals(Integer.valueOf(1), cell.getState(2));
		assertTrue(cell.isNextStateCalculated());

		cell.applyNextState();
		assertEquals(Integer.valueOf(4), cell.getCurrentState());
		assertEquals(Integer.valueOf(4), cell.getState(0));
		assertEquals(Integer.valueOf(3), cell.getState(1));
		assertEquals(Integer.valueOf(2), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());

		cell.setCurrentState(18);
		assertEquals(Integer.valueOf(18), cell.getCurrentState());
		assertEquals(Integer.valueOf(18), cell.getState(0));
		assertEquals(Integer.valueOf(3), cell.getState(1));
		assertEquals(Integer.valueOf(2), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());
	}

	public void testCellDimensions() {
		ICell<String> cell;
		IRule<String> rule = ruleFactory.getStaticRuleInstance();

		cell = new GenericCell<String>("", 1, 1);
		cell.setRule(rule);
		assertEquals(1, cell.getDimensions());

		cell = new GenericCell<String>("", 2, 1);
		cell.setRule(rule);
		assertEquals(2, cell.getDimensions());

		cell = new GenericCell<String>("", 3, 1);
		cell.setRule(rule);
		assertEquals(3, cell.getDimensions());

	}

	public void testCellsAround() {
		int dimension = 2;
		IRule<String> rule = ruleFactory.getStaticRuleInstance();

		ICell<String> cell = new GenericCell<String>("middle", dimension, 1);
		cell.setRule(rule);
		ICell<String> neighborTop = new GenericCell<String>("top", dimension,
				1);
		neighborTop.setRule(rule);
		ICell<String> neighborBottom = new GenericCell<String>("bottom",
				dimension, 1);
		neighborBottom.setRule(rule);
		ICell<String> neighborLeft = new GenericCell<String>("left",
				dimension, 1);
		neighborLeft.setRule(rule);
		ICell<String> neighborRight = new GenericCell<String>("right",
				dimension, 1);
		neighborRight.setRule(rule);

		assertEquals(cell, cell.getPreviousCellOnDimension(0));
		assertEquals(cell, cell.getNextCellOnDimension(0));
		assertEquals(cell, cell.getPreviousCellOnDimension(1));
		assertEquals(cell, cell.getNextCellOnDimension(1));

		cell.setPreviousCellOnDimension(0, neighborLeft);
		neighborLeft.setNextCellOnDimension(0, cell);
		assertEquals(neighborLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(cell, cell.getNextCellOnDimension(0));
		assertEquals(cell, cell.getPreviousCellOnDimension(1));
		assertEquals(cell, cell.getNextCellOnDimension(1));

		cell.setNextCellOnDimension(0, neighborRight);
		neighborRight.setPreviousCellOnDimension(0, cell);
		assertEquals(neighborLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(neighborRight, cell.getNextCellOnDimension(0));
		assertEquals(cell, cell.getPreviousCellOnDimension(1));
		assertEquals(cell, cell.getNextCellOnDimension(1));

		cell.setPreviousCellOnDimension(1, neighborBottom);
		neighborBottom.setNextCellOnDimension(1, cell);
		assertEquals(neighborLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(neighborRight, cell.getNextCellOnDimension(0));
		assertEquals(neighborBottom, cell.getPreviousCellOnDimension(1));
		assertEquals(cell, cell.getNextCellOnDimension(1));

		cell.setNextCellOnDimension(1, neighborTop);
		neighborTop.setPreviousCellOnDimension(1, cell);
		assertEquals(neighborLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(neighborRight, cell.getNextCellOnDimension(0));
		assertEquals(neighborBottom, cell.getPreviousCellOnDimension(1));
		assertEquals(neighborTop, cell.getNextCellOnDimension(1));

		ICell<String> neighborTopLeft = new GenericCell<String>("top-left",
				dimension, 1);
		neighborTopLeft.setRule(rule);
		ICell<String> neighborTopRight = new GenericCell<String>("top-right",
				dimension, 1);
		neighborTopRight.setRule(rule);
		ICell<String> neighborBottomLeft = new GenericCell<String>(
				"bottom-left", dimension, 1);
		neighborBottomLeft.setRule(rule);
		ICell<String> neighborBottomRight = new GenericCell<String>(
				"bottom-right", dimension, 1);
		neighborBottomRight.setRule(rule);
		neighborTopLeft.setPreviousCellOnDimension(0, neighborTopRight);
		neighborTopLeft.setNextCellOnDimension(0, neighborTop);
		neighborTopLeft.setPreviousCellOnDimension(1, neighborLeft);
		neighborTopLeft.setNextCellOnDimension(1, neighborBottomLeft);
		neighborTopRight.setPreviousCellOnDimension(0, neighborTop);
		neighborTopRight.setNextCellOnDimension(0, neighborTopLeft);
		neighborTopRight.setPreviousCellOnDimension(1, neighborRight);
		neighborTopRight.setNextCellOnDimension(1, neighborBottomRight);
		neighborBottomLeft.setPreviousCellOnDimension(0, neighborBottomRight);
		neighborBottomLeft.setNextCellOnDimension(0, neighborBottom);
		neighborBottomLeft.setPreviousCellOnDimension(1, neighborTopLeft);
		neighborBottomLeft.setNextCellOnDimension(1, neighborLeft);
		neighborBottomRight.setPreviousCellOnDimension(0, neighborBottom);
		neighborBottomRight.setNextCellOnDimension(0, neighborBottomLeft);
		neighborBottomRight.setPreviousCellOnDimension(1, neighborTopRight);
		neighborBottomRight.setNextCellOnDimension(1, neighborRight);

		neighborTop.setPreviousCellOnDimension(0, neighborTopLeft);
		neighborTop.setNextCellOnDimension(0, neighborTopRight);
		neighborBottom.setPreviousCellOnDimension(0, neighborBottomLeft);
		neighborBottom.setNextCellOnDimension(0, neighborBottomRight);
		neighborLeft.setPreviousCellOnDimension(1, neighborBottomLeft);
		neighborLeft.setNextCellOnDimension(1, neighborTopLeft);
		neighborRight.setPreviousCellOnDimension(1, neighborBottomRight);
		neighborRight.setNextCellOnDimension(1, neighborTopRight);

		assertEquals(neighborBottomLeft, cell.getRelativeCell(-1, -1));
		assertEquals(neighborLeft, cell.getRelativeCell(-1, 0));
		assertEquals(neighborTopLeft, cell.getRelativeCell(-1, 1));
		assertEquals(neighborBottom, cell.getRelativeCell(0, -1));
		assertEquals(cell, cell.getRelativeCell(0, 0));
		assertEquals(neighborTop, cell.getRelativeCell(0, 1));
		assertEquals(neighborBottomRight, cell.getRelativeCell(1, -1));
		assertEquals(neighborRight, cell.getRelativeCell(1, 0));
		assertEquals(neighborTopRight, cell.getRelativeCell(1, 1));
	}

	public void testCoords() {
		ICell<String> cell = new GenericCell<String>("", 3, 1);
		cell.setRule(ruleFactory.getStaticRuleInstance());

		cell.setCoords(3, 5, 2);
		Assert.assertArrayEquals(new int[] { 3, 5, 2 }, cell.getCoords());

		cell.setCoords(1, 3, 4);
		Assert.assertArrayEquals(new int[] { 1, 3, 4 }, cell.getCoords());
	}
}
