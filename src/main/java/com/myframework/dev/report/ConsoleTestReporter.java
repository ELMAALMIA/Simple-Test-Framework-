package com.myframework.dev.report;

import com.myframework.dev.core.TestResult;

public class ConsoleTestReporter implements TestReporter {

    @Override
    public void testStarted(String testName) {
        System.out.println("Running: " + testName);
    }

    @Override
    public void testFinished(TestResult result) {
        if (result.isSkipped()) {
            String reason = result.getSkipReason();
            System.out.println("  [SKIP] " + result.getTestName() + 
                (reason != null && !reason.isEmpty() ? " - " + reason : ""));
        } else if (result.isSuccess()) {
            System.out.println("  [PASS] " + result.getTestName()
                    + " (" + result.getExecutionTimeMillis() + " ms)");
        } else {
            System.out.println("  [FAIL] " + result.getTestName()
                    + " (" + result.getExecutionTimeMillis() + " ms)");
            System.out.println("        Reason: " + result.getFailureCause());
        }
    }

    @Override
    public void testRunFinished(int total, int passed, int failed, int skipped) {
        System.out.println("==================================");
        System.out.println("Total:   " + total);
        System.out.println("Passed:  " + passed);
        System.out.println("Failed:  " + failed);
        System.out.println("Skipped: " + skipped);
    }
}
