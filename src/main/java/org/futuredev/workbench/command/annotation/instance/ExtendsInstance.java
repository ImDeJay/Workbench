package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Extends;

import java.lang.annotation.Annotation;

public class ExtendsInstance implements Annotation, Extends {

    String value;

    public ExtendsInstance (String value) {
        this.value = value;
    }

    public String value () {
        return this.value;
    }

    public Class<? extends Annotation> annotationType () {
        return Extends.class;
    }

}