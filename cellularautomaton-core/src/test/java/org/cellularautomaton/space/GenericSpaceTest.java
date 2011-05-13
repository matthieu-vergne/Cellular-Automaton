package org.cellularautomaton.space;

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
		SpaceBuilder<TestState> builder = new SpaceBuilder<TestState>() {
			@Override
			protected ISpace<TestState> instantiateEmptySpace() {
				return new GenericSpace<TestState>();
			}
		};
		return builder.setStateFactory(stateFactory).createNewSpace()
				.addDimension(5).addDimension(5).getSpaceOfCell();
	}
}
