package com.myframework.dev.annotations;

import java.lang.annotation.*;

/**
 * Marks a test method or test class as disabled.
 * Disabled tests will be skipped during test execution.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Disabled {
    /**
     * Optional reason for disabling the test.
     */
    String value() default "";
}

