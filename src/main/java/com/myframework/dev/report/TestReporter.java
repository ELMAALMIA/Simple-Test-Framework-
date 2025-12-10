package com.myframework.dev.report;

import com.myframework.dev.core.TestResult;

public interface TestReporter {

    void testStarted(String testName);

    void testFinished(TestResult result);

    void testRunFinished(int total, int passed, int failed, int skipped);

    // For backward compatibility
    default void testRunFinished(int total, int passed, int failed) {
        testRunFinished(total, passed, failed, 0);
    }
}
