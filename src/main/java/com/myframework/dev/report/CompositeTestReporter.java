package com.myframework.dev.report;

import com.myframework.dev.core.TestResult;

/**
 * Composite reporter that delegates to multiple reporters.
 * Uses Composite pattern to combine multiple reporting strategies.
 */
public class CompositeTestReporter implements TestReporter {

    private final TestReporter[] reporters;

    public CompositeTestReporter(TestReporter... reporters) {
        this.reporters = reporters;
    }

    @Override
    public void testStarted(String testName) {
        for (TestReporter reporter : reporters) {
            reporter.testStarted(testName);
        }
    }

    @Override
    public void testFinished(TestResult result) {
        for (TestReporter reporter : reporters) {
            reporter.testFinished(result);
        }
    }

    @Override
    public void testRunFinished(int total, int passed, int failed, int skipped) {
        for (TestReporter reporter : reporters) {
            reporter.testRunFinished(total, passed, failed, skipped);
        }
    }
}

