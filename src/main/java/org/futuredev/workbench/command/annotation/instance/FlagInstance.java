package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Flag;

import java.lang.annotation.Annotation;

public class FlagInstance implements Annotation, Flag {

    char value;

    public FlagInstance (char value) {
        this.value = value;
    }

    public char value () {
        return this.value;
    }

    public Class<? extends Annotation> annotationType () {
        return Flag.class;
    }

}