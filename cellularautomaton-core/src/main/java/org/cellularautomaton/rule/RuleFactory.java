package org.cellularautomaton.rule;

/**
 * A rule factory allows to create different type of rules easily.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public class RuleFactory<StateType> {

	/**
	 * The static rule instance.
	 */
	private IRule<StateType> staticRule = null;

	/**
	 * 
	 * @return a rule which does not change the current state of the cell
	 */
	public IRule<StateType> getStaticRuleInstance() {
		if (staticRule == null) {
			staticRule = new StaticRule<StateType>();
		}
		return staticRule;
	}
}
