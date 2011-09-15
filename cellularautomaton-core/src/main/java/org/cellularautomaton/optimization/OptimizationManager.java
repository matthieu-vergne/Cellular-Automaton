package org.cellularautomaton.optimization;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.cellularautomaton.optimization.step.OptimizationStep;
import org.cellularautomaton.optimization.type.AutoRemoveOptimization;
import org.cellularautomaton.optimization.type.GenericOptimization;
import org.cellularautomaton.optimization.type.OptimizationType;

/**
 * This manager gives a convenient way to manage optimizations.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <OwnerType>
 *            The type of components the optimization should optimize.
 */
public class OptimizationManager<OwnerType> extends
		AbstractOptimization<OwnerType> implements Optimizable<OwnerType> {
	private final Collection<Optimization<OwnerType>> optimizations = new HashSet<Optimization<OwnerType>>();
	private final Map<Class<? extends OptimizationType<OwnerType>>, OptimizationExecutor<OwnerType>> executors = new HashMap<Class<? extends OptimizationType<OwnerType>>, OptimizationExecutor<OwnerType>>();

	/**
	 * Create a new manager, already able to execute {@link GenericOptimization}
	 * . Of course this behavior can be adapted setting a new executor to this
	 * class.
	 */
	@SuppressWarnings("unchecked")
	public OptimizationManager() {
		setExecutor(
				(Class<? extends OptimizationType<OwnerType>>) GenericOptimization.class,
				new OptimizationExecutor<OwnerType>() {

					@Override
					public void execute(Optimization<OwnerType> optimization) {
						((GenericOptimization<OwnerType>) optimization)
								.execute();
					}
				});
	}

	@Override
	public void add(Optimization<OwnerType> optimization) {
		if (getOwner() == null) {
			throw new IllegalStateException(
					"No owner has been given for this manager.");
		} else if (!(optimization instanceof OptimizationStep)) {
			throw new IllegalArgumentException(
					"No step has been given for the optimization "
							+ optimization);
		} else if (!(optimization instanceof OptimizationType)) {
			throw new IllegalArgumentException(
					"No type has been given for the optimization "
							+ optimization);
		} else if (optimization.getOwner() != null) {
			throw new IllegalArgumentException(
					"Another owner use the optimization " + optimization);
		} else {
			optimization.setOwner(getOwner());
			optimizations.add(optimization);
		}
	}

	@Override
	public void remove(Optimization<OwnerType> optimization) {
		if (optimizations.remove(optimization)) {
			optimization.setOwner(null);
		}
	}

	@Override
	public boolean contains(Optimization<OwnerType> optimization) {
		clean();
		return optimizations.contains(optimization);
	}

	/**
	 * Giving an executor to a specific class of optimizations is a way to tell
	 * how to execute such optimizations. As the execution of a specific type of
	 * optimization is dependent of the owner of the optimization, it is logical
	 * to let this owner decide how to execute it, especially for the coupling
	 * with internal fields.
	 * 
	 * @param type
	 *            the class identifying the type of optimization
	 * @param executor
	 *            the executor describing how to execute this type of
	 *            optimization
	 */
	public void setExecutor(Class<? extends OptimizationType<OwnerType>> type,
			OptimizationExecutor<OwnerType> executor) {
		executors.put(type, executor);
	}

	/**
	 * Apply the optimizations of a specific step.
	 * 
	 * @param step
	 *            the current step
	 */
	public void execute(Class<? extends OptimizationStep<OwnerType>> step) {
		clean();
		for (Optimization<OwnerType> optimization : getOptimizationsOf(step)) {
			// look for exact matching
			OptimizationExecutor<OwnerType> executor = executors
					.get(optimization);

			// look for global matching
			if (executor == null) {
				HashSet<Class<? extends OptimizationType<OwnerType>>> candidates = new HashSet<Class<? extends OptimizationType<OwnerType>>>();
				for (Class<? extends OptimizationType<OwnerType>> type : executors
						.keySet()) {
					if (type.isInstance(optimization)) {
						candidates.add(type);
					}
				}
				if (candidates.isEmpty()) {
					throw new IllegalStateException(
							"No executor has been defined for the optimization "
									+ optimization);
				} else if (candidates.size() > 1) {
					String classes = "";
					for (Class<? extends OptimizationType<OwnerType>> candidate : candidates) {
						classes += ", " + candidate.getCanonicalName();
					}
					classes = classes.substring(2);
					throw new IllegalStateException(
							"No executor has been defined for the optimizations "
									+ optimization.getClass()
											.getCanonicalName()
									+ " and the parent classes are in conflict : "
									+ classes);
				} else {
					executor = executors.get(candidates.iterator().next());
				}
			}

			// execution
			if (executor != null) {
				executor.execute(optimization);
			}
		}
	}

	/**
	 * Remove all the {@link AutoRemoveOptimization} which ask to be removed.
	 */
	private void clean() {
		Iterator<Optimization<OwnerType>> iterator = optimizations.iterator();
		while (iterator.hasNext()) {
			Optimization<OwnerType> optimization = iterator.next();
			if (optimization instanceof AutoRemoveOptimization
					&& ((AutoRemoveOptimization<OwnerType>) optimization)
							.removeNow()) {
				remove(optimization);
			}
		}
	}

	/**
	 * This method is a simple getter for the optimizations, except that you can
	 * give a filter to get only specific optimizations. You can also give null
	 * to get all the optimizations.
	 * 
	 * @param filter
	 *            the class of the wanted optimizations (generally a child of
	 *            {@link OptimizationStep} or {@link OptimizationType})
	 * @return the optimizations corresponding to the given class
	 */
	private Collection<Optimization<OwnerType>> getOptimizationsOf(
			Class<? extends Optimization<OwnerType>> filter) {
		Collection<Optimization<OwnerType>> optimizations = new HashSet<Optimization<OwnerType>>();
		for (Optimization<OwnerType> optimization : this.optimizations) {
			if (filter == null || filter.isInstance(optimization)) {
				optimizations.add(optimization);
			}
		}
		return optimizations;
	}

	/**
	 * An executor is a way to specify how to execute an optimization. Each
	 * {@link Optimizable} element can have its own optimizations, or some
	 * optimizations can be shared but used in different ways. This class has to
	 * be used by owners of optimizations to tell how to use them.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <OwnerType>
	 *            The type of components the optimization should optimize.
	 */
	public static interface OptimizationExecutor<OwnerType> {
		/**
		 * This method must implement the way to execute the optimization,
		 * especially giving the good inputs and using the output (if there is).
		 * 
		 * @param optimization
		 *            the optimization to execute
		 */
		public void execute(Optimization<OwnerType> optimization);

	}

}
