package com.myframework.dev.core;

/**
 * Factory for creating TestScanner instances.
 * Uses Factory pattern to encapsulate scanner creation logic.
 */
public class TestScannerFactory {

    /**
     * Creates a default test scanner.
     */
    public static TestScanner createDefault() {
        return new TestScanner();
    }
}

