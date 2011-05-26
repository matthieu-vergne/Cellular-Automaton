package org.cellularautomaton.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.cellularautomaton.state.EnumStateFactoryTest.TestEnum;
import org.junit.Test;


public class EnumStateFactoryTest extends AbstractStateFactoryTest<TestEnum> {

	public static enum TestEnum {
		VAL1,
		VAL2,
		VAL3,
	}
	
	@Override
	public IStateFactory<TestEnum> createFactory() {
		return new EnumStateFactory<TestEnum>() {
			@Override
			public Class<TestEnum> getEnumType() {
				return TestEnum.class;
			}
		};
	}
	
	@Test
	public void testCompletePossibilities() {
		IStateFactory<TestEnum> factory = createFactory();
		List<TestEnum> states = factory.getPossibleStates();
		assertEquals(3, states.size());
		assertTrue(states.contains(TestEnum.VAL1));
		assertTrue(states.contains(TestEnum.VAL2));
		assertTrue(states.contains(TestEnum.VAL3));
	}
	
}
