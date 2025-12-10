package com.myframework.dev.core;

/**
 * Represents the result of a test execution.
 */
public class TestResult {

    /**
     * Test execution status.
     */
    public enum Status {
        PASSED, FAILED, SKIPPED
    }

    private final String testName;
    private final Status status;
    private final Throwable failureCause;
    private final long executionTimeMillis;
    private final String skipReason;

    public TestResult(String testName, Status status, Throwable failureCause, long executionTimeMillis, String skipReason) {
        this.testName = testName;
        this.status = status;
        this.failureCause = failureCause;
        this.executionTimeMillis = executionTimeMillis;
        this.skipReason = skipReason;
    }

    // Convenience constructors for backward compatibility
    public TestResult(String testName, boolean success, Throwable failureCause, long executionTimeMillis) {
        this(testName, success ? Status.PASSED : Status.FAILED, failureCause, executionTimeMillis, null);
    }

    public static TestResult skipped(String testName, String reason) {
        return new TestResult(testName, Status.SKIPPED, null, 0, reason);
    }

    public static TestResult passed(String testName, long executionTimeMillis) {
        return new TestResult(testName, Status.PASSED, null, executionTimeMillis, null);
    }

    public static TestResult failed(String testName, Throwable cause, long executionTimeMillis) {
        return new TestResult(testName, Status.FAILED, cause, executionTimeMillis, null);
    }

    public String getTestName() {
        return testName;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return status == Status.PASSED;
    }

    public boolean isSkipped() {
        return status == Status.SKIPPED;
    }

    public Throwable getFailureCause() {
        return failureCause;
    }

    public long getExecutionTimeMillis() {
        return executionTimeMillis;
    }

    public String getSkipReason() {
        return skipReason;
    }
}
