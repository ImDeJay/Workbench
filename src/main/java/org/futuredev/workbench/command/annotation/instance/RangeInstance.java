package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Range;

import java.lang.annotation.Annotation;

public class RangeInstance implements Annotation, Range {

    double min, max;

    public RangeInstance (double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double min () { return this.min; }
    public double max () { return this.max; }

    public Class<? extends Annotation> annotationType () {
        return Range.class;
    }

}