package com.deriv.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeTest {
  @Test
  void makeTest() {
    Tree<Integer> newTree = Tree.makeNode(1);
    newTree.add(Tree.makeNode(2));
    newTree.add(Tree.makeNode(17));
    newTree.add(Tree.makeNode(5));

    Tree<Integer> tree2 = Tree.of(3);
    assertEquals(new Integer(3), tree2.getValue());

    System.out.println(newTree); // toString
    System.out.println(newTree.toJSON()); // toJSON
  }

  @Test
  void childTest() {
    Tree<Integer> newTree = Tree.makeNode(1);
    assertFalse(newTree.hasChildren());
    assertTrue(newTree.getChildren().isEmpty());

    newTree.add(Tree.makeNode(2));
    assertTrue(newTree.hasChildren());
    assertFalse(newTree.getChildren().isEmpty());
  }

  @Test
  void valueTest() {
    Tree<Integer> newTree = Tree.makeNode(1);
    assertEquals(new Integer(1), newTree.getValue());
  }
}
