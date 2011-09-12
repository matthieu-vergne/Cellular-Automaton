package org.cellularautomaton.util.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * This builder allows to create a tree of elements without manage with
 * {@link TreeNode} (they are managed internally).
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 *            The type of the elements contained by the nodes of the tree.
 */
public class TreeBuilder<T> {

	/**
	 * The elements in the tree and the nodes they are linked to.
	 */
	private final Map<T, TreeNode<T>> nodes = new HashMap<T, TreeNode<T>>();
	/**
	 * The root of the tree.
	 */
	private Tree<T> tree = new Tree<T>();
	/**
	 * The node of the tree the builder is working on.
	 */
	private TreeNode<T> actualNode = null;

	/**
	 * Create a new empty tree.
	 */
	public void createNewTree() {
		tree = new Tree<T>();
		nodes.clear();
	}

	/**
	 * 
	 * @return the actual builded tree
	 */
	public Tree<T> getTree() {
		return tree;
	}

	/**
	 * Set the root of the new tree. If the root is already set, an exception
	 * will occur.
	 * 
	 * @param root
	 *            the root of the tree
	 * @throws IllegalStateException
	 *             if the root is already set
	 */
	public void setRoot(T root) {
		if (!tree.isEmpty()) {
			throw new IllegalStateException("The tree already has a root : "
					+ tree.getRoot().getContent());
		}
		tree.setRoot(getNodeFor(root, true));
		goTo(root);
	}

	/**
	 * 
	 * @return the root of the tree, null if there is not
	 */
	public T getRoot() {
		return tree.isEmpty() ? null : tree.getRoot().getContent();
	}

	/**
	 * Give the node linked to a given element. If the node does not exists, it
	 * is possible to create it automatically or to throw an exception.
	 * 
	 * @param element
	 *            the element to consider
	 * @param createIfNeeded
	 *            tell if the node as to be created if it does not exists yet
	 * @return the node linked to the given element
	 * @throws IllegalStateException
	 *             if there is no node for the given element
	 */
	private TreeNode<T> getNodeFor(T element, Boolean createIfNeeded) {
		if (!nodes.containsKey(element)) {
			if (createIfNeeded) {
				nodes.put(element, new TreeNode<T>(element));
			} else {
				throw new IllegalStateException("The node for the element "
						+ element + " does not exists.");
			}
		}
		return nodes.get(element);
	}

	/**
	 * Tell which node will be modified next.
	 * 
	 * @param element
	 *            the element to consider
	 */
	public void goTo(T element) {
		actualNode = getNodeFor(element, false);
	}

	/**
	 * 
	 * @return the actual element which is considered, null if there is not.
	 * @see #goTo(Object)
	 */
	public T getActualElement() {
		return actualNode == null ? null : actualNode.getContent();
	}

	/**
	 * Add children to the considered element.
	 * 
	 * @param children
	 *            the elements to add as children of the actual one
	 * @see #goTo(Object)
	 */
	public void addChildren(T... children) {
		if (actualNode == null) {
			String message;
			if (tree.isEmpty()) {
				message = "No root set, set the root before to add children.";
			} else {
				message = "No node selected, go to a node before to add children.";
			}
			throw new IllegalStateException(message);
		}
		for (T element : children) {
			actualNode.addChild(getNodeFor(element, true));
		}
	}

}
