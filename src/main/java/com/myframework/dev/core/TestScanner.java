package com.myframework.dev.core;

import com.myframework.dev.annotations.*;
import com.myframework.dev.annotations.Test.None;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans a test class and discovers all test methods and lifecycle methods.
 */
public class TestScanner {

    public List<TestCase> scan(Class<?> testClass) {
        List<Method> beforeEach = new ArrayList<>();
        List<Method> afterEach = new ArrayList<>();
        List<Method> beforeAll = new ArrayList<>();
        List<Method> afterAll = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        // Check if class is disabled
        boolean classDisabled = testClass.isAnnotationPresent(Disabled.class);
        String classDisabledReason = "";
        if (classDisabled) {
            Disabled classDisabledAnnotation = testClass.getAnnotation(Disabled.class);
            classDisabledReason = classDisabledAnnotation.value();
        }

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforeEach.add(method);
            }
            if (method.isAnnotationPresent(AfterEach.class)) {
                afterEach.add(method);
            }
            if (method.isAnnotationPresent(BeforeAll.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    beforeAll.add(method);
                } else {
                    System.err.println("Warning: @BeforeAll method " + method.getName() + " must be static. Ignoring.");
                }
            }
            if (method.isAnnotationPresent(AfterAll.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    afterAll.add(method);
                } else {
                    System.err.println("Warning: @AfterAll method " + method.getName() + " must be static. Ignoring.");
                }
            }
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

        List<TestCase> testCases = new ArrayList<>();
        for (Method testMethod : testMethods) {
            Test testAnnotation = testMethod.getAnnotation(Test.class);
            Class<? extends Throwable> expectedException = null;
            long timeout = 0;
            
            if (testAnnotation != null) {
                Class<? extends Throwable> expected = testAnnotation.expected();
                if (expected != null && !expected.equals(None.class)) {
                    expectedException = expected;
                }
                timeout = testAnnotation.timeout();
            }
            
            // Check if method is disabled
            boolean methodDisabled = testMethod.isAnnotationPresent(Disabled.class) || classDisabled;
            String disabledReason = "";
            if (testMethod.isAnnotationPresent(Disabled.class)) {
                disabledReason = testMethod.getAnnotation(Disabled.class).value();
            } else if (classDisabled) {
                disabledReason = classDisabledReason;
            }
            
            testCases.add(new TestCaseBuilder()
                .testClass(testClass)
                .testMethod(testMethod)
                .beforeEachMethods(beforeEach)
                .afterEachMethods(afterEach)
                .beforeAllMethods(beforeAll)
                .afterAllMethods(afterAll)
                .expectedException(expectedException)
                .timeout(timeout)
                .disabled(methodDisabled)
                .disabledReason(disabledReason)
                .build());
        }
        return testCases;
    }
}
