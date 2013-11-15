package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Complex;

import java.lang.annotation.Annotation;

public class ComplexInstance implements Annotation, Complex {

    String value;

    public ComplexInstance (String value) {
        this.value = value;
    }

    public String value () { return this.value; }

    public Class<? extends Annotation> annotationType () {
        return Complex.class;
    }

}