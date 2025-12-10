package com.myframework.dev.core;

/**
 * Utility class for test assertions.
 * Provides methods to verify test conditions.
 */
public final class Assert {

    private Assert() {
        // utility class
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but was false");
        }
    }

    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but was true");
        }
    }

    public static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError("Expected <" + expected + "> but was <" + actual + ">");
    }

    public static void assertNotEquals(Object unexpected, Object actual) {
        if (unexpected == null && actual == null) {
            throw new AssertionError("Expected not equal, but both were null");
        }
        if (unexpected != null && unexpected.equals(actual)) {
            throw new AssertionError("Expected not equal to <" + unexpected + "> but was equal");
        }
    }

    public static void assertNull(Object object) {
        if (object != null) {
            throw new AssertionError("Expected null but was <" + object + ">");
        }
    }

    public static void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Expected not null but was null");
        }
    }

    /**
     * Asserts that the given code throws an exception of the expected type.
     * Returns the thrown exception if the assertion passes.
     */
    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
        try {
            executable.execute();
        } catch (Throwable actualException) {
            if (expectedType.isInstance(actualException)) {
                return expectedType.cast(actualException);
            }
            throw new AssertionError(
                "Expected exception: " + expectedType.getName() + 
                " but was: " + actualException.getClass().getName(),
                actualException
            );
        }
        throw new AssertionError("Expected " + expectedType.getName() + " to be thrown, but nothing was thrown");
    }

    /**
     * Functional interface for code that may throw an exception.
     */
    @FunctionalInterface
    public interface Executable {
        void execute() throws Throwable;
    }
}
