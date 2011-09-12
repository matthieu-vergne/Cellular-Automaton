package org.cellularautomaton.space.expression;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.util.Coords;

public class CellComparisonExpression implements Expression {
	public static final Coords ORIGIN = new Coords();

	private Character reference;
	private Coords target;
	private ICell<Character> origin;

	@Override
	public boolean evaluate() {
		if (getReference() == null) {
			throw new IllegalStateException("The reference is missing.");
		} else if (getOrigin() == null) {
			throw new IllegalStateException("The origin cell is missing.");
		} else if (getTarget() == null) {
			throw new IllegalStateException("The target coords are missing.");
		} else {
			ICell<Character> cell = getOrigin();
			if (getTarget() != ORIGIN) {
				cell = cell.getRelativeCell(getTarget().getAll());
			}
			return getReference().equals(cell.getCurrentState());
		}
	}

	public Character getReference() {
		return reference;
	}

	public void setReference(Character reference) {
		this.reference = reference;
	}

	public Coords getTarget() {
		return target;
	}

	public void setTarget(Coords target) {
		this.target = target;
	}

	public ICell<Character> getOrigin() {
		return origin;
	}

	public void setOrigin(ICell<Character> origin) {
		this.origin = origin;
	}
}
