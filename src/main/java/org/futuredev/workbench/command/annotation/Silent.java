package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tells the compiler to be return invalid values
 * instead of raising exceptions (i.e. let the
 * developer handle it himself)
 *
 * @author afistofirony
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Silent {}
