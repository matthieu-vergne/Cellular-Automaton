package org.cellularautomaton.rule;

import java.util.ArrayList;
import java.util.List;

import org.cellularautomaton.cell.ICell;

/**
 * <p>
 * A dynamic rule is a rule which can be dynamically builded. That means each
 * part of the rule can be given at any time. A part of the rule is a pair
 * <em>{condition, state}</em>, meanings that when the <em>condition</em> is
 * verified (true) the corresponding <em>state</em> is selected as the next
 * state of the cell.
 * </p>
 * <p>
 * The different parts of the rule are checked in the same order they are added.
 * If no part is verified, the current state of the cell is returned.
 * Consequently, this rule acts basically like a {@link StaticRule}, the added
 * parts giving the logics to change the state of the cell. This default
 * behavior can be replaced by another, adding a part where the condition is
 * always true. Such part should be the last one as it avoid to read the next
 * parts.
 * </p>
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <StateType>
 *            the type of data used by the cell, it can be {@link Boolean} for a
 *            simple "On/Off" state, a numeral state like {@link Integer} or
 *            {@link Float} for arithmetical states, or any specific type of
 *            data for particular uses.
 */
public class DynamicRule<StateType> implements IRule<StateType> {

	private final List<RulePart<StateType>> rules = new ArrayList<RulePart<StateType>>();

	@Override
	public StateType calculateNextStateOf(ICell<StateType> cell) {
		for (RulePart<StateType> rule : rules) {
			if (rule.isVerifiedBy(cell)) {
				return rule.getAssignedState();
			}
		}
		return cell.getCurrentState();
	}

	public void addPart(RulePart<StateType> part) {
		rules.add(part);
	}

	public void removePart(RulePart<StateType> part) {
		rules.remove(part);
	}

	public static interface RulePart<StateType> {
		public boolean isVerifiedBy(ICell<StateType> cell);

		public StateType getAssignedState();
	}
}
