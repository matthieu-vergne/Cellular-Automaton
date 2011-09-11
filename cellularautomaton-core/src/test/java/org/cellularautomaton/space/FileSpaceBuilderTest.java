package org.cellularautomaton.space;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.util.Coords;
import org.junit.Test;

// TODO test 4D
// TODO test 5D
// TODO test composed rule (several cells)
//TODO test complex rule (parentheses)
//TODO test composed rule in non cyclic space
public class FileSpaceBuilderTest {
	@Test
	public void testStates() throws IOException {
		// create config file
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
		FileSpaceBuilder builder = new FileSpaceBuilder();
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
		FileSpaceBuilder builder = new FileSpaceBuilder();
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
		FileSpaceBuilder builder = new FileSpaceBuilder();
		builder.createSpaceFromString(description);
		ISpace<Character> spaceString = builder.getSpaceOfCell();

		// generate file space
		builder.createSpaceFromFile(file);
		ISpace<Character> spaceFile = builder.getSpaceOfCell();

		// TODO checks similarity
		fail("todo");

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
		FileSpaceBuilder builder = new FileSpaceBuilder();
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
			pw.println("---");
			pw.println("-X-");
			pw.println("---");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		FileSpaceBuilder builder = new FileSpaceBuilder();
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
		assertEquals((Character) '-', cell00.getCurrentState());
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
	public void testCyclicSpace3D() throws IOException {
		// create description
		String description;
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("[config]");
			pw.println("states=X-");
			pw.println("[cells]");
			pw.println("--|--");
			pw.println("-X|--");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		FileSpaceBuilder builder = new FileSpaceBuilder();
		builder.createSpaceFromString(description);

		// get cells
		ICell<Character> cell000 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell001 = cell000.getNextCellOnDimension(0);
		ICell<Character> cell010 = cell000.getNextCellOnDimension(1);
		ICell<Character> cell011 = cell010.getNextCellOnDimension(0);
		ICell<Character> cell100 = cell000.getNextCellOnDimension(2);
		ICell<Character> cell101 = cell100.getNextCellOnDimension(0);
		ICell<Character> cell110 = cell100.getNextCellOnDimension(1);
		ICell<Character> cell111 = cell110.getNextCellOnDimension(0);

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
		assertEquals(cell001, cell000.getPreviousCellOnDimension(0));
		assertEquals(cell001, cell000.getNextCellOnDimension(0));
		assertEquals(cell010, cell000.getPreviousCellOnDimension(1));
		assertEquals(cell010, cell000.getNextCellOnDimension(1));
		assertEquals(cell100, cell000.getPreviousCellOnDimension(2));
		assertEquals(cell100, cell000.getNextCellOnDimension(2));

		assertEquals(cell000, cell001.getPreviousCellOnDimension(0));
		assertEquals(cell000, cell001.getNextCellOnDimension(0));
		assertEquals(cell011, cell001.getPreviousCellOnDimension(1));
		assertEquals(cell011, cell001.getNextCellOnDimension(1));
		assertEquals(cell101, cell001.getPreviousCellOnDimension(2));
		assertEquals(cell101, cell001.getNextCellOnDimension(2));

		assertEquals(cell011, cell010.getPreviousCellOnDimension(0));
		assertEquals(cell011, cell010.getNextCellOnDimension(0));
		assertEquals(cell000, cell010.getPreviousCellOnDimension(1));
		assertEquals(cell000, cell010.getNextCellOnDimension(1));
		assertEquals(cell110, cell010.getPreviousCellOnDimension(2));
		assertEquals(cell110, cell010.getNextCellOnDimension(2));

		assertEquals(cell010, cell011.getPreviousCellOnDimension(0));
		assertEquals(cell010, cell011.getNextCellOnDimension(0));
		assertEquals(cell001, cell011.getPreviousCellOnDimension(1));
		assertEquals(cell001, cell011.getNextCellOnDimension(1));
		assertEquals(cell111, cell011.getPreviousCellOnDimension(2));
		assertEquals(cell111, cell011.getNextCellOnDimension(2));

		assertEquals(cell101, cell100.getPreviousCellOnDimension(0));
		assertEquals(cell101, cell100.getNextCellOnDimension(0));
		assertEquals(cell110, cell100.getPreviousCellOnDimension(1));
		assertEquals(cell110, cell100.getNextCellOnDimension(1));
		assertEquals(cell000, cell100.getPreviousCellOnDimension(2));
		assertEquals(cell000, cell100.getNextCellOnDimension(2));

		assertEquals(cell100, cell101.getPreviousCellOnDimension(0));
		assertEquals(cell100, cell101.getNextCellOnDimension(0));
		assertEquals(cell111, cell101.getPreviousCellOnDimension(1));
		assertEquals(cell111, cell101.getNextCellOnDimension(1));
		assertEquals(cell001, cell101.getPreviousCellOnDimension(2));
		assertEquals(cell001, cell101.getNextCellOnDimension(2));

		assertEquals(cell111, cell110.getPreviousCellOnDimension(0));
		assertEquals(cell111, cell110.getNextCellOnDimension(0));
		assertEquals(cell100, cell110.getPreviousCellOnDimension(1));
		assertEquals(cell100, cell110.getNextCellOnDimension(1));
		assertEquals(cell010, cell110.getPreviousCellOnDimension(2));
		assertEquals(cell010, cell110.getNextCellOnDimension(2));

		assertEquals(cell110, cell111.getPreviousCellOnDimension(0));
		assertEquals(cell110, cell111.getNextCellOnDimension(0));
		assertEquals(cell101, cell111.getPreviousCellOnDimension(1));
		assertEquals(cell101, cell111.getNextCellOnDimension(1));
		assertEquals(cell011, cell111.getPreviousCellOnDimension(2));
		assertEquals(cell011, cell111.getNextCellOnDimension(2));

		// check coords
		assertEquals(new Coords(0, 0, 0), cell000.getCoords());
		assertEquals(new Coords(1, 0, 0), cell001.getCoords());
		assertEquals(new Coords(0, 1, 0), cell010.getCoords());
		assertEquals(new Coords(1, 1, 0), cell011.getCoords());
		assertEquals(new Coords(0, 0, 1), cell100.getCoords());
		assertEquals(new Coords(1, 0, 1), cell101.getCoords());
		assertEquals(new Coords(0, 1, 1), cell110.getCoords());
		assertEquals(new Coords(1, 1, 1), cell111.getCoords());

		// check initial values
		assertEquals((Character) '-', cell000.getCurrentState());
		assertEquals((Character) '-', cell001.getCurrentState());
		assertEquals((Character) '-', cell010.getCurrentState());
		assertEquals((Character) '-', cell011.getCurrentState());
		assertEquals((Character) '-', cell100.getCurrentState());
		assertEquals((Character) '-', cell101.getCurrentState());
		assertEquals((Character) 'X', cell110.getCurrentState());
		assertEquals((Character) '-', cell111.getCurrentState());
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
			pw.println("=X:-");
			pw.println("=-:X");
			pw.println("[cells]");
			pw.println("-X-");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		FileSpaceBuilder builder = new FileSpaceBuilder();
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
			pw.println("=X : -");
			pw.println("=- & (-1)=X | =- & (+1)=X : X");
			pw.println("[cells]");
			pw.println("X----");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		FileSpaceBuilder builder = new FileSpaceBuilder();
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
			pw.println("=X : -");
			pw.println("=- & ((-1)=X | (+1)=X) : X");
			pw.println("[cells]");
			pw.println("X----");
			pw.close();
			description = sw.getBuffer().toString();
		}

		// generate space
		FileSpaceBuilder builder = new FileSpaceBuilder();
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
