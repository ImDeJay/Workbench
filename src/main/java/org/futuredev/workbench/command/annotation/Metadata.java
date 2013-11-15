package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a parameter as metadata (not filled by
 * an argument).
 *
 * @author afistofirony
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Metadata {}
