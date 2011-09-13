package org.cellularautomaton.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A switcher is a convenient way to manage similar objects in a given order. It
 * can be compared to the iterator, excepted that when the iterator gives the
 * last element it stops, while the switcher restart at the beginning.
 * Considering this point, the switcher can be particularly useful when you want
 * to manage <u>several times</u> the same components (while the iterator is
 * adapted for <u>one time</u>).
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Component>
 *            The type of components to wrap in the switcher.
 */
public class Switcher<Component> {

	/**
	 * The components to manage.
	 */
	private final List<Component> components = new ArrayList<Component>();
	/**
	 * The index of the actual component.
	 */
	private int index = 0;

	/**
	 * Add a component to this switcher. This component is added at the end of
	 * the list of the switcher, so it will be placed just after the last added
	 * element. There is no unicity check, so you can add several time the same
	 * component.
	 * 
	 * @param component
	 *            the component to manage
	 */
	public void add(Component component) {
		components.add(component);
	}

	/**
	 * Remove the next given component registered to this switcher. If the
	 * component is not registered, nothing happen.
	 * 
	 * @param component
	 *            the component to remove
	 */
	public void remove(Component component) {
		if (components.contains(component)) {
			int lastIndex = index;
			while (!get().equals(component)) {
				switchComponent();
			}
			components.remove(index);
			if (index < lastIndex) {
				lastIndex--;
			}
			index = lastIndex;
			index --;
			switchComponent();
		}
	}

	/**
	 * 
	 * @return true it no component is registered in this switcher, false
	 *         otherwise.
	 */
	public boolean isEmpty() {
		return components.isEmpty();
	}

	/**
	 * 
	 * @return the currently managed component
	 */
	public Component get() {
		return components.get(index);
	}

	/**
	 * Pass to the next component to manage.
	 */
	public void switchComponent() {
		if (components.isEmpty()) {
			index = 0;
		}
		else {
			index++;
			index %= components.size();
		}
	}

	/**
	 * This method is a convenient way to use the switcher like the iterator. It
	 * is equivalent to use {@link #switchComponent()} followed by
	 * {@link #get()}.
	 * 
	 * @return the next component of the switcher
	 */
	public Component next() {
		switchComponent();
		return get();
	}

	/**
	 * 
	 * @return all the components registered to this switcher
	 */
	public List<Component> getAll() {
		return new ArrayList<Component>(components);
	}
}
