package org.cellularautomaton.space;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.CellFactory;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.state.AbstractStateFactory;
import org.cellularautomaton.state.IStateFactory;
import org.junit.Test;

public abstract class ISpaceTest<StateType> {

	public abstract ISpace<StateType> createSpace();

	@Test
	public void testGetAllCells() {
		ISpace<StateType> space = createSpace();

		// get cells
		ICell<StateType> origin = space.getOrigin();
		Collection<ICell<StateType>> expectedCells = new HashSet<ICell<StateType>>();
		{
			Collection<ICell<StateType>> toCheck = new HashSet<ICell<StateType>>();
			toCheck.add(origin);
			while (!toCheck.isEmpty()) {
				ICell<StateType> cell = toCheck.iterator().next();
				expectedCells.add(cell);
				toCheck.addAll(cell.getAllCellsAround());
				toCheck.removeAll(expectedCells);
			}
		}

		// test init
		Collection<ICell<StateType>> list = space.getAllCells();
		assertEquals(expectedCells.size(), list.size());
		assertTrue(expectedCells.containsAll(list));
		assertTrue(list.containsAll(expectedCells));

		// test update
		Iterator<ICell<StateType>> iterator = expectedCells.iterator();
		CellFactory<StateType> cellFactory = new CellFactory<StateType>()
				.setDimensions(2);
		ICell<StateType> hostCell = iterator.next();

		ICell<StateType> intruderAccessible = cellFactory.createCell();
		intruderAccessible.setPreviousCellOnDimension(0, hostCell);
		hostCell.setNextCellOnDimension(0, intruderAccessible);

		ICell<StateType> intruderNotAccessible = cellFactory
				.createCell();
		intruderNotAccessible.setNextCellOnDimension(0, hostCell);

		expectedCells.add(intruderAccessible);
		list = space.getAllCells();
		assertEquals(expectedCells.size(), list.size());
		assertTrue(expectedCells.containsAll(list));
		assertTrue(list.containsAll(expectedCells));
	}

	@Test
	public void testCellsIterator() {
		ISpace<StateType> space = createSpace();

		// get cells
		ICell<StateType> origin = space.getOrigin();
		Collection<ICell<StateType>> expectedCells = new HashSet<ICell<StateType>>();
		{
			Collection<ICell<StateType>> toCheck = new HashSet<ICell<StateType>>();
			toCheck.add(origin);
			while (!toCheck.isEmpty()) {
				ICell<StateType> cell = toCheck.iterator().next();
				expectedCells.add(cell);
				toCheck.addAll(cell.getAllCellsAround());
				toCheck.removeAll(expectedCells);
			}
		}

		// test init
		{
			Collection<ICell<StateType>> cellsToView = space.getAllCells();
			Iterator<ICell<StateType>> iterator = space.iterator();
			while (iterator.hasNext()) {
				ICell<StateType> cell = iterator.next();
				assertTrue(cellsToView.contains(cell));
				cellsToView.remove(cell);
			}
			assertTrue(cellsToView.isEmpty());
		}

		// test update
		{
			Iterator<ICell<StateType>> iterator = expectedCells.iterator();
			CellFactory<StateType> cellFactory = new CellFactory<StateType>()
					.setDimensions(2);
			ICell<StateType> hostCell = iterator.next();

			ICell<StateType> intruderAccessible = cellFactory
					.createCell();
			intruderAccessible.setPreviousCellOnDimension(0, hostCell);
			hostCell.setNextCellOnDimension(0, intruderAccessible);

			ICell<StateType> intruderNotAccessible = cellFactory
					.createCell();
			intruderNotAccessible.setNextCellOnDimension(0, hostCell);

			Collection<ICell<StateType>> cellsToView = space.getAllCells();
			iterator = space.iterator();
			while (iterator.hasNext()) {
				ICell<StateType> cell = iterator.next();
				assertTrue("cell not found", cellsToView.contains(cell));
				cellsToView.remove(cell);
			}
			assertTrue(cellsToView.isEmpty());
		}
	}

	@Test
	public void testOriginCell() {
		// generate space of cells
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).setMemorySize(1)
				.createNewSpace().addDimension(3);

		// get cells
		ISpace<String> space = builder.getSpaceOfCell();
		ICell<String> cell0 = space.getOrigin();

		// generate automaton
		CellularAutomaton<String> automaton1D = new CellularAutomaton<String>(
				space);

		// check init
		assertEquals(cell0, automaton1D.getSpace().getOrigin());

		// check invariability
		automaton1D.doStep();
		assertEquals(cell0, automaton1D.getSpace().getOrigin());

		automaton1D.doStep();
		assertEquals(cell0, automaton1D.getSpace().getOrigin());

		automaton1D.doStep();
		assertEquals(cell0, automaton1D.getSpace().getOrigin());
	}
}
