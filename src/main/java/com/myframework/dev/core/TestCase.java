package com.myframework.dev.core;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Represents a single test case with its configuration and lifecycle methods.
 */
public class TestCase {

    private final Class<?> testClass;
    private final Method testMethod;
    private final List<Method> beforeEachMethods;
    private final List<Method> afterEachMethods;
    private final List<Method> beforeAllMethods;
    private final List<Method> afterAllMethods;
    private final Class<? extends Throwable> expectedException;
    private final long timeout;
    private final boolean disabled;
    private final String disabledReason;

    public TestCase(Class<?> testClass,
                    Method testMethod,
                    List<Method> beforeEachMethods,
                    List<Method> afterEachMethods,
                    List<Method> beforeAllMethods,
                    List<Method> afterAllMethods,
                    Class<? extends Throwable> expectedException,
                    long timeout,
                    boolean disabled,
                    String disabledReason) {
        this.testClass = testClass;
        this.testMethod = testMethod;
        this.beforeEachMethods = beforeEachMethods;
        this.afterEachMethods = afterEachMethods;
        this.beforeAllMethods = beforeAllMethods;
        this.afterAllMethods = afterAllMethods;
        this.expectedException = expectedException;
        this.timeout = timeout;
        this.disabled = disabled;
        this.disabledReason = disabledReason;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public Method getTestMethod() {
        return testMethod;
    }

    public List<Method> getBeforeEachMethods() {
        return beforeEachMethods;
    }

    public List<Method> getAfterEachMethods() {
        return afterEachMethods;
    }

    public List<Method> getBeforeAllMethods() {
        return beforeAllMethods;
    }

    public List<Method> getAfterAllMethods() {
        return afterAllMethods;
    }

    public Class<? extends Throwable> getExpectedException() {
        return expectedException;
    }

    public long getTimeout() {
        return timeout;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getDisabledReason() {
        return disabledReason;
    }
}
