package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a method as the tab completer for a command.
 *
 * @author afistofirony
 */
@Retention (RetentionPolicy.RUNTIME)
public @interface Completer {

    String value ();

}
