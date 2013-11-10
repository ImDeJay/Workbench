package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents a command which falls under another command.
 *
 * String can contain spaces.
 *
 * @author afistofirony
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Extends {

    String value();

}
