package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Complex;

import java.lang.annotation.Annotation;

public class ComplexInstance implements Annotation, Complex {

    public ComplexInstance () {}

    public Class<? extends Annotation> annotationType () {
        return Complex.class;
    }

}