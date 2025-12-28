# Simple Test Framework 
![Java](https://img.shields.io/badge/Java-17+-blue)
![Maven](https://img.shields.io/badge/Maven-3.6+-brightgreen)
![Build](https://img.shields.io/badge/Build-Passing-success)
![Reports](https://img.shields.io/badge/Reports-HTML%20%7C%20XML-orange)


A lightweight, educational Java test framework simulation inspired by JUnit. This framework demonstrates core testing concepts including annotations, reflection-based test execution, and assertion mechanisms.

## Table of Contents
1. [Quick Start](#quick-start)
2. [Features](#features)
3. [Building the Framework](#building-the-framework)
4. [Using the Framework](#using-the-framework)
5. [Test Reports](#test-reports)
6. [Available Assertions](#available-assertions)
7. [Advanced Features](#advanced-features)
8. [License](#license)
## Quick Start

```bash
# 1. Build the framework
mvn clean package

# 2. Run the example tests
# Linux / macOS
java -cp "target/classes:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html --open \
     com.myframework.dev.examples.CalculatorTest

# Windows
java -cp "target\classes;target\simple-test-framework-1.0-SNAPSHOT.jar" ^
     com.myframework.dev.runner.TestRunner ^
     --html --open ^
     com.myframework.dev.examples.CalculatorTest
```
##  Features


- **@Test** annotation for marking test methods
- **@Test(expected = Exception.class)** for testing expected exceptions
- **@Test(timeout = ms)** for test timeout support
- **@BeforeEach** and **@AfterEach** for setup and teardown
- **@BeforeAll** and **@AfterAll** for one-time setup and cleanup
- **@Disabled** annotation to skip tests
- **Assert** class with comprehensive assertion methods
- **Console-based test reporting** with pass/fail/skip statistics
- **HTML test reports** with simple table design
- **XML test reports** in JUnit format for CI/CD integration
- **Test filtering** by name pattern
- **Reflection-based test discovery** and execution

##  Building the Framework

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Package as JAR

```bash
# Clean and build the project
mvn clean package

# The JAR will be created in: target/simple-test-framework-1.0-SNAPSHOT.jar
```

### Generate JavaDoc Documentation

```bash
# Generate JavaDoc API documentation
mvn javadoc:javadoc

# Or as part of site generation
mvn site
# The documentation will be created in: target/site/apidocs/index.html
# Open this file in your browser to view the API documentation

```

##  Using the Framework

### Step 1: Add the JAR to Your Project

**Option A: Using Maven (Local Repository)**
```bash
# Install to local Maven repository
mvn install

# Then add to your project's pom.xml:
```
```xml
<dependency>
    <groupId>com.myframework</groupId>
    <artifactId>simple-test-framework</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

**Option B: Using JAR Directly**
```bash
# Compile your test classes with the framework JAR in classpath
javac -cp target/simple-test-framework-1.0-SNAPSHOT.jar YourTest.java

# Run tests
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     com.yourpackage.YourTest
```

### Step 2: Write Your Tests

Create a test class using the framework annotations:

```java
package com.yourpackage;

import com.myframework.dev.annotations.*;
import com.myframework.dev.core.Assert;

public class MyTest {
    
    private Calculator calc;
    
    @BeforeEach
    public void setUp() {
        calc = new Calculator();
    }
    
    @AfterEach
    public void tearDown() {
        // Cleanup code
    }
    
    @Test
    public void testAddition() {
        int result = calc.add(2, 3);
        Assert.assertEquals(5, result);
    }
    
    @Test
    public void testSubtraction() {
        int result = calc.sub(10, 4);
        Assert.assertEquals(6, result);
    }
    
    @BeforeAll
    public static void setUpClass() {
        // One-time setup before all tests
    }
    
    @AfterAll
    public static void tearDownClass() {
        // One-time cleanup after all tests
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExpectedException() {
        // This test will pass if IllegalArgumentException is thrown
        throw new IllegalArgumentException("Expected exception");
    }
    
    @Test(timeout = 1000)
    public void testWithTimeout() {
        // Test will fail if it takes longer than 1000ms
    }
    
    @Disabled("Not implemented yet")
    @Test
    public void testSkipped() {
        // This test will be skipped
    }
}
```

### Step 3: Run Your Tests

**Console Output Only:**
```bash
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     com.yourpackage.MyTest
```

**With HTML Report:**
```bash
# Generate HTML report (default: test-report.html)
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html \
     com.yourpackage.MyTest

# Generate HTML report to custom file
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html my-custom-report.html \
     com.yourpackage.MyTest

# Generate HTML report and open in browser automatically
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html --open \
     com.yourpackage.MyTest
```

**With XML Report (for CI/CD):**
```bash
# Generate XML report in JUnit format (default: test-report.xml)
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --xml \
     com.yourpackage.MyTest

# Generate both HTML and XML reports
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html --xml \
     com.yourpackage.MyTest
```

**Filter Tests by Name:**
```bash
# Run only tests matching a pattern
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --filter "testAdd.*" \
     com.yourpackage.MyTest
```

**Run multiple test classes:**
```bash
java -cp ".:target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html \
     com.yourpackage.Test1 \
     com.yourpackage.Test2
```

## Example

The project includes a working example. Run it:

```bash
# Build the project first
mvn clean package

# Run the example test with HTML report
java -cp "target/classes;target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html test-report.html \
     com.myframework.dev.examples.CalculatorTest
```

**Note:** On Windows, use `;` as classpath separator. On Linux/Mac, use `:`.

**Command-Line Options:**
- `--html [file]` - Generate HTML report (default: test-report.html)
- `--xml [file]` - Generate XML report in JUnit format (default: test-report.xml)
- `--open` - Open HTML report in browser after generation
- `--filter <pattern>` - Only run tests matching the pattern (regex)
- `--help, -h` - Show help message

##  Test Reports

### HTML Reports

The framework can generate simple, clean HTML reports with all test information in easy-to-read tables.

**Features:**
- Summary table showing total, passed, failed, and skipped tests
- Detailed results table with test name, status, execution time, and error details
- Use `--open` flag to automatically open report in browser
- Standalone HTML file - no external dependencies

### XML Reports (JUnit Format)

Generate XML reports compatible with CI/CD tools like Jenkins, GitLab CI, GitHub Actions, etc.

**Features:**
- JUnit-compatible XML format
- Includes test results, execution times, and failure details
- Perfect for CI/CD pipeline integration
- Use `--xml` flag to generate XML report

**Example for CI/CD:**
```bash
# Generate XML report for CI/CD
java -cp "target/classes;target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --xml test-results.xml \
     com.yourpackage.MyTest
```



##  Available Assertions

- `Assert.assertEquals(expected, actual)` - Compare two values
- `Assert.assertNotEquals(unexpected, actual)` - Verify two values are not equal
- `Assert.assertTrue(condition)` - Verify condition is true
- `Assert.assertFalse(condition)` - Verify condition is false
- `Assert.assertNull(object)` - Verify object is null
- `Assert.assertNotNull(object)` - Verify object is not null
- `Assert.assertThrows(exceptionClass, executable)` - Verify code throws expected exception

##  Advanced Features

### Testing Expected Exceptions

You can test that methods throw specific exceptions using the `expected` parameter:

```java
@Test(expected = IllegalArgumentException.class)
public void testExpectedException() {
    throw new IllegalArgumentException("Expected");
}
```

### Using assertThrows

Alternatively, use `assertThrows` for more control:

```java
@Test
public void testException() {
    IllegalArgumentException ex = Assert.assertThrows(
        IllegalArgumentException.class,
        () -> {
            throw new IllegalArgumentException("Error");
        }
    );
    Assert.assertEquals("Error", ex.getMessage());
}
```

### Test Timeout

Set a timeout for tests that should complete quickly:

```java
@Test(timeout = 1000)
public void testWithTimeout() {
    // Test will fail if it takes longer than 1000ms
}
```

### Disabling Tests

Skip tests that are not ready or temporarily broken:

```java
@Disabled("Not implemented yet")
@Test
public void testSkipped() {
    // This test will be skipped
}
```

### BeforeAll and AfterAll

Run setup and cleanup once for the entire test class:

```java
@BeforeAll
public static void setUpClass() {
    // Runs once before all tests
}

@AfterAll
public static void tearDownClass() {
    // Runs once after all tests
}
```

**Example Test Classes:**

**Run Exception Tests:**
```bash
# Basic console output
java -cp "target/classes;target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     com.myframework.dev.examples.ExceptionTest

# With HTML report and open in browser
java -cp "target/classes;target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html --open \
     com.myframework.dev.examples.ExceptionTest

# With HTML and XML reports
java -cp "target/classes;target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     --html --xml \
     com.myframework.dev.examples.ExceptionTest
```

**Run Advanced Features Tests:**
```bash
# Run the advanced features example
java -cp "target/classes;target/simple-test-framework-1.0-SNAPSHOT.jar" \
     com.myframework.dev.runner.TestRunner \
     com.myframework.dev.examples.AdvancedTest
```



##  License

This is an educational project. Feel free to use and modify as needed.

---




