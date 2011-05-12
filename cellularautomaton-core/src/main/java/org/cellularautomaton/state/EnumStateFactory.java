package org.cellularautomaton.state;

import java.util.Arrays;
import java.util.List;

import javax.naming.spi.StateFactory;


/**
 * An enumeration state factory is a {@link StateFactory} specialized in {@link Enum}
 * types. Especially, the possible states are all the enumeration values.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 */
public abstract class EnumStateFactory<StateType extends Enum<?>> extends
		AbstractStateFactory<StateType> {

	public abstract Class<StateType> getEnumType();

	public List<StateType> getPossibleStates() {
		Class<StateType> class1 = getEnumType();
		StateType[] enumConstants = class1.getEnumConstants();
		return Arrays.asList(enumConstants);
	}
}
