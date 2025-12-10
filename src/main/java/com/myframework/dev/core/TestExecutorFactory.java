package com.myframework.dev.core;

/**
 * Factory for creating TestExecutor instances.
 * Uses Factory pattern to encapsulate executor creation logic.
 */
public class TestExecutorFactory {

    /**
     * Creates a default test executor.
     * Currently returns ReflectionTestExecutor.
     */
    public static TestExecutor createDefault() {
        return new ReflectionTestExecutor();
    }

    /**
     * Creates a reflection-based test executor.
     */
    public static TestExecutor createReflectionExecutor() {
        return new ReflectionTestExecutor();
    }
}

