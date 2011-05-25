package org.cellularautomaton.state;

import java.util.Arrays;
import java.util.List;

import javax.naming.spi.StateFactory;

/**
 * An enumeration state factory is a {@link StateFactory} specialized in
 * {@link Enum} types. Especially, the possible states are all the enumeration
 * values.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public abstract class EnumStateFactory<StateType extends Enum<StateType>> extends
		AbstractStateFactory<StateType> {

	/**
	 * This method is used to get the values of the enumeration, as we cannot
	 * get them from the generics and we cannot get the corresponding Class<?>
	 * in a simple automatic way.
	 * 
	 * @return the class of the enum type used for states
	 */
	public abstract Class<StateType> getEnumType();

	/**
	 * This method give all the values defined by the enumeration type.
	 */
	public List<StateType> getPossibleStates() {
		return Arrays.asList(getEnumType().getEnumConstants());
	}
}
