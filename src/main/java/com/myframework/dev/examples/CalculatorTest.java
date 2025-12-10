package com.myframework.dev.examples;


import com.myframework.dev.annotations.BeforeEach;
import com.myframework.dev.annotations.AfterEach;
import com.myframework.dev.annotations.Test;
import com.myframework.dev.core.Assert;

public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
        System.out.println("  setUp()");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("  tearDown()");
    }

    @Test
    public void testAdd() {
        int result = calculator.add(2, 3);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testSub() {
        int result = calculator.sub(10, 4);
        Assert.assertEquals(6, result);
    }

    @Test
    public void failingExample() {
        int result = calculator.add(1, 1);
        // Intentionally wrong to see a FAIL
        Assert.assertEquals(3, result);
    }
}
