package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention (RetentionPolicy.RUNTIME)
public @interface Command {

    String[] aliases() default {};
    String simple() default "";

    boolean console() default true;

    String[] illegal() default {};

}
