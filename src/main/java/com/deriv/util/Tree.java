package com.deriv.util;

import org.json.JSONArray;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

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
   * Set of subtrees. This enforces the property that each node can have at
   * most edge to each other node.
   */
  private Set<Tree<T>> children;

  /**
   * Empty constructor for a Tree.
   *
   * @param value of the tree node.
   */
  private Tree(T value) {
    this.value = value;
    this.children = new HashSet<>();
  }

  /**
   * Constructor for a Tree that takes variable arguments.
   *
   * @param children subtrees
   */
  private Tree(T value, Set<Tree<T>> children) {
    this.value = value;
    this.children = children;
  }

  /**
   * Convert the Tree into JSON.
   * @return JSON array
   */
  public JSONArray toJSON() {
    return hasChildren()
             ? new JSONArray().put(value)
                 .put(children.stream().map(Tree::toJSON)
                        .collect(Collectors.toList()))
             : new JSONArray().put(value);
  }

  @Override
  public String toString() {
    return value.toString()
             + " : "
             + this.children.stream().map(x -> x.hasChildren() ? x.toString() : x.getValue())
                 .collect(toSet()).toString();
  }

  /**
   * Public, static constructor for a tree. Takes a variable number of Tree arguments.
   *
   * @param value of tree node
   * @param subtrees variable number of Trees
   * @param <T> type parameter
   * @return a newly constructed Tree
   */
  @SafeVarargs
  public static <T> Tree<T> of(T value, Tree<T>... subtrees) {
    return new Tree<>(value, new HashSet<>(Arrays.asList(subtrees)));
  }

  /**
   * Public, static constructor for a tree node.
   *
   * @param value of tree node
   * @param <T> type parameter
   * @return a newly constructed tree with no children
   */
  public static <T> Tree<T> makeNode(T value) {
    return new Tree<>(value);
  }

  /**
   * Returns a boolean that tells us whether or not a tree has children.
   * @return boolean value
   */
  public boolean hasChildren() {
    return this.children.size() > 0;
  }

  /**
   * Getter method for the value at the root of our tree.
   * @return value
   */
  public T getValue() {
    return value;
  }

  /**
   * Getter method for children of a tree.
   * @return children
   */
  public Set<Tree<T>> getChildren() {
    return children;
  }

  /**
   * Adds a child to the Tree.
   *
   * @param child to be added
   */
  public void add(Tree<T> child) {
    children.add(child);
  }
}