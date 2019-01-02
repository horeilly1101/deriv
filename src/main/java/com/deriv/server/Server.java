package com.deriv.server;

import static spark.Spark.*;

public class Server {
  // runs the server on localhost:4567
  public static void main(String[] args) {
    get("/", (req, res) -> "Hello World");
  }
}