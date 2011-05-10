package org.cellularautomaton.definition;

import junit.framework.TestCase;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.factory.RuleFactory;
import org.junit.Assert;

/**
 * This test case is a model for all the {@link ICell} implementations. All the
 * implementations of the {@link ICell} interface must have a test class
 * extending this test case.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
public abstract class ICellTest extends TestCase {

	public abstract <StateType> ICell<StateType> createCell(
			StateType initialState, int dimensions, int memorySize);

	private RuleFactory<String> ruleFactory = new RuleFactory<String>();

	public void testCellState() {
		int dimension = 2;
		IRule<Integer> rule = new IRule<Integer>() {
			public Integer calculateNextStateOf(ICell<Integer> cell) {
				return cell.getCurrentState() + 1;
			}
		};
		ICell<Integer> cell = createCell(0, dimension, 3);
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

		cell = createCell("", 1, 1);
		cell.setRule(rule);
		assertEquals(1, cell.getDimensions());

		cell = createCell("", 2, 1);
		cell.setRule(rule);
		assertEquals(2, cell.getDimensions());

		cell = createCell("", 3, 1);
		cell.setRule(rule);
		assertEquals(3, cell.getDimensions());

	}

	public void testCellsAround() {
		int dimension = 2;
		IRule<String> rule = ruleFactory.getStaticRuleInstance();

		ICell<String> cell = createCell("middle", dimension, 1);
		cell.setRule(rule);
		ICell<String> neighborTop = createCell("top", dimension, 1);
		neighborTop.setRule(rule);
		ICell<String> neighborBottom = createCell("bottom", dimension, 1);
		neighborBottom.setRule(rule);
		ICell<String> neighborLeft = createCell("left", dimension, 1);
		neighborLeft.setRule(rule);
		ICell<String> neighborRight = createCell("right", dimension, 1);
		neighborRight.setRule(rule);

		ICell<String> actualLeft = cell.getPreviousCellOnDimension(0);
		ICell<String> actualRight = cell.getNextCellOnDimension(0);
		ICell<String> actualBottom = cell.getPreviousCellOnDimension(1);
		ICell<String> actualTop = cell.getNextCellOnDimension(1);
		
		assertEquals(actualLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(actualRight, cell.getNextCellOnDimension(0));
		assertEquals(actualBottom, cell.getPreviousCellOnDimension(1));
		assertEquals(actualTop, cell.getNextCellOnDimension(1));

		cell.setPreviousCellOnDimension(0, neighborLeft);
		neighborLeft.setNextCellOnDimension(0, cell);
		assertEquals(neighborLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(actualRight, cell.getNextCellOnDimension(0));
		assertEquals(actualBottom, cell.getPreviousCellOnDimension(1));
		assertEquals(actualTop, cell.getNextCellOnDimension(1));

		cell.setNextCellOnDimension(0, neighborRight);
		neighborRight.setPreviousCellOnDimension(0, cell);
		assertEquals(neighborLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(neighborRight, cell.getNextCellOnDimension(0));
		assertEquals(actualBottom, cell.getPreviousCellOnDimension(1));
		assertEquals(actualTop, cell.getNextCellOnDimension(1));

		cell.setPreviousCellOnDimension(1, neighborBottom);
		neighborBottom.setNextCellOnDimension(1, cell);
		assertEquals(neighborLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(neighborRight, cell.getNextCellOnDimension(0));
		assertEquals(neighborBottom, cell.getPreviousCellOnDimension(1));
		assertEquals(actualTop, cell.getNextCellOnDimension(1));

		cell.setNextCellOnDimension(1, neighborTop);
		neighborTop.setPreviousCellOnDimension(1, cell);
		assertEquals(neighborLeft, cell.getPreviousCellOnDimension(0));
		assertEquals(neighborRight, cell.getNextCellOnDimension(0));
		assertEquals(neighborBottom, cell.getPreviousCellOnDimension(1));
		assertEquals(neighborTop, cell.getNextCellOnDimension(1));

		ICell<String> neighborTopLeft = createCell("top-left", dimension, 1);
		neighborTopLeft.setRule(rule);
		ICell<String> neighborTopRight = createCell("top-right", dimension, 1);
		neighborTopRight.setRule(rule);
		ICell<String> neighborBottomLeft = createCell("bottom-left", dimension,
				1);
		neighborBottomLeft.setRule(rule);
		ICell<String> neighborBottomRight = createCell("bottom-right",
				dimension, 1);
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
		ICell<String> cell = createCell("", 3, 1);
		cell.setRule(ruleFactory.getStaticRuleInstance());

		cell.setCoords(3, 5, 2);
		Assert.assertArrayEquals(new int[] { 3, 5, 2 }, cell.getCoords());

		cell.setCoords(1, 3, 4);
		Assert.assertArrayEquals(new int[] { 1, 3, 4 }, cell.getCoords());
	}
}
