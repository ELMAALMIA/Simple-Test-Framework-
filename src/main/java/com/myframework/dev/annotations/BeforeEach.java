package com.myframework.dev.annotations;

import java.lang.annotation.*;

/**
 * Marks a method to be executed before each test method in the class.
 * Use this for setup operations that need to run before every test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeEach {
}
