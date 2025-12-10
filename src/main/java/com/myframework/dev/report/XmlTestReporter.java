package com.myframework.dev.report;

import com.myframework.dev.core.TestResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JUnit-style XML reporter for CI/CD integration.
 * Generates XML reports compatible with Jenkins, GitLab CI, etc.
 * Uses DOM API for proper XML generation.
 */
public class XmlTestReporter implements TestReporter {

    private final List<TestResult> results = new ArrayList<>();
    private final String outputPath;
    private long startTime;

    public XmlTestReporter(String outputPath) {
        this.outputPath = outputPath != null ? outputPath : "test-report.xml";
    }

    public XmlTestReporter() {
        this("test-report.xml");
    }

    @Override
    public void testStarted(String testName) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void testFinished(TestResult result) {
        results.add(result);
    }

    @Override
    public void testRunFinished(int total, int passed, int failed, int skipped) {
        generateXmlReport(total, passed, failed, skipped);
    }

    private void generateXmlReport(int total, int passed, int failed, int skipped) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Create root element
            Element testsuites = doc.createElement("testsuites");
            testsuites.setAttribute("name", "Test Results");
            testsuites.setAttribute("tests", String.valueOf(total));
            testsuites.setAttribute("failures", String.valueOf(failed));
            testsuites.setAttribute("skipped", String.valueOf(skipped));
            
            long totalTime = System.currentTimeMillis() - startTime;
            double totalTimeSeconds = totalTime / 1000.0;
            testsuites.setAttribute("time", String.valueOf(totalTimeSeconds));
            
            doc.appendChild(testsuites);

            // Group tests by class
            Map<String, List<TestResult>> testsByClass = new HashMap<>();
            for (TestResult result : results) {
                String[] parts = result.getTestName().split("\\.");
                String className = parts.length > 1 ? parts[0] : "TestClass";
                testsByClass.computeIfAbsent(className, k -> new ArrayList<>()).add(result);
            }

            // Create testsuite elements for each class
            for (Map.Entry<String, List<TestResult>> entry : testsByClass.entrySet()) {
                String className = entry.getKey();
                List<TestResult> classResults = entry.getValue();
                
                int classTests = classResults.size();
                int classFailures = 0;
                int classSkipped = 0;
                double classTime = 0;

                Element testsuite = doc.createElement("testsuite");
                testsuite.setAttribute("name", className);
                testsuite.setAttribute("tests", String.valueOf(classTests));
                testsuite.setAttribute("timestamp", 
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

                // Process each test in the class
                for (TestResult result : classResults) {
                    String[] parts = result.getTestName().split("\\.");
                    String methodName = parts.length > 1 ? parts[1] : parts[0];
                    
                    double testTimeSeconds = result.getExecutionTimeMillis() / 1000.0;
                    classTime += testTimeSeconds;

                    Element testcase = doc.createElement("testcase");
                    testcase.setAttribute("name", methodName);
                    testcase.setAttribute("classname", className);
                    testcase.setAttribute("time", String.valueOf(testTimeSeconds));

                    if (result.isSkipped()) {
                        classSkipped++;
                        Element skippedElement = doc.createElement("skipped");
                        if (result.getSkipReason() != null && !result.getSkipReason().isEmpty()) {
                            skippedElement.setAttribute("message", result.getSkipReason());
                        }
                        testcase.appendChild(skippedElement);
                    } else if (!result.isSuccess()) {
                        classFailures++;
                        Element failure = doc.createElement("failure");
                        if (result.getFailureCause() != null) {
                            String message = result.getFailureCause().getMessage();
                            if (message != null) {
                                failure.setAttribute("message", message);
                            }
                            failure.setAttribute("type", result.getFailureCause().getClass().getName());
                            failure.setTextContent(getStackTrace(result.getFailureCause()));
                        }
                        testcase.appendChild(failure);
                    }

                    testsuite.appendChild(testcase);
                }

                testsuite.setAttribute("failures", String.valueOf(classFailures));
                testsuite.setAttribute("skipped", String.valueOf(classSkipped));
                testsuite.setAttribute("time", String.valueOf(classTime));
                
                testsuites.appendChild(testsuite);
            }

            // Write XML to file
            writeXmlDocument(doc, outputPath);
            System.out.println("XML report generated: " + outputPath);

        } catch (ParserConfigurationException e) {
            System.err.println("Failed to create XML document: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to generate XML report: " + e.getMessage());
        }
    }

    private void writeXmlDocument(Document doc, String filePath) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    private String getStackTrace(Throwable throwable) {
        if (throwable == null) return "";
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}

