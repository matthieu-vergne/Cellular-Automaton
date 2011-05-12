package org.cellularautomaton;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.IRule;
import org.cellularautomaton.space.ISpace;
import org.cellularautomaton.space.SpaceBuilder;
import org.cellularautomaton.state.AbstractStateFactory;
import org.cellularautomaton.state.IStateFactory;
import org.junit.Test;

public class CellularAutomatonTest extends TestCase {

	IStateFactory<String> stateFactory1D = new AbstractStateFactory<String>() {
		public List<String> getPossibleStates() {
			return Arrays.asList(new String[] { "" });
		}

		@Override
		public String getStateFor(ICell<String> cell) {
			return "" + cell.getCoords().get(0);
		}
	};

	IStateFactory<String> stateFactory2D = new AbstractStateFactory<String>() {
		public List<String> getPossibleStates() {
			return Arrays.asList(new String[] { "" });
		}

		@Override
		public String getStateFor(ICell<String> cell) {
			return "" + cell.getCoords().get(1) + cell.getCoords().get(0);
		}
	};

	public void testSpace() {
		// create space
		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory1D).setRule(new IRule<String>() {
			public String calculateNextStateOf(ICell<String> cell) {
				return cell.getRelativeCell(-1).getCurrentState()
						+ cell.getRelativeCell(+1).getCurrentState();
			}
		}).createNewSpace(1).addDimension(4);
		ISpace<String> space = builder.getSpaceOfCell();

		// create automaton
		CellularAutomaton<String> automaton1D = new CellularAutomaton<String>(
				space);

		// check space
		assertEquals(space, automaton1D.getSpace());
	}

	@Test
	public void testEvolutionOf1DAutomaton() {
		// generate space of cells
		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory1D).setRule(new IRule<String>() {
			public String calculateNextStateOf(ICell<String> cell) {
				return cell.getRelativeCell(-1).getCurrentState()
						+ cell.getRelativeCell(+1).getCurrentState();
			}
		}).createNewSpace(1).addDimension(4);

		// get cells
		ISpace<String> space = builder.getSpaceOfCell();
		ICell<String> cell0 = space.getOrigin();
		ICell<String> cell1 = cell0.getNextCellOnDimension(0);
		ICell<String> cell2 = cell1.getNextCellOnDimension(0);
		ICell<String> cell3 = cell2.getNextCellOnDimension(0);

		// generate automaton
		CellularAutomaton<String> automaton1D = new CellularAutomaton<String>(
				space);

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
		SpaceBuilder<String> builder = new SpaceBuilder<String>();
		builder.setStateFactory(stateFactory2D).setMemorySize(1)
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
		ISpace<String> space = builder.getSpaceOfCell();
		ICell<String> cell00 = space.getOrigin();
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
				space);

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
