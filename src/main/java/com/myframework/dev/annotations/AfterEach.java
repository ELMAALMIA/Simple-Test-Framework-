package com.myframework.dev.annotations;

import java.lang.annotation.*;

/**
 * Marks a method to be executed after each test method in the class.
 * Use this for cleanup operations that need to run after every test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterEach {
}
