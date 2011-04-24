package org.cellularautomaton;

import org.junit.Assert;

import junit.framework.TestCase;

public class CellTest extends TestCase {

	public void testCellState() {
		int dimension = 2;
		Cell<Integer> cell = new Cell<Integer>(0, dimension, 3) {
			@Override
			protected Integer calculateState() {
				return getCurrentState() + 1;
			}
		};
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
	}

	public void testCellDimensions() {
		Cell<String> cell;

		cell = new Cell<String>("", 1) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		assertEquals(1, cell.getDimensions());

		cell = new Cell<String>("", 2) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		assertEquals(2, cell.getDimensions());

		cell = new Cell<String>("", 3) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		assertEquals(3, cell.getDimensions());

	}

	public void testCellNeighbors() {
		int dimension = 2;
		Cell<String> cell = new Cell<String>("middle", dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		Cell<String> neighborTop = new Cell<String>("top", dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		Cell<String> neighborBottom = new Cell<String>("bottom", dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		Cell<String> neighborLeft = new Cell<String>("left", dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		Cell<String> neighborRight = new Cell<String>("right", dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
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

		Cell<String> neighborTopLeft = new Cell<String>("top-left", dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		Cell<String> neighborTopRight = new Cell<String>("top-right", dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		Cell<String> neighborBottomLeft = new Cell<String>("bottom-left",
				dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
		Cell<String> neighborBottomRight = new Cell<String>("bottom-right",
				dimension) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};
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

		assertEquals(neighborBottomLeft, cell.getRelativeNeighbor(-1, -1));
		assertEquals(neighborLeft, cell.getRelativeNeighbor(-1, 0));
		assertEquals(neighborTopLeft, cell.getRelativeNeighbor(-1, 1));
		assertEquals(neighborBottom, cell.getRelativeNeighbor(0, -1));
		assertEquals(cell, cell.getRelativeNeighbor(0, 0));
		assertEquals(neighborTop, cell.getRelativeNeighbor(0, 1));
		assertEquals(neighborBottomRight, cell.getRelativeNeighbor(1, -1));
		assertEquals(neighborRight, cell.getRelativeNeighbor(1, 0));
		assertEquals(neighborTopRight, cell.getRelativeNeighbor(1, 1));
	}

	public void testCoords() {
		Cell<String> cell = new Cell<String>("", 3) {
			@Override
			protected String calculateState() {
				return getCurrentState();
			}
		};

		cell.setCoords(3, 5, 2);
		Assert.assertArrayEquals(new int[] { 3, 5, 2 }, cell.getCoords());

		cell.setCoords(1, 3, 4);
		Assert.assertArrayEquals(new int[] { 1, 3, 4 }, cell.getCoords());
	}
}
