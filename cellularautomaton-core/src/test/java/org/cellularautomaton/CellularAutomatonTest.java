package org.cellularautomaton;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.cellularautomaton.builder.CellSpaceBuilder;
import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.factory.CellFactory;
import org.junit.Test;

public class CellularAutomatonTest extends TestCase {

	@Test
	public void testGetAllCells() {
		// generate space of cells
		CellSpaceBuilder<String> builder = new CellSpaceBuilder<String>();
		builder.setInitialState("").setMemorySize(1).createNewSpace(2)
				.addDimension(4).addDimension(4);

		// get cells
		ICell<String> cell00 = builder.getSpaceOfCellOrigin();
		ICell<String> cell01 = cell00.getNextCellOnDimension(0);
		ICell<String> cell02 = cell01.getNextCellOnDimension(0);
		ICell<String> cell03 = cell02.getNextCellOnDimension(0);
		ICell<String> cell10 = cell00.getNextCellOnDimension(1);
		ICell<String> cell11 = cell10.getNextCellOnDimension(0);
		ICell<String> cell12 = cell11.getNextCellOnDimension(0);
		ICell<String> cell13 = cell12.getNextCellOnDimension(0);
		ICell<String> cell20 = cell10.getNextCellOnDimension(1);
		ICell<String> cell21 = cell20.getNextCellOnDimension(0);
		ICell<String> cell22 = cell21.getNextCellOnDimension(0);
		ICell<String> cell23 = cell22.getNextCellOnDimension(0);
		ICell<String> cell30 = cell20.getNextCellOnDimension(1);
		ICell<String> cell31 = cell30.getNextCellOnDimension(0);
		ICell<String> cell32 = cell31.getNextCellOnDimension(0);
		ICell<String> cell33 = cell32.getNextCellOnDimension(0);

		// generate automaton
		CellularAutomaton<String> automaton2D = new CellularAutomaton<String>(
				cell00);

		// test init
		Collection<ICell<String>> list = automaton2D.getAllCells();
		assertEquals(16, list.size());
		assertTrue(list.contains(cell00));
		assertTrue(list.contains(cell01));
		assertTrue(list.contains(cell02));
		assertTrue(list.contains(cell03));
		assertTrue(list.contains(cell10));
		assertTrue(list.contains(cell11));
		assertTrue(list.contains(cell12));
		assertTrue(list.contains(cell13));
		assertTrue(list.contains(cell20));
		assertTrue(list.contains(cell21));
		assertTrue(list.contains(cell22));
		assertTrue(list.contains(cell23));
		assertTrue(list.contains(cell30));
		assertTrue(list.contains(cell31));
		assertTrue(list.contains(cell32));
		assertTrue(list.contains(cell33));

		// test invariability (no ordering is considered)
		automaton2D.doStep();
		list = automaton2D.getAllCells();
		assertEquals(16, list.size());
		assertTrue(list.contains(cell00));
		assertTrue(list.contains(cell01));
		assertTrue(list.contains(cell02));
		assertTrue(list.contains(cell03));
		assertTrue(list.contains(cell10));
		assertTrue(list.contains(cell11));
		assertTrue(list.contains(cell12));
		assertTrue(list.contains(cell13));
		assertTrue(list.contains(cell20));
		assertTrue(list.contains(cell21));
		assertTrue(list.contains(cell22));
		assertTrue(list.contains(cell23));
		assertTrue(list.contains(cell30));
		assertTrue(list.contains(cell31));
		assertTrue(list.contains(cell32));
		assertTrue(list.contains(cell33));

		automaton2D.doStep();
		list = automaton2D.getAllCells();
		assertEquals(16, list.size());
		assertTrue(list.contains(cell00));
		assertTrue(list.contains(cell01));
		assertTrue(list.contains(cell02));
		assertTrue(list.contains(cell03));
		assertTrue(list.contains(cell10));
		assertTrue(list.contains(cell11));
		assertTrue(list.contains(cell12));
		assertTrue(list.contains(cell13));
		assertTrue(list.contains(cell20));
		assertTrue(list.contains(cell21));
		assertTrue(list.contains(cell22));
		assertTrue(list.contains(cell23));
		assertTrue(list.contains(cell30));
		assertTrue(list.contains(cell31));
		assertTrue(list.contains(cell32));
		assertTrue(list.contains(cell33));

		automaton2D.doStep();
		list = automaton2D.getAllCells();
		assertEquals(16, list.size());
		assertTrue(list.contains(cell00));
		assertTrue(list.contains(cell01));
		assertTrue(list.contains(cell02));
		assertTrue(list.contains(cell03));
		assertTrue(list.contains(cell10));
		assertTrue(list.contains(cell11));
		assertTrue(list.contains(cell12));
		assertTrue(list.contains(cell13));
		assertTrue(list.contains(cell20));
		assertTrue(list.contains(cell21));
		assertTrue(list.contains(cell22));
		assertTrue(list.contains(cell23));
		assertTrue(list.contains(cell30));
		assertTrue(list.contains(cell31));
		assertTrue(list.contains(cell32));
		assertTrue(list.contains(cell33));

		// test accessibility
		CellFactory<String> cellFactory = new CellFactory<String>()
				.setDimensions(2);
		ICell<String> intruderFullyAccessible = cellFactory.setInitialState(
				"total intruder").createCyclicCell();
		intruderFullyAccessible.setPreviousCellOnDimension(0, cell20);
		intruderFullyAccessible.setNextCellOnDimension(0, cell12);
		intruderFullyAccessible.setPreviousCellOnDimension(1, cell11);
		intruderFullyAccessible.setNextCellOnDimension(1, cell21);
		cell20.setNextCellOnDimension(0, intruderFullyAccessible);
		cell12.setPreviousCellOnDimension(0, intruderFullyAccessible);
		cell11.setNextCellOnDimension(1, intruderFullyAccessible);
		cell21.setPreviousCellOnDimension(1, intruderFullyAccessible);

		ICell<String> intruderPartiallyAccessible = cellFactory
				.setInitialState("partial intruder").createCyclicCell();
		intruderPartiallyAccessible.setPreviousCellOnDimension(0, cell32);
		intruderPartiallyAccessible.setNextCellOnDimension(0, cell20);
		intruderPartiallyAccessible.setPreviousCellOnDimension(1, cell23);
		intruderPartiallyAccessible.setNextCellOnDimension(1, cell33);
		cell23.setNextCellOnDimension(1, intruderPartiallyAccessible);

		ICell<String> intruderNotAccessible = cellFactory.setInitialState(
				"invisible intruder").createCyclicCell();
		intruderNotAccessible.setPreviousCellOnDimension(0, cell12);
		intruderNotAccessible.setNextCellOnDimension(0, cell00);
		intruderNotAccessible.setPreviousCellOnDimension(1, cell03);
		intruderNotAccessible.setNextCellOnDimension(1, cell13);

		list = automaton2D.getAllCells();
		assertEquals(18, list.size());
		assertTrue(list.contains(cell00));
		assertTrue(list.contains(cell01));
		assertTrue(list.contains(cell02));
		assertTrue(list.contains(cell03));
		assertTrue(list.contains(cell10));
		assertTrue(list.contains(cell11));
		assertTrue(list.contains(cell12));
		assertTrue(list.contains(cell13));
		assertTrue(list.contains(cell20));
		assertTrue(list.contains(cell21));
		assertTrue(list.contains(cell22));
		assertTrue(list.contains(cell23));
		assertTrue(list.contains(cell30));
		assertTrue(list.contains(cell31));
		assertTrue(list.contains(cell32));
		assertTrue(list.contains(cell33));
		assertTrue(list.contains(intruderFullyAccessible));
		assertTrue(list.contains(intruderPartiallyAccessible));
		assertFalse(list.contains(intruderNotAccessible));
	}

	@Test
	public void testCellsIterator() {
		// generate space of cells
		CellSpaceBuilder<String> builder = new CellSpaceBuilder<String>();
		builder.setInitialState("").setMemorySize(1).createNewSpace(2)
				.addDimension(4).addDimension(4);

		// get cells
		ICell<String> cell00 = builder.getSpaceOfCellOrigin();
		ICell<String> cell01 = cell00.getNextCellOnDimension(0);
		ICell<String> cell02 = cell01.getNextCellOnDimension(0);
		ICell<String> cell03 = cell02.getNextCellOnDimension(0);
		ICell<String> cell10 = cell00.getNextCellOnDimension(1);
		ICell<String> cell11 = cell10.getNextCellOnDimension(0);
		ICell<String> cell12 = cell11.getNextCellOnDimension(0);
		ICell<String> cell13 = cell12.getNextCellOnDimension(0);
		ICell<String> cell20 = cell10.getNextCellOnDimension(1);
		ICell<String> cell21 = cell20.getNextCellOnDimension(0);
		ICell<String> cell22 = cell21.getNextCellOnDimension(0);
		ICell<String> cell23 = cell22.getNextCellOnDimension(0);
		ICell<String> cell30 = cell20.getNextCellOnDimension(1);
		ICell<String> cell31 = cell30.getNextCellOnDimension(0);
		ICell<String> cell32 = cell31.getNextCellOnDimension(0);
		ICell<String> cell33 = cell32.getNextCellOnDimension(0);

		// generate automaton
		CellularAutomaton<String> automaton2D = new CellularAutomaton<String>(
				cell00);

		// test init
		Collection<ICell<String>> cellsToView = automaton2D.getAllCells();
		Iterator<ICell<String>> iterator = automaton2D.iterator();
		while (iterator.hasNext()) {
			ICell<String> cell = iterator.next();
			assertTrue(cellsToView.contains(cell));
			cellsToView.remove(cell);
		}
		assertTrue(cellsToView.isEmpty());

		// test invariability (no ordering is considered)
		automaton2D.doStep();
		cellsToView = automaton2D.getAllCells();
		iterator = automaton2D.iterator();
		while (iterator.hasNext()) {
			ICell<String> cell = iterator.next();
			assertTrue(cellsToView.contains(cell));
			cellsToView.remove(cell);
		}
		assertTrue(cellsToView.isEmpty());

		automaton2D.doStep();
		cellsToView = automaton2D.getAllCells();
		iterator = automaton2D.iterator();
		while (iterator.hasNext()) {
			ICell<String> cell = iterator.next();
			assertTrue(cellsToView.contains(cell));
			cellsToView.remove(cell);
		}
		assertTrue(cellsToView.isEmpty());

		automaton2D.doStep();
		cellsToView = automaton2D.getAllCells();
		iterator = automaton2D.iterator();
		while (iterator.hasNext()) {
			ICell<String> cell = iterator.next();
			assertTrue(cellsToView.contains(cell));
			cellsToView.remove(cell);
		}
		assertTrue(cellsToView.isEmpty());

		// test accessibility
		CellFactory<String> cellFactory = new CellFactory<String>()
				.setDimensions(2);
		ICell<String> intruderFullyAccessible = cellFactory.setInitialState(
				"total intruder").createCyclicCell();
		intruderFullyAccessible.setPreviousCellOnDimension(0, cell20);
		intruderFullyAccessible.setNextCellOnDimension(0, cell12);
		intruderFullyAccessible.setPreviousCellOnDimension(1, cell11);
		intruderFullyAccessible.setNextCellOnDimension(1, cell21);
		cell20.setNextCellOnDimension(0, intruderFullyAccessible);
		cell12.setPreviousCellOnDimension(0, intruderFullyAccessible);
		cell11.setNextCellOnDimension(1, intruderFullyAccessible);
		cell21.setPreviousCellOnDimension(1, intruderFullyAccessible);

		ICell<String> intruderPartiallyAccessible = cellFactory
				.setInitialState("partial intruder").createCyclicCell();
		intruderPartiallyAccessible.setPreviousCellOnDimension(0, cell32);
		intruderPartiallyAccessible.setNextCellOnDimension(0, cell20);
		intruderPartiallyAccessible.setPreviousCellOnDimension(1, cell23);
		intruderPartiallyAccessible.setNextCellOnDimension(1, cell33);
		cell23.setNextCellOnDimension(1, intruderPartiallyAccessible);

		ICell<String> intruderNotAccessible = cellFactory.setInitialState(
				"invisible intruder").createCyclicCell();
		intruderNotAccessible.setPreviousCellOnDimension(0, cell12);
		intruderNotAccessible.setNextCellOnDimension(0, cell00);
		intruderNotAccessible.setPreviousCellOnDimension(1, cell03);
		intruderNotAccessible.setNextCellOnDimension(1, cell13);

		cellsToView = automaton2D.getAllCells();
		iterator = automaton2D.iterator();
		while (iterator.hasNext()) {
			ICell<String> cell = iterator.next();
			assertTrue(cellsToView.contains(cell));
			cellsToView.remove(cell);
		}
		assertTrue(cellsToView.isEmpty());
	}

	@Test
	public void testOriginCell() {
		// generate space of cells
		CellSpaceBuilder<String> builder = new CellSpaceBuilder<String>();
		builder.setInitialState("").setMemorySize(1).createNewSpace(1)
				.addDimension(3);

		// get cells
		ICell<String> cell0 = builder.getSpaceOfCellOrigin();

		// generate automaton
		CellularAutomaton<String> automaton1D = new CellularAutomaton<String>(
				cell0);

		// check init
		assertEquals(cell0, automaton1D.getOriginCell());

		// check invariability
		automaton1D.doStep();
		assertEquals(cell0, automaton1D.getOriginCell());

		automaton1D.doStep();
		assertEquals(cell0, automaton1D.getOriginCell());

		automaton1D.doStep();
		assertEquals(cell0, automaton1D.getOriginCell());
	}

	@Test
	public void testEvolutionOf1DAutomaton() {
		// generate space of cells
		CellSpaceBuilder<String> builder = new CellSpaceBuilder<String>();
		builder.setInitialState("").setMemorySize(1)
				.setRule(new IRule<String>() {
					public String calculateNextStateOf(ICell<String> cell) {
						return cell.getRelativeCell(-1).getCurrentState()
								+ cell.getRelativeCell(+1).getCurrentState();
					}
				}).createNewSpace(1).addDimension(4);

		// get cells
		ICell<String> cell0 = builder.getSpaceOfCellOrigin();
		ICell<String> cell1 = cell0.getNextCellOnDimension(0);
		ICell<String> cell2 = cell1.getNextCellOnDimension(0);
		ICell<String> cell3 = cell2.getNextCellOnDimension(0);
		cell0.setCurrentState("0");
		cell1.setCurrentState("1");
		cell2.setCurrentState("2");
		cell3.setCurrentState("3");

		// generate automaton
		CellularAutomaton<String> automaton1D = new CellularAutomaton<String>(
				cell0);

		// check init
		assertEquals("0", cell0.getCurrentState());
		assertEquals("1", cell1.getCurrentState());
		assertEquals("2", cell2.getCurrentState());
		assertEquals("3", cell3.getCurrentState());

		// test automaton
		automaton1D.doStep();

		assertEquals("31", cell0.getCurrentState());
		assertEquals("02", cell1.getCurrentState());
		assertEquals("13", cell2.getCurrentState());
		assertEquals("20", cell3.getCurrentState());

		automaton1D.doStep();

		assertEquals("2002", cell0.getCurrentState());
		assertEquals("3113", cell1.getCurrentState());
		assertEquals("0220", cell2.getCurrentState());
		assertEquals("1331", cell3.getCurrentState());
	}

	@Test
	public void testEvolutionOf2DAutomaton() {
		// generate space of cells
		CellSpaceBuilder<String> builder = new CellSpaceBuilder<String>();
		builder.setInitialState("").setMemorySize(1)
				.setRule(new IRule<String>() {
					public String calculateNextStateOf(ICell<String> cell) {
						return cell.getRelativeCell(-1, -1).getCurrentState()
								+ cell.getRelativeCell(+0, -1)
										.getCurrentState()
								+ cell.getRelativeCell(+1, -1)
										.getCurrentState()
								+ cell.getRelativeCell(-1, +0)
										.getCurrentState()
								+ cell.getRelativeCell(+0, +0)
										.getCurrentState() // current
								+ cell.getRelativeCell(+1, +0)
										.getCurrentState()
								+ cell.getRelativeCell(-1, +1)
										.getCurrentState()
								+ cell.getRelativeCell(+0, +1)
										.getCurrentState()
								+ cell.getRelativeCell(+1, +1)
										.getCurrentState();
					}
				}).createNewSpace(2).addDimension(4).addDimension(4);

		// get cells
		ICell<String> cell00 = builder.getSpaceOfCellOrigin();
		ICell<String> cell01 = cell00.getNextCellOnDimension(0);
		ICell<String> cell02 = cell01.getNextCellOnDimension(0);
		ICell<String> cell03 = cell02.getNextCellOnDimension(0);
		ICell<String> cell10 = cell00.getNextCellOnDimension(1);
		ICell<String> cell11 = cell10.getNextCellOnDimension(0);
		ICell<String> cell12 = cell11.getNextCellOnDimension(0);
		ICell<String> cell13 = cell12.getNextCellOnDimension(0);
		ICell<String> cell20 = cell10.getNextCellOnDimension(1);
		ICell<String> cell21 = cell20.getNextCellOnDimension(0);
		ICell<String> cell22 = cell21.getNextCellOnDimension(0);
		ICell<String> cell23 = cell22.getNextCellOnDimension(0);
		ICell<String> cell30 = cell20.getNextCellOnDimension(1);
		ICell<String> cell31 = cell30.getNextCellOnDimension(0);
		ICell<String> cell32 = cell31.getNextCellOnDimension(0);
		ICell<String> cell33 = cell32.getNextCellOnDimension(0);
		cell00.setCurrentState("00");
		cell01.setCurrentState("01");
		cell02.setCurrentState("02");
		cell03.setCurrentState("03");
		cell10.setCurrentState("10");
		cell11.setCurrentState("11");
		cell12.setCurrentState("12");
		cell13.setCurrentState("13");
		cell20.setCurrentState("20");
		cell21.setCurrentState("21");
		cell22.setCurrentState("22");
		cell23.setCurrentState("23");
		cell30.setCurrentState("30");
		cell31.setCurrentState("31");
		cell32.setCurrentState("32");
		cell33.setCurrentState("33");

		// generate automaton
		CellularAutomaton<String> automaton2D = new CellularAutomaton<String>(
				cell00);

		// check init
		assertEquals("00", cell00.getCurrentState());
		assertEquals("01", cell01.getCurrentState());
		assertEquals("02", cell02.getCurrentState());
		assertEquals("03", cell03.getCurrentState());
		assertEquals("10", cell10.getCurrentState());
		assertEquals("11", cell11.getCurrentState());
		assertEquals("12", cell12.getCurrentState());
		assertEquals("13", cell13.getCurrentState());
		assertEquals("20", cell20.getCurrentState());
		assertEquals("21", cell21.getCurrentState());
		assertEquals("22", cell22.getCurrentState());
		assertEquals("23", cell23.getCurrentState());
		assertEquals("30", cell30.getCurrentState());
		assertEquals("31", cell31.getCurrentState());
		assertEquals("32", cell32.getCurrentState());
		assertEquals("33", cell33.getCurrentState());

		// test automaton
		automaton2D.doStep();

		assertEquals("333031030001131011", cell00.getCurrentState());
		assertEquals("303132000102101112", cell01.getCurrentState());
		assertEquals("313233010203111213", cell02.getCurrentState());
		assertEquals("323330020300121310", cell03.getCurrentState());
		assertEquals("030001131011232021", cell10.getCurrentState());
		assertEquals("000102101112202122", cell11.getCurrentState());
		assertEquals("010203111213212223", cell12.getCurrentState());
		assertEquals("020300121310222320", cell13.getCurrentState());
		assertEquals("131011232021333031", cell20.getCurrentState());
		assertEquals("101112202122303132", cell21.getCurrentState());
		assertEquals("111213212223313233", cell22.getCurrentState());
		assertEquals("121310222320323330", cell23.getCurrentState());
		assertEquals("232021333031030001", cell30.getCurrentState());
		assertEquals("202122303132000102", cell31.getCurrentState());
		assertEquals("212223313233010203", cell32.getCurrentState());
		assertEquals("222320323330020300", cell33.getCurrentState());
	}

}
