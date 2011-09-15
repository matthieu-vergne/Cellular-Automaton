package org.cellularautomaton.space;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.builder.ScriptSpaceBuilder;
import org.cellularautomaton.util.Coords;
import org.junit.Test;

// TODO test 5D
// TODO test composed rule in non cyclic space (not available cells)
public class ScriptSpaceBuilderTest {
	@Test
	public void testStates() throws IOException {
		// create configuration file
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[cells]");
			pw.println("-X-");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// check states
		List<Character> possibleStates = builder.getStateFactory()
				.getPossibleStates();
		assertEquals(2, possibleStates.size());
		assertTrue(possibleStates.contains('X'));
		assertTrue(possibleStates.contains('-'));
	}

	@Test
	public void testMemory() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("memorySize=5");
			pw.println("[cells]");
			pw.println("-X-");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell0 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell1 = cell0.getNextCellOnDimension(0);
		ICell<Character> cell2 = cell1.getNextCellOnDimension(0);

		// check memory size
		int memorySize = 5;
		assertEquals(memorySize, cell0.getMemorySize());
		assertEquals(memorySize, cell1.getMemorySize());
		assertEquals(memorySize, cell2.getMemorySize());
	}

	@Test
	public void testGenerationFromFile() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[cells]");
			pw.println("-X-");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// create file
		String filePath = new Exception().getStackTrace()[0].getMethodName();
		File file = new File(filePath);
		file.createNewFile();
		file.deleteOnExit();
		FileWriter fw = new FileWriter(file);
		fw.append(description);
		fw.close();

		// generate description space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);
		ISpace<Character> space1 = builder.getSpaceOfCell();

		// generate file space
		builder.createSpaceFromFile(file);
		ISpace<Character> space2 = builder.getSpaceOfCell();

		// checks similarity between spaces
		Collection<ICell<Character>> cells1 = space1.getAllCells();
		Collection<ICell<Character>> cells2 = space2.getAllCells();
		assertEquals(cells1.size(), cells2.size());
		for (ICell<Character> c1 : cells1) {
			boolean found = false;
			for (ICell<Character> c2 : cells2) {
				if (c1.getCoords().equals(c2.getCoords())
						&& c1.getCurrentState().equals(c2.getCurrentState())
						&& c1.getMemorySize() == c2.getMemorySize()) {
					found = true;
					break;
				}
			}
			assertTrue(c1 + " has not been found", found);
		}

		ICell<Character> c1 = space1.getOrigin();
		ICell<Character> c2 = space2.getOrigin();
		assertEquals(c1.getCoords(), c2.getCoords());
		assertEquals(c1.getCurrentState(), c2.getCurrentState());
		assertEquals(c1.getMemorySize(), c2.getMemorySize());

		// delete the config file
		file.delete();
	}

	@Test
	public void testCyclicSpace1D() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[cells]");
			pw.println("-X-");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell0 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell1 = cell0.getNextCellOnDimension(0);
		ICell<Character> cell2 = cell1.getNextCellOnDimension(0);

		// check no intruders
		List<ICell<Character>> list = new ArrayList<ICell<Character>>();
		list.add(cell0);
		list.add(cell1);
		list.add(cell2);
		assertTrue(list.containsAll(cell0.getAllCellsAround()));
		assertTrue(list.containsAll(cell1.getAllCellsAround()));
		assertTrue(list.containsAll(cell2.getAllCellsAround()));

		// check cells exclusivity
		List<ICell<Character>> cells = new ArrayList<ICell<Character>>(list);
		for (int i = 0; i < list.size(); i++) {
			ICell<Character> expected = cells.get(i);
			for (int j = 0; j < list.size(); j++) {
				ICell<Character> result = cells.get(j);
				if (i == j) {
					assertSame(expected, result);
				} else {
					assertNotSame(expected, result);
				}
			}
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

		// check initial values
		assertEquals((Character) '-', cell0.getCurrentState());
		assertEquals((Character) 'X', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());
	}

	@Test
	public void testCyclicSpace2D() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[cells]");
			pw.println("X--");
			pw.println("-X-");
			pw.println("---");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell00 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell01 = cell00.getNextCellOnDimension(0);
		ICell<Character> cell02 = cell01.getNextCellOnDimension(0);
		ICell<Character> cell10 = cell00.getNextCellOnDimension(1);
		ICell<Character> cell11 = cell10.getNextCellOnDimension(0);
		ICell<Character> cell12 = cell11.getNextCellOnDimension(0);
		ICell<Character> cell20 = cell10.getNextCellOnDimension(1);
		ICell<Character> cell21 = cell20.getNextCellOnDimension(0);
		ICell<Character> cell22 = cell21.getNextCellOnDimension(0);

		// check no intruders
		List<ICell<Character>> list = new ArrayList<ICell<Character>>();
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

		// check cells exclusivity
		List<ICell<Character>> cells = new ArrayList<ICell<Character>>(list);
		for (int i = 0; i < list.size(); i++) {
			ICell<Character> expected = cells.get(i);
			for (int j = 0; j < list.size(); j++) {
				ICell<Character> result = cells.get(j);
				if (i == j) {
					assertSame(expected, result);
				} else {
					assertNotSame(expected, result);
				}
			}
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

		// check initial values
		assertEquals((Character) 'X', cell00.getCurrentState());
		assertEquals((Character) '-', cell01.getCurrentState());
		assertEquals((Character) '-', cell02.getCurrentState());
		assertEquals((Character) '-', cell10.getCurrentState());
		assertEquals((Character) 'X', cell11.getCurrentState());
		assertEquals((Character) '-', cell12.getCurrentState());
		assertEquals((Character) '-', cell20.getCurrentState());
		assertEquals((Character) '-', cell21.getCurrentState());
		assertEquals((Character) '-', cell22.getCurrentState());
	}

	@Test
	public void testNotSquareSpace2D() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[cells]");
			pw.println("X-");
			pw.println("-X");
			pw.println("--");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell00 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell01 = cell00.getNextCellOnDimension(1);
		ICell<Character> cell10 = cell00.getNextCellOnDimension(0);
		ICell<Character> cell11 = cell10.getNextCellOnDimension(1);
		ICell<Character> cell20 = cell10.getNextCellOnDimension(0);
		ICell<Character> cell21 = cell20.getNextCellOnDimension(1);

		// check coords
		assertEquals(new Coords(0, 0), cell00.getCoords());
		assertEquals(new Coords(0, 1), cell01.getCoords());
		assertEquals(new Coords(1, 0), cell10.getCoords());
		assertEquals(new Coords(1, 1), cell11.getCoords());
		assertEquals(new Coords(2, 0), cell20.getCoords());
		assertEquals(new Coords(2, 1), cell21.getCoords());

		// check initial values
		assertEquals((Character) 'X', cell00.getCurrentState());
		assertEquals((Character) '-', cell01.getCurrentState());
		assertEquals((Character) '-', cell10.getCurrentState());
		assertEquals((Character) 'X', cell11.getCurrentState());
		assertEquals((Character) '-', cell20.getCurrentState());
		assertEquals((Character) '-', cell21.getCurrentState());

		// check no intruders
		List<ICell<Character>> list = new ArrayList<ICell<Character>>();
		list.add(cell00);
		list.add(cell01);
		list.add(cell10);
		list.add(cell11);
		list.add(cell20);
		list.add(cell21);
		assertTrue(list.containsAll(cell00.getAllCellsAround()));
		assertTrue(list.containsAll(cell01.getAllCellsAround()));
		assertTrue(list.containsAll(cell10.getAllCellsAround()));
		assertTrue(list.containsAll(cell11.getAllCellsAround()));
		assertTrue(list.containsAll(cell20.getAllCellsAround()));
		assertTrue(list.containsAll(cell21.getAllCellsAround()));

		// check cells exclusivity
		List<ICell<Character>> cells = new ArrayList<ICell<Character>>(list);
		for (int i = 0; i < list.size(); i++) {
			ICell<Character> expected = cells.get(i);
			for (int j = 0; j < list.size(); j++) {
				ICell<Character> result = cells.get(j);
				if (i == j) {
					assertSame(expected, result);
				} else {
					assertNotSame(expected, result);
				}
			}
		}

		// check cells links
		assertEquals(cell01, cell00.getPreviousCellOnDimension(1));
		assertEquals(cell01, cell00.getNextCellOnDimension(1));
		assertEquals(cell20, cell00.getPreviousCellOnDimension(0));
		assertEquals(cell10, cell00.getNextCellOnDimension(0));

		assertEquals(cell00, cell01.getPreviousCellOnDimension(1));
		assertEquals(cell00, cell01.getNextCellOnDimension(1));
		assertEquals(cell21, cell01.getPreviousCellOnDimension(0));
		assertEquals(cell11, cell01.getNextCellOnDimension(0));

		assertEquals(cell11, cell10.getPreviousCellOnDimension(1));
		assertEquals(cell11, cell10.getNextCellOnDimension(1));
		assertEquals(cell00, cell10.getPreviousCellOnDimension(0));
		assertEquals(cell20, cell10.getNextCellOnDimension(0));

		assertEquals(cell10, cell11.getPreviousCellOnDimension(1));
		assertEquals(cell10, cell11.getNextCellOnDimension(1));
		assertEquals(cell01, cell11.getPreviousCellOnDimension(0));
		assertEquals(cell21, cell11.getNextCellOnDimension(0));

		assertEquals(cell21, cell20.getPreviousCellOnDimension(1));
		assertEquals(cell21, cell20.getNextCellOnDimension(1));
		assertEquals(cell10, cell20.getPreviousCellOnDimension(0));
		assertEquals(cell00, cell20.getNextCellOnDimension(0));

		assertEquals(cell20, cell21.getPreviousCellOnDimension(1));
		assertEquals(cell20, cell21.getNextCellOnDimension(1));
		assertEquals(cell11, cell21.getPreviousCellOnDimension(0));
		assertEquals(cell01, cell21.getNextCellOnDimension(0));
	}

	@Test
	public void testCyclicSpace3DHorizontal() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-W");
			pw.println("[cells]");
			pw.println("--|W-");
			pw.println("-X|--");
			pw.close();
			description = sw.getBuffer().toString();
		}

		testCyclicSpace3D(description);
	}

	@Test
	public void testCyclicSpace3DVertical() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=XW-");
			pw.println("[cells]");
			pw.println("--");
			pw.println("-X");
			pw.println("++");
			pw.println("W-");
			pw.println("--");
			pw.close();
			description = sw.getBuffer().toString();
		}

		testCyclicSpace3D(description);
	}

	private void testCyclicSpace3D(String description) {
		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell000 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell001 = cell000.getNextCellOnDimension(2);
		ICell<Character> cell010 = cell000.getNextCellOnDimension(1);
		ICell<Character> cell011 = cell010.getNextCellOnDimension(2);
		ICell<Character> cell100 = cell000.getNextCellOnDimension(0);
		ICell<Character> cell101 = cell100.getNextCellOnDimension(2);
		ICell<Character> cell110 = cell100.getNextCellOnDimension(1);
		ICell<Character> cell111 = cell110.getNextCellOnDimension(2);

		// check no intruders
		List<ICell<Character>> list = new ArrayList<ICell<Character>>();
		list.add(cell000);
		list.add(cell001);
		list.add(cell010);
		list.add(cell011);
		list.add(cell100);
		list.add(cell101);
		list.add(cell110);
		list.add(cell111);
		assertTrue(list.containsAll(cell000.getAllCellsAround()));
		assertTrue(list.containsAll(cell001.getAllCellsAround()));
		assertTrue(list.containsAll(cell010.getAllCellsAround()));
		assertTrue(list.containsAll(cell011.getAllCellsAround()));
		assertTrue(list.containsAll(cell100.getAllCellsAround()));
		assertTrue(list.containsAll(cell101.getAllCellsAround()));
		assertTrue(list.containsAll(cell110.getAllCellsAround()));
		assertTrue(list.containsAll(cell111.getAllCellsAround()));

		// check cells exclusivity
		List<ICell<Character>> cells = new ArrayList<ICell<Character>>(list);
		for (int i = 0; i < list.size(); i++) {
			ICell<Character> expected = cells.get(i);
			for (int j = 0; j < list.size(); j++) {
				ICell<Character> result = cells.get(j);
				if (i == j) {
					assertSame(expected, result);
				} else {
					assertNotSame(expected, result);
				}
			}
		}

		// check cells links
		assertEquals(cell001, cell000.getPreviousCellOnDimension(2));
		assertEquals(cell001, cell000.getNextCellOnDimension(2));
		assertEquals(cell010, cell000.getPreviousCellOnDimension(1));
		assertEquals(cell010, cell000.getNextCellOnDimension(1));
		assertEquals(cell100, cell000.getPreviousCellOnDimension(0));
		assertEquals(cell100, cell000.getNextCellOnDimension(0));

		assertEquals(cell000, cell001.getPreviousCellOnDimension(2));
		assertEquals(cell000, cell001.getNextCellOnDimension(2));
		assertEquals(cell011, cell001.getPreviousCellOnDimension(1));
		assertEquals(cell011, cell001.getNextCellOnDimension(1));
		assertEquals(cell101, cell001.getPreviousCellOnDimension(0));
		assertEquals(cell101, cell001.getNextCellOnDimension(0));

		assertEquals(cell011, cell010.getPreviousCellOnDimension(2));
		assertEquals(cell011, cell010.getNextCellOnDimension(2));
		assertEquals(cell000, cell010.getPreviousCellOnDimension(1));
		assertEquals(cell000, cell010.getNextCellOnDimension(1));
		assertEquals(cell110, cell010.getPreviousCellOnDimension(0));
		assertEquals(cell110, cell010.getNextCellOnDimension(0));

		assertEquals(cell010, cell011.getPreviousCellOnDimension(2));
		assertEquals(cell010, cell011.getNextCellOnDimension(2));
		assertEquals(cell001, cell011.getPreviousCellOnDimension(1));
		assertEquals(cell001, cell011.getNextCellOnDimension(1));
		assertEquals(cell111, cell011.getPreviousCellOnDimension(0));
		assertEquals(cell111, cell011.getNextCellOnDimension(0));

		assertEquals(cell101, cell100.getPreviousCellOnDimension(2));
		assertEquals(cell101, cell100.getNextCellOnDimension(2));
		assertEquals(cell110, cell100.getPreviousCellOnDimension(1));
		assertEquals(cell110, cell100.getNextCellOnDimension(1));
		assertEquals(cell000, cell100.getPreviousCellOnDimension(0));
		assertEquals(cell000, cell100.getNextCellOnDimension(0));

		assertEquals(cell100, cell101.getPreviousCellOnDimension(2));
		assertEquals(cell100, cell101.getNextCellOnDimension(2));
		assertEquals(cell111, cell101.getPreviousCellOnDimension(1));
		assertEquals(cell111, cell101.getNextCellOnDimension(1));
		assertEquals(cell001, cell101.getPreviousCellOnDimension(0));
		assertEquals(cell001, cell101.getNextCellOnDimension(0));

		assertEquals(cell111, cell110.getPreviousCellOnDimension(2));
		assertEquals(cell111, cell110.getNextCellOnDimension(2));
		assertEquals(cell100, cell110.getPreviousCellOnDimension(1));
		assertEquals(cell100, cell110.getNextCellOnDimension(1));
		assertEquals(cell010, cell110.getPreviousCellOnDimension(0));
		assertEquals(cell010, cell110.getNextCellOnDimension(0));

		assertEquals(cell110, cell111.getPreviousCellOnDimension(2));
		assertEquals(cell110, cell111.getNextCellOnDimension(2));
		assertEquals(cell101, cell111.getPreviousCellOnDimension(1));
		assertEquals(cell101, cell111.getNextCellOnDimension(1));
		assertEquals(cell011, cell111.getPreviousCellOnDimension(0));
		assertEquals(cell011, cell111.getNextCellOnDimension(0));

		// check coords
		assertEquals(new Coords(0, 0, 0), cell000.getCoords());
		assertEquals(new Coords(0, 0, 1), cell001.getCoords());
		assertEquals(new Coords(0, 1, 0), cell010.getCoords());
		assertEquals(new Coords(0, 1, 1), cell011.getCoords());
		assertEquals(new Coords(1, 0, 0), cell100.getCoords());
		assertEquals(new Coords(1, 0, 1), cell101.getCoords());
		assertEquals(new Coords(1, 1, 0), cell110.getCoords());
		assertEquals(new Coords(1, 1, 1), cell111.getCoords());

		// check initial values
		assertEquals((Character) '-', cell000.getCurrentState());
		assertEquals((Character) '-', cell001.getCurrentState());
		assertEquals((Character) '-', cell010.getCurrentState());
		assertEquals((Character) 'X', cell011.getCurrentState());
		assertEquals((Character) 'W', cell100.getCurrentState());
		assertEquals((Character) '-', cell101.getCurrentState());
		assertEquals((Character) '-', cell110.getCurrentState());
		assertEquals((Character) '-', cell111.getCurrentState());
	}

	@Test
	public void testCyclicSpace4DHorizontal() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-W");
			pw.println("[cells]");
			pw.println("--|W-+X-|--");
			pw.println("-X|--+--|-W");
			pw.close();
			description = sw.getBuffer().toString();
		}

		testCyclicSpace4D(description);
	}

	@Test
	public void testCyclicSpace4DBalanced() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-W");
			pw.println("[cells]");
			pw.println("--|W-");
			pw.println("-X|--");
			pw.println("+++++");
			pw.println("X-|--");
			pw.println("--|-W");
			pw.close();
			description = sw.getBuffer().toString();
		}

		testCyclicSpace4D(description);
	}

	@Test
	public void testCyclicSpace4DVertical() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=XW-");
			pw.println("[cells]");
			pw.println("--");
			pw.println("-X");
			pw.println("++");
			pw.println("W-");
			pw.println("--");
			pw.println("**");
			pw.println("X-");
			pw.println("--");
			pw.println("++");
			pw.println("--");
			pw.println("-W");
			pw.close();
			description = sw.getBuffer().toString();
		}

		testCyclicSpace4D(description);
	}

	private void testCyclicSpace4D(String description) {
		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell0000 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell0001 = cell0000.getNextCellOnDimension(3);
		ICell<Character> cell0010 = cell0000.getNextCellOnDimension(2);
		ICell<Character> cell0011 = cell0010.getNextCellOnDimension(3);
		ICell<Character> cell0100 = cell0000.getNextCellOnDimension(1);
		ICell<Character> cell0101 = cell0100.getNextCellOnDimension(3);
		ICell<Character> cell0110 = cell0100.getNextCellOnDimension(2);
		ICell<Character> cell0111 = cell0110.getNextCellOnDimension(3);
		ICell<Character> cell1000 = cell0000.getNextCellOnDimension(0);
		ICell<Character> cell1001 = cell1000.getNextCellOnDimension(3);
		ICell<Character> cell1010 = cell1000.getNextCellOnDimension(2);
		ICell<Character> cell1011 = cell1010.getNextCellOnDimension(3);
		ICell<Character> cell1100 = cell1000.getNextCellOnDimension(1);
		ICell<Character> cell1101 = cell1100.getNextCellOnDimension(3);
		ICell<Character> cell1110 = cell1100.getNextCellOnDimension(2);
		ICell<Character> cell1111 = cell1110.getNextCellOnDimension(3);

		// check no intruders
		List<ICell<Character>> list = new ArrayList<ICell<Character>>();
		list.add(cell0000);
		list.add(cell0001);
		list.add(cell0010);
		list.add(cell0011);
		list.add(cell0100);
		list.add(cell0101);
		list.add(cell0110);
		list.add(cell0111);
		list.add(cell1000);
		list.add(cell1001);
		list.add(cell1010);
		list.add(cell1011);
		list.add(cell1100);
		list.add(cell1101);
		list.add(cell1110);
		list.add(cell1111);
		assertTrue(list.containsAll(cell0000.getAllCellsAround()));
		assertTrue(list.containsAll(cell0001.getAllCellsAround()));
		assertTrue(list.containsAll(cell0010.getAllCellsAround()));
		assertTrue(list.containsAll(cell0011.getAllCellsAround()));
		assertTrue(list.containsAll(cell0100.getAllCellsAround()));
		assertTrue(list.containsAll(cell0101.getAllCellsAround()));
		assertTrue(list.containsAll(cell0110.getAllCellsAround()));
		assertTrue(list.containsAll(cell0111.getAllCellsAround()));
		assertTrue(list.containsAll(cell1000.getAllCellsAround()));
		assertTrue(list.containsAll(cell1001.getAllCellsAround()));
		assertTrue(list.containsAll(cell1010.getAllCellsAround()));
		assertTrue(list.containsAll(cell1011.getAllCellsAround()));
		assertTrue(list.containsAll(cell1100.getAllCellsAround()));
		assertTrue(list.containsAll(cell1101.getAllCellsAround()));
		assertTrue(list.containsAll(cell1110.getAllCellsAround()));
		assertTrue(list.containsAll(cell1111.getAllCellsAround()));

		// check cells exclusivity
		List<ICell<Character>> cells = new ArrayList<ICell<Character>>(list);
		for (int i = 0; i < list.size(); i++) {
			ICell<Character> expected = cells.get(i);
			for (int j = 0; j < list.size(); j++) {
				ICell<Character> result = cells.get(j);
				if (i == j) {
					assertSame(expected, result);
				} else {
					assertNotSame(expected, result);
				}
			}
		}

		// check cells links
		assertEquals(cell0001, cell0000.getPreviousCellOnDimension(3));
		assertEquals(cell0001, cell0000.getNextCellOnDimension(3));
		assertEquals(cell0010, cell0000.getPreviousCellOnDimension(2));
		assertEquals(cell0010, cell0000.getNextCellOnDimension(2));
		assertEquals(cell0100, cell0000.getPreviousCellOnDimension(1));
		assertEquals(cell0100, cell0000.getNextCellOnDimension(1));
		assertEquals(cell1000, cell0000.getPreviousCellOnDimension(0));
		assertEquals(cell1000, cell0000.getNextCellOnDimension(0));

		assertEquals(cell0000, cell0001.getPreviousCellOnDimension(3));
		assertEquals(cell0000, cell0001.getNextCellOnDimension(3));
		assertEquals(cell0011, cell0001.getPreviousCellOnDimension(2));
		assertEquals(cell0011, cell0001.getNextCellOnDimension(2));
		assertEquals(cell0101, cell0001.getPreviousCellOnDimension(1));
		assertEquals(cell0101, cell0001.getNextCellOnDimension(1));
		assertEquals(cell1001, cell0001.getPreviousCellOnDimension(0));
		assertEquals(cell1001, cell0001.getNextCellOnDimension(0));

		assertEquals(cell0011, cell0010.getPreviousCellOnDimension(3));
		assertEquals(cell0011, cell0010.getNextCellOnDimension(3));
		assertEquals(cell0000, cell0010.getPreviousCellOnDimension(2));
		assertEquals(cell0000, cell0010.getNextCellOnDimension(2));
		assertEquals(cell0110, cell0010.getPreviousCellOnDimension(1));
		assertEquals(cell0110, cell0010.getNextCellOnDimension(1));
		assertEquals(cell1010, cell0010.getPreviousCellOnDimension(0));
		assertEquals(cell1010, cell0010.getNextCellOnDimension(0));

		assertEquals(cell0010, cell0011.getPreviousCellOnDimension(3));
		assertEquals(cell0010, cell0011.getNextCellOnDimension(3));
		assertEquals(cell0001, cell0011.getPreviousCellOnDimension(2));
		assertEquals(cell0001, cell0011.getNextCellOnDimension(2));
		assertEquals(cell0111, cell0011.getPreviousCellOnDimension(1));
		assertEquals(cell0111, cell0011.getNextCellOnDimension(1));
		assertEquals(cell1011, cell0011.getPreviousCellOnDimension(0));
		assertEquals(cell1011, cell0011.getNextCellOnDimension(0));

		assertEquals(cell0101, cell0100.getPreviousCellOnDimension(3));
		assertEquals(cell0101, cell0100.getNextCellOnDimension(3));
		assertEquals(cell0110, cell0100.getPreviousCellOnDimension(2));
		assertEquals(cell0110, cell0100.getNextCellOnDimension(2));
		assertEquals(cell0000, cell0100.getPreviousCellOnDimension(1));
		assertEquals(cell0000, cell0100.getNextCellOnDimension(1));
		assertEquals(cell1100, cell0100.getPreviousCellOnDimension(0));
		assertEquals(cell1100, cell0100.getNextCellOnDimension(0));

		assertEquals(cell0100, cell0101.getPreviousCellOnDimension(3));
		assertEquals(cell0100, cell0101.getNextCellOnDimension(3));
		assertEquals(cell0111, cell0101.getPreviousCellOnDimension(2));
		assertEquals(cell0111, cell0101.getNextCellOnDimension(2));
		assertEquals(cell0001, cell0101.getPreviousCellOnDimension(1));
		assertEquals(cell0001, cell0101.getNextCellOnDimension(1));
		assertEquals(cell1101, cell0101.getPreviousCellOnDimension(0));
		assertEquals(cell1101, cell0101.getNextCellOnDimension(0));

		assertEquals(cell0111, cell0110.getPreviousCellOnDimension(3));
		assertEquals(cell0111, cell0110.getNextCellOnDimension(3));
		assertEquals(cell0100, cell0110.getPreviousCellOnDimension(2));
		assertEquals(cell0100, cell0110.getNextCellOnDimension(2));
		assertEquals(cell0010, cell0110.getPreviousCellOnDimension(1));
		assertEquals(cell0010, cell0110.getNextCellOnDimension(1));
		assertEquals(cell1110, cell0110.getPreviousCellOnDimension(0));
		assertEquals(cell1110, cell0110.getNextCellOnDimension(0));

		assertEquals(cell0110, cell0111.getPreviousCellOnDimension(3));
		assertEquals(cell0110, cell0111.getNextCellOnDimension(3));
		assertEquals(cell0101, cell0111.getPreviousCellOnDimension(2));
		assertEquals(cell0101, cell0111.getNextCellOnDimension(2));
		assertEquals(cell0011, cell0111.getPreviousCellOnDimension(1));
		assertEquals(cell0011, cell0111.getNextCellOnDimension(1));
		assertEquals(cell1111, cell0111.getPreviousCellOnDimension(0));
		assertEquals(cell1111, cell0111.getNextCellOnDimension(0));

		assertEquals(cell1001, cell1000.getPreviousCellOnDimension(3));
		assertEquals(cell1001, cell1000.getNextCellOnDimension(3));
		assertEquals(cell1010, cell1000.getPreviousCellOnDimension(2));
		assertEquals(cell1010, cell1000.getNextCellOnDimension(2));
		assertEquals(cell1100, cell1000.getPreviousCellOnDimension(1));
		assertEquals(cell1100, cell1000.getNextCellOnDimension(1));
		assertEquals(cell0000, cell1000.getPreviousCellOnDimension(0));
		assertEquals(cell0000, cell1000.getNextCellOnDimension(0));

		assertEquals(cell1000, cell1001.getPreviousCellOnDimension(3));
		assertEquals(cell1000, cell1001.getNextCellOnDimension(3));
		assertEquals(cell1011, cell1001.getPreviousCellOnDimension(2));
		assertEquals(cell1011, cell1001.getNextCellOnDimension(2));
		assertEquals(cell1101, cell1001.getPreviousCellOnDimension(1));
		assertEquals(cell1101, cell1001.getNextCellOnDimension(1));
		assertEquals(cell0001, cell1001.getPreviousCellOnDimension(0));
		assertEquals(cell0001, cell1001.getNextCellOnDimension(0));

		assertEquals(cell1011, cell1010.getPreviousCellOnDimension(3));
		assertEquals(cell1011, cell1010.getNextCellOnDimension(3));
		assertEquals(cell1000, cell1010.getPreviousCellOnDimension(2));
		assertEquals(cell1000, cell1010.getNextCellOnDimension(2));
		assertEquals(cell1110, cell1010.getPreviousCellOnDimension(1));
		assertEquals(cell1110, cell1010.getNextCellOnDimension(1));
		assertEquals(cell0010, cell1010.getPreviousCellOnDimension(0));
		assertEquals(cell0010, cell1010.getNextCellOnDimension(0));

		assertEquals(cell1010, cell1011.getPreviousCellOnDimension(3));
		assertEquals(cell1010, cell1011.getNextCellOnDimension(3));
		assertEquals(cell1001, cell1011.getPreviousCellOnDimension(2));
		assertEquals(cell1001, cell1011.getNextCellOnDimension(2));
		assertEquals(cell1111, cell1011.getPreviousCellOnDimension(1));
		assertEquals(cell1111, cell1011.getNextCellOnDimension(1));
		assertEquals(cell0011, cell1011.getPreviousCellOnDimension(0));
		assertEquals(cell0011, cell1011.getNextCellOnDimension(0));

		assertEquals(cell1101, cell1100.getPreviousCellOnDimension(3));
		assertEquals(cell1101, cell1100.getNextCellOnDimension(3));
		assertEquals(cell1110, cell1100.getPreviousCellOnDimension(2));
		assertEquals(cell1110, cell1100.getNextCellOnDimension(2));
		assertEquals(cell1000, cell1100.getPreviousCellOnDimension(1));
		assertEquals(cell1000, cell1100.getNextCellOnDimension(1));
		assertEquals(cell0100, cell1100.getPreviousCellOnDimension(0));
		assertEquals(cell0100, cell1100.getNextCellOnDimension(0));

		assertEquals(cell1100, cell1101.getPreviousCellOnDimension(3));
		assertEquals(cell1100, cell1101.getNextCellOnDimension(3));
		assertEquals(cell1111, cell1101.getPreviousCellOnDimension(2));
		assertEquals(cell1111, cell1101.getNextCellOnDimension(2));
		assertEquals(cell1001, cell1101.getPreviousCellOnDimension(1));
		assertEquals(cell1001, cell1101.getNextCellOnDimension(1));
		assertEquals(cell0101, cell1101.getPreviousCellOnDimension(0));
		assertEquals(cell0101, cell1101.getNextCellOnDimension(0));

		assertEquals(cell1111, cell1110.getPreviousCellOnDimension(3));
		assertEquals(cell1111, cell1110.getNextCellOnDimension(3));
		assertEquals(cell1100, cell1110.getPreviousCellOnDimension(2));
		assertEquals(cell1100, cell1110.getNextCellOnDimension(2));
		assertEquals(cell1010, cell1110.getPreviousCellOnDimension(1));
		assertEquals(cell1010, cell1110.getNextCellOnDimension(1));
		assertEquals(cell0110, cell1110.getPreviousCellOnDimension(0));
		assertEquals(cell0110, cell1110.getNextCellOnDimension(0));

		assertEquals(cell1110, cell1111.getPreviousCellOnDimension(3));
		assertEquals(cell1110, cell1111.getNextCellOnDimension(3));
		assertEquals(cell1101, cell1111.getPreviousCellOnDimension(2));
		assertEquals(cell1101, cell1111.getNextCellOnDimension(2));
		assertEquals(cell1011, cell1111.getPreviousCellOnDimension(1));
		assertEquals(cell1011, cell1111.getNextCellOnDimension(1));
		assertEquals(cell0111, cell1111.getPreviousCellOnDimension(0));
		assertEquals(cell0111, cell1111.getNextCellOnDimension(0));

		// check coords
		assertEquals(new Coords(0, 0, 0, 0), cell0000.getCoords());
		assertEquals(new Coords(0, 0, 0, 1), cell0001.getCoords());
		assertEquals(new Coords(0, 0, 1, 0), cell0010.getCoords());
		assertEquals(new Coords(0, 0, 1, 1), cell0011.getCoords());
		assertEquals(new Coords(0, 1, 0, 0), cell0100.getCoords());
		assertEquals(new Coords(0, 1, 0, 1), cell0101.getCoords());
		assertEquals(new Coords(0, 1, 1, 0), cell0110.getCoords());
		assertEquals(new Coords(0, 1, 1, 1), cell0111.getCoords());
		assertEquals(new Coords(1, 0, 0, 0), cell1000.getCoords());
		assertEquals(new Coords(1, 0, 0, 1), cell1001.getCoords());
		assertEquals(new Coords(1, 0, 1, 0), cell1010.getCoords());
		assertEquals(new Coords(1, 0, 1, 1), cell1011.getCoords());
		assertEquals(new Coords(1, 1, 0, 0), cell1100.getCoords());
		assertEquals(new Coords(1, 1, 0, 1), cell1101.getCoords());
		assertEquals(new Coords(1, 1, 1, 0), cell1110.getCoords());
		assertEquals(new Coords(1, 1, 1, 1), cell1111.getCoords());

		// check initial values
		assertEquals((Character) '-', cell0000.getCurrentState());
		assertEquals((Character) '-', cell0001.getCurrentState());
		assertEquals((Character) '-', cell0010.getCurrentState());
		assertEquals((Character) 'X', cell0011.getCurrentState());
		assertEquals((Character) 'W', cell0100.getCurrentState());
		assertEquals((Character) '-', cell0101.getCurrentState());
		assertEquals((Character) '-', cell0110.getCurrentState());
		assertEquals((Character) '-', cell0111.getCurrentState());
		assertEquals((Character) 'X', cell1000.getCurrentState());
		assertEquals((Character) '-', cell1001.getCurrentState());
		assertEquals((Character) '-', cell1010.getCurrentState());
		assertEquals((Character) '-', cell1011.getCurrentState());
		assertEquals((Character) '-', cell1100.getCurrentState());
		assertEquals((Character) '-', cell1101.getCurrentState());
		assertEquals((Character) '-', cell1110.getCurrentState());
		assertEquals((Character) 'W', cell1111.getCurrentState());
	}

	@Test
	public void testSimpleRule() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[rule]");
			pw.println("(0)=X:-");
			pw.println("(0)=-:X");
			pw.println("[cells]");
			pw.println("-X-");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell0 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell1 = cell0.getNextCellOnDimension(0);
		ICell<Character> cell2 = cell1.getNextCellOnDimension(0);

		// check initial values
		assertEquals((Character) '-', cell0.getCurrentState());
		assertEquals((Character) 'X', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());

		// check rule
		CellularAutomaton<Character> automaton = new CellularAutomaton<Character>(
				builder.getSpaceOfCell());
		automaton.doStep();
		assertEquals((Character) 'X', cell0.getCurrentState());
		assertEquals((Character) '-', cell1.getCurrentState());
		assertEquals((Character) 'X', cell2.getCurrentState());
		automaton.doStep();
		assertEquals((Character) '-', cell0.getCurrentState());
		assertEquals((Character) 'X', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());
	}

	@Test
	public void testComposedRule() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[rule]");
			pw.println("(0)=X : -");
			pw.println("(0)=- & (-1)=X | (0)=- & (+1)=X : X");
			pw.println("[cells]");
			pw.println("X----");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell0 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell1 = cell0.getNextCellOnDimension(0);
		ICell<Character> cell2 = cell1.getNextCellOnDimension(0);
		ICell<Character> cell3 = cell2.getNextCellOnDimension(0);
		ICell<Character> cell4 = cell3.getNextCellOnDimension(0);

		// check initial values
		assertEquals((Character) 'X', cell0.getCurrentState());
		assertEquals((Character) '-', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());
		assertEquals((Character) '-', cell3.getCurrentState());
		assertEquals((Character) '-', cell4.getCurrentState());

		// check rule
		CellularAutomaton<Character> automaton = new CellularAutomaton<Character>(
				builder.getSpaceOfCell());
		automaton.doStep();
		assertEquals((Character) '-', cell0.getCurrentState());
		assertEquals((Character) 'X', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());
		assertEquals((Character) '-', cell3.getCurrentState());
		assertEquals((Character) 'X', cell4.getCurrentState());
		automaton.doStep();
		assertEquals((Character) 'X', cell0.getCurrentState());
		assertEquals((Character) '-', cell1.getCurrentState());
		assertEquals((Character) 'X', cell2.getCurrentState());
		assertEquals((Character) 'X', cell3.getCurrentState());
		assertEquals((Character) '-', cell4.getCurrentState());
		automaton.doStep();
		assertEquals((Character) '-', cell0.getCurrentState());
		assertEquals((Character) 'X', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());
		assertEquals((Character) '-', cell3.getCurrentState());
		assertEquals((Character) 'X', cell4.getCurrentState());
		automaton.doStep();
		assertEquals((Character) 'X', cell0.getCurrentState());
		assertEquals((Character) '-', cell1.getCurrentState());
		assertEquals((Character) 'X', cell2.getCurrentState());
		assertEquals((Character) 'X', cell3.getCurrentState());
		assertEquals((Character) '-', cell4.getCurrentState());
	}

	@Test
	public void testComplexRule() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[rule]");
			pw.println("(0)=X : -");
			pw.println("(0)=- & ((-1)=X | (+1)=X) : X");
			pw.println("[cells]");
			pw.println("X----");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		ScriptSpaceBuilder builder = new ScriptSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell0 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell1 = cell0.getNextCellOnDimension(0);
		ICell<Character> cell2 = cell1.getNextCellOnDimension(0);
		ICell<Character> cell3 = cell2.getNextCellOnDimension(0);
		ICell<Character> cell4 = cell3.getNextCellOnDimension(0);

		// check initial values
		assertEquals((Character) 'X', cell0.getCurrentState());
		assertEquals((Character) '-', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());
		assertEquals((Character) '-', cell3.getCurrentState());
		assertEquals((Character) '-', cell4.getCurrentState());

		// check rule
		CellularAutomaton<Character> automaton = new CellularAutomaton<Character>(
				builder.getSpaceOfCell());
		automaton.doStep();
		assertEquals((Character) '-', cell0.getCurrentState());
		assertEquals((Character) 'X', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());
		assertEquals((Character) '-', cell3.getCurrentState());
		assertEquals((Character) 'X', cell4.getCurrentState());
		automaton.doStep();
		assertEquals((Character) 'X', cell0.getCurrentState());
		assertEquals((Character) '-', cell1.getCurrentState());
		assertEquals((Character) 'X', cell2.getCurrentState());
		assertEquals((Character) 'X', cell3.getCurrentState());
		assertEquals((Character) '-', cell4.getCurrentState());
		automaton.doStep();
		assertEquals((Character) '-', cell0.getCurrentState());
		assertEquals((Character) 'X', cell1.getCurrentState());
		assertEquals((Character) '-', cell2.getCurrentState());
		assertEquals((Character) '-', cell3.getCurrentState());
		assertEquals((Character) 'X', cell4.getCurrentState());
		automaton.doStep();
		assertEquals((Character) 'X', cell0.getCurrentState());
		assertEquals((Character) '-', cell1.getCurrentState());
		assertEquals((Character) 'X', cell2.getCurrentState());
		assertEquals((Character) 'X', cell3.getCurrentState());
		assertEquals((Character) '-', cell4.getCurrentState());
	}
}
