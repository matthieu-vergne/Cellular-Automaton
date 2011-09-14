package org.cellularautomaton.sample.langtonantoptimized;

import java.util.Collection;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.optimization.CalculateOnlyEvolvingZonesOptimization;
import org.cellularautomaton.sample.langtonant.AntState;

public class AntAutomatonFactory {

	public static CellularAutomaton<AntState> createAutomaton() {
		CellularAutomaton<AntState> automaton = org.cellularautomaton.sample.langtonant.AntAutomatonFactory
				.createAutomaton();
		automaton
				.addOptimization(new CalculateOnlyEvolvingZonesOptimization<AntState>() {
					@Override
					protected Collection<ICell<AntState>> getCellsDependingTo(
							ICell<AntState> cell) {
						// heuristic optimization
						return cell.getAllCellsAround();
					}
				});

		return automaton;
	}
}
