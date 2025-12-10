package com.myframework.dev.report;

import com.myframework.dev.core.TestResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HtmlTestReporter implements TestReporter {

    private final List<TestResult> results = new ArrayList<>();
    private final String outputPath;
    private final List<String> runningTests = new ArrayList<>();

    public HtmlTestReporter(String outputPath) {
        this.outputPath = outputPath != null ? outputPath : "test-report.html";
    }

    public HtmlTestReporter() {
        this("test-report.html");
    }

    @Override
    public void testStarted(String testName) {
        runningTests.add(testName);
    }

    @Override
    public void testFinished(TestResult result) {
        results.add(result);
    }

    @Override
    public void testRunFinished(int total, int passed, int failed, int skipped) {
        generateHtmlReport(total, passed, failed, skipped);
    }

    private void generateHtmlReport(int total, int passed, int failed, int skipped) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang=\"en\">");
            writer.println("<head>");
            writer.println("    <meta charset=\"UTF-8\">");
            writer.println("    <title>Test Report</title>");
            writer.println("    <style>");
            writer.println("        body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("        h1 { color: #333; }");
            writer.println("        h2 { color: #555; margin-top: 30px; }");
            writer.println("        table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
            writer.println("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("        th { background-color: #f2f2f2; font-weight: bold; }");
            writer.println("        tr:nth-child(even) { background-color: #f9f9f9; }");
            writer.println("        .pass { color: green; font-weight: bold; }");
            writer.println("        .fail { color: red; font-weight: bold; }");
            writer.println("        .skip { color: orange; font-weight: bold; }");
            writer.println("        .error { background-color: #ffe6e6; font-family: monospace; font-size: 12px; white-space: pre-wrap; }");
            writer.println("        .summary { margin: 20px 0; }");
            writer.println("        .timestamp { color: #666; font-size: 14px; margin-bottom: 20px; }");
            writer.println("    </style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("    <h1>Test Report</h1>");
            writer.println("    <div class=\"timestamp\">Generated: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</div>");
            
            // Summary Table
            writer.println("    <div class=\"summary\">");
            writer.println("        <h2>Summary</h2>");
            writer.println("        <table>");
            writer.println("            <tr>");
            writer.println("                <th>Total Tests</th>");
            writer.println("                <th>Passed</th>");
            writer.println("                <th>Failed</th>");
            writer.println("                <th>Skipped</th>");
            writer.println("            </tr>");
            writer.println("            <tr>");
            writer.println("                <td>" + total + "</td>");
            writer.println("                <td class=\"pass\">" + passed + "</td>");
            writer.println("                <td class=\"fail\">" + failed + "</td>");
            writer.println("                <td class=\"skip\">" + skipped + "</td>");
            writer.println("            </tr>");
            writer.println("        </table>");
            writer.println("    </div>");
            
            // Test Results Table
            writer.println("    <h2>Test Results</h2>");
            writer.println("    <table>");
            writer.println("        <tr>");
            writer.println("            <th>Test Name</th>");
            writer.println("            <th>Status</th>");
            writer.println("            <th>Execution Time (ms)</th>");
            writer.println("            <th>Details</th>");
            writer.println("        </tr>");
            
            for (TestResult result : results) {
                String statusClass;
                String statusText;
                String details = "";
                
                if (result.isSkipped()) {
                    statusClass = "skip";
                    statusText = "SKIP";
                    details = result.getSkipReason() != null ? result.getSkipReason() : "";
                } else if (result.isSuccess()) {
                    statusClass = "pass";
                    statusText = "PASS";
                } else {
                    statusClass = "fail";
                    statusText = "FAIL";
                    if (result.getFailureCause() != null) {
                        details = escapeHtml(getStackTrace(result.getFailureCause()));
                    }
                }
                
                writer.println("        <tr>");
                writer.println("            <td>" + escapeHtml(result.getTestName()) + "</td>");
                writer.println("            <td class=\"" + statusClass + "\">" + statusText + "</td>");
                writer.println("            <td>" + result.getExecutionTimeMillis() + "</td>");
                writer.println("            <td class=\"error\">" + details + "</td>");
                writer.println("        </tr>");
            }
            
            writer.println("    </table>");
            writer.println("    <p style=\"margin-top: 20px; color: #666; font-size: 12px;\">Generated by Simple Test Framework</p>");
            writer.println("</body>");
            writer.println("</html>");
            
            System.out.println("HTML report generated: " + outputPath);
        } catch (IOException e) {
            System.err.println("Failed to generate HTML report: " + e.getMessage());
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }

    private String getStackTrace(Throwable throwable) {
        if (throwable == null) return "";
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}

