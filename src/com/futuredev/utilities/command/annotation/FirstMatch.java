package com.futuredev.utilities.command.annotation;

/**
 * Using this will return an integer value for the first
 * expression matched.
 *
 * @author afistofirony
 */
public @interface FirstMatch {

    String[] value() default "[.]+";

}
