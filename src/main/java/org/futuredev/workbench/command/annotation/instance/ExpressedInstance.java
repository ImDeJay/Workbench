package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Expressed;

import java.lang.annotation.Annotation;

public class ExpressedInstance implements Annotation, Expressed {

    String value;

    public ExpressedInstance (String value) {
        this.value = value;
    }

    public String value () {
        return this.value;
    }

    public Class<? extends Annotation> annotationType () {
        return Expressed.class;
    }

}