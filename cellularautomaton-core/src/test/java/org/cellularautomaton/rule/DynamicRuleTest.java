package org.cellularautomaton.rule;

import static org.junit.Assert.assertEquals;

import org.cellularautomaton.cell.GenericCell;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.DynamicRule.RulePart;
import org.junit.Test;

public class DynamicRuleTest {

	@Test
	public void testBasicBehavior() {
		DynamicRule<Integer> rule = new DynamicRule<Integer>();
		ICell<Integer> cell = new GenericCell<Integer>();
		cell.setMemory(1, 0);
		cell.setRule(rule);

		{
			Integer state = 0;
			cell.setCurrentState(state);
			cell.calculateNextState();
			cell.applyNextState();
			assertEquals(state, cell.getCurrentState());
		}

		{
			Integer state = 50;
			cell.setCurrentState(state);
			cell.calculateNextState();
			cell.applyNextState();
			assertEquals(state, cell.getCurrentState());
		}

		{
			Integer state = -120;
			cell.setCurrentState(state);
			cell.calculateNextState();
			cell.applyNextState();
			assertEquals(state, cell.getCurrentState());
		}
	}

	@Test
	public void testDynamicBehavior() {
		DynamicRule<Integer> rule = new DynamicRule<Integer>();
		ICell<Integer> cell = new GenericCell<Integer>();
		cell.setMemory(1, 0);
		cell.setRule(rule);

		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(0, (int) cell.getCurrentState());

		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(0, (int) cell.getCurrentState());

		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(0, (int) cell.getCurrentState());

		RulePart<Integer> part1 = new RulePart<Integer>() {
			private ICell<Integer> cell;

			@Override
			public boolean isVerifiedBy(ICell<Integer> cell) {
				this.cell = cell;
				return cell.getCurrentState() < 3;
			}

			@Override
			public Integer getAssignedState() {
				return cell.getCurrentState() + 1;
			}
		};
		rule.addPart(part1);
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(1, (int) cell.getCurrentState());
		
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(2, (int) cell.getCurrentState());
		
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(3, (int) cell.getCurrentState());
		
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(3, (int) cell.getCurrentState());
		
		RulePart<Integer> part2 = new RulePart<Integer>() {
			@Override
			public boolean isVerifiedBy(ICell<Integer> cell) {
				return cell.getCurrentState() == 2;
			}

			@Override
			public Integer getAssignedState() {
				return 0;
			}
		};
		rule.removePart(part1);
		rule.addPart(part2);
		rule.addPart(part1);
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(3, (int) cell.getCurrentState());
		
		cell.setCurrentState(0);
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(1, (int) cell.getCurrentState());
		
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(2, (int) cell.getCurrentState());
		
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(0, (int) cell.getCurrentState());
		
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(1, (int) cell.getCurrentState());
		
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(2, (int) cell.getCurrentState());
		
		cell.calculateNextState();
		cell.applyNextState();
		assertEquals(0, (int) cell.getCurrentState());
	}
}
