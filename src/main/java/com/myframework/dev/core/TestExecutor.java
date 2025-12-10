package com.myframework.dev.core;

import com.myframework.dev.report.TestReporter;

/**
 * Interface for executing test cases.
 */
public interface TestExecutor {

    /**
     * Executes a test case and returns the result.
     */
    TestResult execute(TestCase testCase, TestReporter reporter);
}
