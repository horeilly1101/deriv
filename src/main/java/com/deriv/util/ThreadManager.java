package com.deriv.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Thin wrapper around a thread pool.
 */
public class ThreadManager {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(8);

    public static <T> Future<T> submitTask(Callable<T> computation) {
        return EXECUTOR_SERVICE.submit(computation);
    }
}
