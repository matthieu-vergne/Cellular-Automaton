package org.cellularautomaton.util;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * A set stack allows to manage a stack (First-In-First-Out queue), considering
 * an element cannot appear several times : if the element already is in the
 * stack, it is not added a second time, but if it is popped out the stack, it
 * can be pushed in another time.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 * @param <E>
 */
public class SetStack<E> {

	/**
	 * The set of elements stored in this stack.
	 */
	private final LinkedHashSet<E> set = new LinkedHashSet<E>();

	/**
	 * Create an empty stack.
	 */
	public SetStack() {
	}

	/**
	 * 
	 * @param element
	 *            the element to push at the end of the stack
	 */
	public void push(E element) {
		assert element != null;
		synchronized (set) {
			set.add(element);
		}
	}

	/**
	 * 
	 * @return the element at the beginning of the stack, this element is
	 *         removed of the stack
	 */
	public E pop() {
		synchronized (set) {
			if (set.isEmpty()) {
				throw new IllegalStateException(
						"no element can be popped out, the stack is empty");
			}
			E element = set.iterator().next();
			set.remove(element);
			return element;
		}
	}

	/**
	 * 
	 * @return true if this stack is empty, false otherwise
	 */
	public boolean isEmpty() {
		synchronized (set) {
			return set.isEmpty();
		}
	}

	/**
	 * 
	 * @return the number of elements this stack has
	 */
	public int getSize() {
		synchronized (set) {
			return set.size();
		}
	}

	/**
	 * 
	 * @param index
	 *            the index of the asked element
	 * @return the asked element in the stack, this element is not removed from
	 *         the stack
	 */
	public E get(int index) {
		assert index >= 0 && index < getSize();

		synchronized (set) {
			Iterator<E> iterator = set.iterator();
			while (index > 0) {
				iterator.next();
				index--;
			}
			return iterator.next();
		}
	}

	@Override
	public String toString() {
		String result = "";
		synchronized (set) {
			Iterator<E> iterator = set.iterator();
			while (iterator.hasNext()) {
				result += "," + iterator.next().toString();
			}
		}
		result = "stack{" + result.substring(",".length()) + "}";
		return result;
	}
}
