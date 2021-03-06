package org.cellularautomaton.cell;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.rule.RuleFactory;
import org.cellularautomaton.util.Coords;
import org.junit.Assert;
import org.junit.Test;

public abstract class ICellTest {

	public abstract <StateType> ICell<StateType> createCell();

	@Test
	public void testNotUniqueInstanciation() {
		Collection<ICell<?>> cells = new HashSet<ICell<?>>();
		for (int i = 0; i < 100; i++) {
			ICell<Object> cell = createCell();
			assertNotNull(cell);
			assertFalse(cells.contains(cell));
			cells.add(cell);
		}
	}

	@Test
	public void testCellState() {
		ICell<Integer> cell = createCell();
		assertNotNull(cell);
		int memorySize = 3;
		cell.setMemory(memorySize, 0);
		assertEquals(memorySize, cell.getMemorySize());
		assertEquals(Integer.valueOf(0), cell.getCurrentState());
		assertEquals(Integer.valueOf(0), cell.getState(0));
		assertEquals(Integer.valueOf(0), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));

		cell.setRule(new IRule<Integer>() {
			public Integer calculateNextStateOf(ICell<Integer> cell) {
				return cell.getState(0) + cell.getState(1);
			}
		});
		cell.setCurrentState(1);
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
		assertFalse(cell.isNextStateDifferent());

		cell.applyNextState();
		assertEquals(Integer.valueOf(1), cell.getCurrentState());
		assertEquals(Integer.valueOf(1), cell.getState(0));
		assertEquals(Integer.valueOf(1), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());

		cell.calculateNextState();
		assertEquals(Integer.valueOf(1), cell.getCurrentState());
		assertEquals(Integer.valueOf(1), cell.getState(0));
		assertEquals(Integer.valueOf(1), cell.getState(1));
		assertEquals(Integer.valueOf(0), cell.getState(2));
		assertTrue(cell.isNextStateCalculated());
		assertTrue(cell.isNextStateDifferent());

		cell.applyNextState();
		assertEquals(Integer.valueOf(2), cell.getCurrentState());
		assertEquals(Integer.valueOf(2), cell.getState(0));
		assertEquals(Integer.valueOf(1), cell.getState(1));
		assertEquals(Integer.valueOf(1), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());

		cell.calculateNextState();
		assertEquals(Integer.valueOf(2), cell.getCurrentState());
		assertEquals(Integer.valueOf(2), cell.getState(0));
		assertEquals(Integer.valueOf(1), cell.getState(1));
		assertEquals(Integer.valueOf(1), cell.getState(2));
		assertTrue(cell.isNextStateCalculated());
		assertTrue(cell.isNextStateDifferent());

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
		assertTrue(cell.isNextStateDifferent());

		cell.applyNextState();
		assertEquals(Integer.valueOf(5), cell.getCurrentState());
		assertEquals(Integer.valueOf(5), cell.getState(0));
		assertEquals(Integer.valueOf(3), cell.getState(1));
		assertEquals(Integer.valueOf(2), cell.getState(2));
		assertFalse(cell.isNextStateCalculated());
	}

	@Test
	public void testCellDimensions() {
		ICell<String> cell = createCell();
		assertNotNull(cell);
		cell.setMemory(1, "");

		cell.setDimensions(2);
		assertEquals(2, cell.getDimensions());

		cell.setDimensions(1);
		assertEquals(1, cell.getDimensions());

		cell.setDimensions(5);
		assertEquals(5, cell.getDimensions());
	}

	@Test
	public void testCellsAround() {
		int dimensions = 2;
		IRule<String> rule = new RuleFactory<String>().getStaticRuleInstance();

		ICell<String> cell = createCell();
		assertNotNull(cell);
		cell.setMemory(1, "middle");
		cell.setRule(rule);
		cell.setDimensions(dimensions);
		ICell<String> neighborTop = createCell();
		assertNotNull(neighborTop);
		cell.setMemory(1, "top");
		neighborTop.setRule(rule);
		neighborTop.setDimensions(dimensions);
		ICell<String> neighborBottom = createCell();
		assertNotNull(neighborBottom);
		cell.setMemory(1, "bottom");
		neighborBottom.setRule(rule);
		neighborBottom.setDimensions(dimensions);
		ICell<String> neighborLeft = createCell();
		assertNotNull(neighborLeft);
		cell.setMemory(1, "left");
		neighborLeft.setRule(rule);
		neighborLeft.setDimensions(dimensions);
		ICell<String> neighborRight = createCell();
		assertNotNull(neighborRight);
		cell.setMemory(1, "right");
		neighborRight.setRule(rule);
		neighborRight.setDimensions(dimensions);

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

		ICell<String> neighborTopLeft = createCell();
		assertNotNull(neighborTopLeft);
		cell.setMemory(1, "top-left");
		neighborTopLeft.setRule(rule);
		neighborTopLeft.setDimensions(dimensions);
		ICell<String> neighborTopRight = createCell();
		assertNotNull(neighborTopRight);
		cell.setMemory(1, "top-right");
		neighborTopRight.setRule(rule);
		neighborTopRight.setDimensions(dimensions);
		ICell<String> neighborBottomLeft = createCell();
		assertNotNull(neighborBottomLeft);
		cell.setMemory(1, "bottom-left");
		neighborBottomLeft.setRule(rule);
		neighborBottomLeft.setDimensions(dimensions);
		ICell<String> neighborBottomRight = createCell();
		assertNotNull(neighborBottomRight);
		cell.setMemory(1, "bottom-right");
		neighborBottomRight.setRule(rule);
		neighborBottomRight.setDimensions(dimensions);

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

	@Test
	public void testCoords() {
		ICell<String> cell = createCell();
		assertNotNull(cell);
		cell.setMemory(1, "");
		Assert.assertEquals(new Coords(), cell.getCoords());

		cell.setDimensions(3);
		Assert.assertEquals(new Coords(0, 0, 0), cell.getCoords());

		cell.getCoords().setAll(1, 3, 4);
		Assert.assertEquals(new Coords(1, 3, 4), cell.getCoords());

		cell.setDimensions(4);
		Assert.assertEquals(new Coords(1, 3, 4, 0), cell.getCoords());

		cell.setDimensions(2);
		Assert.assertEquals(new Coords(1, 3), cell.getCoords());
	}
}
