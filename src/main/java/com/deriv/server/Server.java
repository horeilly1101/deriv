package com.deriv.server;

import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    get("/", (req, res) -> "Hello World");
  }
}