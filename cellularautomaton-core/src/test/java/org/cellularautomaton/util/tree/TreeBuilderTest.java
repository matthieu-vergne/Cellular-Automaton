package org.cellularautomaton.util.tree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class TreeBuilderTest {
	Comparator<TreeNode<Integer>> nodeComparator = new Comparator<TreeNode<Integer>>() {
		@Override
		public int compare(TreeNode<Integer> n1, TreeNode<Integer> n2) {
			Integer i1 = n1.getContent();
			Integer i2 = n2.getContent();
			return i1.compareTo(i2);
		}
	};

	@Test
	public void testEmptyTree() {
		TreeBuilder<Integer> builder = new TreeBuilder<Integer>();
		builder.createNewTree();
		Tree<Integer> tree = builder.getTree();
		assertTrue(tree.isEmpty());
	}

	@Test
	public void testRoot() {
		TreeBuilder<Integer> builder = new TreeBuilder<Integer>();
		builder.createNewTree();
		assertNull(builder.getRoot());
		
		Integer root = 0;
		builder.setRoot(root);
		assertEquals(root, builder.getRoot());
	}

	@Test
	public void testTreeBuilding() {
		Integer root = 0;
		Integer r1 = 1;
		Integer r2 = 2;
		Integer r3 = 3;
		Integer r4 = 4;
		Integer r5 = 5;

		TreeBuilder<Integer> builder = new TreeBuilder<Integer>();
		builder.createNewTree();
		builder.setRoot(root);
		builder.addChildren(r1, r2, r3);
		builder.goTo(r2);
		builder.addChildren(r4, r5);
		Tree<Integer> tree = builder.getTree();

		// check the root
		TreeNode<Integer> rootNode = tree.getRoot();
		assertEquals(root, rootNode.getContent());

		// check the first level of children
		assertEquals(3, rootNode.getChildren().size());
		List<TreeNode<Integer>> childrenRoot = new ArrayList<TreeNode<Integer>>(
				rootNode.getChildren());
		Collections.sort(childrenRoot, nodeComparator);
		TreeNode<Integer> n1 = childrenRoot.get(0);
		TreeNode<Integer> n2 = childrenRoot.get(1);
		TreeNode<Integer> n3 = childrenRoot.get(2);
		assertEquals(r1, n1.getContent());
		assertEquals(r2, n2.getContent());
		assertEquals(r3, n3.getContent());

		// check the second level of children
		assertEquals(0, n1.getChildren().size());
		assertEquals(2, n2.getChildren().size());
		assertEquals(0, n3.getChildren().size());
		List<TreeNode<Integer>> childrenN2 = new ArrayList<TreeNode<Integer>>(
				n2.getChildren());
		Collections.sort(childrenN2, nodeComparator);
		TreeNode<Integer> n4 = childrenN2.get(0);
		TreeNode<Integer> n5 = childrenN2.get(1);
		assertEquals(r4, n4.getContent());
		assertEquals(r5, n5.getContent());
	}

	@Test
	public void testGraphExploring() {
		Integer root = 0;
		Integer r1 = 1;
		Integer r2 = 2;
		Integer r3 = 3;
		Integer r4 = 4;
		Integer r5 = 5;

		TreeBuilder<Integer> builder = new TreeBuilder<Integer>();
		builder.createNewTree();
		assertNull(builder.getActualElement());
		builder.setRoot(root);
		assertEquals(root, builder.getActualElement());
		builder.addChildren(r1, r2, r3);
		builder.goTo(r2);
		assertEquals(r2, builder.getActualElement());
		builder.addChildren(r4, r5);

		builder.goTo(root);
		assertEquals(root, builder.getActualElement());
		builder.goTo(r1);
		assertEquals(r1, builder.getActualElement());
		builder.goTo(r2);
		assertEquals(r2, builder.getActualElement());
		builder.goTo(r3);
		assertEquals(r3, builder.getActualElement());
		builder.goTo(r4);
		assertEquals(r4, builder.getActualElement());
		builder.goTo(r5);
		assertEquals(r5, builder.getActualElement());
	}

}
