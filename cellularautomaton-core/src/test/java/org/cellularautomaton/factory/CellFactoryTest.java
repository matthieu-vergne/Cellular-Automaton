package org.cellularautomaton.factory;

import junit.framework.TestCase;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.ICellTest;
import org.cellularautomaton.impl.StaticRule;
import org.junit.Test;

public class CellFactoryTest extends TestCase {

	@Test
	public void testInitialParameters() {
		CellFactory<String> factory = new CellFactory<String>();
		assertEquals(null, factory.getInitialState());
		assertEquals(1, factory.getDimensions());
		assertEquals(1, factory.getMemorySize());
		assertEquals(StaticRule.class, factory.getRule().getClass());
	}

	/**
	 * Basic test for isolated cell instance.
	 * 
	 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
	 * 
	 */
	static public class IsolatedCellTest extends ICellTest {
		public <StateType> ICell<StateType> createCell(StateType initialState,
				int memorySize) {
			CellFactory<StateType> factory = new CellFactory<StateType>();
			return factory.setInitialState(initialState)
					.setMemorySize(memorySize).createIsolatedCell();
		};

		@Test
		public void testIsIsolated() {
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
	}

	/**
	 * Basic test for cyclic cell instance.
	 * 
	 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
	 * 
	 */
	static public class CyclicCellTest extends ICellTest {
		public <StateType> ICell<StateType> createCell(StateType initialState,
				int memorySize) {
			CellFactory<StateType> factory = new CellFactory<StateType>();
			return factory.setInitialState(initialState)
					.setMemorySize(memorySize).createCyclicCell();
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
