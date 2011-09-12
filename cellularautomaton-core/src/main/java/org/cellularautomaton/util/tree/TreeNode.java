package org.cellularautomaton.util.tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

/**
 * A tree node is a node of a tree, linked to a specific content and having zero
 * or more children. It is especially used in {@link Tree}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 *            The type of the elements contained by the nodes of the tree.
 */
public class TreeNode<T> {
	/**
	 * Comparator used to sort the children for {@link #toString()}. If the
	 * content of the tree nodes are {@link Comparable}, the comparator use this
	 * characteristics, otherwise the string representations of the contents are
	 * used.
	 */
	public final Comparator<TreeNode<T>> CONTENT_COMPARATOR = new Comparator<TreeNode<T>>() {
		@SuppressWarnings("unchecked")
		public int compare(TreeNode<T> n1, TreeNode<T> n2) {
			Object content1 = n1.getContent();
			Object content2 = n2.getContent();
			if (!(content1 instanceof Comparable)) {
				content1 = content1.toString();
				content2 = content2.toString();
			}
			return ((Comparable<Object>) content1)
					.compareTo(((Comparable<Object>) content2));
		};
	};

	/**
	 * The content assigned to this node.
	 */
	private T content;
	/**
	 * The children of this node.
	 */
	private final Collection<TreeNode<T>> children = new HashSet<TreeNode<T>>();

	/**
	 * Create a new node with a null content.
	 */
	public TreeNode() {
		// do nothing
	}

	/**
	 * Create a new node with the given content.
	 * 
	 * @param element
	 *            the content of the node
	 */
	public TreeNode(T element) {
		setContent(element);
	}

	/**
	 * 
	 * @return the content assigned to this node
	 */
	public T getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 *            the content assigned to this node
	 */
	public void setContent(T content) {
		this.content = content;
	}

	/**
	 * Tell if the node is a leaf, meaning it as no children.
	 * 
	 * @return true if the node is a leaf, false otherwise
	 */
	public Boolean isLeaf() {
		return children.isEmpty();
	}

	/**
	 * 
	 * @return the children of this node
	 */
	public Collection<TreeNode<T>> getChildren() {
		return new HashSet<TreeNode<T>>(children);
	}

	/**
	 * Add a child to this node. A check is done to ensure the tree stay a tree,
	 * meaning an acyclic graph.
	 * 
	 * @param child
	 *            the child to add to this node
	 * @throws IllegalArgumentException
	 *             if adding this child create a cycle
	 */
	public void addChild(TreeNode<T> child) {
		if (child.getAllChildren().contains(this)) {
			throw new IllegalArgumentException(
					"The current node is already in the given tree, you cannot add this node (a tree is an acyclic graph).");
		}
		children.add(child);
	}

	/**
	 * Remove a child. If the given node is not a child, nothing happen.
	 * 
	 * @param child
	 *            the child to remove
	 */
	public void removeChild(TreeNode<T> child) {
		children.remove(child);
	}

	/**
	 * This method allows to get all the child, grand-child, ... of this node.
	 * Concretely, considering this node is the root of a tree, this method
	 * return all the nodes of the tree excepted the root.
	 * 
	 * @return all the children of this node in a recursive way
	 */
	public Collection<TreeNode<T>> getAllChildren() {
		Collection<TreeNode<T>> children = new HashSet<TreeNode<T>>();
		for (TreeNode<T> child : this.children) {
			children.add(child);
			children.addAll(child.getAllChildren());
		}
		return children;
	}

	@Override
	public String toString() {
		String string = getContent().toString();
		if (!isLeaf()) {
			@SuppressWarnings("unchecked")
			TreeNode<T>[] array = children
					.toArray(new TreeNode[children.size()]);
			Arrays.sort(array, CONTENT_COMPARATOR);
			string += Arrays.deepToString(array);
		}
		return string;
	}
}
