package org.cellularautomaton.space.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.util.Coords;

public class CellComparisonExpression implements Expression {
	private final Map<Character, Integer> constraints = new HashMap<Character, Integer>(1);
	private final List<Coords> targets = new ArrayList<Coords>(1);
	private ICell<Character> origin;

	@Override
	public boolean evaluate() {
		if (getConstraints() == null) {
			throw new IllegalStateException("The reference is missing.");
		} else if (getOrigin() == null) {
			throw new IllegalStateException("The origin cell is missing.");
		} else if (getTargets().isEmpty()) {
			throw new IllegalStateException("There is no target coords.");
		} else {
			Map<Character, Integer> values = new HashMap<Character, Integer>();
			for (Coords target : targets) {
				Character state = getOrigin().getRelativeCell(target.getAll())
						.getCurrentState();
				if (!values.containsKey(state)) {
					values.put(state, 0);
				}
				values.put(state, values.get(state) + 1);
			}

			for (Character state : constraints.keySet()) {
				if (!values.containsKey(state)
						|| !values.get(state).equals(constraints.get(state))) {
					return false;
				}
			}
			return true;
		}
	}

	public Map<Character, Integer> getConstraints() {
		return constraints;
	}

	public void addConstraint(Character state, Integer count) {
		if (!constraints.containsKey(state)) {
			constraints.put(state, 0);
		}
		constraints.put(state, constraints.get(state) + count);
	}

	public Collection<Coords> getTargets() {
		return targets;
	}

	public void addTarget(Coords target) {
		targets.add(target);
	}

	public ICell<Character> getOrigin() {
		return origin;
	}

	public void setOrigin(ICell<Character> origin) {
		this.origin = origin;
	}
}
