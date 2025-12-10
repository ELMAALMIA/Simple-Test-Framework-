package com.myframework.dev.core;

import com.myframework.dev.report.TestReporter;

import java.util.concurrent.*;

/**
 * Base class for test executors that handles test execution logic.
 */
public abstract class AbstractTestExecutor implements TestExecutor {

    @Override
    public final TestResult execute(TestCase testCase, TestReporter reporter) {
        String testName = testCase.getTestClass().getSimpleName() + "." +
                testCase.getTestMethod().getName();
        reporter.testStarted(testName);

        // Check if test is disabled
        if (testCase.isDisabled()) {
            String reason = testCase.getDisabledReason();
            return TestResult.skipped(testName, reason.isEmpty() ? "Disabled" : reason);
        }

        long timeout = testCase.getTimeout();
        Class<? extends Throwable> expectedException = testCase.getExpectedException();

        if (timeout > 0) {
            return executeWithTimeout(testCase, testName, timeout, expectedException);
        } else {
            return executeNormal(testCase, testName, expectedException);
        }
    }

    private TestResult executeWithTimeout(TestCase testCase, String testName, long timeout, 
                                          Class<? extends Throwable> expectedException) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<TestResult> future = executor.submit(() -> executeNormal(testCase, testName, expectedException));

        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            AssertionError error = new AssertionError("Test timed out after " + timeout + " ms");
            return TestResult.failed(testName, error, timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AssertionError error = new AssertionError("Test was interrupted");
            return TestResult.failed(testName, error, 0);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            return TestResult.failed(testName, cause, 0);
        } finally {
            executor.shutdownNow();
        }
    }

    private TestResult executeNormal(TestCase testCase, String testName, 
                                     Class<? extends Throwable> expectedException) {
        long start = System.currentTimeMillis();

        try {
            Object instance = createTestInstance(testCase);
            runBeforeEach(testCase, instance);
            runTestMethod(testCase, instance);
            runAfterEach(testCase, instance);

            // If we reach here, no exception was thrown
            if (expectedException != null) {
                // Exception was expected but not thrown - test fails
                long end = System.currentTimeMillis();
                AssertionError error = new AssertionError(
                    "Expected exception: " + expectedException.getName() + " but no exception was thrown");
                return TestResult.failed(testName, error, end - start);
            }

            // No exception expected and none thrown - test passes
            long end = System.currentTimeMillis();
            return TestResult.passed(testName, end - start);
        } catch (Throwable t) {
            long end = System.currentTimeMillis();
            Throwable cause = t.getCause() != null ? t.getCause() : t;
            
            if (expectedException != null) {
                // Check if the thrown exception is the expected type or a subclass
                if (expectedException.isInstance(cause)) {
                    // Expected exception was thrown - test passes
                    return TestResult.passed(testName, end - start);
                } else {
                    // Different exception was thrown - test fails
                    AssertionError error = new AssertionError(
                        "Expected exception: " + expectedException.getName() + 
                        " but got: " + cause.getClass().getName() + " - " + cause.getMessage());
                    error.initCause(cause);
                    return TestResult.failed(testName, error, end - start);
                }
            } else {
                // No exception expected but one was thrown - test fails
                return TestResult.failed(testName, cause, end - start);
            }
        }
    }

    protected abstract Object createTestInstance(TestCase testCase) throws Exception;

    protected abstract void runBeforeEach(TestCase testCase, Object instance) throws Exception;

    protected abstract void runTestMethod(TestCase testCase, Object instance) throws Exception;

    protected abstract void runAfterEach(TestCase testCase, Object instance) throws Exception;
}
