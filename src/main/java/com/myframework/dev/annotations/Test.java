package com.myframework.dev.annotations;

import java.lang.annotation.*;

/**
 * Marks a method as a test method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    /**
     * The exception class that the test method is expected to throw.
     * If the test throws this exception, the test will pass.
     */
    Class<? extends Throwable> expected() default None.class;

    /**
     * Timeout in milliseconds. If the test takes longer than this, it will fail.
     * A value of 0 means no timeout.
     */
    long timeout() default 0;

    /**
     * Internal class used to represent "no exception expected"
     */
    class None extends Throwable {
        private None() {}
    }
}
