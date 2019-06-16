package com.deriv.util;

import com.deriv.expression.Expression;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.*;

class ThreadManagerTest {
    @Test
    void submitStringTest() {
        Future<String> futureString = ThreadManager.submitTask(() -> "hello");

        String retrievedString;
        try {
            retrievedString = futureString.get();
        } catch (InterruptedException | ExecutionException e) {
            retrievedString = null;
        }

        assertNotNull(retrievedString);
        assertEquals("hello", retrievedString);
    }

    @Test
    void submitExpressionTest() {
        Future<Optional<Expression>> futureOExpression
                = ThreadManager.submitTask(() -> x().differentiate(x()));

        Expression retrievedExpression;
        try {
            retrievedExpression = futureOExpression.get().get(); // get from the future and the optional
        } catch (InterruptedException | ExecutionException e) {
            retrievedExpression = null;
        }

        assertNotNull(retrievedExpression);
        assertEquals(multID(), retrievedExpression);
    }
}
