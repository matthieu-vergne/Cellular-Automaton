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
import java.util.ArrayList;
import java.util.List;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.util.Coords;
import org.junit.Test;


public class FileSpaceBuilderTest {
	@Test
	public void testIsolatedSpace1D() throws IOException {
		// create config file
		String filePath = "testBuilderFromFile";
		File file = new File(filePath);
		file.createNewFile();
		file.deleteOnExit();
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		pw.println("[config]");
		pw.println("states=X-");
		pw.println("memorySize=5");
		pw.println("[rule]");
		pw.println(""); // TODO create a rule scripting language
		pw.println("[cells]");
		pw.println("-X-");
		pw.close();
		
		// generate space
		FileSpaceBuilder builder = new FileSpaceBuilder();
		builder.createSpaceFromFile(filePath);
		
		// check state factory
		List<Character> possibleStates = builder.getStateFactory().getPossibleStates();
		assertEquals(2, possibleStates.size());
		assertTrue(possibleStates.contains('X'));
		assertTrue(possibleStates.contains('-'));
		
		// get cells
		ICell<Character> cell000 = builder.getSpaceOfCell().getOrigin();
		ICell<Character> cell001 = cell000.getNextCellOnDimension(0);
		ICell<Character> cell002 = cell001.getNextCellOnDimension(0);
		
		// check no intruders
		List<ICell<Character>> list = new ArrayList<ICell<Character>>();
		list.add(cell000);
		list.add(cell001);
		list.add(cell002);
		assertTrue(list.containsAll(cell000.getAllCellsAround()));
		assertTrue(list.containsAll(cell001.getAllCellsAround()));
		assertTrue(list.containsAll(cell002.getAllCellsAround()));

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
		assertEquals(cell002, cell000.getPreviousCellOnDimension(0));
		assertEquals(cell001, cell000.getNextCellOnDimension(0));

		assertEquals(cell000, cell001.getPreviousCellOnDimension(0));
		assertEquals(cell002, cell001.getNextCellOnDimension(0));

		assertEquals(cell001, cell002.getPreviousCellOnDimension(0));
		assertEquals(cell000, cell002.getNextCellOnDimension(0));


		// check coords
		assertEquals(new Coords(0), cell000.getCoords());
		assertEquals(new Coords(1), cell001.getCoords());
		assertEquals(new Coords(2), cell002.getCoords());
		
		// check initial values
		assertEquals((Character) '-', cell000.getCurrentState());
		assertEquals((Character) 'X', cell001.getCurrentState());
		assertEquals((Character) '-', cell002.getCurrentState());
		
		// check memory size
		int memorySize = 5;
		assertEquals(memorySize, cell000.getMemorySize());
		assertEquals(memorySize, cell001.getMemorySize());
		assertEquals(memorySize, cell002.getMemorySize());
		
		// check rule
		fail("todo");
		
		// delete the config file
		file.delete();
	}
}
