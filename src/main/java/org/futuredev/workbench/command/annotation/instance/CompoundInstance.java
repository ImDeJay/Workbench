package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Compound;

import java.lang.annotation.Annotation;

public class CompoundInstance implements Annotation, Compound {

    public Class<? extends Annotation> annotationType () {
        return Compound.class;
    }

}
