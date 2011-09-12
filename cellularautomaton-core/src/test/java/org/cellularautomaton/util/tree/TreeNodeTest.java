package org.cellularautomaton.util.tree;

import static org.junit.Assert.*;

import java.util.Collection;

import org.cellularautomaton.util.tree.TreeNode;
import org.junit.Test;

public class TreeNodeTest {

	@Test
	public void testValue() {
		TreeNode<Integer> node = new TreeNode<Integer>(1);
		assertEquals(1, (int) node.getContent());

		node.setContent(2);
		assertEquals(2, (int) node.getContent());
	}

	@Test
	public void testChildren() {
		TreeNode<Integer> root = new TreeNode<Integer>(0);
		TreeNode<Integer> node1 = new TreeNode<Integer>(1);
		TreeNode<Integer> node2 = new TreeNode<Integer>(2);
		TreeNode<Integer> node11 = new TreeNode<Integer>(11);
		TreeNode<Integer> node12 = new TreeNode<Integer>(12);
		root.addChild(node1);
		root.addChild(node2);
		node1.addChild(node11);
		node1.addChild(node12);

		Collection<TreeNode<Integer>> childrenRoot = root.getChildren();
		assertEquals(2, childrenRoot.size());
		assertTrue(childrenRoot.contains(node1));
		assertTrue(childrenRoot.contains(node2));

		Collection<TreeNode<Integer>> childrenNode1 = node1.getChildren();
		assertEquals(2, childrenNode1.size());
		assertTrue(childrenNode1.contains(node11));
		assertTrue(childrenNode1.contains(node12));

		assertEquals(0, node2.getChildren().size());

		assertEquals(0, node11.getChildren().size());

		assertEquals(0, node12.getChildren().size());

		root.removeChild(node1);
		childrenRoot = root.getChildren();
		assertEquals(1, childrenRoot.size());
		assertTrue(childrenRoot.contains(node2));
	}

	@Test
	public void testAllChildren() {
		TreeNode<Integer> root = new TreeNode<Integer>(0);
		TreeNode<Integer> node1 = new TreeNode<Integer>(1);
		TreeNode<Integer> node2 = new TreeNode<Integer>(2);
		TreeNode<Integer> node11 = new TreeNode<Integer>(11);
		TreeNode<Integer> node12 = new TreeNode<Integer>(12);
		root.addChild(node1);
		root.addChild(node2);
		node1.addChild(node11);
		node1.addChild(node12);

		Collection<TreeNode<Integer>> childrenRoot = root.getAllChildren();
		assertEquals(4, childrenRoot.size());
		assertTrue(childrenRoot.contains(node1));
		assertTrue(childrenRoot.contains(node2));
		assertTrue(childrenRoot.contains(node11));
		assertTrue(childrenRoot.contains(node12));

		Collection<TreeNode<Integer>> childrenNode1 = node1.getAllChildren();
		assertEquals(2, childrenNode1.size());
		assertTrue(childrenNode1.contains(node11));
		assertTrue(childrenNode1.contains(node12));

		assertEquals(0, node2.getAllChildren().size());

		assertEquals(0, node11.getAllChildren().size());

		assertEquals(0, node12.getAllChildren().size());
	}

	@Test
	public void testLeaf() {
		TreeNode<Integer> root = new TreeNode<Integer>(0);
		TreeNode<Integer> node1 = new TreeNode<Integer>(1);
		TreeNode<Integer> node2 = new TreeNode<Integer>(2);
		TreeNode<Integer> node11 = new TreeNode<Integer>(11);
		TreeNode<Integer> node12 = new TreeNode<Integer>(12);
		root.addChild(node1);
		root.addChild(node2);
		node1.addChild(node11);
		node1.addChild(node12);

		assertFalse(root.isLeaf());
		assertFalse(node1.isLeaf());
		assertTrue(node2.isLeaf());
		assertTrue(node11.isLeaf());
		assertTrue(node12.isLeaf());
	}

	@Test
	public void testToString() {
		TreeNode<Integer> root = new TreeNode<Integer>(0);
		TreeNode<Integer> node1 = new TreeNode<Integer>(1);
		TreeNode<Integer> node2 = new TreeNode<Integer>(2);
		TreeNode<Integer> node11 = new TreeNode<Integer>(11);
		TreeNode<Integer> node12 = new TreeNode<Integer>(12);
		root.addChild(node1);
		root.addChild(node2);
		node1.addChild(node11);
		node1.addChild(node12);

		assertEquals("0[1[11, 12], 2]", root.toString());
	}
}
