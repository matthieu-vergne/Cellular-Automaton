package org.cellularautomaton.space;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.builder.SpaceBuilder;
import org.cellularautomaton.state.AbstractStateFactory;
import org.cellularautomaton.state.IStateFactory;
import org.cellularautomaton.util.Coords;
import org.junit.Test;

public class SpaceBuilderTest {

	@Test
	public void testIsolatedSpace1D() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).setMemorySize(1).createNewSpace()
				.addDimension(3, false);

		// get cells
		ICell<String> cell0 = builder.getSpaceOfCell().getOrigin();
		ICell<String> cell1 = cell0.getNextCellOnDimension(0);
		ICell<String> cell2 = cell1.getNextCellOnDimension(0);

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

		// check no intruders
		{
			List<ICell<String>> list = new ArrayList<ICell<String>>();
			list.add(cell0);
			list.add(cell1);
			list.add(cell2);
			assertTrue(list.containsAll(cell0.getAllCellsAround()));
			assertTrue(list.containsAll(cell1.getAllCellsAround()));
			assertTrue(list.containsAll(cell2.getAllCellsAround()));
		}

		// check cells links
		assertEquals(null, cell0.getPreviousCellOnDimension(0));
		assertEquals(cell1, cell0.getNextCellOnDimension(0));

		assertEquals(cell0, cell1.getPreviousCellOnDimension(0));
		assertEquals(cell2, cell1.getNextCellOnDimension(0));

		assertEquals(cell1, cell2.getPreviousCellOnDimension(0));
		assertEquals(null, cell2.getNextCellOnDimension(0));

		// check coords
		assertEquals(new Coords(0), cell0.getCoords());
		assertEquals(new Coords(1), cell1.getCoords());
		assertEquals(new Coords(2), cell2.getCoords());
	}

	@Test
	public void testCyclicSpace1D() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).setMemorySize(1).createNewSpace()
				.addDimension(3);

		// get cells
		ICell<String> cell0 = builder.getSpaceOfCell().getOrigin();
		ICell<String> cell1 = cell0.getNextCellOnDimension(0);
		ICell<String> cell2 = cell1.getNextCellOnDimension(0);

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

		// check no intruders
		{
			List<ICell<String>> list = new ArrayList<ICell<String>>();
			list.add(cell0);
			list.add(cell1);
			list.add(cell2);
			assertTrue(list.containsAll(cell0.getAllCellsAround()));
			assertTrue(list.containsAll(cell1.getAllCellsAround()));
			assertTrue(list.containsAll(cell2.getAllCellsAround()));
		}

		// check cells links
		assertEquals(cell2, cell0.getPreviousCellOnDimension(0));
		assertEquals(cell1, cell0.getNextCellOnDimension(0));

		assertEquals(cell0, cell1.getPreviousCellOnDimension(0));
		assertEquals(cell2, cell1.getNextCellOnDimension(0));

		assertEquals(cell1, cell2.getPreviousCellOnDimension(0));
		assertEquals(cell0, cell2.getNextCellOnDimension(0));

		// check coords
		assertEquals(new Coords(0), cell0.getCoords());
		assertEquals(new Coords(1), cell1.getCoords());
		assertEquals(new Coords(2), cell2.getCoords());
	}

	@Test
	public void testIsolatedSpace2D() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).setMemorySize(1).createNewSpace()
				.addDimension(3, false).addDimension(3, false);

		// get cells
		ICell<String> cell00 = builder.getSpaceOfCell().getOrigin();
		ICell<String> cell01 = cell00.getNextCellOnDimension(0);
		ICell<String> cell02 = cell01.getNextCellOnDimension(0);
		ICell<String> cell10 = cell00.getNextCellOnDimension(1);
		ICell<String> cell11 = cell10.getNextCellOnDimension(0);
		ICell<String> cell12 = cell11.getNextCellOnDimension(0);
		ICell<String> cell20 = cell10.getNextCellOnDimension(1);
		ICell<String> cell21 = cell20.getNextCellOnDimension(0);
		ICell<String> cell22 = cell21.getNextCellOnDimension(0);

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

		// check no intruders
		{
			List<ICell<String>> list = new ArrayList<ICell<String>>();
			list.add(cell00);
			list.add(cell01);
			list.add(cell02);
			list.add(cell10);
			list.add(cell11);
			list.add(cell12);
			list.add(cell20);
			list.add(cell21);
			list.add(cell22);
			assertTrue(list.containsAll(cell00.getAllCellsAround()));
			assertTrue(list.containsAll(cell01.getAllCellsAround()));
			assertTrue(list.containsAll(cell02.getAllCellsAround()));
			assertTrue(list.containsAll(cell10.getAllCellsAround()));
			assertTrue(list.containsAll(cell11.getAllCellsAround()));
			assertTrue(list.containsAll(cell12.getAllCellsAround()));
			assertTrue(list.containsAll(cell20.getAllCellsAround()));
			assertTrue(list.containsAll(cell21.getAllCellsAround()));
			assertTrue(list.containsAll(cell22.getAllCellsAround()));
		}

		// check cells links
		assertEquals(null, cell00.getPreviousCellOnDimension(0));
		assertEquals(cell01, cell00.getNextCellOnDimension(0));
		assertEquals(null, cell00.getPreviousCellOnDimension(1));
		assertEquals(cell10, cell00.getNextCellOnDimension(1));

		assertEquals(cell00, cell01.getPreviousCellOnDimension(0));
		assertEquals(cell02, cell01.getNextCellOnDimension(0));
		assertEquals(null, cell01.getPreviousCellOnDimension(1));
		assertEquals(cell11, cell01.getNextCellOnDimension(1));

		assertEquals(cell01, cell02.getPreviousCellOnDimension(0));
		assertEquals(null, cell02.getNextCellOnDimension(0));
		assertEquals(null, cell02.getPreviousCellOnDimension(1));
		assertEquals(cell12, cell02.getNextCellOnDimension(1));

		assertEquals(null, cell10.getPreviousCellOnDimension(0));
		assertEquals(cell11, cell10.getNextCellOnDimension(0));
		assertEquals(cell00, cell10.getPreviousCellOnDimension(1));
		assertEquals(cell20, cell10.getNextCellOnDimension(1));

		assertEquals(cell10, cell11.getPreviousCellOnDimension(0));
		assertEquals(cell12, cell11.getNextCellOnDimension(0));
		assertEquals(cell01, cell11.getPreviousCellOnDimension(1));
		assertEquals(cell21, cell11.getNextCellOnDimension(1));

		assertEquals(cell11, cell12.getPreviousCellOnDimension(0));
		assertEquals(null, cell12.getNextCellOnDimension(0));
		assertEquals(cell02, cell12.getPreviousCellOnDimension(1));
		assertEquals(cell22, cell12.getNextCellOnDimension(1));

		assertEquals(null, cell20.getPreviousCellOnDimension(0));
		assertEquals(cell21, cell20.getNextCellOnDimension(0));
		assertEquals(cell10, cell20.getPreviousCellOnDimension(1));
		assertEquals(null, cell20.getNextCellOnDimension(1));

		assertEquals(cell20, cell21.getPreviousCellOnDimension(0));
		assertEquals(cell22, cell21.getNextCellOnDimension(0));
		assertEquals(cell11, cell21.getPreviousCellOnDimension(1));
		assertEquals(null, cell21.getNextCellOnDimension(1));

		assertEquals(cell21, cell22.getPreviousCellOnDimension(0));
		assertEquals(null, cell22.getNextCellOnDimension(0));
		assertEquals(cell12, cell22.getPreviousCellOnDimension(1));
		assertEquals(null, cell22.getNextCellOnDimension(1));

		// check coords
		assertEquals(new Coords(0, 0), cell00.getCoords());
		assertEquals(new Coords(1, 0), cell01.getCoords());
		assertEquals(new Coords(2, 0), cell02.getCoords());
		assertEquals(new Coords(0, 1), cell10.getCoords());
		assertEquals(new Coords(1, 1), cell11.getCoords());
		assertEquals(new Coords(2, 1), cell12.getCoords());
		assertEquals(new Coords(0, 2), cell20.getCoords());
		assertEquals(new Coords(1, 2), cell21.getCoords());
		assertEquals(new Coords(2, 2), cell22.getCoords());
	}

	@Test
	public void testCyclicSpace2D() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).setMemorySize(1).createNewSpace()
				.addDimension(3).addDimension(3);

		// get cells
		ICell<String> cell00 = builder.getSpaceOfCell().getOrigin();
		ICell<String> cell01 = cell00.getNextCellOnDimension(0);
		ICell<String> cell02 = cell01.getNextCellOnDimension(0);
		ICell<String> cell10 = cell00.getNextCellOnDimension(1);
		ICell<String> cell11 = cell10.getNextCellOnDimension(0);
		ICell<String> cell12 = cell11.getNextCellOnDimension(0);
		ICell<String> cell20 = cell10.getNextCellOnDimension(1);
		ICell<String> cell21 = cell20.getNextCellOnDimension(0);
		ICell<String> cell22 = cell21.getNextCellOnDimension(0);

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

		// check no intruders
		{
			List<ICell<String>> list = new ArrayList<ICell<String>>();
			list.add(cell00);
			list.add(cell01);
			list.add(cell02);
			list.add(cell10);
			list.add(cell11);
			list.add(cell12);
			list.add(cell20);
			list.add(cell21);
			list.add(cell22);
			assertTrue(list.containsAll(cell00.getAllCellsAround()));
			assertTrue(list.containsAll(cell01.getAllCellsAround()));
			assertTrue(list.containsAll(cell02.getAllCellsAround()));
			assertTrue(list.containsAll(cell10.getAllCellsAround()));
			assertTrue(list.containsAll(cell11.getAllCellsAround()));
			assertTrue(list.containsAll(cell12.getAllCellsAround()));
			assertTrue(list.containsAll(cell20.getAllCellsAround()));
			assertTrue(list.containsAll(cell21.getAllCellsAround()));
			assertTrue(list.containsAll(cell22.getAllCellsAround()));
		}

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

		// check coords
		assertEquals(new Coords(0, 0), cell00.getCoords());
		assertEquals(new Coords(1, 0), cell01.getCoords());
		assertEquals(new Coords(2, 0), cell02.getCoords());
		assertEquals(new Coords(0, 1), cell10.getCoords());
		assertEquals(new Coords(1, 1), cell11.getCoords());
		assertEquals(new Coords(2, 1), cell12.getCoords());
		assertEquals(new Coords(0, 2), cell20.getCoords());
		assertEquals(new Coords(1, 2), cell21.getCoords());
		assertEquals(new Coords(2, 2), cell22.getCoords());
	}

	@Test
	public void testSemiCyclicSpace2D() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).setMemorySize(1).createNewSpace()
				.addDimension(3, false).addDimension(3);

		// get cells
		ICell<String> cell00 = builder.getSpaceOfCell().getOrigin();
		ICell<String> cell01 = cell00.getNextCellOnDimension(0);
		ICell<String> cell02 = cell01.getNextCellOnDimension(0);
		ICell<String> cell10 = cell00.getNextCellOnDimension(1);
		ICell<String> cell11 = cell10.getNextCellOnDimension(0);
		ICell<String> cell12 = cell11.getNextCellOnDimension(0);
		ICell<String> cell20 = cell10.getNextCellOnDimension(1);
		ICell<String> cell21 = cell20.getNextCellOnDimension(0);
		ICell<String> cell22 = cell21.getNextCellOnDimension(0);

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

		// check no intruders
		{
			List<ICell<String>> list = new ArrayList<ICell<String>>();
			list.add(cell00);
			list.add(cell01);
			list.add(cell02);
			list.add(cell10);
			list.add(cell11);
			list.add(cell12);
			list.add(cell20);
			list.add(cell21);
			list.add(cell22);
			assertTrue(list.containsAll(cell00.getAllCellsAround()));
			assertTrue(list.containsAll(cell01.getAllCellsAround()));
			assertTrue(list.containsAll(cell02.getAllCellsAround()));
			assertTrue(list.containsAll(cell10.getAllCellsAround()));
			assertTrue(list.containsAll(cell11.getAllCellsAround()));
			assertTrue(list.containsAll(cell12.getAllCellsAround()));
			assertTrue(list.containsAll(cell20.getAllCellsAround()));
			assertTrue(list.containsAll(cell21.getAllCellsAround()));
			assertTrue(list.containsAll(cell22.getAllCellsAround()));
		}

		// check cells links
		assertEquals(null, cell00.getPreviousCellOnDimension(0));
		assertEquals(cell01, cell00.getNextCellOnDimension(0));
		assertEquals(cell20, cell00.getPreviousCellOnDimension(1));
		assertEquals(cell10, cell00.getNextCellOnDimension(1));

		assertEquals(cell00, cell01.getPreviousCellOnDimension(0));
		assertEquals(cell02, cell01.getNextCellOnDimension(0));
		assertEquals(cell21, cell01.getPreviousCellOnDimension(1));
		assertEquals(cell11, cell01.getNextCellOnDimension(1));

		assertEquals(cell01, cell02.getPreviousCellOnDimension(0));
		assertEquals(null, cell02.getNextCellOnDimension(0));
		assertEquals(cell22, cell02.getPreviousCellOnDimension(1));
		assertEquals(cell12, cell02.getNextCellOnDimension(1));

		assertEquals(null, cell10.getPreviousCellOnDimension(0));
		assertEquals(cell11, cell10.getNextCellOnDimension(0));
		assertEquals(cell00, cell10.getPreviousCellOnDimension(1));
		assertEquals(cell20, cell10.getNextCellOnDimension(1));

		assertEquals(cell10, cell11.getPreviousCellOnDimension(0));
		assertEquals(cell12, cell11.getNextCellOnDimension(0));
		assertEquals(cell01, cell11.getPreviousCellOnDimension(1));
		assertEquals(cell21, cell11.getNextCellOnDimension(1));

		assertEquals(cell11, cell12.getPreviousCellOnDimension(0));
		assertEquals(null, cell12.getNextCellOnDimension(0));
		assertEquals(cell02, cell12.getPreviousCellOnDimension(1));
		assertEquals(cell22, cell12.getNextCellOnDimension(1));

		assertEquals(null, cell20.getPreviousCellOnDimension(0));
		assertEquals(cell21, cell20.getNextCellOnDimension(0));
		assertEquals(cell10, cell20.getPreviousCellOnDimension(1));
		assertEquals(cell00, cell20.getNextCellOnDimension(1));

		assertEquals(cell20, cell21.getPreviousCellOnDimension(0));
		assertEquals(cell22, cell21.getNextCellOnDimension(0));
		assertEquals(cell11, cell21.getPreviousCellOnDimension(1));
		assertEquals(cell01, cell21.getNextCellOnDimension(1));

		assertEquals(cell21, cell22.getPreviousCellOnDimension(0));
		assertEquals(null, cell22.getNextCellOnDimension(0));
		assertEquals(cell12, cell22.getPreviousCellOnDimension(1));
		assertEquals(cell02, cell22.getNextCellOnDimension(1));

		// check coords
		assertEquals(new Coords(0, 0), cell00.getCoords());
		assertEquals(new Coords(1, 0), cell01.getCoords());
		assertEquals(new Coords(2, 0), cell02.getCoords());
		assertEquals(new Coords(0, 1), cell10.getCoords());
		assertEquals(new Coords(1, 1), cell11.getCoords());
		assertEquals(new Coords(2, 1), cell12.getCoords());
		assertEquals(new Coords(0, 2), cell20.getCoords());
		assertEquals(new Coords(1, 2), cell21.getCoords());
		assertEquals(new Coords(2, 2), cell22.getCoords());
	}

	@Test
	public void testIsolatedSpace3D() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).setMemorySize(1).createNewSpace()
				.addDimension(3, false).addDimension(3, false)
				.addDimension(3, false);

		// get cells
		ICell<String> cell000 = builder.getSpaceOfCell().getOrigin();
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

		// check no intruders
		List<ICell<String>> list = new ArrayList<ICell<String>>();
		list.add(cell000);
		list.add(cell001);
		list.add(cell002);
		list.add(cell010);
		list.add(cell011);
		list.add(cell012);
		list.add(cell020);
		list.add(cell021);
		list.add(cell022);
		list.add(cell100);
		list.add(cell101);
		list.add(cell102);
		list.add(cell110);
		list.add(cell111);
		list.add(cell112);
		list.add(cell120);
		list.add(cell121);
		list.add(cell122);
		list.add(cell200);
		list.add(cell201);
		list.add(cell202);
		list.add(cell210);
		list.add(cell211);
		list.add(cell212);
		list.add(cell220);
		list.add(cell221);
		list.add(cell222);
		assertTrue(list.containsAll(cell000.getAllCellsAround()));
		assertTrue(list.containsAll(cell001.getAllCellsAround()));
		assertTrue(list.containsAll(cell002.getAllCellsAround()));
		assertTrue(list.containsAll(cell010.getAllCellsAround()));
		assertTrue(list.containsAll(cell011.getAllCellsAround()));
		assertTrue(list.containsAll(cell012.getAllCellsAround()));
		assertTrue(list.containsAll(cell020.getAllCellsAround()));
		assertTrue(list.containsAll(cell021.getAllCellsAround()));
		assertTrue(list.containsAll(cell022.getAllCellsAround()));
		assertTrue(list.containsAll(cell100.getAllCellsAround()));
		assertTrue(list.containsAll(cell101.getAllCellsAround()));
		assertTrue(list.containsAll(cell102.getAllCellsAround()));
		assertTrue(list.containsAll(cell110.getAllCellsAround()));
		assertTrue(list.containsAll(cell111.getAllCellsAround()));
		assertTrue(list.containsAll(cell112.getAllCellsAround()));
		assertTrue(list.containsAll(cell120.getAllCellsAround()));
		assertTrue(list.containsAll(cell121.getAllCellsAround()));
		assertTrue(list.containsAll(cell122.getAllCellsAround()));
		assertTrue(list.containsAll(cell200.getAllCellsAround()));
		assertTrue(list.containsAll(cell201.getAllCellsAround()));
		assertTrue(list.containsAll(cell202.getAllCellsAround()));
		assertTrue(list.containsAll(cell210.getAllCellsAround()));
		assertTrue(list.containsAll(cell211.getAllCellsAround()));
		assertTrue(list.containsAll(cell212.getAllCellsAround()));
		assertTrue(list.containsAll(cell220.getAllCellsAround()));
		assertTrue(list.containsAll(cell221.getAllCellsAround()));
		assertTrue(list.containsAll(cell222.getAllCellsAround()));

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
		assertEquals(null, cell000.getPreviousCellOnDimension(0));
		assertEquals(cell001, cell000.getNextCellOnDimension(0));
		assertEquals(null, cell000.getPreviousCellOnDimension(1));
		assertEquals(cell010, cell000.getNextCellOnDimension(1));
		assertEquals(null, cell000.getPreviousCellOnDimension(2));
		assertEquals(cell100, cell000.getNextCellOnDimension(2));

		assertEquals(cell000, cell001.getPreviousCellOnDimension(0));
		assertEquals(cell002, cell001.getNextCellOnDimension(0));
		assertEquals(null, cell001.getPreviousCellOnDimension(1));
		assertEquals(cell011, cell001.getNextCellOnDimension(1));
		assertEquals(null, cell001.getPreviousCellOnDimension(2));
		assertEquals(cell101, cell001.getNextCellOnDimension(2));

		assertEquals(cell001, cell002.getPreviousCellOnDimension(0));
		assertEquals(null, cell002.getNextCellOnDimension(0));
		assertEquals(null, cell002.getPreviousCellOnDimension(1));
		assertEquals(cell012, cell002.getNextCellOnDimension(1));
		assertEquals(null, cell002.getPreviousCellOnDimension(2));
		assertEquals(cell102, cell002.getNextCellOnDimension(2));

		assertEquals(null, cell010.getPreviousCellOnDimension(0));
		assertEquals(cell011, cell010.getNextCellOnDimension(0));
		assertEquals(cell000, cell010.getPreviousCellOnDimension(1));
		assertEquals(cell020, cell010.getNextCellOnDimension(1));
		assertEquals(null, cell010.getPreviousCellOnDimension(2));
		assertEquals(cell110, cell010.getNextCellOnDimension(2));

		assertEquals(cell010, cell011.getPreviousCellOnDimension(0));
		assertEquals(cell012, cell011.getNextCellOnDimension(0));
		assertEquals(cell001, cell011.getPreviousCellOnDimension(1));
		assertEquals(cell021, cell011.getNextCellOnDimension(1));
		assertEquals(null, cell011.getPreviousCellOnDimension(2));
		assertEquals(cell111, cell011.getNextCellOnDimension(2));

		assertEquals(cell011, cell012.getPreviousCellOnDimension(0));
		assertEquals(null, cell012.getNextCellOnDimension(0));
		assertEquals(cell002, cell012.getPreviousCellOnDimension(1));
		assertEquals(cell022, cell012.getNextCellOnDimension(1));
		assertEquals(null, cell012.getPreviousCellOnDimension(2));
		assertEquals(cell112, cell012.getNextCellOnDimension(2));

		assertEquals(null, cell020.getPreviousCellOnDimension(0));
		assertEquals(cell021, cell020.getNextCellOnDimension(0));
		assertEquals(cell010, cell020.getPreviousCellOnDimension(1));
		assertEquals(null, cell020.getNextCellOnDimension(1));
		assertEquals(null, cell020.getPreviousCellOnDimension(2));
		assertEquals(cell120, cell020.getNextCellOnDimension(2));

		assertEquals(cell020, cell021.getPreviousCellOnDimension(0));
		assertEquals(cell022, cell021.getNextCellOnDimension(0));
		assertEquals(cell011, cell021.getPreviousCellOnDimension(1));
		assertEquals(null, cell021.getNextCellOnDimension(1));
		assertEquals(null, cell021.getPreviousCellOnDimension(2));
		assertEquals(cell121, cell021.getNextCellOnDimension(2));

		assertEquals(cell021, cell022.getPreviousCellOnDimension(0));
		assertEquals(null, cell022.getNextCellOnDimension(0));
		assertEquals(cell012, cell022.getPreviousCellOnDimension(1));
		assertEquals(null, cell022.getNextCellOnDimension(1));
		assertEquals(null, cell022.getPreviousCellOnDimension(2));
		assertEquals(cell122, cell022.getNextCellOnDimension(2));

		assertEquals(null, cell100.getPreviousCellOnDimension(0));
		assertEquals(cell101, cell100.getNextCellOnDimension(0));
		assertEquals(null, cell100.getPreviousCellOnDimension(1));
		assertEquals(cell110, cell100.getNextCellOnDimension(1));
		assertEquals(cell000, cell100.getPreviousCellOnDimension(2));
		assertEquals(cell200, cell100.getNextCellOnDimension(2));

		assertEquals(cell100, cell101.getPreviousCellOnDimension(0));
		assertEquals(cell102, cell101.getNextCellOnDimension(0));
		assertEquals(null, cell101.getPreviousCellOnDimension(1));
		assertEquals(cell111, cell101.getNextCellOnDimension(1));
		assertEquals(cell001, cell101.getPreviousCellOnDimension(2));
		assertEquals(cell201, cell101.getNextCellOnDimension(2));

		assertEquals(cell101, cell102.getPreviousCellOnDimension(0));
		assertEquals(null, cell102.getNextCellOnDimension(0));
		assertEquals(null, cell102.getPreviousCellOnDimension(1));
		assertEquals(cell112, cell102.getNextCellOnDimension(1));
		assertEquals(cell002, cell102.getPreviousCellOnDimension(2));
		assertEquals(cell202, cell102.getNextCellOnDimension(2));

		assertEquals(null, cell110.getPreviousCellOnDimension(0));
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
		assertEquals(null, cell112.getNextCellOnDimension(0));
		assertEquals(cell102, cell112.getPreviousCellOnDimension(1));
		assertEquals(cell122, cell112.getNextCellOnDimension(1));
		assertEquals(cell012, cell112.getPreviousCellOnDimension(2));
		assertEquals(cell212, cell112.getNextCellOnDimension(2));

		assertEquals(null, cell120.getPreviousCellOnDimension(0));
		assertEquals(cell121, cell120.getNextCellOnDimension(0));
		assertEquals(cell110, cell120.getPreviousCellOnDimension(1));
		assertEquals(null, cell120.getNextCellOnDimension(1));
		assertEquals(cell020, cell120.getPreviousCellOnDimension(2));
		assertEquals(cell220, cell120.getNextCellOnDimension(2));

		assertEquals(cell120, cell121.getPreviousCellOnDimension(0));
		assertEquals(cell122, cell121.getNextCellOnDimension(0));
		assertEquals(cell111, cell121.getPreviousCellOnDimension(1));
		assertEquals(null, cell121.getNextCellOnDimension(1));
		assertEquals(cell021, cell121.getPreviousCellOnDimension(2));
		assertEquals(cell221, cell121.getNextCellOnDimension(2));

		assertEquals(cell121, cell122.getPreviousCellOnDimension(0));
		assertEquals(null, cell122.getNextCellOnDimension(0));
		assertEquals(cell112, cell122.getPreviousCellOnDimension(1));
		assertEquals(null, cell122.getNextCellOnDimension(1));
		assertEquals(cell022, cell122.getPreviousCellOnDimension(2));
		assertEquals(cell222, cell122.getNextCellOnDimension(2));

		assertEquals(null, cell200.getPreviousCellOnDimension(0));
		assertEquals(cell201, cell200.getNextCellOnDimension(0));
		assertEquals(null, cell200.getPreviousCellOnDimension(1));
		assertEquals(cell210, cell200.getNextCellOnDimension(1));
		assertEquals(cell100, cell200.getPreviousCellOnDimension(2));
		assertEquals(null, cell200.getNextCellOnDimension(2));

		assertEquals(cell200, cell201.getPreviousCellOnDimension(0));
		assertEquals(cell202, cell201.getNextCellOnDimension(0));
		assertEquals(null, cell201.getPreviousCellOnDimension(1));
		assertEquals(cell211, cell201.getNextCellOnDimension(1));
		assertEquals(cell101, cell201.getPreviousCellOnDimension(2));
		assertEquals(null, cell201.getNextCellOnDimension(2));

		assertEquals(cell201, cell202.getPreviousCellOnDimension(0));
		assertEquals(null, cell202.getNextCellOnDimension(0));
		assertEquals(null, cell202.getPreviousCellOnDimension(1));
		assertEquals(cell212, cell202.getNextCellOnDimension(1));
		assertEquals(cell102, cell202.getPreviousCellOnDimension(2));
		assertEquals(null, cell202.getNextCellOnDimension(2));

		assertEquals(null, cell210.getPreviousCellOnDimension(0));
		assertEquals(cell211, cell210.getNextCellOnDimension(0));
		assertEquals(cell200, cell210.getPreviousCellOnDimension(1));
		assertEquals(cell220, cell210.getNextCellOnDimension(1));
		assertEquals(cell110, cell210.getPreviousCellOnDimension(2));
		assertEquals(null, cell210.getNextCellOnDimension(2));

		assertEquals(cell210, cell211.getPreviousCellOnDimension(0));
		assertEquals(cell212, cell211.getNextCellOnDimension(0));
		assertEquals(cell201, cell211.getPreviousCellOnDimension(1));
		assertEquals(cell221, cell211.getNextCellOnDimension(1));
		assertEquals(cell111, cell211.getPreviousCellOnDimension(2));
		assertEquals(null, cell211.getNextCellOnDimension(2));

		assertEquals(cell211, cell212.getPreviousCellOnDimension(0));
		assertEquals(null, cell212.getNextCellOnDimension(0));
		assertEquals(cell202, cell212.getPreviousCellOnDimension(1));
		assertEquals(cell222, cell212.getNextCellOnDimension(1));
		assertEquals(cell112, cell212.getPreviousCellOnDimension(2));
		assertEquals(null, cell212.getNextCellOnDimension(2));

		assertEquals(null, cell220.getPreviousCellOnDimension(0));
		assertEquals(cell221, cell220.getNextCellOnDimension(0));
		assertEquals(cell210, cell220.getPreviousCellOnDimension(1));
		assertEquals(null, cell220.getNextCellOnDimension(1));
		assertEquals(cell120, cell220.getPreviousCellOnDimension(2));
		assertEquals(null, cell220.getNextCellOnDimension(2));

		assertEquals(cell220, cell221.getPreviousCellOnDimension(0));
		assertEquals(cell222, cell221.getNextCellOnDimension(0));
		assertEquals(cell211, cell221.getPreviousCellOnDimension(1));
		assertEquals(null, cell221.getNextCellOnDimension(1));
		assertEquals(cell121, cell221.getPreviousCellOnDimension(2));
		assertEquals(null, cell221.getNextCellOnDimension(2));

		assertEquals(cell221, cell222.getPreviousCellOnDimension(0));
		assertEquals(null, cell222.getNextCellOnDimension(0));
		assertEquals(cell212, cell222.getPreviousCellOnDimension(1));
		assertEquals(null, cell222.getNextCellOnDimension(1));
		assertEquals(cell122, cell222.getPreviousCellOnDimension(2));
		assertEquals(null, cell222.getNextCellOnDimension(2));

		// check coords
		assertEquals(new Coords(0, 0, 0), cell000.getCoords());
		assertEquals(new Coords(1, 0, 0), cell001.getCoords());
		assertEquals(new Coords(2, 0, 0), cell002.getCoords());
		assertEquals(new Coords(0, 1, 0), cell010.getCoords());
		assertEquals(new Coords(1, 1, 0), cell011.getCoords());
		assertEquals(new Coords(2, 1, 0), cell012.getCoords());
		assertEquals(new Coords(0, 2, 0), cell020.getCoords());
		assertEquals(new Coords(1, 2, 0), cell021.getCoords());
		assertEquals(new Coords(2, 2, 0), cell022.getCoords());
		assertEquals(new Coords(0, 0, 1), cell100.getCoords());
		assertEquals(new Coords(1, 0, 1), cell101.getCoords());
		assertEquals(new Coords(2, 0, 1), cell102.getCoords());
		assertEquals(new Coords(0, 1, 1), cell110.getCoords());
		assertEquals(new Coords(1, 1, 1), cell111.getCoords());
		assertEquals(new Coords(2, 1, 1), cell112.getCoords());
		assertEquals(new Coords(0, 2, 1), cell120.getCoords());
		assertEquals(new Coords(1, 2, 1), cell121.getCoords());
		assertEquals(new Coords(2, 2, 1), cell122.getCoords());
		assertEquals(new Coords(0, 0, 2), cell200.getCoords());
		assertEquals(new Coords(1, 0, 2), cell201.getCoords());
		assertEquals(new Coords(2, 0, 2), cell202.getCoords());
		assertEquals(new Coords(0, 1, 2), cell210.getCoords());
		assertEquals(new Coords(1, 1, 2), cell211.getCoords());
		assertEquals(new Coords(2, 1, 2), cell212.getCoords());
		assertEquals(new Coords(0, 2, 2), cell220.getCoords());
		assertEquals(new Coords(1, 2, 2), cell221.getCoords());
		assertEquals(new Coords(2, 2, 2), cell222.getCoords());
	}

	@Test
	public void testCyclicSpace3D() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).setMemorySize(1).createNewSpace()
				.addDimension(3).addDimension(3).addDimension(3);

		// get cells
		ICell<String> cell000 = builder.getSpaceOfCell().getOrigin();
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

		// check no intruders
		List<ICell<String>> list = new ArrayList<ICell<String>>();
		list.add(cell000);
		list.add(cell001);
		list.add(cell002);
		list.add(cell010);
		list.add(cell011);
		list.add(cell012);
		list.add(cell020);
		list.add(cell021);
		list.add(cell022);
		list.add(cell100);
		list.add(cell101);
		list.add(cell102);
		list.add(cell110);
		list.add(cell111);
		list.add(cell112);
		list.add(cell120);
		list.add(cell121);
		list.add(cell122);
		list.add(cell200);
		list.add(cell201);
		list.add(cell202);
		list.add(cell210);
		list.add(cell211);
		list.add(cell212);
		list.add(cell220);
		list.add(cell221);
		list.add(cell222);
		assertTrue(list.containsAll(cell000.getAllCellsAround()));
		assertTrue(list.containsAll(cell001.getAllCellsAround()));
		assertTrue(list.containsAll(cell002.getAllCellsAround()));
		assertTrue(list.containsAll(cell010.getAllCellsAround()));
		assertTrue(list.containsAll(cell011.getAllCellsAround()));
		assertTrue(list.containsAll(cell012.getAllCellsAround()));
		assertTrue(list.containsAll(cell020.getAllCellsAround()));
		assertTrue(list.containsAll(cell021.getAllCellsAround()));
		assertTrue(list.containsAll(cell022.getAllCellsAround()));
		assertTrue(list.containsAll(cell100.getAllCellsAround()));
		assertTrue(list.containsAll(cell101.getAllCellsAround()));
		assertTrue(list.containsAll(cell102.getAllCellsAround()));
		assertTrue(list.containsAll(cell110.getAllCellsAround()));
		assertTrue(list.containsAll(cell111.getAllCellsAround()));
		assertTrue(list.containsAll(cell112.getAllCellsAround()));
		assertTrue(list.containsAll(cell120.getAllCellsAround()));
		assertTrue(list.containsAll(cell121.getAllCellsAround()));
		assertTrue(list.containsAll(cell122.getAllCellsAround()));
		assertTrue(list.containsAll(cell200.getAllCellsAround()));
		assertTrue(list.containsAll(cell201.getAllCellsAround()));
		assertTrue(list.containsAll(cell202.getAllCellsAround()));
		assertTrue(list.containsAll(cell210.getAllCellsAround()));
		assertTrue(list.containsAll(cell211.getAllCellsAround()));
		assertTrue(list.containsAll(cell212.getAllCellsAround()));
		assertTrue(list.containsAll(cell220.getAllCellsAround()));
		assertTrue(list.containsAll(cell221.getAllCellsAround()));
		assertTrue(list.containsAll(cell222.getAllCellsAround()));

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

		// check coords
		assertEquals(new Coords(0, 0, 0), cell000.getCoords());
		assertEquals(new Coords(1, 0, 0), cell001.getCoords());
		assertEquals(new Coords(2, 0, 0), cell002.getCoords());
		assertEquals(new Coords(0, 1, 0), cell010.getCoords());
		assertEquals(new Coords(1, 1, 0), cell011.getCoords());
		assertEquals(new Coords(2, 1, 0), cell012.getCoords());
		assertEquals(new Coords(0, 2, 0), cell020.getCoords());
		assertEquals(new Coords(1, 2, 0), cell021.getCoords());
		assertEquals(new Coords(2, 2, 0), cell022.getCoords());
		assertEquals(new Coords(0, 0, 1), cell100.getCoords());
		assertEquals(new Coords(1, 0, 1), cell101.getCoords());
		assertEquals(new Coords(2, 0, 1), cell102.getCoords());
		assertEquals(new Coords(0, 1, 1), cell110.getCoords());
		assertEquals(new Coords(1, 1, 1), cell111.getCoords());
		assertEquals(new Coords(2, 1, 1), cell112.getCoords());
		assertEquals(new Coords(0, 2, 1), cell120.getCoords());
		assertEquals(new Coords(1, 2, 1), cell121.getCoords());
		assertEquals(new Coords(2, 2, 1), cell122.getCoords());
		assertEquals(new Coords(0, 0, 2), cell200.getCoords());
		assertEquals(new Coords(1, 0, 2), cell201.getCoords());
		assertEquals(new Coords(2, 0, 2), cell202.getCoords());
		assertEquals(new Coords(0, 1, 2), cell210.getCoords());
		assertEquals(new Coords(1, 1, 2), cell211.getCoords());
		assertEquals(new Coords(2, 1, 2), cell212.getCoords());
		assertEquals(new Coords(0, 2, 2), cell220.getCoords());
		assertEquals(new Coords(1, 2, 2), cell221.getCoords());
		assertEquals(new Coords(2, 2, 2), cell222.getCoords());
	}

	@Test
	public void testSpaceStates() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "0", "1", "2" });
			}

			@Override
			public void customize(ICell<String> cell) {
				cell.setCurrentState("" + cell.getCoords().get(0));
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory).createNewSpace().addDimension(3);

		// get cells
		ICell<String> cell0 = builder.getSpaceOfCell().getOrigin();
		ICell<String> cell1 = cell0.getNextCellOnDimension(0);
		ICell<String> cell2 = cell1.getNextCellOnDimension(0);

		// check cells state
		assertEquals("0", cell0.getCurrentState());
		assertEquals("1", cell1.getCurrentState());
		assertEquals("2", cell2.getCurrentState());
	}

	@Test
	public void testFinalisation() {
		// generate space
		IStateFactory<String> stateFactory = new AbstractStateFactory<String>() {
			public List<String> getPossibleStates() {
				return Arrays.asList(new String[] { "" });
			}
		};

		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		assertFalse(builder.isSpaceFinalized());

		builder.setStateFactory(stateFactory);
		assertFalse(builder.isSpaceFinalized());

		builder.setMemorySize(1);
		assertFalse(builder.isSpaceFinalized());

		builder.createNewSpace();
		assertFalse(builder.isSpaceFinalized());

		builder.addDimension(3);
		assertFalse(builder.isSpaceFinalized());

		builder.finalizeSpace();
		assertTrue(builder.isSpaceFinalized());

		builder.getSpaceOfCell();
		assertTrue(builder.isSpaceFinalized());
	}

}
