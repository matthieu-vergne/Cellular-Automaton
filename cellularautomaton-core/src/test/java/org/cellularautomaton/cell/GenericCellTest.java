package org.cellularautomaton.cell;

import org.cellularautomaton.cell.GenericCell;
import org.cellularautomaton.cell.ICell;

/**
 * This test case uses the generic test defined for the {@link ICell} interface.
 * It can be expanded with more tests but the basic tests must be checked too,
 * that is why it extends the abstract test case {@link ICellTest}.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
public class GenericCellTest extends ICellTest {

	public <StateType> ICell<StateType> createCell() {
		return new GenericCell<StateType>();
	};
}
