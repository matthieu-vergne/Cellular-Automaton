package org.cellularautomaton.space;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.GenericSpaceTest.TestState;
import org.cellularautomaton.state.EnumStateFactory;

public class GenericSpaceTest extends ISpaceTest<TestState> {

	public static enum TestState {
		STATE_1, STATE_2, STATE_3, STATE_4,
	}

	private final EnumStateFactory<TestState> stateFactory = new EnumStateFactory<TestState>() {
		@Override
		public Class<TestState> getEnumType() {
			return TestState.class;
		}
	};

	@Override
	public ISpace<TestState> createSpace() {
		ICell<TestState> origin = new SpaceBuilder<TestState>()
				.setStateFactory(stateFactory).createNewSpace(2)
				.addDimension(5).addDimension(5).getSpaceOfCell().getOrigin();
		return new GenericSpace<TestState>(origin);
	}
}
