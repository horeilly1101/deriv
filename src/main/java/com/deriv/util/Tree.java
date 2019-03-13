package com.deriv.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Tree data structure.
 *
 * NOTE: This is not a binary search tree, or any other kind of search tree. This is a tree
 * taken from the context of discrete math (i.e. a connected, acyclic graph).
 *
 * @param <T> type parameter
 */
public class Tree<T> {
  /**
   * The value stored within a tree node.
   */
  private T value;

  /**
   * List of subtrees.
   */
  private List<Tree<T>> children;

  /**
   * Empty constructor for a Tree.
   * @param value of the tree node.
   */
  private Tree(T value) {
    this.value = value;
    this.children = new ArrayList<>();
  }

  /**
   * Constructor for a Tree that takes variable arguments.
   *
   * @param children subtrees
   */
  private Tree(Tree<T>[] children) {
    this.children = Arrays.asList(children);
  }

  @Override
  public String toString() {
    return "{"
             + value.toString() + " "
             + this.children.stream()
               .map(Objects::toString).reduce("", (a, b) -> a + b)
             + "}";
  }

  /**
   * Public, static constructor for a tree. Takes a variable number of Tree arguments.
   *
   * @param value of tree node
   * @param subtrees variable number of Trees
   * @param <T> type parameter
   * @return a newly constructed Tree
   */
  public static <T> Tree<T> of(T value, Tree<T>... subtrees) {
    return new Tree<>(subtrees);
  }

  /**
   * Public, static constructor for a tree node.
   *
   * @param value of tree ndoe
   * @param <T> type parameter
   * @return a newly constructed tree with no children
   */
  public static <T> Tree<T> makeNode(T value) {
    return new Tree<>(value);
  }

  @SuppressWarnings("unchecked")
  public Tree<T> add(Tree child) {
    List<Tree<T>> newChildren = children;
    newChildren.add(child);
    return new Tree<>(newChildren.toArray((Tree<T>[]) new Object[0]));
  }
}