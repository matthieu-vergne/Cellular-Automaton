package org.cellularautomaton.cell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.cellularautomaton.rule.StaticRule;
import org.junit.Test;

public class CellFactoryTest {

	@Test
	public void testInitialParameters() {
		CellFactory<String> factory = new CellFactory<String>();
		assertEquals(null, factory.getInitialState());
		assertEquals(0, factory.getDimensions());
		assertEquals(1, factory.getMemorySize());
		assertEquals(StaticRule.class, factory.getRule().getClass());
	}

	/**
	 * Test for isolated cell instance.
	 * 
	 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
	 * 
	 */
	static public class CellTest extends ICellTest {
		public <StateType> ICell<StateType> createCell() {
			return new CellFactory<StateType>().createCell();
		};

		@Test
		public void testIsIsolated() {
			CellFactory<String> factory = new CellFactory<String>();
			factory.setInitialState("");

			factory.setDimensions(1);
			ICell<String> cell = factory.createCell();
			assertNull(cell.getPreviousCellOnDimension(0));
			assertNull(cell.getNextCellOnDimension(0));

			factory.setDimensions(2);
			cell = factory.createCell();
			assertNull(cell.getPreviousCellOnDimension(0));
			assertNull(cell.getNextCellOnDimension(0));
			assertNull(cell.getPreviousCellOnDimension(1));
			assertNull(cell.getNextCellOnDimension(1));

			factory.setDimensions(3);
			cell = factory.createCell();
			assertNull(cell.getPreviousCellOnDimension(0));
			assertNull(cell.getNextCellOnDimension(0));
			assertNull(cell.getPreviousCellOnDimension(1));
			assertNull(cell.getNextCellOnDimension(1));
			assertNull(cell.getPreviousCellOnDimension(2));
			assertNull(cell.getNextCellOnDimension(2));
		}
	}

	/**
	 * Test for cyclic cell instance.
	 * 
	 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
	 * 
	 */
	static public class CyclicCellTest extends ICellTest {
		public <StateType> ICell<StateType> createCell() {
			return new CellFactory<StateType>().createCyclicCell();
		};

		@Test
		public void testIsCyclic() {
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
}
