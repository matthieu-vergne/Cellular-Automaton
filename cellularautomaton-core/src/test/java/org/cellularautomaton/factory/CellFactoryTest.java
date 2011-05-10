package org.cellularautomaton.factory;

import org.cellularautomaton.definition.ICell;
import org.junit.Test;

import junit.framework.TestCase;


public class CellFactoryTest extends TestCase {

	@Test
	public void testIsolatedCell() {
		CellFactory<String> factory = new CellFactory<String>();
		factory.setInitialState("");
		
		factory.setDimensions(1);
		ICell<String> cell = factory.createIsolatedCell();
		assertNull(cell.getPreviousCellOnDimension(0));
		assertNull(cell.getNextCellOnDimension(0));
		
		factory.setDimensions(2);
		cell = factory.createIsolatedCell();
		assertNull(cell.getPreviousCellOnDimension(0));
		assertNull(cell.getNextCellOnDimension(0));
		assertNull(cell.getPreviousCellOnDimension(1));
		assertNull(cell.getNextCellOnDimension(1));
		
		factory.setDimensions(3);
		cell = factory.createIsolatedCell();
		assertNull(cell.getPreviousCellOnDimension(0));
		assertNull(cell.getNextCellOnDimension(0));
		assertNull(cell.getPreviousCellOnDimension(1));
		assertNull(cell.getNextCellOnDimension(1));
		assertNull(cell.getPreviousCellOnDimension(2));
		assertNull(cell.getNextCellOnDimension(2));
	}

	@Test
	public void testCyclicCell() {
		CellFactory<String> factory = new CellFactory<String>();
		factory.setInitialState("");
		
		factory.setDimensions(1);
		ICell<String> cell = factory.createCyclicCell();
		assertEquals(cell, cell.getPreviousCellOnDimension(0));
		assertEquals(cell, cell.getNextCellOnDimension(0));
		
		factory.setDimensions(2);
		cell = factory.createCyclicCell();
		assertEquals(cell, cell.getPreviousCellOnDimension(0));
		assertEquals(cell, cell.getNextCellOnDimension(0));
		assertEquals(cell, cell.getPreviousCellOnDimension(1));
		assertEquals(cell, cell.getNextCellOnDimension(1));
		
		factory.setDimensions(3);
		cell = factory.createCyclicCell();
		assertEquals(cell, cell.getPreviousCellOnDimension(0));
		assertEquals(cell, cell.getNextCellOnDimension(0));
		assertEquals(cell, cell.getPreviousCellOnDimension(1));
		assertEquals(cell, cell.getNextCellOnDimension(1));
		assertEquals(cell, cell.getPreviousCellOnDimension(2));
		assertEquals(cell, cell.getNextCellOnDimension(2));
	}
}
