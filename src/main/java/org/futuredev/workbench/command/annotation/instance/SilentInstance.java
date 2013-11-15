package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Silent;

import java.lang.annotation.Annotation;

public class SilentInstance implements Annotation, Silent {

    public SilentInstance () {
    }

    public Class<? extends Annotation> annotationType () {
        return Silent.class;
    }

}