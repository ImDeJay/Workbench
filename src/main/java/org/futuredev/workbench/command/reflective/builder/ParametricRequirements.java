package org.futuredev.workbench.command.reflective.builder;

import org.futuredev.workbench.command.annotation.instance.FlagInstance;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

public class ParametricRequirements {

    ArrayList<Parameter> requirements;

    public ParametricRequirements () {
        this (new ArrayList<Parameter>());
    }

    public ParametricRequirements (ArrayList<Parameter> base) {
        this.requirements = base;
    }

    public Parameter get (int index) {
        return this.requirements.get(index);
    }

    public ParametricRequirements append (Parameter param) {
        this.requirements.add(param);
        return this;
    }

    public ParametricRequirements append (char flag) {
        this.requirements.add(new Parameter().append(new FlagInstance(flag)));
        return this;
    }

}