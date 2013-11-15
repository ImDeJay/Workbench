package org.futuredev.workbench.command.annotation;

import org.futuredev.workbench.command.reflective.Resolvable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Resolver {

    Class<? extends Resolvable> value ();

}
