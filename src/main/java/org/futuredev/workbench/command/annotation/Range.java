package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Range {

    double min() default -1;
    double max() default -1;

}
