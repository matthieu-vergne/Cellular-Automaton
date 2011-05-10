package org.cellularautomaton;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.cellularautomaton.definition.ICell;
import org.cellularautomaton.definition.IRule;
import org.cellularautomaton.factory.CellFactory;

public class CellularAutomatonTest extends TestCase {

	private CellFactory<String> cellFactory = new CellFactory<String>();

	public void testGetAllCells() {
		// generate cells
		cellFactory.setDimensions(2);
		ICell<String> cell00 = cellFactory.setInitialState("00")
				.createCyclicCell();
		ICell<String> cell01 = cellFactory.setInitialState("01")
				.createCyclicCell();
		ICell<String> cell02 = cellFactory.setInitialState("02")
				.createCyclicCell();
		ICell<String> cell03 = cellFactory.setInitialState("03")
				.createCyclicCell();
		ICell<String> cell10 = cellFactory.setInitialState("10")
				.createCyclicCell();
		ICell<String> cell11 = cellFactory.setInitialState("11")
				.createCyclicCell();
		ICell<String> cell12 = cellFactory.setInitialState("12")
				.createCyclicCell();
		ICell<String> cell13 = cellFactory.setInitialState("13")
				.createCyclicCell();
		ICell<String> cell20 = cellFactory.setInitialState("20")
				.createCyclicCell();
		ICell<String> cell21 = cellFactory.setInitialState("21")
				.createCyclicCell();
		ICell<String> cell22 = cellFactory.setInitialState("22")
				.createCyclicCell();
		ICell<String> cell23 = cellFactory.setInitialState("23")
				.createCyclicCell();
		ICell<String> cell30 = cellFactory.setInitialState("30")
				.createCyclicCell();
		ICell<String> cell31 = cellFactory.setInitialState("31")
				.createCyclicCell();
		ICell<String> cell32 = cellFactory.setInitialState("32")
				.createCyclicCell();
		ICell<String> cell33 = cellFactory.setInitialState("33")
				.createCyclicCell();

		// link cells
		cell00.setPreviousCellOnDimension(0, cell03);
		cell00.setNextCellOnDimension(0, cell01);
		cell00.setPreviousCellOnDimension(1, cell30);
		cell00.setNextCellOnDimension(1, cell10);

		cell01.setPreviousCellOnDimension(0, cell00);
		cell01.setNextCellOnDimension(0, cell02);
		cell01.setPreviousCellOnDimension(1, cell31);
		cell01.setNextCellOnDimension(1, cell11);

		cell02.setPreviousCellOnDimension(0, cell01);
		cell02.setNextCellOnDimension(0, cell03);
		cell02.setPreviousCellOnDimension(1, cell32);
		cell02.setNextCellOnDimension(1, cell12);

		cell03.setPreviousCellOnDimension(0, cell02);
		cell03.setNextCellOnDimension(0, cell00);
		cell03.setPreviousCellOnDimension(1, cell33);
		cell03.setNextCellOnDimension(1, cell13);
		//
		cell10.setPreviousCellOnDimension(0, cell13);
		cell10.setNextCellOnDimension(0, cell11);
		cell10.setPreviousCellOnDimension(1, cell00);
		cell10.setNextCellOnDimension(1, cell20);

		cell11.setPreviousCellOnDimension(0, cell10);
		cell11.setNextCellOnDimension(0, cell12);
		cell11.setPreviousCellOnDimension(1, cell01);
		cell11.setNextCellOnDimension(1, cell21);

		cell12.setPreviousCellOnDimension(0, cell11);
		cell12.setNextCellOnDimension(0, cell13);
		cell12.setPreviousCellOnDimension(1, cell02);
		cell12.setNextCellOnDimension(1, cell22);

		cell13.setPreviousCellOnDimension(0, cell12);
		cell13.setNextCellOnDimension(0, cell10);
		cell13.setPreviousCellOnDimension(1, cell03);
		cell13.setNextCellOnDimension(1, cell23);
		//
		cell20.setPreviousCellOnDimension(0, cell23);
		cell20.setNextCellOnDimension(0, cell21);
		cell20.setPreviousCellOnDimension(1, cell10);
		cell20.setNextCellOnDimension(1, cell30);

		cell21.setPreviousCellOnDimension(0, cell20);
		cell21.setNextCellOnDimension(0, cell22);
		cell21.setPreviousCellOnDimension(1, cell11);
		cell21.setNextCellOnDimension(1, cell31);

		cell22.setPreviousCellOnDimension(0, cell21);
		cell22.setNextCellOnDimension(0, cell23);
		cell22.setPreviousCellOnDimension(1, cell12);
		cell22.setNextCellOnDimension(1, cell32);

		cell23.setPreviousCellOnDimension(0, cell22);
		cell23.setNextCellOnDimension(0, cell20);
		cell23.setPreviousCellOnDimension(1, cell13);
		cell23.setNextCellOnDimension(1, cell33);
		//
		cell30.setPreviousCellOnDimension(0, cell33);
		cell30.setNextCellOnDimension(0, cell31);
		cell30.setPreviousCellOnDimension(1, cell20);
		cell30.setNextCellOnDimension(1, cell00);

		cell31.setPreviousCellOnDimension(0, cell30);
		cell31.setNextCellOnDimension(0, cell32);
		cell31.setPreviousCellOnDimension(1, cell21);
		cell31.setNextCellOnDimension(1, cell01);

		cell32.setPreviousCellOnDimension(0, cell31);
		cell32.setNextCellOnDimension(0, cell33);
		cell32.setPreviousCellOnDimension(1, cell22);
		cell32.setNextCellOnDimension(1, cell02);

		cell33.setPreviousCellOnDimension(0, cell32);
		cell33.setNextCellOnDimension(0, cell30);
		cell33.setPreviousCellOnDimension(1, cell23);
		cell33.setNextCellOnDimension(1, cell03);

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

	public void testCellsIterator() {
		// generate cells
		cellFactory.setDimensions(2);
		ICell<String> cell00 = cellFactory.setInitialState("00")
				.createCyclicCell();
		ICell<String> cell01 = cellFactory.setInitialState("01")
				.createCyclicCell();
		ICell<String> cell02 = cellFactory.setInitialState("02")
				.createCyclicCell();
		ICell<String> cell03 = cellFactory.setInitialState("03")
				.createCyclicCell();
		ICell<String> cell10 = cellFactory.setInitialState("10")
				.createCyclicCell();
		ICell<String> cell11 = cellFactory.setInitialState("11")
				.createCyclicCell();
		ICell<String> cell12 = cellFactory.setInitialState("12")
				.createCyclicCell();
		ICell<String> cell13 = cellFactory.setInitialState("13")
				.createCyclicCell();
		ICell<String> cell20 = cellFactory.setInitialState("20")
				.createCyclicCell();
		ICell<String> cell21 = cellFactory.setInitialState("21")
				.createCyclicCell();
		ICell<String> cell22 = cellFactory.setInitialState("22")
				.createCyclicCell();
		ICell<String> cell23 = cellFactory.setInitialState("23")
				.createCyclicCell();
		ICell<String> cell30 = cellFactory.setInitialState("30")
				.createCyclicCell();
		ICell<String> cell31 = cellFactory.setInitialState("31")
				.createCyclicCell();
		ICell<String> cell32 = cellFactory.setInitialState("32")
				.createCyclicCell();
		ICell<String> cell33 = cellFactory.setInitialState("33")
				.createCyclicCell();

		// link cells
		cell00.setPreviousCellOnDimension(0, cell03);
		cell00.setNextCellOnDimension(0, cell01);
		cell00.setPreviousCellOnDimension(1, cell30);
		cell00.setNextCellOnDimension(1, cell10);

		cell01.setPreviousCellOnDimension(0, cell00);
		cell01.setNextCellOnDimension(0, cell02);
		cell01.setPreviousCellOnDimension(1, cell31);
		cell01.setNextCellOnDimension(1, cell11);

		cell02.setPreviousCellOnDimension(0, cell01);
		cell02.setNextCellOnDimension(0, cell03);
		cell02.setPreviousCellOnDimension(1, cell32);
		cell02.setNextCellOnDimension(1, cell12);

		cell03.setPreviousCellOnDimension(0, cell02);
		cell03.setNextCellOnDimension(0, cell00);
		cell03.setPreviousCellOnDimension(1, cell33);
		cell03.setNextCellOnDimension(1, cell13);
		//
		cell10.setPreviousCellOnDimension(0, cell13);
		cell10.setNextCellOnDimension(0, cell11);
		cell10.setPreviousCellOnDimension(1, cell00);
		cell10.setNextCellOnDimension(1, cell20);

		cell11.setPreviousCellOnDimension(0, cell10);
		cell11.setNextCellOnDimension(0, cell12);
		cell11.setPreviousCellOnDimension(1, cell01);
		cell11.setNextCellOnDimension(1, cell21);

		cell12.setPreviousCellOnDimension(0, cell11);
		cell12.setNextCellOnDimension(0, cell13);
		cell12.setPreviousCellOnDimension(1, cell02);
		cell12.setNextCellOnDimension(1, cell22);

		cell13.setPreviousCellOnDimension(0, cell12);
		cell13.setNextCellOnDimension(0, cell10);
		cell13.setPreviousCellOnDimension(1, cell03);
		cell13.setNextCellOnDimension(1, cell23);
		//
		cell20.setPreviousCellOnDimension(0, cell23);
		cell20.setNextCellOnDimension(0, cell21);
		cell20.setPreviousCellOnDimension(1, cell10);
		cell20.setNextCellOnDimension(1, cell30);

		cell21.setPreviousCellOnDimension(0, cell20);
		cell21.setNextCellOnDimension(0, cell22);
		cell21.setPreviousCellOnDimension(1, cell11);
		cell21.setNextCellOnDimension(1, cell31);

		cell22.setPreviousCellOnDimension(0, cell21);
		cell22.setNextCellOnDimension(0, cell23);
		cell22.setPreviousCellOnDimension(1, cell12);
		cell22.setNextCellOnDimension(1, cell32);

		cell23.setPreviousCellOnDimension(0, cell22);
		cell23.setNextCellOnDimension(0, cell20);
		cell23.setPreviousCellOnDimension(1, cell13);
		cell23.setNextCellOnDimension(1, cell33);
		//
		cell30.setPreviousCellOnDimension(0, cell33);
		cell30.setNextCellOnDimension(0, cell31);
		cell30.setPreviousCellOnDimension(1, cell20);
		cell30.setNextCellOnDimension(1, cell00);

		cell31.setPreviousCellOnDimension(0, cell30);
		cell31.setNextCellOnDimension(0, cell32);
		cell31.setPreviousCellOnDimension(1, cell21);
		cell31.setNextCellOnDimension(1, cell01);

		cell32.setPreviousCellOnDimension(0, cell31);
		cell32.setNextCellOnDimension(0, cell33);
		cell32.setPreviousCellOnDimension(1, cell22);
		cell32.setNextCellOnDimension(1, cell02);

		cell33.setPreviousCellOnDimension(0, cell32);
		cell33.setNextCellOnDimension(0, cell30);
		cell33.setPreviousCellOnDimension(1, cell23);
		cell33.setNextCellOnDimension(1, cell03);

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

	public void testOrigineCell() {
		// generate cells
		cellFactory.setDimensions(1);
		ICell<String> cell0 = cellFactory.setInitialState("0")
				.createCyclicCell();
		ICell<String> cell1 = cellFactory.setInitialState("1")
				.createCyclicCell();
		ICell<String> cell2 = cellFactory.setInitialState("2")
				.createCyclicCell();
		ICell<String> cell3 = cellFactory.setInitialState("3")
				.createCyclicCell();

		// link cells
		cell0.setPreviousCellOnDimension(0, cell3);
		cell0.setNextCellOnDimension(0, cell1);
		cell1.setPreviousCellOnDimension(0, cell0);
		cell1.setNextCellOnDimension(0, cell2);
		cell2.setPreviousCellOnDimension(0, cell1);
		cell2.setNextCellOnDimension(0, cell3);
		cell3.setPreviousCellOnDimension(0, cell2);
		cell3.setNextCellOnDimension(0, cell0);

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

	public void testEvolutionOnExistentSpaceOfCells1D() {
		// generate cells
		cellFactory.setDimensions(1);
		cellFactory.setRule(new IRule<String>() {
			public String calculateNextStateOf(ICell<String> cell) {
				return cell.getRelativeCell(-1).getCurrentState()
						+ cell.getRelativeCell(+1).getCurrentState();
			}
		});
		ICell<String> cell0 = cellFactory.setInitialState("0")
				.createCyclicCell();
		ICell<String> cell1 = cellFactory.setInitialState("1")
				.createCyclicCell();
		ICell<String> cell2 = cellFactory.setInitialState("2")
				.createCyclicCell();
		ICell<String> cell3 = cellFactory.setInitialState("3")
				.createCyclicCell();

		// link cells
		cell0.setPreviousCellOnDimension(0, cell3);
		cell0.setNextCellOnDimension(0, cell1);
		cell1.setPreviousCellOnDimension(0, cell0);
		cell1.setNextCellOnDimension(0, cell2);
		cell2.setPreviousCellOnDimension(0, cell1);
		cell2.setNextCellOnDimension(0, cell3);
		cell3.setPreviousCellOnDimension(0, cell2);
		cell3.setNextCellOnDimension(0, cell0);

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

	public void testEvolutionOnExistentSpaceOfCells2D() {
		// generate cells
		cellFactory.setDimensions(2);
		cellFactory.setRule(new IRule<String>() {
			public String calculateNextStateOf(ICell<String> cell) {
				return cell.getRelativeCell(-1, -1).getCurrentState()
						+ cell.getRelativeCell(+0, -1).getCurrentState()
						+ cell.getRelativeCell(+1, -1).getCurrentState()
						+ cell.getRelativeCell(-1, +0).getCurrentState()
						+ cell.getRelativeCell(+0, +0).getCurrentState() // current
						+ cell.getRelativeCell(+1, +0).getCurrentState()
						+ cell.getRelativeCell(-1, +1).getCurrentState()
						+ cell.getRelativeCell(+0, +1).getCurrentState()
						+ cell.getRelativeCell(+1, +1).getCurrentState();
			}
		});
		ICell<String> cell00 = cellFactory.setInitialState("00")
				.createCyclicCell();
		ICell<String> cell01 = cellFactory.setInitialState("01")
				.createCyclicCell();
		ICell<String> cell02 = cellFactory.setInitialState("02")
				.createCyclicCell();
		ICell<String> cell03 = cellFactory.setInitialState("03")
				.createCyclicCell();
		ICell<String> cell10 = cellFactory.setInitialState("10")
				.createCyclicCell();
		ICell<String> cell11 = cellFactory.setInitialState("11")
				.createCyclicCell();
		ICell<String> cell12 = cellFactory.setInitialState("12")
				.createCyclicCell();
		ICell<String> cell13 = cellFactory.setInitialState("13")
				.createCyclicCell();
		ICell<String> cell20 = cellFactory.setInitialState("20")
				.createCyclicCell();
		ICell<String> cell21 = cellFactory.setInitialState("21")
				.createCyclicCell();
		ICell<String> cell22 = cellFactory.setInitialState("22")
				.createCyclicCell();
		ICell<String> cell23 = cellFactory.setInitialState("23")
				.createCyclicCell();
		ICell<String> cell30 = cellFactory.setInitialState("30")
				.createCyclicCell();
		ICell<String> cell31 = cellFactory.setInitialState("31")
				.createCyclicCell();
		ICell<String> cell32 = cellFactory.setInitialState("32")
				.createCyclicCell();
		ICell<String> cell33 = cellFactory.setInitialState("33")
				.createCyclicCell();

		// link cells
		cell00.setPreviousCellOnDimension(0, cell03);
		cell00.setNextCellOnDimension(0, cell01);
		cell00.setPreviousCellOnDimension(1, cell30);
		cell00.setNextCellOnDimension(1, cell10);

		cell01.setPreviousCellOnDimension(0, cell00);
		cell01.setNextCellOnDimension(0, cell02);
		cell01.setPreviousCellOnDimension(1, cell31);
		cell01.setNextCellOnDimension(1, cell11);

		cell02.setPreviousCellOnDimension(0, cell01);
		cell02.setNextCellOnDimension(0, cell03);
		cell02.setPreviousCellOnDimension(1, cell32);
		cell02.setNextCellOnDimension(1, cell12);

		cell03.setPreviousCellOnDimension(0, cell02);
		cell03.setNextCellOnDimension(0, cell00);
		cell03.setPreviousCellOnDimension(1, cell33);
		cell03.setNextCellOnDimension(1, cell13);
		//
		cell10.setPreviousCellOnDimension(0, cell13);
		cell10.setNextCellOnDimension(0, cell11);
		cell10.setPreviousCellOnDimension(1, cell00);
		cell10.setNextCellOnDimension(1, cell20);

		cell11.setPreviousCellOnDimension(0, cell10);
		cell11.setNextCellOnDimension(0, cell12);
		cell11.setPreviousCellOnDimension(1, cell01);
		cell11.setNextCellOnDimension(1, cell21);

		cell12.setPreviousCellOnDimension(0, cell11);
		cell12.setNextCellOnDimension(0, cell13);
		cell12.setPreviousCellOnDimension(1, cell02);
		cell12.setNextCellOnDimension(1, cell22);

		cell13.setPreviousCellOnDimension(0, cell12);
		cell13.setNextCellOnDimension(0, cell10);
		cell13.setPreviousCellOnDimension(1, cell03);
		cell13.setNextCellOnDimension(1, cell23);
		//
		cell20.setPreviousCellOnDimension(0, cell23);
		cell20.setNextCellOnDimension(0, cell21);
		cell20.setPreviousCellOnDimension(1, cell10);
		cell20.setNextCellOnDimension(1, cell30);

		cell21.setPreviousCellOnDimension(0, cell20);
		cell21.setNextCellOnDimension(0, cell22);
		cell21.setPreviousCellOnDimension(1, cell11);
		cell21.setNextCellOnDimension(1, cell31);

		cell22.setPreviousCellOnDimension(0, cell21);
		cell22.setNextCellOnDimension(0, cell23);
		cell22.setPreviousCellOnDimension(1, cell12);
		cell22.setNextCellOnDimension(1, cell32);

		cell23.setPreviousCellOnDimension(0, cell22);
		cell23.setNextCellOnDimension(0, cell20);
		cell23.setPreviousCellOnDimension(1, cell13);
		cell23.setNextCellOnDimension(1, cell33);
		//
		cell30.setPreviousCellOnDimension(0, cell33);
		cell30.setNextCellOnDimension(0, cell31);
		cell30.setPreviousCellOnDimension(1, cell20);
		cell30.setNextCellOnDimension(1, cell00);

		cell31.setPreviousCellOnDimension(0, cell30);
		cell31.setNextCellOnDimension(0, cell32);
		cell31.setPreviousCellOnDimension(1, cell21);
		cell31.setNextCellOnDimension(1, cell01);

		cell32.setPreviousCellOnDimension(0, cell31);
		cell32.setNextCellOnDimension(0, cell33);
		cell32.setPreviousCellOnDimension(1, cell22);
		cell32.setNextCellOnDimension(1, cell02);

		cell33.setPreviousCellOnDimension(0, cell32);
		cell33.setNextCellOnDimension(0, cell30);
		cell33.setPreviousCellOnDimension(1, cell23);
		cell33.setNextCellOnDimension(1, cell03);

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

	public void testGeneratedSpaceOfCells1D() {
		// config
		final GeneratorConfiguration<String> config = new GeneratorConfiguration<String>();
		config.initialState = "";
		config.dimensionSizes = new int[] { 3 };

		// generate automaton
		CellularAutomaton<String> automaton1D = new CellularAutomaton<String>(
				config);

		// get cells
		ICell<String> cell0 = automaton1D.getOriginCell();
		ICell<String> cell1 = cell0.getNextCellOnDimension(0);
		ICell<String> cell2 = cell1.getNextCellOnDimension(0);

		// check coords
		assertArrayEquals(new int[] { 0 }, cell0.getCoords());
		assertArrayEquals(new int[] { 1 }, cell1.getCoords());
		assertArrayEquals(new int[] { 2 }, cell2.getCoords());

		// check cells exclusivity
		assertSame(cell0, cell0);
		assertNotSame(cell0, cell1);
		assertNotSame(cell0, cell2);

		assertNotSame(cell1, cell0);
		assertSame(cell1, cell1);
		assertNotSame(cell1, cell2);

		assertNotSame(cell2, cell0);
		assertNotSame(cell2, cell1);
		assertSame(cell2, cell2);

		// check cells existence
		Collection<ICell<String>> list = automaton1D.getAllCells();
		assertEquals(3, list.size());
		assertTrue(list.contains(cell0));
		assertTrue(list.contains(cell1));
		assertTrue(list.contains(cell2));

		// check cells links
		assertEquals(cell2, cell0.getPreviousCellOnDimension(0));
		assertEquals(cell1, cell0.getNextCellOnDimension(0));

		assertEquals(cell0, cell1.getPreviousCellOnDimension(0));
		assertEquals(cell2, cell1.getNextCellOnDimension(0));

		assertEquals(cell1, cell2.getPreviousCellOnDimension(0));
		assertEquals(cell0, cell2.getNextCellOnDimension(0));
	}

	public void testGeneratedSpaceOfCells2D() {
		// config
		final GeneratorConfiguration<String> config = new GeneratorConfiguration<String>();
		config.initialState = "";
		config.dimensionSizes = new int[] { 3, 3 };

		// generate automaton
		CellularAutomaton<String> automaton2D = new CellularAutomaton<String>(
				config);

		// get cells
		ICell<String> cell00 = automaton2D.getOriginCell();
		ICell<String> cell01 = cell00.getNextCellOnDimension(0);
		ICell<String> cell02 = cell01.getNextCellOnDimension(0);
		ICell<String> cell10 = cell00.getNextCellOnDimension(1);
		ICell<String> cell11 = cell10.getNextCellOnDimension(0);
		ICell<String> cell12 = cell11.getNextCellOnDimension(0);
		ICell<String> cell20 = cell10.getNextCellOnDimension(1);
		ICell<String> cell21 = cell20.getNextCellOnDimension(0);
		ICell<String> cell22 = cell21.getNextCellOnDimension(0);

		// check coords
		assertArrayEquals(new int[] { 0, 0 }, cell00.getCoords());
		assertArrayEquals(new int[] { 1, 0 }, cell01.getCoords());
		assertArrayEquals(new int[] { 2, 0 }, cell02.getCoords());
		assertArrayEquals(new int[] { 0, 1 }, cell10.getCoords());
		assertArrayEquals(new int[] { 1, 1 }, cell11.getCoords());
		assertArrayEquals(new int[] { 2, 1 }, cell12.getCoords());
		assertArrayEquals(new int[] { 0, 2 }, cell20.getCoords());
		assertArrayEquals(new int[] { 1, 2 }, cell21.getCoords());
		assertArrayEquals(new int[] { 2, 2 }, cell22.getCoords());

		// check cells exclusivity
		assertSame(cell00, cell00);
		assertNotSame(cell00, cell01);
		assertNotSame(cell00, cell02);
		assertNotSame(cell00, cell10);
		assertNotSame(cell00, cell11);
		assertNotSame(cell00, cell12);
		assertNotSame(cell00, cell20);
		assertNotSame(cell00, cell21);
		assertNotSame(cell00, cell22);

		assertNotSame(cell01, cell00);
		assertSame(cell01, cell01);
		assertNotSame(cell01, cell02);
		assertNotSame(cell01, cell10);
		assertNotSame(cell01, cell11);
		assertNotSame(cell01, cell12);
		assertNotSame(cell01, cell20);
		assertNotSame(cell01, cell21);
		assertNotSame(cell01, cell22);

		assertNotSame(cell02, cell00);
		assertNotSame(cell02, cell01);
		assertSame(cell02, cell02);
		assertNotSame(cell02, cell10);
		assertNotSame(cell02, cell11);
		assertNotSame(cell02, cell12);
		assertNotSame(cell02, cell20);
		assertNotSame(cell02, cell21);
		assertNotSame(cell02, cell22);

		assertNotSame(cell10, cell00);
		assertNotSame(cell10, cell01);
		assertNotSame(cell10, cell02);
		assertSame(cell10, cell10);
		assertNotSame(cell10, cell11);
		assertNotSame(cell10, cell12);
		assertNotSame(cell10, cell20);
		assertNotSame(cell10, cell21);
		assertNotSame(cell10, cell22);

		assertNotSame(cell11, cell00);
		assertNotSame(cell11, cell01);
		assertNotSame(cell11, cell02);
		assertNotSame(cell11, cell10);
		assertSame(cell11, cell11);
		assertNotSame(cell11, cell12);
		assertNotSame(cell11, cell20);
		assertNotSame(cell11, cell21);
		assertNotSame(cell11, cell22);

		assertNotSame(cell12, cell00);
		assertNotSame(cell12, cell01);
		assertNotSame(cell12, cell02);
		assertNotSame(cell12, cell10);
		assertNotSame(cell12, cell11);
		assertSame(cell12, cell12);
		assertNotSame(cell12, cell20);
		assertNotSame(cell12, cell21);
		assertNotSame(cell12, cell22);

		assertNotSame(cell20, cell00);
		assertNotSame(cell20, cell01);
		assertNotSame(cell20, cell02);
		assertNotSame(cell20, cell10);
		assertNotSame(cell20, cell11);
		assertNotSame(cell20, cell12);
		assertSame(cell20, cell20);
		assertNotSame(cell20, cell21);
		assertNotSame(cell20, cell22);

		assertNotSame(cell21, cell00);
		assertNotSame(cell21, cell01);
		assertNotSame(cell21, cell02);
		assertNotSame(cell21, cell10);
		assertNotSame(cell21, cell11);
		assertNotSame(cell21, cell12);
		assertNotSame(cell21, cell20);
		assertSame(cell21, cell21);
		assertNotSame(cell21, cell22);

		assertNotSame(cell22, cell00);
		assertNotSame(cell22, cell01);
		assertNotSame(cell22, cell02);
		assertNotSame(cell22, cell10);
		assertNotSame(cell22, cell11);
		assertNotSame(cell22, cell12);
		assertNotSame(cell22, cell20);
		assertNotSame(cell22, cell21);
		assertSame(cell22, cell22);

		// check cells existence
		Collection<ICell<String>> list = automaton2D.getAllCells();
		assertEquals(9, list.size());
		assertTrue(list.contains(cell00));
		assertTrue(list.contains(cell01));
		assertTrue(list.contains(cell02));
		assertTrue(list.contains(cell10));
		assertTrue(list.contains(cell11));
		assertTrue(list.contains(cell12));
		assertTrue(list.contains(cell20));
		assertTrue(list.contains(cell21));
		assertTrue(list.contains(cell22));

		// check cells links
		assertEquals(cell02, cell00.getPreviousCellOnDimension(0));
		assertEquals(cell01, cell00.getNextCellOnDimension(0));
		assertEquals(cell20, cell00.getPreviousCellOnDimension(1));
		assertEquals(cell10, cell00.getNextCellOnDimension(1));

		assertEquals(cell00, cell01.getPreviousCellOnDimension(0));
		assertEquals(cell02, cell01.getNextCellOnDimension(0));
		assertEquals(cell21, cell01.getPreviousCellOnDimension(1));
		assertEquals(cell11, cell01.getNextCellOnDimension(1));

		assertEquals(cell01, cell02.getPreviousCellOnDimension(0));
		assertEquals(cell00, cell02.getNextCellOnDimension(0));
		assertEquals(cell22, cell02.getPreviousCellOnDimension(1));
		assertEquals(cell12, cell02.getNextCellOnDimension(1));

		assertEquals(cell12, cell10.getPreviousCellOnDimension(0));
		assertEquals(cell11, cell10.getNextCellOnDimension(0));
		assertEquals(cell00, cell10.getPreviousCellOnDimension(1));
		assertEquals(cell20, cell10.getNextCellOnDimension(1));

		assertEquals(cell10, cell11.getPreviousCellOnDimension(0));
		assertEquals(cell12, cell11.getNextCellOnDimension(0));
		assertEquals(cell01, cell11.getPreviousCellOnDimension(1));
		assertEquals(cell21, cell11.getNextCellOnDimension(1));

		assertEquals(cell11, cell12.getPreviousCellOnDimension(0));
		assertEquals(cell10, cell12.getNextCellOnDimension(0));
		assertEquals(cell02, cell12.getPreviousCellOnDimension(1));
		assertEquals(cell22, cell12.getNextCellOnDimension(1));

		assertEquals(cell22, cell20.getPreviousCellOnDimension(0));
		assertEquals(cell21, cell20.getNextCellOnDimension(0));
		assertEquals(cell10, cell20.getPreviousCellOnDimension(1));
		assertEquals(cell00, cell20.getNextCellOnDimension(1));

		assertEquals(cell20, cell21.getPreviousCellOnDimension(0));
		assertEquals(cell22, cell21.getNextCellOnDimension(0));
		assertEquals(cell11, cell21.getPreviousCellOnDimension(1));
		assertEquals(cell01, cell21.getNextCellOnDimension(1));

		assertEquals(cell21, cell22.getPreviousCellOnDimension(0));
		assertEquals(cell20, cell22.getNextCellOnDimension(0));
		assertEquals(cell12, cell22.getPreviousCellOnDimension(1));
		assertEquals(cell02, cell22.getNextCellOnDimension(1));
	}

	public void testGeneratedSpaceOfCells3D() {
		// config
		final GeneratorConfiguration<String> config = new GeneratorConfiguration<String>();
		config.initialState = "";
		config.dimensionSizes = new int[] { 3, 3, 3 };

		// generate automaton
		CellularAutomaton<String> automaton3D = new CellularAutomaton<String>(
				config);

		// get cells
		ICell<String> cell000 = automaton3D.getOriginCell();
		ICell<String> cell001 = cell000.getNextCellOnDimension(0);
		ICell<String> cell002 = cell001.getNextCellOnDimension(0);
		ICell<String> cell010 = cell000.getNextCellOnDimension(1);
		ICell<String> cell011 = cell010.getNextCellOnDimension(0);
		ICell<String> cell012 = cell011.getNextCellOnDimension(0);
		ICell<String> cell020 = cell010.getNextCellOnDimension(1);
		ICell<String> cell021 = cell020.getNextCellOnDimension(0);
		ICell<String> cell022 = cell021.getNextCellOnDimension(0);

		ICell<String> cell100 = cell000.getNextCellOnDimension(2);
		ICell<String> cell101 = cell100.getNextCellOnDimension(0);
		ICell<String> cell102 = cell101.getNextCellOnDimension(0);
		ICell<String> cell110 = cell100.getNextCellOnDimension(1);
		ICell<String> cell111 = cell110.getNextCellOnDimension(0);
		ICell<String> cell112 = cell111.getNextCellOnDimension(0);
		ICell<String> cell120 = cell110.getNextCellOnDimension(1);
		ICell<String> cell121 = cell120.getNextCellOnDimension(0);
		ICell<String> cell122 = cell121.getNextCellOnDimension(0);

		ICell<String> cell200 = cell100.getNextCellOnDimension(2);
		ICell<String> cell201 = cell200.getNextCellOnDimension(0);
		ICell<String> cell202 = cell201.getNextCellOnDimension(0);
		ICell<String> cell210 = cell200.getNextCellOnDimension(1);
		ICell<String> cell211 = cell210.getNextCellOnDimension(0);
		ICell<String> cell212 = cell211.getNextCellOnDimension(0);
		ICell<String> cell220 = cell210.getNextCellOnDimension(1);
		ICell<String> cell221 = cell220.getNextCellOnDimension(0);
		ICell<String> cell222 = cell221.getNextCellOnDimension(0);

		// check coords
		assertArrayEquals(new int[] { 0, 0, 0 }, cell000.getCoords());
		assertArrayEquals(new int[] { 1, 0, 0 }, cell001.getCoords());
		assertArrayEquals(new int[] { 2, 0, 0 }, cell002.getCoords());
		assertArrayEquals(new int[] { 0, 1, 0 }, cell010.getCoords());
		assertArrayEquals(new int[] { 1, 1, 0 }, cell011.getCoords());
		assertArrayEquals(new int[] { 2, 1, 0 }, cell012.getCoords());
		assertArrayEquals(new int[] { 0, 2, 0 }, cell020.getCoords());
		assertArrayEquals(new int[] { 1, 2, 0 }, cell021.getCoords());
		assertArrayEquals(new int[] { 2, 2, 0 }, cell022.getCoords());
		assertArrayEquals(new int[] { 0, 0, 1 }, cell100.getCoords());
		assertArrayEquals(new int[] { 1, 0, 1 }, cell101.getCoords());
		assertArrayEquals(new int[] { 2, 0, 1 }, cell102.getCoords());
		assertArrayEquals(new int[] { 0, 1, 1 }, cell110.getCoords());
		assertArrayEquals(new int[] { 1, 1, 1 }, cell111.getCoords());
		assertArrayEquals(new int[] { 2, 1, 1 }, cell112.getCoords());
		assertArrayEquals(new int[] { 0, 2, 1 }, cell120.getCoords());
		assertArrayEquals(new int[] { 1, 2, 1 }, cell121.getCoords());
		assertArrayEquals(new int[] { 2, 2, 1 }, cell122.getCoords());
		assertArrayEquals(new int[] { 0, 0, 2 }, cell200.getCoords());
		assertArrayEquals(new int[] { 1, 0, 2 }, cell201.getCoords());
		assertArrayEquals(new int[] { 2, 0, 2 }, cell202.getCoords());
		assertArrayEquals(new int[] { 0, 1, 2 }, cell210.getCoords());
		assertArrayEquals(new int[] { 1, 1, 2 }, cell211.getCoords());
		assertArrayEquals(new int[] { 2, 1, 2 }, cell212.getCoords());
		assertArrayEquals(new int[] { 0, 2, 2 }, cell220.getCoords());
		assertArrayEquals(new int[] { 1, 2, 2 }, cell221.getCoords());
		assertArrayEquals(new int[] { 2, 2, 2 }, cell222.getCoords());

		// check cells existence
		Collection<ICell<String>> list = automaton3D.getAllCells();
		assertEquals(27, list.size());
		assertTrue(list.contains(cell000));
		assertTrue(list.contains(cell001));
		assertTrue(list.contains(cell002));
		assertTrue(list.contains(cell010));
		assertTrue(list.contains(cell011));
		assertTrue(list.contains(cell012));
		assertTrue(list.contains(cell020));
		assertTrue(list.contains(cell021));
		assertTrue(list.contains(cell022));
		assertTrue(list.contains(cell100));
		assertTrue(list.contains(cell101));
		assertTrue(list.contains(cell102));
		assertTrue(list.contains(cell110));
		assertTrue(list.contains(cell111));
		assertTrue(list.contains(cell112));
		assertTrue(list.contains(cell120));
		assertTrue(list.contains(cell121));
		assertTrue(list.contains(cell122));
		assertTrue(list.contains(cell200));
		assertTrue(list.contains(cell201));
		assertTrue(list.contains(cell202));
		assertTrue(list.contains(cell210));
		assertTrue(list.contains(cell211));
		assertTrue(list.contains(cell212));
		assertTrue(list.contains(cell220));
		assertTrue(list.contains(cell221));
		assertTrue(list.contains(cell222));

		// check cells exclusivity
		List<ICell<String>> cells = new ArrayList<ICell<String>>(list);
		for (int i = 0; i < 27; i++) {
			ICell<String> expected = cells.get(i);
			for (int j = 0; j < 27; j++) {
				ICell<String> result = cells.get(j);
				if (i == j) {
					assertSame(expected, result);
				} else {
					assertNotSame(expected, result);
				}
			}
		}

		// check cells links
		assertEquals(cell002, cell000.getPreviousCellOnDimension(0));
		assertEquals(cell001, cell000.getNextCellOnDimension(0));
		assertEquals(cell020, cell000.getPreviousCellOnDimension(1));
		assertEquals(cell010, cell000.getNextCellOnDimension(1));
		assertEquals(cell200, cell000.getPreviousCellOnDimension(2));
		assertEquals(cell100, cell000.getNextCellOnDimension(2));

		assertEquals(cell000, cell001.getPreviousCellOnDimension(0));
		assertEquals(cell002, cell001.getNextCellOnDimension(0));
		assertEquals(cell021, cell001.getPreviousCellOnDimension(1));
		assertEquals(cell011, cell001.getNextCellOnDimension(1));
		assertEquals(cell201, cell001.getPreviousCellOnDimension(2));
		assertEquals(cell101, cell001.getNextCellOnDimension(2));

		assertEquals(cell001, cell002.getPreviousCellOnDimension(0));
		assertEquals(cell000, cell002.getNextCellOnDimension(0));
		assertEquals(cell022, cell002.getPreviousCellOnDimension(1));
		assertEquals(cell012, cell002.getNextCellOnDimension(1));
		assertEquals(cell202, cell002.getPreviousCellOnDimension(2));
		assertEquals(cell102, cell002.getNextCellOnDimension(2));

		assertEquals(cell012, cell010.getPreviousCellOnDimension(0));
		assertEquals(cell011, cell010.getNextCellOnDimension(0));
		assertEquals(cell000, cell010.getPreviousCellOnDimension(1));
		assertEquals(cell020, cell010.getNextCellOnDimension(1));
		assertEquals(cell210, cell010.getPreviousCellOnDimension(2));
		assertEquals(cell110, cell010.getNextCellOnDimension(2));

		assertEquals(cell010, cell011.getPreviousCellOnDimension(0));
		assertEquals(cell012, cell011.getNextCellOnDimension(0));
		assertEquals(cell001, cell011.getPreviousCellOnDimension(1));
		assertEquals(cell021, cell011.getNextCellOnDimension(1));
		assertEquals(cell211, cell011.getPreviousCellOnDimension(2));
		assertEquals(cell111, cell011.getNextCellOnDimension(2));

		assertEquals(cell011, cell012.getPreviousCellOnDimension(0));
		assertEquals(cell010, cell012.getNextCellOnDimension(0));
		assertEquals(cell002, cell012.getPreviousCellOnDimension(1));
		assertEquals(cell022, cell012.getNextCellOnDimension(1));
		assertEquals(cell212, cell012.getPreviousCellOnDimension(2));
		assertEquals(cell112, cell012.getNextCellOnDimension(2));

		assertEquals(cell022, cell020.getPreviousCellOnDimension(0));
		assertEquals(cell021, cell020.getNextCellOnDimension(0));
		assertEquals(cell010, cell020.getPreviousCellOnDimension(1));
		assertEquals(cell000, cell020.getNextCellOnDimension(1));
		assertEquals(cell220, cell020.getPreviousCellOnDimension(2));
		assertEquals(cell120, cell020.getNextCellOnDimension(2));

		assertEquals(cell020, cell021.getPreviousCellOnDimension(0));
		assertEquals(cell022, cell021.getNextCellOnDimension(0));
		assertEquals(cell011, cell021.getPreviousCellOnDimension(1));
		assertEquals(cell001, cell021.getNextCellOnDimension(1));
		assertEquals(cell221, cell021.getPreviousCellOnDimension(2));
		assertEquals(cell121, cell021.getNextCellOnDimension(2));

		assertEquals(cell021, cell022.getPreviousCellOnDimension(0));
		assertEquals(cell020, cell022.getNextCellOnDimension(0));
		assertEquals(cell012, cell022.getPreviousCellOnDimension(1));
		assertEquals(cell002, cell022.getNextCellOnDimension(1));
		assertEquals(cell222, cell022.getPreviousCellOnDimension(2));
		assertEquals(cell122, cell022.getNextCellOnDimension(2));

		assertEquals(cell102, cell100.getPreviousCellOnDimension(0));
		assertEquals(cell101, cell100.getNextCellOnDimension(0));
		assertEquals(cell120, cell100.getPreviousCellOnDimension(1));
		assertEquals(cell110, cell100.getNextCellOnDimension(1));
		assertEquals(cell000, cell100.getPreviousCellOnDimension(2));
		assertEquals(cell200, cell100.getNextCellOnDimension(2));

		assertEquals(cell100, cell101.getPreviousCellOnDimension(0));
		assertEquals(cell102, cell101.getNextCellOnDimension(0));
		assertEquals(cell121, cell101.getPreviousCellOnDimension(1));
		assertEquals(cell111, cell101.getNextCellOnDimension(1));
		assertEquals(cell001, cell101.getPreviousCellOnDimension(2));
		assertEquals(cell201, cell101.getNextCellOnDimension(2));

		assertEquals(cell101, cell102.getPreviousCellOnDimension(0));
		assertEquals(cell100, cell102.getNextCellOnDimension(0));
		assertEquals(cell122, cell102.getPreviousCellOnDimension(1));
		assertEquals(cell112, cell102.getNextCellOnDimension(1));
		assertEquals(cell002, cell102.getPreviousCellOnDimension(2));
		assertEquals(cell202, cell102.getNextCellOnDimension(2));

		assertEquals(cell112, cell110.getPreviousCellOnDimension(0));
		assertEquals(cell111, cell110.getNextCellOnDimension(0));
		assertEquals(cell100, cell110.getPreviousCellOnDimension(1));
		assertEquals(cell120, cell110.getNextCellOnDimension(1));
		assertEquals(cell010, cell110.getPreviousCellOnDimension(2));
		assertEquals(cell210, cell110.getNextCellOnDimension(2));

		assertEquals(cell110, cell111.getPreviousCellOnDimension(0));
		assertEquals(cell112, cell111.getNextCellOnDimension(0));
		assertEquals(cell101, cell111.getPreviousCellOnDimension(1));
		assertEquals(cell121, cell111.getNextCellOnDimension(1));
		assertEquals(cell011, cell111.getPreviousCellOnDimension(2));
		assertEquals(cell211, cell111.getNextCellOnDimension(2));

		assertEquals(cell111, cell112.getPreviousCellOnDimension(0));
		assertEquals(cell110, cell112.getNextCellOnDimension(0));
		assertEquals(cell102, cell112.getPreviousCellOnDimension(1));
		assertEquals(cell122, cell112.getNextCellOnDimension(1));
		assertEquals(cell012, cell112.getPreviousCellOnDimension(2));
		assertEquals(cell212, cell112.getNextCellOnDimension(2));

		assertEquals(cell122, cell120.getPreviousCellOnDimension(0));
		assertEquals(cell121, cell120.getNextCellOnDimension(0));
		assertEquals(cell110, cell120.getPreviousCellOnDimension(1));
		assertEquals(cell100, cell120.getNextCellOnDimension(1));
		assertEquals(cell020, cell120.getPreviousCellOnDimension(2));
		assertEquals(cell220, cell120.getNextCellOnDimension(2));

		assertEquals(cell120, cell121.getPreviousCellOnDimension(0));
		assertEquals(cell122, cell121.getNextCellOnDimension(0));
		assertEquals(cell111, cell121.getPreviousCellOnDimension(1));
		assertEquals(cell101, cell121.getNextCellOnDimension(1));
		assertEquals(cell021, cell121.getPreviousCellOnDimension(2));
		assertEquals(cell221, cell121.getNextCellOnDimension(2));

		assertEquals(cell121, cell122.getPreviousCellOnDimension(0));
		assertEquals(cell120, cell122.getNextCellOnDimension(0));
		assertEquals(cell112, cell122.getPreviousCellOnDimension(1));
		assertEquals(cell102, cell122.getNextCellOnDimension(1));
		assertEquals(cell022, cell122.getPreviousCellOnDimension(2));
		assertEquals(cell222, cell122.getNextCellOnDimension(2));

		assertEquals(cell202, cell200.getPreviousCellOnDimension(0));
		assertEquals(cell201, cell200.getNextCellOnDimension(0));
		assertEquals(cell220, cell200.getPreviousCellOnDimension(1));
		assertEquals(cell210, cell200.getNextCellOnDimension(1));
		assertEquals(cell100, cell200.getPreviousCellOnDimension(2));
		assertEquals(cell000, cell200.getNextCellOnDimension(2));

		assertEquals(cell200, cell201.getPreviousCellOnDimension(0));
		assertEquals(cell202, cell201.getNextCellOnDimension(0));
		assertEquals(cell221, cell201.getPreviousCellOnDimension(1));
		assertEquals(cell211, cell201.getNextCellOnDimension(1));
		assertEquals(cell101, cell201.getPreviousCellOnDimension(2));
		assertEquals(cell001, cell201.getNextCellOnDimension(2));

		assertEquals(cell201, cell202.getPreviousCellOnDimension(0));
		assertEquals(cell200, cell202.getNextCellOnDimension(0));
		assertEquals(cell222, cell202.getPreviousCellOnDimension(1));
		assertEquals(cell212, cell202.getNextCellOnDimension(1));
		assertEquals(cell102, cell202.getPreviousCellOnDimension(2));
		assertEquals(cell002, cell202.getNextCellOnDimension(2));

		assertEquals(cell212, cell210.getPreviousCellOnDimension(0));
		assertEquals(cell211, cell210.getNextCellOnDimension(0));
		assertEquals(cell200, cell210.getPreviousCellOnDimension(1));
		assertEquals(cell220, cell210.getNextCellOnDimension(1));
		assertEquals(cell110, cell210.getPreviousCellOnDimension(2));
		assertEquals(cell010, cell210.getNextCellOnDimension(2));

		assertEquals(cell210, cell211.getPreviousCellOnDimension(0));
		assertEquals(cell212, cell211.getNextCellOnDimension(0));
		assertEquals(cell201, cell211.getPreviousCellOnDimension(1));
		assertEquals(cell221, cell211.getNextCellOnDimension(1));
		assertEquals(cell111, cell211.getPreviousCellOnDimension(2));
		assertEquals(cell011, cell211.getNextCellOnDimension(2));

		assertEquals(cell211, cell212.getPreviousCellOnDimension(0));
		assertEquals(cell210, cell212.getNextCellOnDimension(0));
		assertEquals(cell202, cell212.getPreviousCellOnDimension(1));
		assertEquals(cell222, cell212.getNextCellOnDimension(1));
		assertEquals(cell112, cell212.getPreviousCellOnDimension(2));
		assertEquals(cell012, cell212.getNextCellOnDimension(2));

		assertEquals(cell222, cell220.getPreviousCellOnDimension(0));
		assertEquals(cell221, cell220.getNextCellOnDimension(0));
		assertEquals(cell210, cell220.getPreviousCellOnDimension(1));
		assertEquals(cell200, cell220.getNextCellOnDimension(1));
		assertEquals(cell120, cell220.getPreviousCellOnDimension(2));
		assertEquals(cell020, cell220.getNextCellOnDimension(2));

		assertEquals(cell220, cell221.getPreviousCellOnDimension(0));
		assertEquals(cell222, cell221.getNextCellOnDimension(0));
		assertEquals(cell211, cell221.getPreviousCellOnDimension(1));
		assertEquals(cell201, cell221.getNextCellOnDimension(1));
		assertEquals(cell121, cell221.getPreviousCellOnDimension(2));
		assertEquals(cell021, cell221.getNextCellOnDimension(2));

		assertEquals(cell221, cell222.getPreviousCellOnDimension(0));
		assertEquals(cell220, cell222.getNextCellOnDimension(0));
		assertEquals(cell212, cell222.getPreviousCellOnDimension(1));
		assertEquals(cell202, cell222.getNextCellOnDimension(1));
		assertEquals(cell122, cell222.getPreviousCellOnDimension(2));
		assertEquals(cell022, cell222.getNextCellOnDimension(2));
	}
}
