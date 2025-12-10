package com.myframework.dev.core;

import java.lang.reflect.Method;

/**
 * Executes test cases using Java reflection.
 */
public class ReflectionTestExecutor extends AbstractTestExecutor {

    @Override
    protected Object createTestInstance(TestCase testCase) throws Exception {
        return testCase.getTestClass().getDeclaredConstructor().newInstance();
    }

    @Override
    protected void runBeforeEach(TestCase testCase, Object instance) throws Exception {
        for (Method m : testCase.getBeforeEachMethods()) {
            m.setAccessible(true);
            m.invoke(instance);
        }
    }

    @Override
    protected void runTestMethod(TestCase testCase, Object instance) throws Exception {
        Method m = testCase.getTestMethod();
        m.setAccessible(true);
        m.invoke(instance);
    }

    @Override
    protected void runAfterEach(TestCase testCase, Object instance) throws Exception {
        for (Method m : testCase.getAfterEachMethods()) {
            m.setAccessible(true);
            m.invoke(instance);
        }
    }
}
