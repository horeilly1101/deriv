package com.deriv.server;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerTest {
  @Test
  public void jsonTest() {
    List<Integer> lst = Stream.of(1, 2, 3).collect(Collectors.toList());
    JSONArray obj = (new JSONArray());
    System.out.println(obj);
  }
}
