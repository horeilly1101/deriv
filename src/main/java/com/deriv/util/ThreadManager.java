package com.deriv.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Thin wrapper around a thread pool. This exists to create one thread pool that the entire application can use
 * to schedule tasks.
 */
public class ThreadManager {
    /**
     * Static, final singleton thread pool to be used throughout the application.
     */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    /**
     * Static method to submit a task to the internal thread pool.
     *
     * @param task task to be submitted
     * @param <T> return type of the given thread
     * @return future of the return of the task
     */
    public static <T> Future<T> submitTask(Callable<T> task) {
        return EXECUTOR_SERVICE.submit(task);
    }
}
