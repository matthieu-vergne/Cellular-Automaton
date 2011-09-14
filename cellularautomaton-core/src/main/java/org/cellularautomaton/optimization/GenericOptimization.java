package org.cellularautomaton.optimization;

/**
 * This optimization is used for whatever you need which does not interact with
 * the internal components of the automaton (as nothing is given in argument and
 * no output is used by the automaton).
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface GenericOptimization<StateType> extends
		OptimizationType<StateType> {

	public void execute();
}
