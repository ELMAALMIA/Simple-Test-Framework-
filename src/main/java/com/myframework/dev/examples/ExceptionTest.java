package com.myframework.dev.examples;

import com.myframework.dev.annotations.Test;
import com.myframework.dev.core.Assert;

public class ExceptionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testExpectedException() {
        // This test expects an IllegalArgumentException to be thrown
        throw new IllegalArgumentException("This is expected");
    }

    @Test(expected = ArithmeticException.class)
    public void testDivisionByZero() {
        // This test expects an ArithmeticException
        int result = 10 / 0; // This will throw ArithmeticException
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        // This test expects a NullPointerException
        String str = null;
        int length = str.length(); // This will throw NullPointerException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongExceptionType() {
        // This test expects IllegalArgumentException but throws NullPointerException
        // This test should FAIL
        throw new NullPointerException("Wrong exception type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoExceptionThrown() {
        // This test expects an exception but doesn't throw one
        // This test should FAIL
        int sum = 2 + 2;
        Assert.assertEquals(4, sum);
    }

    @Test
    public void testNormalTestWithoutException() {
        // Normal test without expected exception
        int result = 5 + 3;
        Assert.assertEquals(8, result);
    }
}

