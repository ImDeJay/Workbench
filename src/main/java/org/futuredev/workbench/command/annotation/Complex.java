package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to indicate that a command is complex and should not have
 * its parameters lexed into documentation.
 *
 * @author afistofirony
 */
@Retention (RetentionPolicy.RUNTIME)
public @interface Complex {

    String value() default "";

}