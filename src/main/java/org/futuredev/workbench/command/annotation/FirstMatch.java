package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Using this will return an integer value for the first
 * expression matched.
 *
 * @author afistofirony
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FirstMatch {

    String[] value() default "[.]+";

}
