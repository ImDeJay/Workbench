package org.futuredev.workbench.command.annotation;

import org.futuredev.workbench.command.reflective.Completable;
import org.futuredev.workbench.command.reflective.Resolvable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention (RetentionPolicy.RUNTIME)
public abstract @interface Tab {

    Class<? extends Completable>[] value();

}