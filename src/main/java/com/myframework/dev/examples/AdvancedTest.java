package com.myframework.dev.examples;

import com.myframework.dev.annotations.*;
import com.myframework.dev.core.Assert;


public class AdvancedTest {

    private static int setupCount = 0;
    private String testData;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("=== @BeforeAll: Setting up test class ===");
        setupCount++;
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("=== @AfterAll: Cleaning up test class ===");
        System.out.println("Setup was called " + setupCount + " time(s)");
    }

    @BeforeEach
    public void setUp() {
        testData = "test";
        System.out.println("  @BeforeEach: setUp()");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("  @AfterEach: tearDown()");
    }

    @Test
    public void testAssertNotNull() {
        String value = "Hello";
        Assert.assertNotNull(value);
    }

    @Test
    public void testAssertNull() {
        String value = null;
        Assert.assertNull(value);
    }

    @Test
    public void testAssertThrows() {
        // Using assertThrows to verify exception is thrown
        IllegalArgumentException ex = Assert.assertThrows(
            IllegalArgumentException.class,
            () -> {
                throw new IllegalArgumentException("Expected error");
            }
        );
        Assert.assertNotNull(ex);
        Assert.assertEquals("Expected error", ex.getMessage());
    }

    @Test
    public void testAssertNotEquals() {
        Assert.assertNotEquals(1, 2);
        Assert.assertNotEquals("a", "b");
    }

    @Test(timeout = 1000)
    public void testWithTimeout() {
        // This test should pass - it completes within 1000ms
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        Assert.assertTrue(sum > 0);
    }

    @Test(timeout = 100)
    public void testTimeoutFails() {
        // This test should FAIL - it takes longer than 100ms
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignored
        }
    }

    @Disabled("This test is not yet implemented")
    @Test
    public void testSkipped() {
        // This test will be skipped
        Assert.assertTrue(false);
    }

    @Disabled
    @Test
    public void testSkippedNoReason() {
        // This test will be skipped without a reason
        Assert.assertTrue(false);
    }
}

