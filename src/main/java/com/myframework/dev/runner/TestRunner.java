package com.myframework.dev.runner;

import com.myframework.dev.core.*;
import com.myframework.dev.report.*;

import java.awt.Desktop;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Main test runner that discovers and executes test classes.
 */
public class TestRunner {

    private final TestScanner scanner;
    private final TestExecutor executor;
    private final TestReporter reporter;
    private String filterPattern;

    public TestRunner(TestScanner scanner, TestExecutor executor, TestReporter reporter) {
        this.scanner = scanner;
        this.executor = executor;
        this.reporter = reporter;
    }

    public void setFilterPattern(String filterPattern) {
        this.filterPattern = filterPattern;
    }

    public void run(String... testClassNames) {
        int total = 0;
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (String className : testClassNames) {
            try {
                Class<?> clazz = Class.forName(className);
                List<TestCase> testCases = scanner.scan(clazz);

                // Filter tests by pattern if specified
                if (filterPattern != null && !filterPattern.isEmpty()) {
                    Pattern pattern = Pattern.compile(filterPattern, Pattern.CASE_INSENSITIVE);
                    testCases = testCases.stream()
                        .filter(tc -> pattern.matcher(tc.getTestMethod().getName()).find() ||
                                     pattern.matcher(tc.getTestClass().getSimpleName() + "." + tc.getTestMethod().getName()).find())
                        .toList();
                }

                if (testCases.isEmpty()) {
                    continue;
                }

                // Run @BeforeAll methods once per class
                Set<Class<?>> classesProcessed = new HashSet<>();
                for (TestCase testCase : testCases) {
                    if (!classesProcessed.contains(testCase.getTestClass())) {
                        runBeforeAllMethods(testCase);
                        classesProcessed.add(testCase.getTestClass());
                    }
                }

                // Run tests
                for (TestCase testCase : testCases) {
                    total++;
                    TestResult result = executor.execute(testCase, reporter);
                    reporter.testFinished(result);

                    if (result.isSkipped()) {
                        skipped++;
                    } else if (result.isSuccess()) {
                        passed++;
                    } else {
                        failed++;
                    }
                }

                // Run @AfterAll methods once per class
                classesProcessed.clear();
                for (TestCase testCase : testCases) {
                    if (!classesProcessed.contains(testCase.getTestClass())) {
                        runAfterAllMethods(testCase);
                        classesProcessed.add(testCase.getTestClass());
                    }
                }

            } catch (ClassNotFoundException e) {
                System.out.println("Test class not found: " + className);
            }
        }

        reporter.testRunFinished(total, passed, failed, skipped);
    }

    private void runBeforeAllMethods(TestCase testCase) {
        for (Method method : testCase.getBeforeAllMethods()) {
            try {
                method.setAccessible(true);
                method.invoke(null); // static method
            } catch (Exception e) {
                System.err.println("Failed to run @BeforeAll method: " + method.getName() + " - " + e.getMessage());
            }
        }
    }

    private void runAfterAllMethods(TestCase testCase) {
        for (Method method : testCase.getAfterAllMethods()) {
            try {
                method.setAccessible(true);
                method.invoke(null); // static method
            } catch (Exception e) {
                System.err.println("Failed to run @AfterAll method: " + method.getName() + " - " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        TestScanner scanner = TestScannerFactory.createDefault();
        TestExecutor executor = TestExecutorFactory.createDefault();
        
        List<String> testClasses = new ArrayList<>();
        String htmlReportPath = null;
        String xmlReportPath = null;
        String filterPattern = null;
        boolean generateHtml = false;
        boolean generateXml = false;
        boolean openInBrowser = false;
        
        // Parse arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--html":
                    generateHtml = true;
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        htmlReportPath = args[++i];
                    }
                    break;
                case "--xml":
                    generateXml = true;
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        xmlReportPath = args[++i];
                    }
                    break;
                case "--open":
                    openInBrowser = true;
                    break;
                case "--filter":
                    if (i + 1 < args.length) {
                        filterPattern = args[++i];
                    }
                    break;
                case "--help":
                case "-h":
                    printUsage();
                    return;
                default:
                    if (!args[i].startsWith("--")) {
                        testClasses.add(args[i]);
                    }
                    break;
            }
        }
        
        if (testClasses.isEmpty()) {
            System.out.println("Error: No test classes specified");
            return;
        }
        
        // Create reporter(s) using factory
        String finalHtmlPath = htmlReportPath != null ? htmlReportPath : "test-report.html";
        String finalXmlPath = xmlReportPath != null ? xmlReportPath : "test-report.xml";
        TestReporter reporter = TestReporterFactory.createFromConfig(
            generateHtml, generateXml, finalHtmlPath, finalXmlPath
        );

        if (openInBrowser && !generateHtml) {
            System.out.println("Warning: --open requires --html flag. Ignoring --open.");
            openInBrowser = false;
        }

        TestRunner runner = new TestRunner(scanner, executor, reporter);
        if (filterPattern != null) {
            runner.setFilterPattern(filterPattern);
        }
        runner.run(testClasses.toArray(new String[0]));
        
        // Open HTML report in browser if requested
        if (openInBrowser && generateHtml) {
            openReportInBrowser(finalHtmlPath);
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java ... TestRunner [options] <fully.qualified.TestClass> ...");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --html [file]     Generate HTML report (default: test-report.html)");
        System.out.println("  --xml [file]      Generate XML report in JUnit format (default: test-report.xml)");
        System.out.println("  --open            Open HTML report in browser after generation");
        System.out.println("  --filter <regex>  Only run tests matching the pattern");
        System.out.println("  --help, -h        Show this help message");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java ... TestRunner com.example.MyTest");
        System.out.println("  java ... TestRunner --html --xml com.example.MyTest");
        System.out.println("  java ... TestRunner --filter \"testAdd.*\" com.example.MyTest");
    }
    
    private static void openReportInBrowser(String filePath) {
        try {
            File reportFile = new File(filePath);
            if (!reportFile.exists()) {
                System.out.println("Warning: Report file not found: " + filePath);
                return;
            }
            
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(reportFile.toURI());
                    System.out.println("Opened report in browser: " + filePath);
                } else {
                    System.out.println("Browser opening not supported on this system");
                }
            } else {
                System.out.println("Desktop API not supported on this system");
            }
        } catch (Exception e) {
            System.err.println("Failed to open report in browser: " + e.getMessage());
            System.out.println("Please open manually: " + filePath);
        }
    }
}
