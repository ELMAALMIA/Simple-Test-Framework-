package com.myframework.dev.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for creating TestCase instances.
 * Uses Builder pattern to handle many constructor parameters.
 */
public class TestCaseBuilder {
    private Class<?> testClass;
    private Method testMethod;
    private List<Method> beforeEachMethods = new ArrayList<>();
    private List<Method> afterEachMethods = new ArrayList<>();
    private List<Method> beforeAllMethods = new ArrayList<>();
    private List<Method> afterAllMethods = new ArrayList<>();
    private Class<? extends Throwable> expectedException;
    private long timeout = 0;
    private boolean disabled = false;
    private String disabledReason = "";

    public TestCaseBuilder testClass(Class<?> testClass) {
        this.testClass = testClass;
        return this;
    }

    public TestCaseBuilder testMethod(Method testMethod) {
        this.testMethod = testMethod;
        return this;
    }

    public TestCaseBuilder beforeEachMethods(List<Method> beforeEachMethods) {
        this.beforeEachMethods = beforeEachMethods != null ? beforeEachMethods : new ArrayList<>();
        return this;
    }

    public TestCaseBuilder afterEachMethods(List<Method> afterEachMethods) {
        this.afterEachMethods = afterEachMethods != null ? afterEachMethods : new ArrayList<>();
        return this;
    }

    public TestCaseBuilder beforeAllMethods(List<Method> beforeAllMethods) {
        this.beforeAllMethods = beforeAllMethods != null ? beforeAllMethods : new ArrayList<>();
        return this;
    }

    public TestCaseBuilder afterAllMethods(List<Method> afterAllMethods) {
        this.afterAllMethods = afterAllMethods != null ? afterAllMethods : new ArrayList<>();
        return this;
    }

    public TestCaseBuilder expectedException(Class<? extends Throwable> expectedException) {
        this.expectedException = expectedException;
        return this;
    }

    public TestCaseBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public TestCaseBuilder disabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public TestCaseBuilder disabledReason(String disabledReason) {
        this.disabledReason = disabledReason != null ? disabledReason : "";
        return this;
    }

    public TestCase build() {
        return new TestCase(
            testClass, testMethod, beforeEachMethods, afterEachMethods,
            beforeAllMethods, afterAllMethods, expectedException, timeout,
            disabled, disabledReason
        );
    }
}

