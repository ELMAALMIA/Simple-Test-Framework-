package com.myframework.dev.annotations;

import java.lang.annotation.*;

/**
 * Marks a method to be executed once after all test methods in the class.
 * The method must be static. Use this for one-time cleanup operations.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterAll {
}

