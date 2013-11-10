package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Default;

import java.lang.annotation.Annotation;

public class DefaultInstance implements Annotation, Default {

    String value;

    public DefaultInstance (String value) {
        this.value = value;
    }

    public DefaultInstance (Default def) {
        this (def.value());
    }

    public String value () {
        return this.value;
    }

    public Object resolvedValue (Class<?> resolveTo) {
        // TODO: Resolve.

        return false;
    }

    public Class<? extends Annotation> annotationType () {
        return Default.class;
    }


}