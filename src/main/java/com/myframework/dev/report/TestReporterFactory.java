package com.myframework.dev.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating TestReporter instances.
 * Uses Factory pattern to encapsulate reporter creation logic.
 */
public class TestReporterFactory {

    /**
     * Creates a console reporter.
     */
    public static TestReporter createConsoleReporter() {
        return new ConsoleTestReporter();
    }

    /**
     * Creates an HTML reporter with default file path.
     */
    public static TestReporter createHtmlReporter() {
        return new HtmlTestReporter();
    }

    /**
     * Creates an HTML reporter with custom file path.
     */
    public static TestReporter createHtmlReporter(String filePath) {
        return new HtmlTestReporter(filePath);
    }

    /**
     * Creates an XML reporter with default file path.
     */
    public static TestReporter createXmlReporter() {
        return new XmlTestReporter();
    }

    /**
     * Creates an XML reporter with custom file path.
     */
    public static TestReporter createXmlReporter(String filePath) {
        return new XmlTestReporter(filePath);
    }

    /**
     * Creates a composite reporter from multiple reporters.
     */
    public static TestReporter createCompositeReporter(TestReporter... reporters) {
        return new CompositeTestReporter(reporters);
    }

    /**
     * Creates reporters based on configuration flags.
     * Returns a single reporter or composite reporter as needed.
     */
    public static TestReporter createFromConfig(boolean generateHtml, boolean generateXml,
                                                String htmlPath, String xmlPath) {
        List<TestReporter> reporters = new ArrayList<>();
        reporters.add(createConsoleReporter());

        if (generateHtml) {
            String htmlFile = htmlPath != null ? htmlPath : "test-report.html";
            reporters.add(createHtmlReporter(htmlFile));
        }

        if (generateXml) {
            String xmlFile = xmlPath != null ? xmlPath : "test-report.xml";
            reporters.add(createXmlReporter(xmlFile));
        }

        if (reporters.size() == 1) {
            return reporters.get(0);
        } else {
            return createCompositeReporter(reporters.toArray(new TestReporter[0]));
        }
    }
}

