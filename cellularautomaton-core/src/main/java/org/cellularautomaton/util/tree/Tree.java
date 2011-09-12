package org.cellularautomaton.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A tree is an acyclic graph. This implementation is a tree of {@link TreeNode}
 * .
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 *            The type of the elements contained by the nodes of the tree.
 */
public class Tree<T> {

	/**
	 * The root of the tree.
	 */
	private TreeNode<T> root = null;

	/**
	 * 
	 * @return the root of the tree
	 */
	public TreeNode<T> getRoot() {
		return root;
	}

	/**
	 * 
	 * @param root
	 *            the node to set as the root of the tree
	 */
	public void setRoot(TreeNode<T> root) {
		this.root = root;
	}

	/**
	 * The tree is empty if no root is set.
	 * 
	 * @return true if the tree is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * 
	 * @return all the nodes of this tree
	 */
	public Collection<TreeNode<T>> getAllNodes() {
		if (isEmpty()) {
			return new HashSet<TreeNode<T>>();
		} else {
			Collection<TreeNode<T>> nodes = root.getAllChildren();
			nodes.add(root);
			return nodes;
		}
	}

	/**
	 * This method is equivalent to {@link #getAllNodes()}, except the contents
	 * only are returned. Especially, if some nodes have the same content, this
	 * content will appear several times in the returned collection.
	 * 
	 * @return all the contents of this tree
	 */
	public Collection<T> getAllContents() {
		Collection<T> contents = new ArrayList<T>();
		Iterator<TreeNode<T>> iterator = getAllNodes().iterator();
		while (iterator.hasNext()) {
			contents.add(iterator.next().getContent());
		}
		return contents;
	}

	@Override
	public String toString() {
		return root != null ? root.toString() : "(empty tree)";
	}
}
