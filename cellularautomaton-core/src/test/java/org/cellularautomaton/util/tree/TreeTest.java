package org.cellularautomaton.util.tree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TreeTest {

	@Test
	public void testRoot() {
		Tree<Integer> tree = new Tree<Integer>();
		assertEquals(null, tree.getRoot());

		TreeNode<Integer> r1 = new TreeNode<Integer>();
		tree.setRoot(r1);
		assertEquals(r1, tree.getRoot());

		TreeNode<Integer> r2 = new TreeNode<Integer>();
		tree.setRoot(r2);
		assertEquals(r2, tree.getRoot());
	}

	@Test
	public void testIsEmpty() {
		Tree<Integer> tree = new Tree<Integer>();
		assertTrue(tree.isEmpty());

		TreeNode<Integer> root = new TreeNode<Integer>();
		tree.setRoot(root);
		assertFalse(tree.isEmpty());
	}

	@Test
	public void testAllNodes() {
		Tree<Integer> tree = new Tree<Integer>();
		assertTrue(tree.getAllNodes().isEmpty());

		TreeNode<Integer> n0 = new TreeNode<Integer>(0);
		tree.setRoot(n0);
		assertEquals(1, tree.getAllNodes().size());
		assertTrue(tree.getAllNodes().contains(n0));

		TreeNode<Integer> n1 = new TreeNode<Integer>(1);
		n0.addChild(n1);
		assertEquals(2, tree.getAllNodes().size());
		assertTrue(tree.getAllNodes().contains(n0));
		assertTrue(tree.getAllNodes().contains(n1));

		TreeNode<Integer> n2 = new TreeNode<Integer>(1);
		n0.addChild(n2);
		assertEquals(3, tree.getAllNodes().size());
		assertTrue(tree.getAllNodes().contains(n0));
		assertTrue(tree.getAllNodes().contains(n1));
		assertTrue(tree.getAllNodes().contains(n2));

		TreeNode<Integer> n3 = new TreeNode<Integer>(2);
		n1.addChild(n3);
		assertEquals(4, tree.getAllNodes().size());
		assertTrue(tree.getAllNodes().contains(n0));
		assertTrue(tree.getAllNodes().contains(n1));
		assertTrue(tree.getAllNodes().contains(n2));
		assertTrue(tree.getAllNodes().contains(n3));
	}

	@Test
	public void testAllContents() {
		Tree<Integer> tree = new Tree<Integer>();
		List<Integer> contents = new ArrayList<Integer>(tree.getAllContents());
		assertTrue(contents.isEmpty());

		TreeNode<Integer> n0 = new TreeNode<Integer>(0);
		tree.setRoot(n0);
		contents = new ArrayList<Integer>(tree.getAllContents());
		assertEquals(1, contents.size());
		assertTrue(contents.contains(0));

		TreeNode<Integer> n1 = new TreeNode<Integer>(1);
		n0.addChild(n1);
		contents = new ArrayList<Integer>(tree.getAllContents());
		assertEquals(2, contents.size());
		assertTrue(contents.contains(0));
		assertTrue(contents.contains(1));

		TreeNode<Integer> n2 = new TreeNode<Integer>(1);
		n0.addChild(n2);
		contents = new ArrayList<Integer>(tree.getAllContents());
		Collections.sort(contents);
		assertEquals(3, contents.size());
		assertEquals(0, (int) contents.get(0));
		assertEquals(1, (int) contents.get(1));
		assertEquals(1, (int) contents.get(2));

		TreeNode<Integer> n3 = new TreeNode<Integer>(2);
		n1.addChild(n3);
		contents = new ArrayList<Integer>(tree.getAllContents());
		Collections.sort(contents);
		assertEquals(4, contents.size());
		assertEquals(0, (int) contents.get(0));
		assertEquals(1, (int) contents.get(1));
		assertEquals(1, (int) contents.get(2));
		assertEquals(2, (int) contents.get(3));
	}

}
